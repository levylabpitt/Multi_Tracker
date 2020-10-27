/*
 * Copyright 2016 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openid.appauth;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;

import com.google.gson.Gson;

import net.openid.appauth.AuthorizationException.AuthorizationRequestErrors;
import net.openid.appauth.internal.Logger;
import net.openid.appauth.model.user_detail.UserDetailsResponse;
import net.openid.appauth.model.user_detail.Workspace;
import net.openid.appauth.network.APIError;
import net.openid.appauth.network.Constants;
import net.openid.appauth.network.RequestListener;
import net.openid.appauth.network.RetrofitManager;
import net.openid.appauth.network.SharedPreferencesUtil;
import net.openid.appauth.network.model.initial_token.InitialToken;

import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Stores state and handles events related to the authorization flow. The activity is
 * started by {@link AuthorizationService#performAuthorizationRequest
 * AuthorizationService.performAuthorizationRequest}, and records all state pertinent to
 * the authorization request before invoking the authorization intent. It also functions
 * to control the back stack, ensuring that the authorization activity will not be reachable
 * via the back button after the flow completes.
 * <p>
 * The following diagram illustrates the operation of the activity:
 * <p>
 * ```
 * Back Stack Towards Top
 * +------------------------------------------>
 * <p>
 * +------------+            +---------------+      +----------------+      +--------------+
 * |            |     (1)    |               | (2)  |                | (S1) |              |
 * | Initiating +----------->| Authorization +----->| Authorization  +----->| Redirect URI |
 * |  Activity  |            |  Management   |      |   Activity     |      |   Receiver   |
 * |            |<-----------+   Activity    |<-----+ (e.g. browser) |      |   Activity   |
 * |            | (C2b, S3b) |               | (C1) |                |      |              |
 * +------------+            +-+---+---------+      +----------------+      +-------+------+
 * |  |  ^                                              |
 * |  |  |                                              |
 * +-------+  |  |                      (S2)                    |
 * |          |  +----------------------------------------------+
 * |          |
 * |          v (S3a)
 * (C2a) |      +------------+
 * |      |            |
 * |      | Completion |
 * |      |  Activity  |
 * |      |            |
 * |      +------------+
 * |
 * |      +-------------+
 * |      |             |
 * +----->| Cancelation |
 * |  Activity   |
 * |             |
 * +-------------+
 * ```
 * <p>
 * The process begins with an activity requesting that an authorization flow be started,
 * using {@link AuthorizationService#performAuthorizationRequest}.
 * <p>
 * - Step 1: Using an intent derived from {@link #createStartIntent}, this activity is
 * started. The state delivered in this intent is recorded for future use.
 * <p>
 * - Step 2: The authorization intent, typically a browser tab, is started. At this point,
 * depending on user action, we will either end up in a "completion" flow (S) or
 * "cancelation flow" (C).
 * <p>
 * - Cancelation (C) flow:
 * - Step C1: If the user presses the back button or otherwise causes the authorization
 * activity to finish, the AuthorizationManagementActivity will be recreated or restarted.
 * <p>
 * - Step C2a: If a cancellation PendingIntent was provided in the call to
 * {@link AuthorizationService#performAuthorizationRequest}, then this is
 * used to invoke a cancelation activity.
 * <p>
 * - Step C2b: If no cancellation PendingIntent was provided (legacy behavior, or
 * AuthorizationManagementActivity was started with an intent from
 * {@link AuthorizationService#getAuthorizationRequestIntent}), then the
 * AuthorizationManagementActivity simply finishes after calling {@link Activity#setResult},
 * with {@link Activity#RESULT_CANCELED}, returning control to the activity above
 * it in the back stack (typically, the initiating activity).
 * <p>
 * - Completion (S) flow:
 * - Step S1: The authorization activity completes with a success or failure, and sends this
 * result to {@link RedirectUriReceiverActivity}.
 * <p>
 * - Step S2: {@link RedirectUriReceiverActivity} extracts the forwarded data, and invokes
 * AuthorizationManagementActivity using an intent derived from
 * {@link #createResponseHandlingIntent}. This intent has flag CLEAR_TOP set, which will
 * result in both the authorization activity and {@link RedirectUriReceiverActivity} being
 * destroyed, if necessary, such that AuthorizationManagementActivity is once again at the
 * top of the back stack.
 * <p>
 * - Step S3a: If this activity was invoked via
 * {@link AuthorizationService#performAuthorizationRequest}, then the pending intent provided
 * for completion of the authorization flow is invoked, providing the decoded
 * {@link AuthorizationResponse} or {@link AuthorizationException} as appropriate.
 * The AuthorizationManagementActivity finishes, removing itself from the back stack.
 * <p>
 * - Step S3b: If this activity was invoked via an intent returned by
 * {@link AuthorizationService#getAuthorizationRequestIntent}, then this activity
 * calls {@link Activity#setResult(int, Intent)} with {@link Activity#RESULT_OK}
 * and a data intent containing the {@link AuthorizationResponse} or
 * {@link AuthorizationException} as appropriate.
 * The AuthorizationManagementActivity finishes, removing itself from the back stack.
 */
