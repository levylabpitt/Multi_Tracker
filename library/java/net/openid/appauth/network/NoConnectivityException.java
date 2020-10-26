package net.openid.appauth.network;

import java.io.IOException;

/**
 * Created by Jitendra on 22,March,2019
 */
public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }

}
