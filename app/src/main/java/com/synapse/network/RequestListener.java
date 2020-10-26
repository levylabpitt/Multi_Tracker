package com.synapse.network;



import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by Jitendra on 22,March,2019
 */

public interface RequestListener {

    void onSuccess(Response<ResponseBody> response, Constants.API_TYPE apiType);

    void onFailure(Response<ResponseBody> response, Constants.API_TYPE apiType);

    void onApiException(APIError error, Response<ResponseBody> response, Constants.API_TYPE apiType);

}