public class AuthorizationManagementActivity extends Activity implements RequestListener {

    @VisibleForTesting
    static final String KEY_AUTH_INTENT = "authIntent";

    @VisibleForTesting
    static final String KEY_AUTH_REQUEST = "authRequest";

    @VisibleForTesting
    static final String KEY_COMPLETE_INTENT = "completeIntent";

    @VisibleForTesting
    static final String KEY_CANCEL_INTENT = "cancelIntent";

    @VisibleForTesting
    static final String KEY_AUTHORIZATION_STARTED = "authStarted";

    private boolean mAuthorizationStarted = false;
    private Intent mAuthIntent;
    private AuthorizationRequest mAuthRequest;
    private PendingIntent mCompleteIntent;
    private PendingIntent mCancelIntent;
    static Intent my_intent;

    public static Intent createStartIntent(
            Context context,
            AuthorizationRequest request,
            Intent authIntent,
            PendingIntent completeIntent,
            PendingIntent cancelIntent) {
        Intent intent = createBaseIntent(context);
        intent.putExtra(KEY_AUTH_INTENT, authIntent);
        intent.putExtra(KEY_AUTH_REQUEST, request.jsonSerializeString());
        intent.putExtra(KEY_COMPLETE_INTENT, completeIntent);
        intent.putExtra(KEY_CANCEL_INTENT, cancelIntent);
        return intent;
    }


    public static Intent createStartForResultIntent(
            Context context,
            AuthorizationRequest request,
            Intent authIntent) {
        return createStartIntent(context, request, authIntent, null, null);
    }


    public static Intent createResponseHandlingIntent(Context context, Intent _intent) {
        my_intent = _intent;
        Intent intent = createBaseIntent(context);
        intent.setData(_intent.getData());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private static Intent createBaseIntent(Context context) {
        return new Intent(context, AuthorizationManagementActivity.class);
    }

     RetrofitManager retrofitManager =  RetrofitManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            extractState(getIntent().getExtras());
        } else {
            extractState(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * If this is the first run of the activity, start the authorization intent.
         * Note that we do not finish the activity at this point, in order to remain on the back
         * stack underneath the authorization activity.
         */

        if (!mAuthorizationStarted) {
            startActivity(mAuthIntent);
            mAuthorizationStarted = true;
            return;
        }

        /*
         * On a subsequent run, it must be determined whether we have returned to this activity
         * due to an OAuth2 redirect, or the user canceling the authorization flow. This can
         * be done by checking whether a response URI is available, which would be provided by
         * RedirectUriReceiverActivity. If it is not, we have returned here due to the user
         * pressing the back button, or the authorization activity finishing without
         * RedirectUriReceiverActivity having been invoked - this can occur when the user presses
         * the back button, or closes the browser tab.
         */


        if (getIntent().getData() != null) {
            //  handleAuthorizationComplete();
            getInitialToken(getIntent());
        } else {
            handleAuthorizationCanceled();
        }
        // finish();    //nks
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_AUTHORIZATION_STARTED, mAuthorizationStarted);
        outState.putParcelable(KEY_AUTH_INTENT, mAuthIntent);
        outState.putString(KEY_AUTH_REQUEST, mAuthRequest.jsonSerializeString());
        outState.putParcelable(KEY_COMPLETE_INTENT, mCompleteIntent);
        outState.putParcelable(KEY_CANCEL_INTENT, mCancelIntent);
    }

