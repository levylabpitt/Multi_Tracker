package net.openid.appauth.network;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

/*import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.BuildConfig;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;*/

import net.openid.appauth.BuildConfig;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager implements OnRetryCallback {

    public static Retrofit retrofit = null;
    public static RetrofitManager retrofitManager = null;
    public static API_Interface retroService = null;
    private Call<ResponseBody> call = null;
    //private Call<AuthenticationToken> call2 = null;

    private Callback<ResponseBody> mCallback = null;
    private final String format = "json";
    private String TAG = "RetrofitManager";
    Activity activity = null;
    private OnRetryCallback mRetryCallback = this;
    private static String BASE_URL;
    private Context context;
    static {

        BASE_URL = Constants.BASE_URL_LIVE;

    }

/*public RetrofitManager(Context context){
    this.context = context;
}*/
    private RetrofitManager() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        //  httpClient.addInterceptor(new ConnectivityInterceptor(GlobalVar.get()));
        //     httpClient.addInterceptor(new SupportInterceptor());
       // httpClient.addInterceptor(new SuppoertInterceptorJava());
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

        activity = (Activity) mContext;

     /*   if (mContext == null) {
            Util.DEBUG_LOG(1, TAG, "context is null");
            return;
        }*/

        if (showProgress) {
            //Dialogs.showProgressDialog(activity, "Please wait..");
        }

        mCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    APIError error = null;
                    try {
                        // error = ErrorUtils.parseError(response, retrofitManager);

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
                        JSONObject obj = new JSONObject(strResponse);
                        String s = obj.getString("details");
                        if (s.equalsIgnoreCase("Expired token") || s.equalsIgnoreCase("Signature verification failed")) {
                            // Intent in = new Intent(activity, SplashActivity.class);
                            //  activity.startActivity(in);
                            //  activity.finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  Log.e("Log_Failure", response.toString());
                    mRequestListener.onFailure(response, mApiType);
                }

                //  Dialogs.hideProgressDialog(mContext);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                try {
                    if (t instanceof SocketTimeoutException) {
                        // Dialogs.showTryAgainDialog(mContext, "SOCKET_ERROR", mRetryCallback);

                    } else if (t instanceof NoConnectivityException) {
                        //   Dialogs.showAlert(mContext,"INTERNET_ERROR");

                    } else if (t instanceof NetworkErrorException) {
                        // Dialogs.showAlert(mContext, t.getMessage());
                        //   Dialogs.showAlert(mContext, "ERROR_SOMETHING_WENT_WRONG");
                    }
                    // Dialogs.hideProgressDialog(mContext);
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
            //  Dialogs.showProgressDialog(activity, "Please wait..");
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


    public void getInitialToken(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String auth_code,
            final String code_verifier,
            final boolean showProgress) {

        call = retroService.getInitialToken("authorization_code", Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.REDIRECT_URI, auth_code, code_verifier);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void refreshToken(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String refresh_token,
            final String code_verifier,
            final boolean showProgress) {

        call = retroService.refreshToken("refresh_token", Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.REDIRECT_URI, refresh_token, code_verifier);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void getUserDetails(
            final RequestListener mRequestListener,
            final Context mContext,
            final Constants.API_TYPE mApiType,
            final String token,
            final boolean showProgress) {

        call = retroService.getUserDetails(token);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

}
