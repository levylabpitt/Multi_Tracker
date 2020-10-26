package com.synapse.network;


import com.google.gson.JsonObject;
import com.synapse.model.update_task_input.UpdateTAskinput;

import org.json.JSONObject;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API_Interface {


    @GET("/-/oauth_authorize")
    Call<ResponseBody> getAuthorizationCode(@Query("client_id") String client_id,@Query("redirect_uri") String redirect_uri,
                                            @Query("response_type") String response_type,@Query("state") String state,
                                            @Query("scope") String scope);


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


    @GET("/api/1.0/workspaces")
    Call<ResponseBody> getOrganizationDetails(@Query("opt_fields") String opt_fields);

    @GET("/api/1.0/projects")
    Call<ResponseBody> getProjects();

    @GET("/api/1.0/workspaces/{workspace_gid}/projects")
    Call<ResponseBody> getProjectsByWorkspace(@Path("workspace_gid") String workspace_gid);


    @GET("/api/1.0/workspaces/{workspace_gid}/projects")
    Call<ResponseBody> getProjectsByWorkspaceNew(@Path("workspace_gid") String workspace_gid,@Query("limit") String limit);

    @GET("/api/1.0/workspaces/{workspace_gid}/projects")
    Call<ResponseBody> getNextProjectsByWorkspace(@Path("workspace_gid") String workspace_gid,
                                                  @Query("limit") String limit,
                                                  @Query("offset") String offset);


    @GET("/api/1.0/projects/{project_gid}/tasks")
    Call<ResponseBody> getTasksByProject(@Path("project_gid") String project_gid,@Query("limit") String limit);

    @GET("/api/1.0/projects/{project_gid}/tasks")
    Call<ResponseBody> getNextTasksByProject(@Path("project_gid") String project_gid,
                                             @Query("limit") String limit,
                                             @Query("offset") String offset);

    //https://app.asana.com/api/1.0/workspaces/914676468468/typeahead?resource_type=task&query=CBL
    // https://app.asana.com/api/1.0/workspaces/914676468468/typeahead?resource_type=project&query=LevyLab AoT (all items)
    @GET("/api/1.0/workspaces/{workspace_gid}/typeahead")
    Call<ResponseBody> searchProject_Task(@Path("workspace_gid") String workspace_gid,
                                             @Query("resource_type") String resource_type,
                                             @Query("query") String query,
                                             @Query("opt_fields") String opt_fields);


    @Multipart
    @POST("/api/1.0/tasks/{task_gid}/attachments")
    Call<ResponseBody> uploadAttachment(@Path("task_gid") String task_gid,@Part() MultipartBody.Part image);


    @PUT("/api/1.0/tasks/{task_gid}")
    Call<ResponseBody> updateTask1(@Path("task_gid") String task_gid, @Body JsonObject jsonObject);


    @GET("/api/1.0/tasks/{task_gid}")
    Call<ResponseBody> getTaskDetails(@Path("task_gid") String task_gid);


    @GET("/api/1.0/users/me")
    Call<ResponseBody> getUserDetails();

    @GET("/api/1.0/organizations/{workspace_gid}/teams")
    Call<ResponseBody> getTeamsInOrganization(@Path("workspace_gid") String workspace_gid);


    @GET("/api/1.0/users/{user_gid}/teams")
    Call<ResponseBody> getTeamsForUser(@Path("user_gid") String user_gid,@Query("organization") String workspace_gid);










































   /*
    @FormUrlEncoded
    @POST("token")
    Call<ResponseBody> getAuthorizationToken(@Field("UserName") String UserName,
                                             @Field("Password") String Password,
                                             @Field("grant_type") String grant_type,
                                             @Field("scope") String scope);


    @FormUrlEncoded
    @POST("api/Login/GetUser")
    Call<ResponseBody> getUser(@Field("UserId") String UserId,
                               @Field("Password") String Password
    );

    @FormUrlEncoded
    @POST("api/Login/ForgotPassword")
    Call<ResponseBody> getPassword(@Field("UserId") String id);

    @FormUrlEncoded
    @POST("api/Login/ChangePassword")
    Call<ResponseBody> changePassword(
            @Field("OldPassword") String oldPass,
            @Field("Password") String password,
            @Field("NewPassword") String newPass);

    //get orderList
    @GET("api/ContactUs/GetUshaContactDetails")
    Call<ResponseBody> getContactDetails(@Header("Authorization") String token);


    @GET("/api/Tile/GetByType/Others")
    Call<ResponseBody>getUsefulLinks();

    @GET("api/Tile/GetByType/ProductCatalogue")
    Call<ResponseBody> getProductCatolugeDetails();

    @GET("api/Tile/GetByType/SocialNetworking")
    Call<ResponseBody> getSocialNetworking();

    @GET("api/Announcement/GetByRole")
    Call<ResponseBody> getAnnouncement(@Header("Authorization") String token);
    @GET("api/Scheme/GetByUserId")
    Call<ResponseBody> getSchemeByUserId(@Header("Authorization") String token);
    @GET("api/Division/GetByUserId")
    Call<ResponseBody> getDownloadDivisionList();

    @GET("/api/TrackOrder/GetOrdersByUserId")// get list of order
    Call<ResponseBody> getOrderList(@Query("UserId") String id);

    @POST("api/Scheme/GetByDivCode")
    Call<ResponseBody> getSchemeByDivision(@Header("Authorization") String token);

    // get orderList Details
    @GET("/api/TrackOrder/GetDetailsByOrderId")// get list of order
    Call<ResponseBody> getOrderListDetails(@Query("id") String id);

    @GET("api/ProductCategory/GetProductCategory")
    Call<ResponseBody> getProductCategory(@Query("Id") String UserId
    );


    @FormUrlEncoded
    @POST("api/ProductCategory/GetDropDownsByProductCategory")
    Call<ResponseBody> getDivisionByProductCategory(@Field("UserId") String UserId,
                                                    @Field("Catcode") String Catcode, @Field("CallType") String CallType
    );



    @FormUrlEncoded
    @POST("api/Division/GetDivisionsByCatCode")
    Call<ResponseBody> getDivisionByCategoryCode(
            @Field("CatCode") String CatCode);


//  http://dpwebservicesbeta.businesstowork.com/api/ProductCategory/GetDropDownsByProductCategory

    @FormUrlEncoded
    @POST("api/ProductCategory/GetDropDownsByProductCategory")
    Call<ResponseBody> getCategoryTypeByProductCategory(@Field("CatCode") String CatCode,
                                                        @Field("DivCode") String DivCode,
                                                        @Field("CallType") String CallType
    );


    @FormUrlEncoded
    @POST("api/ProductCategory/GetDropDownsByProductCategory")
    Call<ResponseBody> getSubCategoryByProductCategory(@Field("CatCode") String CatCode,
                                                       @Field("DivCode") String DivCode,
                                                       @Field("CatType") String CatType,
                                                       @Field("CallType") String CallType
    );



    @FormUrlEncoded
    @POST("/api/Order/GetProductsForList")
    Call<ResponseBody>getProductsForList(@Field("DivCode") String DivCode,
                                         @Field("CategoryType") String CategoryType,
                                         @Field("SubCategory") String SubCategory,
                                         @Field("CallType") String CallType,
                                         @Field("PageNo") int PageNo,
                                         @Field("PageSize") int PageSize
    );


    @FormUrlEncoded
    @POST("/api/Order/GetProductsForList")
    Call<ResponseBody>getProductsForListNew(@Field("DivCode") String DivCode,
                                            @Field("CategoryType") String CategoryType,
                                            @Field("SubCategory") String SubCategory,
                                            @Field("CallType") String CallType,
                                            @Field("PreFix") String PreFix,
                                            @Field("PageNo") int PageNo,
                                            @Field("PageSize") int PageSize
    );
*/







}