    private void handleAuthorizationComplete() {
        Uri responseUri = getIntent().getData();
        Intent responseData = extractResponseData(responseUri);
        if (responseData == null) {
            Logger.error("Failed to extract OAuth2 response from redirect");
            return;
        }
        responseData.setData(responseUri);

        if (mCompleteIntent != null) {
            Logger.debug("Authorization complete - invoking completion intent");
            try {
                mCompleteIntent.send(this, 0, responseData);
            } catch (CanceledException ex) {
                Logger.error("Failed to send completion intent", ex);
            }
        } else {
            setResult(RESULT_OK, responseData);
        }
    }

    private void handleAuthorizationCanceled() {
        Logger.debug("Authorization flow canceled by user");
        Intent cancelData = AuthorizationException.fromTemplate(
                AuthorizationException.GeneralErrors.USER_CANCELED_AUTH_FLOW,
                null)
                .toIntent();
        if (mCancelIntent != null) {
            try {
                mCancelIntent.send(this, 0, cancelData);
            } catch (CanceledException ex) {
                Logger.error("Failed to send cancel intent", ex);
            }
        } else {
            setResult(RESULT_CANCELED, cancelData);
            Logger.debug("No cancel intent set - will return to previous activity");
        }
        finish();//nks
    }

    private void extractState(Bundle state) {
        if (state == null) {
            Logger.warn("No stored state - unable to handle response");
            finish();
            return;
        }

        mAuthIntent = state.getParcelable(KEY_AUTH_INTENT);
        mAuthorizationStarted = state.getBoolean(KEY_AUTHORIZATION_STARTED, false);
        try {
            String authRequestJson = state.getString(KEY_AUTH_REQUEST, null);
            mAuthRequest = authRequestJson != null
                    ? AuthorizationRequest.jsonDeserialize(authRequestJson)
                    : null;
        } catch (JSONException ex) {
            throw new IllegalStateException("Unable to deserialize authorization request", ex);
        }
        mCompleteIntent = state.getParcelable(KEY_COMPLETE_INTENT);
        mCancelIntent = state.getParcelable(KEY_CANCEL_INTENT);
    }

    private Intent extractResponseData(Uri responseUri) {
        if (responseUri.getQueryParameterNames().contains(AuthorizationException.PARAM_ERROR)) {
            return AuthorizationException.fromOAuthRedirect(responseUri).toIntent();
        } else {
            AuthorizationResponse response = new AuthorizationResponse.Builder(mAuthRequest)
                    .fromUri(responseUri)
                    .build();

            if (mAuthRequest.state == null && response.state != null
                    || (mAuthRequest.state != null && !mAuthRequest.state.equals(response.state))) {

                Logger.warn("State returned in authorization response (%s) does not match state "
                                + "from request (%s) - discarding response",
                        response.state,
                        mAuthRequest.state);

                return AuthorizationRequestErrors.STATE_MISMATCH.toIntent();
            }

            return response.toIntent();
        }
    }


    /*nks*/
    private void hitAPIGetAsanaUserDetails() {
        String token= SharedPreferencesUtil.getAuthToken(this);
        retrofitManager.getUserDetails(this, this, Constants.API_TYPE.GET_USER_DETAILS, token,false);
    }

