package net.openid.appauth.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API_Interface {

/*
    @GET("/-/oauth_authorize")
    Call<ResponseBody> getAuthorizationCode(@Query("client_id") String client_id, @Query("redirect_uri") String redirect_uri,
                                            @Query("response_type") String response_type, @Query("state") String state,
                                            @Query("scope") String scope);*/


    @FormUrlEncoded
    @POST("/-/oauth_token")
    Call<ResponseBody> getInitialToken(@Field("grant_type") String grant_type,
                                       @Field("client_id") String client_id,
                                       @Field("client_secret") String client_secret,
                                       @Field("redirect_uri") String redirect_uri,
                                       @Field("code") String auth_code,
                                       @Field("code_verifier") String code_verifier);

    @FormUrlEncoded
    @POST("/-/oauth_token")
    Call<ResponseBody> refreshToken(@Field("grant_type") String grant_type,
                                    @Field("client_id") String client_id,
                                    @Field("client_secret") String client_secret,
                                    @Field("redirect_uri") String redirect_uri,
                                    @Field("refresh_token") String refresh_token,
                                    @Field("code_verifier") String code_verifier);


    @GET("/api/1.0/users/me")
    Call<ResponseBody> getUserDetails();
}
