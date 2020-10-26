package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
/*import android.support.annotation.NonNull;
import android.support.p003v7.app.AlertDialog.Builder;*/
import android.view.View;
/*import com.anoto.adna.sdk.C0635R;*/
import androidx.annotation.NonNull;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
//import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
///import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Pattern;

public class BasicUtil {
    public static boolean checkEmail(Context context, String str) {
        return Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$").matcher(str).matches();
    }
    @NonNull
    public static Activity getActivity(View view) {
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("View ");
        sb.append(view);
        sb.append(" is not attached to an Activity");
        throw new IllegalStateException(sb.toString());
    }

    public static String getDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getUserAccount(Context context) {

       return GlobalVar.USER_ACCOUNT;
      //  return BeaconReferenceApplication.USER_ACCOUNT;
    }
    public static String getIP(Context context) {
        try {
            int ipAddress = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
            return String.format(Locale.getDefault(), "%d.%d.%d.%d", new Object[]{Integer.valueOf(ipAddress & 255), Integer.valueOf((ipAddress >> 8) & 255), Integer.valueOf((ipAddress >> 16) & 255), Integer.valueOf((ipAddress >> 24) & 255)});
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getMobileIP() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (true) {
                    if (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException unused) {
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        return packageInfo.versionCode;
    }

    public static String getVersionName(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        return packageInfo.versionName;
    }

    public static void showAlertDialog(final Context context, String str) {
        (VERSION.SDK_INT >= 21 ? new AlertDialog.Builder(context) : new AlertDialog.Builder(context)).setMessage((CharSequence) str).setPositiveButton(R.string.txt_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity) context).onBackPressed();
            }
        }).setIcon(R.mipmap.ic_launcher).show();
    }
}
