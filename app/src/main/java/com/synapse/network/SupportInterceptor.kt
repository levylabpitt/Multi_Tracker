package dpusha.app.com.usha.network

import android.util.Log
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar
import com.synapse.SharedPreferencesUtil
import com.synapse.network.API_Interface
import com.synapse.network.Constants
import com.synapse.network.RetrofitManager
import okhttp3.*
import org.json.JSONObject
import retrofit2.http.Headers

/**
 * Created by Unknown on 1/15/2020.
 **/

class SupportInterceptor : Interceptor, Authenticator {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authenticationRequest = request(originalRequest)
        val initialResponse = chain.proceed(authenticationRequest)

        Log.e("Log_originalRequest", originalRequest.toString());
        Log.e("Log_ResponseCode", initialResponse.body.toString());

        when {
            initialResponse.code == 401 -> {
                val responseNewTokenLoginModel =
                    RetrofitManager.retrofit.create(API_Interface::class.java).refreshToken(
                        "refresh_token",
                        Constants.CLIENT_ID,
                        Constants.CLIENT_SECRET,
                        Constants.REDIRECT_URI,
                        SharedPreferencesUtil.getRefreshToken(GlobalVar.get()),
                        SharedPreferencesUtil.getCodeVerifier(GlobalVar.get())
                    ).execute()

                when {
                    responseNewTokenLoginModel == null || responseNewTokenLoginModel.code() != 200 -> {
                        return initialResponse
                    }
                    else -> {
                        initialResponse.body?.close();
                        responseNewTokenLoginModel.body()?.close()
                        var strResponse = responseNewTokenLoginModel.body()?.string()

                        val jsonObject = JSONObject(strResponse)
                        val accesstoken: String = jsonObject.getString("access_token")
                        val token_type = jsonObject.getString("token_type")

                        SharedPreferencesUtil.setAuthToken(
                            GlobalVar.get(),
                            "$token_type $accesstoken"
                        )
                        val newAuthenticationRequest = request(originalRequest)
                        return chain.proceed(newAuthenticationRequest)
                    }
                }
            }
            else -> return initialResponse


        }

    }

    private fun request(originalRequest: Request): Request {
        if (originalRequest.url.toString().contains("/-/oauth_token")) {
            return originalRequest;
        }
        val token = SharedPreferencesUtil.getAuthToken(GlobalVar.get())
        Log.e("token--", token);
        return originalRequest.newBuilder()
            .addHeader("Authorization", token.toString())
            .addHeader("Content-Type", "application/json")
            .build()


    }

    /**
     * Authenticator for when the authToken need to be refresh and updated
     * everytime we get a 401 error code
     */

    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        val token: String = SharedPreferencesUtil.getAuthToken(GlobalVar.get())
        Log.e("Log_token auth", token.toString());
        if (token.toString().equals(response.request.header("Authorization"))) {
            return null; // If we already failed with these credentials, don't retry.
        }
        try {
            requestAvailable = response.request.newBuilder()
                .addHeader("Authorization", token.toString())
                .addHeader("Content-Type", "application/json")
                .build()
            return requestAvailable
        } catch (ex: Exception) {
        }
        return requestAvailable
    }

}