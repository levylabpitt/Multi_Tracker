package net.openid.appauth.network;

import android.app.Application;
public class GlobalVar extends Application {

    private static GlobalVar mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        String s="";


    }


    public static GlobalVar get() {
        return mInstance;
    }
}