    private void getInitialToken(Intent intent) {

        Intent responseData = extractResponseData(intent.getData());
        AuthorizationResponse response = AuthorizationResponse.fromIntent(responseData);
        if (response != null) {
            String auth_code = response.authorizationCode;
            String code_verifier = response.request.codeVerifier;
            SharedPreferencesUtil.setCodeVerifier(AuthorizationManagementActivity.this, code_verifier);
            retrofitManager.getInitialToken(this, this, Constants.API_TYPE.INITIAL_TOKEN, auth_code, code_verifier, true);
        } else {
            //nks
            ClipData clipData = my_intent.getClipData();
            if (clipData != null) {
                if (clipData.getItemCount() > 0) {
                    ClipData.Item item = clipData.getItemAt(0);
                    String auth_data = item.getText().toString();
                    Uri uri = Uri.parse(auth_data);
                    String auth_code = uri.getQueryParameter("code");
                    String state = uri.getQueryParameter("state");
                    String id_token = uri.getQueryParameter("id_token");
                    String code_verifier = mAuthRequest.codeVerifier;
                    SharedPreferencesUtil.setCodeVerifier(AuthorizationManagementActivity.this, code_verifier);
                    retrofitManager.getInitialToken(this, this, Constants.API_TYPE.INITIAL_TOKEN, auth_code, code_verifier, true);
                }
            }

        }

    }


    @Override
    public void onSuccess(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        try {
            String strResponse = response.body().string();
            Log.e("APIResponse---->", response.body().toString());
            if (apiType == Constants.API_TYPE.INITIAL_TOKEN) {
                InitialToken initialToken = new Gson().fromJson(strResponse, InitialToken.class);
                //Toast.makeText(getApplicationContext(),strResponse,Toast.LENGTH_SHORT).show();
                String AccessToken = initialToken.getAccessToken();
                String RefreshToken = initialToken.getRefreshToken();
                SharedPreferencesUtil.setAuthToken(AuthorizationManagementActivity.this, initialToken.getTokenType() + " " + AccessToken);
                SharedPreferencesUtil.setRefreshToken(AuthorizationManagementActivity.this, RefreshToken);
                SharedPreferencesUtil.setAsanaEmail(AuthorizationManagementActivity.this, initialToken.getData().getEmail());
                SharedPreferencesUtil.setAsanaName(AuthorizationManagementActivity.this, initialToken.getData().getName());
                // SharedPreferencesUtil.setAsanaEmail(AuthorizationManagementActivity.this, initialToken.getData().getEmail());
                hitAPIGetAsanaUserDetails();
            } else if (apiType == Constants.API_TYPE.GET_USER_DETAILS) {
                SharedPreferencesUtil.setUserDetails(this, strResponse);
                UserDetailsResponse userDetailsResponse = new Gson().fromJson(strResponse, UserDetailsResponse.class);
                String user_gid = userDetailsResponse.getData().getGid();
                //   SharedPreferencesUtil.setAsanaUserId(this, user_gid);
            /*    List<Workspace> workspaces = userDetailsResponse.getData().getWorkspaces();
                if (workspaces != null && workspaces.size() > 0) {
                    String workspace_gid = workspaces.get(0).getGid();
                    //  SharedPreferencesUtil.setAsanaWorkspaceId(this, workspace_gid);
                }
                for (int i = 0; i < workspaces.size(); i++) {
                    if (workspaces.get(i).getName().equals("levylab.org")) {
                        String LevyLab_workspace_gid = workspaces.get(i).getGid();
                        SharedPreferencesUtil.setLevyLabWorkspaceId(this, workspaces.get(i).getGid());
                        break;
                    } else if (i == workspaces.size() - 1) {
                         Toast.makeText(this, "'Levy Lab' workspace not found.\n Please get access of this project for updating info on Asana. ", Toast.LENGTH_SHORT).show();

                    }
                }*/
                setResult(RESULT_OK, getIntent());
                finish();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        //Toast.makeText(getApplicationContext(),"strResponse",Toast.LENGTH_SHORT).show();
        String messgae = response.message();
        ResponseBody s = response.errorBody();
        String dd = response.toString();
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, Constants.API_TYPE apiType) {
        //  Toast.makeText(getApplicationContext(),"strResponse",Toast.LENGTH_SHORT).show();
        String messgae = response.message();
    }




    /*nks*/
}
