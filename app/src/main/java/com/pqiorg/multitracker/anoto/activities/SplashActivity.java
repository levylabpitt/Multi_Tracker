package com.pqiorg.multitracker.anoto.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
/*import android.support.annotation.NonNull;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.app.AppCompatActivity;*/
import android.util.Log;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.VersionObject.VersionData;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.PermissionUtil;
import com.anoto.adna.util.PermissionUtil.IPermissionListener;*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.appevents.AppEventsConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.VersionObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements ADNAListener, PermissionUtil.IPermissionListener {
    /* access modifiers changed from: private */
    public String TAG = "SplashActivity";
    private ADNAClient mApiClient;
    /* access modifiers changed from: private */
    public PermissionUtil mPermissionUtil;
    private SettingManager mSettingManager;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        startActivity(new Intent(SplashActivity.this.getApplicationContext(), MainActivity.class));

       // initADNA();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("SDK : ");
        sb.append(VERSION.SDK_INT);
        Log.e(str, sb.toString());
        if (VERSION.SDK_INT < 23) {
           // appVersionCheck();
            return;
        }
        this.mPermissionUtil = new PermissionUtil(this);
        this.mPermissionUtil.setListener(this);
        this.mPermissionUtil.requestPermissions();
    }

    private void appVersionCheck() {
        String versionName = BasicUtil.getVersionName(getApplicationContext());
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("APP Version Code: ");
        sb.append(versionName);
        Log.e(str, sb.toString());
        if (this.mApiClient != null) {
            this.mApiClient.getVersion();
        }
    }

    private void initADNA() {
        String string = getResources().getString(R.string.server_address);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setServerURL(string);
        String string2 = getResources().getString(R.string.adna_app_id);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setApiKey(string2);
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this);
    }

    private void showUpdateDialog(boolean z) {
        AlertDialog.Builder builder = VERSION.SDK_INT >= 21 ? new AlertDialog.Builder(this, 16974374) : new AlertDialog.Builder(this);
        int i = R.string.txt_version_update;
        if (z) {
            i = R.string.txt_version_update_force;
        }
        builder.setTitle((int) R.string.txt_update);
        builder.setMessage(i);
        builder.setIcon((int) R.drawable.ic_dialog_alert);
        builder.setPositiveButton((int) R.string.txt_update, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.intent.action.VIEW");
                StringBuilder sb = new StringBuilder();
                sb.append("market://details?id=");
                sb.append(SplashActivity.this.getPackageName());
                intent.setData(Uri.parse(sb.toString()));
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        });
        if (!z) {
            builder.setNegativeButton((int) R.string.txt_later, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SplashActivity.this.onBackPressed();
                    SplashActivity.this.startNextActivity();
                }
            });
        }
        builder.setCancelable(false);
        builder.show();
    }

    /* access modifiers changed from: private */
    public void startNextActivity() {
        final boolean z = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_about_dont_show", false);
        new Timer().schedule(new TimerTask() {
            public void run() {
                SplashActivity splashActivity;
                Intent putExtra;
                if (z) {
                    splashActivity = SplashActivity.this;
                    putExtra = new Intent(SplashActivity.this.getApplicationContext(), MainActivity.class);
                } else {
                    splashActivity = SplashActivity.this;
                    putExtra = new Intent(SplashActivity.this.getApplicationContext(), AboutActivity.class).putExtra("from", SplashActivity.class.getSimpleName());
                }
                splashActivity.startActivity(putExtra);
                SplashActivity.this.finish();
            }
        }, 1500);
    }

    public void hasPermissions() {
        appVersionCheck();
    }

    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */


    public void onFailedToReceiveADNA(int i, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("onFailedToReceiveADNA. ");
        sb.append(i);
        sb.append(" ");
        sb.append(str);
        DevLog.defaultLogging(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Error Type: ");
        sb2.append(i);
        sb2.append(" Error Message:");
        sb2.append(str);
        DevLog.defaultLogging(sb2.toString());
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        startNextActivity();
    }

    public void onReceiveADNA(int i, Object obj) {
        if (i == 1) {
            String versionName = BasicUtil.getVersionName(getApplicationContext());
            StringBuilder sb = new StringBuilder();
            sb.append("APP Version Code: ");
            sb.append(versionName);
            DevLog.defaultLogging(sb.toString());
            VersionObject.VersionData versionData = (VersionObject.VersionData) obj;
            DevLog.defaultLogging("Version onSuccess.");
            DevLog.defaultLogging(versionData.result.toString());
            DevLog.defaultLogging(versionData.data.toString());
            if (versionData.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("APP Version Code: ");
                sb2.append(versionName);
                sb2.append(":");
                sb2.append(versionData.data.app_version);
                DevLog.defaultLogging(sb2.toString());
                if (!versionName.equals(versionData.data.app_version)) {
                    if ("Y".equals(versionData.data.force_update_yn)) {
                        showUpdateDialog(true);
                        return;
                    } else {
                        showUpdateDialog(false);
                        return;
                    }
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Code");
                sb3.append(versionData.result.code);
                sb3.append("\n");
                sb3.append(versionData.result.message);
                DevLog.defaultLogging(sb3.toString());
            }
            startNextActivity();
        }
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        runOnUiThread(new Runnable() {
            public void run() {
                SplashActivity.this.mPermissionUtil.onRequestPermissionsResult(i, strArr, iArr);
                for (int i = 0; i < strArr.length; i++) {
                    String b = SplashActivity.this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("permissions ");
                    sb.append(i);
                    sb.append(": ");
                    sb.append(strArr[i]);
                    sb.append(" / ");
                    sb.append(iArr[i]);
                    Log.e(b, sb.toString());
                }
            }
        });
    }
}
