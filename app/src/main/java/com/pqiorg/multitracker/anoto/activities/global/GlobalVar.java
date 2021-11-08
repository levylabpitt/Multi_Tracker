package com.pqiorg.multitracker.anoto.activities.global;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.pqiorg.multitracker.BuildConfig;
import com.synapse.AppResumeListener;
import com.synapse.AppVisibilityDetector;
import com.synapse.Constants;
import com.synapse.HomeActivity;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.service.BeaconScannerService;


public class GlobalVar extends Application {
    public static String USER_ACCOUNT = "";
    private static GlobalVar mInstance = null;
    private static AppResumeListener resumeEventReceiverListener;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mInstance = this;
        checkForApplicationLevelEvents();
      //  System.loadLibrary("adna-sdk");

        try {
            // initialize();
        } catch (Exception e) {
            e.getMessage();

        }

    }

    public void setAppResumeListener(AppResumeListener listener) {
        resumeEventReceiverListener = listener;
    }

    protected static native void initialize();

    private void checkForApplicationLevelEvents() {
        AppVisibilityDetector.init(mInstance, new AppVisibilityDetector.AppVisibilityCallback() {
            @Override
            public void onAppGotoForeground() {
                //app is from background to foreground
                Utility.ReportNonFatalError("Application-----", "GotoForeground");

                SharedPreferencesUtil.setAppInForeground(mInstance, true);


                if (!Utility.isMyServiceRunning(BeaconScannerService.class, mInstance)) {
                 startForegroundService(new Intent(getApplicationContext(), BeaconScannerService.class));
                }


                Log.e("CHECK", "background to foreground: ");
                if (resumeEventReceiverListener != null) {
                    resumeEventReceiverListener.onAppResume();
                }
            }

            @Override
            public void onAppGotoBackground() {
                Utility.ReportNonFatalError("Application-----", "Gotobackground");
                //app is from foreground to background
                SharedPreferencesUtil.setAppInForeground(mInstance, false);
                Log.e("CHECK", "foreground to background :");
                stopService(new Intent(getApplicationContext(),BeaconScannerService.class));
                if (!BuildConfig.DEBUG) {
                    // Utility.killAppFromBg();
                }

            }
        });
    }

    public static GlobalVar get() {
        return mInstance;
    }
}
