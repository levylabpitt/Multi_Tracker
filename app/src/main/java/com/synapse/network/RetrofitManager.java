package com.synapse.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

//import com.android.volley.BuildConfig;

import com.google.gson.JsonObject;
import com.pqiorg.multitracker.BuildConfig;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.synapse.Utility;
import com.synapse.model.update_task_input.UpdateTAskinput;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

/*import dpusha.app.com.usha.R;
import dpusha.app.com.usha.activity.USHAApplication;
import dpusha.app.com.usha.model.CartItem;
import dpusha.app.com.usha.model.Material;
import dpusha.app.com.usha.orders_home.util.Constants;
import dpusha.app.com.usha.orders_home.util.Dialogs;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;*/
import dpusha.app.com.usha.network.SupportInterceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import dpusha.app.com.usha.BuildConfig;
//import com.bumptech.glide.request.RequestListener;


public class RetrofitManager implements OnRetryCallback {

    public static Retrofit retrofit = null;
    public static RetrofitManager retrofitManager = null;
    public static API_Interface retroService = null;
    private Call<ResponseBody> call = null;
    //private Call<AuthenticationToken> call2 = null;

    private Callback<ResponseBody> mCallback = null;
    private final String format = "json";
    private String TAG = "RetrofitManager";
    Context activity = null;
    private OnRetryCallback mRetryCallback = this;
    private static String BASE_URL;

    static {

        BASE_URL = Constants.BASE_URL_LIVE;

    }


    private RetrofitManager() {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(100, TimeUnit.SECONDS);
        httpClient.addInterceptor(new ConnectivityInterceptor(GlobalVar.get()));
        httpClient.addInterceptor(new SupportInterceptor());

        if (BuildConfig.DEBUG) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retroService = retrofit.create(API_Interface.class);

    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }


