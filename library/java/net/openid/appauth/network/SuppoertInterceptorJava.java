package net.openid.appauth.network;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;

public class SuppoertInterceptorJava implements Interceptor, Authenticator {
    @NotNull
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request originalRequest = chain.request();
        Request authenticationRequest = this.request(originalRequest);
        Response initialResponse = null;
        try {
            initialResponse = chain.proceed(authenticationRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Log_originalRequest", originalRequest.toString());
        Log.e("Log_ResponseCode", String.valueOf(initialResponse.body()));
        if (initialResponse.code() == 401) {
            retrofit2.Response responseNewTokenLoginModel = ((API_Interface) RetrofitManager.retrofit.create(API_Interface.class)).refreshToken("refresh_token", Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.REDIRECT_URI, SharedPreferencesUtil.getRefreshToken((Context) GlobalVar.get()), SharedPreferencesUtil.getCodeVerifier((Context) GlobalVar.get())).execute();
            if (responseNewTokenLoginModel != null && responseNewTokenLoginModel.code() == 200) {
                ResponseBody var10000 = initialResponse.body();
                if (var10000 != null) {
                    var10000.close();
                }

                var10000 = (ResponseBody) responseNewTokenLoginModel.body();
                if (var10000 != null) {
                    var10000.close();
                }

                var10000 = (ResponseBody) responseNewTokenLoginModel.body();
                String strResponse = null;
                Request newAuthenticationRequest = null;
                try {
                    strResponse = var10000 != null ? var10000.string() : null;
                    JSONObject jsonObject = new JSONObject(strResponse);
                    String accesstoken = jsonObject.getString("access_token");
                    String token_type = jsonObject.getString("token_type");
                    SharedPreferencesUtil.setAuthToken((Context) GlobalVar.get(), token_type + ' ' + accesstoken);
                    newAuthenticationRequest = this.request(originalRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return chain.proceed(newAuthenticationRequest);
            } else {
                return initialResponse;
            }
        } else {
            return initialResponse;
        }
    }

    private final Request request(Request originalRequest) {
        if (originalRequest.url().toString().contains("/-/oauth_token")) {
            return originalRequest;
        }
        String token = SharedPreferencesUtil.getAuthToken((Context) GlobalVar.get());
        Log.e("token--", token);
        return originalRequest.newBuilder()
                .addHeader("Authorization", token.toString())
                .addHeader("Content-Type", "application/json")
                .build();

    }

    @Nullable
    public Request authenticate(@Nullable Route route, @NotNull Response response) {
        Request requestAvailable = (Request) null;
        String token = SharedPreferencesUtil.getAuthToken((Context) GlobalVar.get());
        if (token.equals(response.request().header("Authorization"))) {
            return null;
        }
            try {
                requestAvailable = response.request().newBuilder()
                        .addHeader("Authorization", token.toString())
                        .addHeader("Content-Type", "application/json")
                        .build();
                return requestAvailable;
            } catch (Exception var6) {
                return requestAvailable;
            }

    }
}
