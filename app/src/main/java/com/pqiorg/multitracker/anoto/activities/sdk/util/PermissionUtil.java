package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
/*import android.support.annotation.NonNull;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;*/
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pqiorg.multitracker.R;
//import com.anoto.adna.sdk.R;

public class PermissionUtil {
    private final int MY_PERMISSIONS_REQUEST_ALL = 0;
    private String TAG = "PermissionUtil";
    private boolean mCameraPermission;
    private boolean mLocationPermission;
    /* access modifiers changed from: private */
    public Activity mParentActivity;
    private IPermissionListener mPermissionListener;
    private boolean mPhoneStatePermission;
    private boolean mStoragePermission;

    public interface IPermissionListener {
        void hasPermissions();
    }

    public PermissionUtil(Activity activity) {
        this.mParentActivity = activity;
    }

    public static boolean isNetworkConnect(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void requestAllPermissions() {
        if (VERSION.SDK_INT >= 23) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.mParentActivity, "android.permission.CAMERA")) {
                showRequestPermissionRationale(0);
            } else {
                ActivityCompat.requestPermissions(this.mParentActivity, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE"}, 0);
            }
        }
    }

    @TargetApi(17)
    private void showRequestPermissionRationale(final int i) {
        int i2;
        final String[] strArr;
        if (VERSION.SDK_INT >= 23) {
            boolean z = true;
            if (i == 0) {
                strArr = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};
                i2 = R.string.txt_permission_msg;
            } else {
                strArr = null;
                z = false;
                i2 = 0;
            }
            if (z) {
                new Builder(this.mParentActivity).setTitle(R.string.txt_permission_title).setMessage(i2).setIcon(R.drawable.ic_dialog_alert).setPositiveButton(R.string.txt_ok, null).setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        ActivityCompat.requestPermissions(PermissionUtil.this.mParentActivity, strArr, i);
                    }
                }).show();
            }
        }
    }

    public boolean hasPermissions() {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        return this.mCameraPermission && this.mStoragePermission && this.mLocationPermission && this.mPhoneStatePermission;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (VERSION.SDK_INT >= 23 && i == 0) {
            for (int i2 : iArr) {
                if (i2 == 0) {
                    requestPermissions();
                } else {
                    Toast.makeText(this.mParentActivity, "To use the app, you must allow all permissions.", Toast.LENGTH_SHORT).show();
                    this.mParentActivity.finish();
                }
            }
        }
    }

    public void requestPermissions() {
        if (VERSION.SDK_INT >= 23 && !this.mCameraPermission && !this.mStoragePermission && !this.mLocationPermission) {
            if (ContextCompat.checkSelfPermission(this.mParentActivity, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this.mParentActivity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(this.mParentActivity, "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(this.mParentActivity, "android.permission.READ_PHONE_STATE") == 0 && (ActivityCompat.checkSelfPermission(this.mParentActivity, "android.permission.WRITE_CALENDAR") == 0 || ActivityCompat.checkSelfPermission(this.mParentActivity, "android.permission.ACCESS_COARSE_LOCATION") == 0)) {
                Log.e(this.TAG, "requestPermissions() : all permission is allowed. ");
                this.mCameraPermission = true;
                this.mStoragePermission = true;
                this.mLocationPermission = true;
                this.mPhoneStatePermission = true;
                if (this.mPermissionListener != null) {
                    this.mPermissionListener.hasPermissions();
                }
            } else {
                Log.e(this.TAG, "requestPermissions(): permission is denied. ");
                requestAllPermissions();
            }
        }
    }

    public void setListener(IPermissionListener iPermissionListener) {
        this.mPermissionListener = iPermissionListener;
    }
}