    /**
     * Method to resolve the API callbacks
     *
     * @param mRequestListener
     * @param mContext
     * @param mApiType
     * @param showProgress
     */
    public void performCallback(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final boolean showProgress) {

        activity = (Context) mContext;

     /*   if (mContext == null) {
            Util.DEBUG_LOG(1, TAG, "context is null");
            return;
        }*/

        if (showProgress) {
            Dialogs.showProgressDialog(activity, "Please wait..");
        }

        mCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    APIError error = null;
                    try {
                        // error = ErrorUtils.parseError(response, retrofitManager);

                       /* try{
                            Response<ResponseBody> response2=response;
                            String url = response2.raw().request().url().toString();
                            String headers = response2.raw().request().headers().toString();
                            Utility.ReportNonFatalError("onSuccess----", "<-url-> "+url+" <-headers->"+headers+" <-Response->"+response2.body().string());
                        }catch (Exception e){

                        }*/

                        if (error == null) {
                           // Log.e("Log_Response", response.body().string());
                            mRequestListener.onSuccess(response, mApiType);


                        } else {
                            //Log.e("Log_Error", response.toString());
                            mRequestListener.onApiException(error, response, mApiType);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {

                        String strResponse = response.errorBody().string();
                        Log.d(TAG, strResponse);
                    //    JSONObject obj = new JSONObject(strResponse);
                      /*  String s = obj.getString("details");
                        if (s.equalsIgnoreCase("Expired token") || s.equalsIgnoreCase("Signature verification failed")) {
                            // Intent in = new Intent(activity, SplashActivity.class);
                            //  activity.startActivity(in);
                            //  activity.finish();
                        }
*/
                        String url = response.raw().request().url().toString();
                        String headers = response.raw().request().headers().toString();
                        Utility.ReportNonFatalError("onFailure----", "<-url-> "+url+" <-headers->"+headers+" <-error->"+strResponse);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  //  Log.e("Log_Failure", response.toString());



                    mRequestListener.onFailure(response, mApiType);
                }

                Dialogs.hideProgressDialog(mContext);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                try {
                    if (t instanceof SocketTimeoutException) {
                        Dialogs.showTryAgainDialog(mContext, mContext.getString(R.string.ERROR_SOCKET), mRetryCallback);

                    } else if (t instanceof NoConnectivityException) {
                        Dialogs.showAlert(mContext, mContext.getString(R.string.ERROR_INTERNET));

                    } else if (t instanceof NetworkErrorException) {
                        // Dialogs.showAlert(mContext, t.getMessage());
                        Dialogs.showAlert(mContext, mContext.getString(R.string.ERROR_SOMETHING_WENT_WRONG));
                    }
                    Dialogs.hideProgressDialog(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                mRequestListener.onFailure(t, mApiType);


            }
        };

        call.enqueue(mCallback);
    }


    @Override
    public void OnRetry(boolean isRetry) {
        if (isRetry) {
            Dialogs.showProgressDialog(activity, "Please wait..");
            call.clone().enqueue(mCallback);
        } else {

        }
    }

    /**
     * Method to cancel the call
     */
    public void cancelRequest() {
        call.cancel();
    }


    public void getAuthorizationCode(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final boolean showProgress) {

        call = retroService.getAuthorizationCode(Constants.CLIENT_ID, Constants.REDIRECT_URI, "code", "RandomString","default");
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }


    public void getInitialToken(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String auth_code,
            final String code_verifier,
            final boolean showProgress) {

        call = retroService.getInitialToken("authorization_code", Constants.CLIENT_ID, Constants.CLIENT_SECRET,Constants.REDIRECT_URI,auth_code,code_verifier);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void refreshToken(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String refresh_token,
            final String code_verifier,
            final boolean showProgress) {

        call = retroService.refreshToken("refresh_token", Constants.CLIENT_ID, Constants.CLIENT_SECRET,Constants.REDIRECT_URI,refresh_token,code_verifier);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }


    public void getProjects(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final boolean showProgress) {

        call = retroService.getProjects();
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getProjectsByWorkspace(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String workspace_gid,
            final boolean showProgress) {

        call = retroService.getProjectsByWorkspace(workspace_gid);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void searchProjectByWorkspace(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String workspace_gid,
            final String query,
            final boolean showProgress) {
        call = retroService.searchProject_Task(workspace_gid,"project",query,"id,name,resource_type");
        performCallback(mRequestListener, mContext, mApiType, showProgress);
    }


    //opt_fields=id,created_at,modified_at,name,notes,assignee.id,assignee.name,completed,assignee_status,completed_at,due_on,due_at,projects.id,projects.name,memberships,tags,workspace.id,workspace.name,num_hearts,parent,hearts,followers.id,followers.name,hearted
    public void searchTaskByWorkspace(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String workspace_gid,
            final String query,
            final boolean showProgress) {
        call = retroService.searchProject_Task(workspace_gid,"task",query,"id,name,projects.id,projects.name,resource_type");
        performCallback(mRequestListener, mContext, mApiType, showProgress);
    }
    public void searchTaskByWorkspaceAssignedToMe(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String workspace_gid,
            final String query,
            final boolean showProgress) {
        call = retroService.searchProject_Task(workspace_gid,"task",query,"name,assignee,created_at,completed_at");
        performCallback(mRequestListener, mContext, mApiType, showProgress);
    }


    public void getTasksByProject(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String project_gid,
            final int limit,
            final boolean showProgress) {

        call = retroService.getTasksByProject(project_gid,String.valueOf(limit));
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getNextTasksByProject(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String project_gid,
            final int limit,
            final String offset,
            final boolean showProgress) {

        call = retroService.getNextTasksByProject(project_gid,String.valueOf(limit),offset);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getTaskDetails(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String task_gid,
            final boolean showProgress) {

        call = retroService.getTaskDetails(task_gid);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void uploadAttachment(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final  MultipartBody.Part image,
            final String task_gid,
            final boolean showProgress
    ) {

        call = retroService.uploadAttachment(task_gid,image);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void updateTask1(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String task_gid,
            final JsonObject input,
            final boolean showProgress) {

        call = retroService.updateTask1(task_gid,input);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }





    public void getTeams(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final boolean showProgress) {

        call = retroService.getProjects();
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }


    public void getUserDetails(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final boolean showProgress) {

        call = retroService.getUserDetails();
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getTeamsInOrganization(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String workspace_gid,
            final boolean showProgress) {

        call = retroService.getTeamsInOrganization(workspace_gid);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void getTeamsForUser(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final  String user_gid,
            final  String workspace_gid,
            final boolean showProgress) {

        call = retroService.getTeamsForUser(user_gid,workspace_gid);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }


}
