package com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager;

//import com.anoto.adna.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.http.NameValuePair;

public class ResponseManager {
    private static ResponseManager instance;
    private NetworkManager mNetworkManager = NetworkManager.getInstance();

    public static ResponseManager getInstance() {
        if (instance == null) {
            instance = new ResponseManager();
        }
        return instance;
    }

    public String analyseDeleteResponse(String str, List<NameValuePair> list) {
        this.mNetworkManager.connect(this.mNetworkManager.getServerMode());
        return this.mNetworkManager.performDELETE(str, list);
    }

    public String analyseGetResponse(String str) {
        this.mNetworkManager.connect(this.mNetworkManager.getServerMode());
        return this.mNetworkManager.getAndGetString(str);
    }

    public String analyseGetResponse(String str, List<NameValuePair> list) {
        DevLog.defaultLogging("JH analyseGetResponse 1111");
        this.mNetworkManager.connect(this.mNetworkManager.getServerMode());
        return this.mNetworkManager.getAndGetString(str, list);
    }

    public String analysePostResponse(String str, List<NameValuePair> list) {
        this.mNetworkManager.connect(this.mNetworkManager.getServerMode());
        return this.mNetworkManager.postAndGetString(str, list);
    }

    public String analysePutResponse(String str, List<NameValuePair> list) {
        this.mNetworkManager.connect(this.mNetworkManager.getServerMode());
        return this.mNetworkManager.putString(str, list);
    }

    public String requestGetResponse(String str) {
        try {
            this.mNetworkManager.connect(new URL(str).getHost());
            return this.mNetworkManager.getAndGetRequest(str);
        } catch (MalformedURLException unused) {
            return null;
        }
    }
}
