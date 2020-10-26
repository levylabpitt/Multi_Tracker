package com.pqiorg.multitracker.anoto.activities.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
/*import com.anoto.adna.R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.LoginActivity;
import com.anoto.adna.activities.MainActivity;
import com.anoto.adna.activities.MyScanActivity;
import com.anoto.adna.activities.MyStickerList1Activity;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.MyAlertDialog;
import com.anoto.adna.util.MyProgress;
import com.anoto.adna.util.PermissionUtil;*/
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
//import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.LoginActivity;
import com.pqiorg.multitracker.anoto.activities.MainActivity;
import com.pqiorg.multitracker.anoto.activities.MyScanActivity;
import com.pqiorg.multitracker.anoto.activities.MyStickerList1Activity;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
import com.pqiorg.multitracker.anoto.activities.util.MyAlertDialog;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;
//import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.model.people.Person;
import java.util.Arrays;
import java.util.Collection;
import org.json.JSONException;
import org.json.JSONObject;

public class MypagesFragment extends Fragment implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    public static String SCAN_TYPE_ADD_FLAG = "1";
    public static String SCAN_TYPE_LINK_FLAG = "2";
    public static LinearLayout view_login1;
    public static LinearLayout view_menu;
    public String TAG = "MypagesFragment";

    /* renamed from: a */
    GlobalVar f3014a;
    //BeaconReferenceApplication f3014a;

    /* renamed from: b */
    SignInButton f3015b;
    private Button btn_user_account_login;

    /* renamed from: c */
    LoginButton f3016c;

    /* renamed from: d */
    Button f3017d;

    /* renamed from: e */
    Button f3018e;

    /* renamed from: f */
    Button f3019f;

    /* renamed from: g */
    String f3020g;

    /* renamed from: h */
    View f3021h;

    /* renamed from: i */
    ADNAListener f3022i = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            if (i == 2000) {
                MypagesFragment.this.setEusrAccountInfo(MypagesFragment.this.f3020g);
            } else {
                Toast.makeText(MypagesFragment.this.getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 19) {
                MyProgress.hides();
                DevLog.defaultLogging("signupUserEmail onSuccess.");
                new MyAlertDialog(MypagesFragment.this.getActivity()).noCloseAlert(MypagesFragment.this.getResources().getString(R.string.txt_signup_account_ok));
                MypagesFragment.this.setEusrAccountInfo(MypagesFragment.this.f3020g);
            }
        }
    };
    private ADNAListener listener;
    private ADNAClient mApiClient;
    private CallbackManager mFacebookCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SettingManager mSettingManager;
    private float orgBrightness = -1.0f;
    private LayoutParams params;

    private void initADNA() {
        String string = getResources().getString(R.string.server_address);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setServerURL(string);
        this.mApiClient = ADNAClient.getInstance(getActivity());
        this.mApiClient.setADNAListener(this.f3022i);
    }

    private void setOrgBrightness() {
        this.params.screenBrightness = this.orgBrightness;
        getActivity().getWindow().setAttributes(this.params);
    }

    /* access modifiers changed from: private */
    public void signupUserEmail(String str, String str2, String str3) {
        if (!PermissionUtil.isNetworkConnect(getContext())) {
            Toast.makeText(getContext(), R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(getActivity());
        if (this.mApiClient != null) {
            this.mApiClient.signupUserEmail(str, str2, str3);
        }
    }

    public void handleSignResult(GoogleSignInResult googleSignInResult) {
        try {
            GoogleSignInAccount signInAccount = googleSignInResult.getSignInAccount();
            if (signInAccount != null) {
                signInAccount.getDisplayName();
                String email = signInAccount.getEmail();
                this.f3020g = email;
                signupUserEmail(email, "", "google");
            }
        } catch (Exception unused) {
        }
    }

    public void init() {
        view_login1 = (LinearLayout) this.f3021h.findViewById(R.id.view_login1);
        view_menu = (LinearLayout) this.f3021h.findViewById(R.id.view_menu);
        this.f3015b = (SignInButton) this.f3021h.findViewById(R.id.btn_google_login);
        this.f3016c = (LoginButton) this.f3021h.findViewById(R.id.btn_facebook_login);
        this.btn_user_account_login = (Button) this.f3021h.findViewById(R.id.btn_user_account_login);
        this.f3017d = (Button) this.f3021h.findViewById(R.id.btn_my_stickers);
        this.f3018e = (Button) this.f3021h.findViewById(R.id.btn_add_stickers);
        this.f3019f = (Button) this.f3021h.findViewById(R.id.btn_link_registration);
        this.f3015b.setOnClickListener(this);
        this.btn_user_account_login.setOnClickListener(this);
        this.f3017d.setOnClickListener(this);
        this.f3018e.setOnClickListener(this);
        this.f3019f.setOnClickListener(this);
        try {
            this.mGoogleApiClient = new Builder(getActivity()).enableAutoManage(getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()).build();
        } catch (Exception e) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mGoogleApiClient ::: ");
            sb.append(e.getMessage().toString());
            Log.d(str, sb.toString());
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.mFacebookCallbackManager.onActivityResult(i, i2, intent);
        super.onActivityResult(i, i2, intent);
        if (i == 9001) {
            handleSignResult(Auth.GoogleSignInApi.getSignInResultFromIntent(intent));
        }
    }

    public void onClick(View view) {
        Intent intent;
        String str="";
        String str2="";
        switch (view.getId()) {
            case R.id.btn_add_stickers /*2131296334*/:
                intent = new Intent(getActivity(), MyScanActivity.class);
                str2 = "SCAN_TYPE";
                str = SCAN_TYPE_ADD_FLAG;
                break;
            case R.id.btn_facebook_login /*2131296340*/:
                LoginManager.getInstance().logInWithPublishPermissions((Activity) getActivity(), (Collection<String>) Arrays.asList(new String[]{"public_profile"}));
                return;
            case R.id.btn_google_login /*2131296341*/:
                signIn();
                return;
            case R.id.btn_link_registration /*2131296344*/:
                intent = new Intent(getActivity(), MyScanActivity.class);
                str2 = "SCAN_TYPE";
                str = SCAN_TYPE_LINK_FLAG;
                break;
            case R.id.btn_my_stickers /*2131296347*/:
                intent = new Intent(getActivity(), MyStickerList1Activity.class);
                break;
            case R.id.btn_user_account_login /*2131296349*/:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(0, 0);
                return;
            default:
                return;
        }
        intent.putExtra(str2, str);
        startActivity(intent);
    }

    public void onConnected(@Nullable Bundle bundle) {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(this.mGoogleApiClient);
        new Account(Plus.AccountApi.getAccountName(this.mGoogleApiClient), "com.google");
        if (currentPerson != null) {
            Toast.makeText(getActivity(), currentPerson.getId(), Toast.LENGTH_SHORT).show();
        } else {
            Log.w(this.TAG, "error");
        }
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), 9001);
            } catch (SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    public void onConnectionSuspended(int i) {
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            this.params = getActivity().getWindow().getAttributes();
        } catch (Exception unused) {
        }
        setOrgBrightness();
        getActivity().setTitle(R.string.txt_mypages);
        MainActivity.selectNavigation(3);
        this.f3021h = layoutInflater.inflate(R.layout.fragment_mypages, viewGroup, false);
        return this.f3021h;
    }

    public void onPause() {
        super.onPause();
        this.mGoogleApiClient.stopAutoManage(getActivity());
        this.mGoogleApiClient.disconnect();
    }

    public void onResume() {
        super.onResume();
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.f3014a = (GlobalVar) getActivity().getApplicationContext();
        //this.f3014a = (BeaconReferenceApplication) getActivity().getApplicationContext();
        initADNA();
        init();
        this.mFacebookCallbackManager = Factory.create();
        this.f3016c.setReadPermissions("email");
        this.f3016c.setFragment((Fragment) this);
        this.f3016c.registerCallback(this.mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            public void onCancel() {
            }

            public void onError(FacebookException facebookException) {
            }

            public void onSuccess(LoginResult loginResult) {
                GraphRequest newMeRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphJSONObjectCallback() {
                    public void onCompleted(JSONObject jSONObject, GraphResponse graphResponse) {
                        Log.v("facebook result ==", jSONObject.toString());
                        try {
                            MypagesFragment.this.f3020g = jSONObject.getString("email");
                            MypagesFragment.this.signupUserEmail(MypagesFragment.this.f3020g, "", "facebook");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString(GraphRequest.FIELDS_PARAM, "name,email");
                newMeRequest.setParameters(bundle);
                newMeRequest.executeAsync();
            }
        });
    }

    public void setEusrAccountInfo(String str) {
        try {
            GlobalVar.USER_ACCOUNT = str;
           // BeaconReferenceApplication.USER_ACCOUNT = str;
            Editor edit = getActivity().getSharedPreferences("eusr_info", 0).edit();
            edit.putString("user_email", str);
            edit.commit();
            startActivity(new Intent(getActivity(), MyStickerList1Activity.class));
        } catch (Exception unused) {
        }
    }

    public void signIn() {
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(this.mGoogleApiClient), 9001);
    }
}
