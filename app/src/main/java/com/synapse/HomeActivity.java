package com.synapse;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import com.pqiorg.multitracker.drive.DriveActivity;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.pqiorg.multitracker.openid_appauth.AsanaLoginActivity;
import com.pqiorg.multitracker.R;
//import com.pqiorg.multitracker.beacon.beaconreference.MonitoringActivity;
import com.pqiorg.multitracker.feasybeacon.TabbedActivityBeacon;
import com.pqiorg.multitracker.qr_scanner.ContinuousCaptureActivityNew;
import com.pqiorg.multitracker.spreadsheet.creater.SpreadSheetListActivity;
import com.pqiorg.multitracker.help.TabbedActivity;
import com.pqiorg.multitracker.tag_anchor.TagAnchorsActivity;
import com.synapse.network.APIError;
import com.synapse.network.Constants;
import com.synapse.network.NetworkUtil;
import com.synapse.network.NoConnectivityException;
import com.synapse.network.RequestListener;
import com.synapse.network.RetrofitManager;
import com.synapse.service.BeaconScannerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;


public class HomeActivity extends AppCompatActivity implements RequestListener {
    final int PERMISSION_REQUEST_CODE = 100;
    static String[] Permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    LocationManager locationManager;

    int GPS_REQUEST = 111;
    private FscBeaconApi fscBeaconApi;
    private static final int ENABLE_BT_REQUEST_ID = 1;


    @BindView(R.id.btn_beacon)
    Button btn_beacon;

    @BindView(R.id.webview)
    WebView webview;


    private final RetrofitManager retrofitManager = RetrofitManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        //    startNotification();

        //   sendNotification("dddd","Ddddddd");

        hitAPIRefreshToken();
        if (CheckingPermissionIsEnabledOrNot(this)) {
            CheckGpsStatus();
        } else {
            RequestMultiplePermission();
        }


    }


    private void hitAPIRefreshToken() {
        Log.e("Token", SharedPreferencesUtil.getAuthToken(this));
        retrofitManager.refreshToken(this, this, Constants.API_TYPE.REFRESH_TOKEN, SharedPreferencesUtil.getRefreshToken(this), SharedPreferencesUtil.getCodeVerifier(this), false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initFeasyBeaconAPI();
            if (!Utility.isMyServiceRunning(BeaconScannerService.class, HomeActivity.this)) {
                this.startForegroundService(new Intent(this, BeaconScannerService.class));
            }
        } catch (Exception e) {
            Utility.ReportNonFatalError("Home", e.getMessage());
        }
    }


    void initFeasyBeaconAPI() { // Todo
        //if (fscBeaconApi == null) {
        fscBeaconApi = FscBeaconApiImp.getInstance(this);
        // }
        if (fscBeaconApi == null) return;
        fscBeaconApi.initialize();
        if (!fscBeaconApi.isBtEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
            return;
        }
        fscBeaconApi.startScan(0);
    }

    public static boolean CheckingPermissionIsEnabledOrNot(Context context) {
        return
                ContextCompat.checkSelfPermission(context, Permissions[0]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[1]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[2]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[3]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[4]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[5]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[6]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Permissions[7]) == PackageManager.PERMISSION_GRANTED;

    }


    private void RequestMultiplePermission() {
        try {
            ActivityCompat.requestPermissions(this, Permissions, PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* @Override
     public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
         switch (requestCode) {
             case PERMISSION_REQUEST_CODE:
                 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     // main logic
                 } else {
                     Toast.makeText(getApplicationContext(), "Permission required!", Toast.LENGTH_SHORT).show();

                 }
                 break;
         }
     }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 2) {
                    boolean Camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean LOCATION = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean bLUETOOTH = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (LOCATION) {
                        CheckGpsStatus();
                    }
                }
                break;
        }
    }

    @OnClick({
            R.id.btn_adna,
            R.id.btn_beacon,
            R.id.btn_qr,
            R.id.btn_spreadsheet,
            R.id.btn_drive, R.id.btn_asana, R.id.btn_help, R.id.btn_anchor_tagging
    })
    public void onClick(@NonNull View view) {

        switch (view.getId()) {
            case R.id.btn_adna:
               /* if (checkPermission()) {
                    startActivity(new Intent(this, MyScanActivity.class));
                } else {
                    requestPermission();
                }*/

                String msg = "aDNA sdk initialization crashing the app.";
                Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_beacon:

                //  startActivity(new Intent(this, MonitoringActivity.class));
                //  startActivity(new Intent(this, MainActivityBeacon.class));
                // startActivity(new Intent(this, MainActivityBeacon2.class));

                startActivity(new Intent(this, TabbedActivityBeacon.class));
                break;
            case R.id.btn_qr:
                if (!NetworkUtil.isOnline(this)) {
                    Utility.showToast(this, "No internet available!");
                    return;
                }

                checkDefaultSpreadSheetAvailability();

                break;

            case R.id.btn_spreadsheet:

                if (!NetworkUtil.isOnline(this)) {
                    Utility.showToast(this, "No internet available!");
                    return;
                }


                startActivity(new Intent(HomeActivity.this, SpreadSheetListActivity.class));
                break;
            case R.id.btn_drive:
                if (!NetworkUtil.isOnline(this)) {
                    Utility.showToast(this, "No internet available!");
                    return;
                }

                startActivity(new Intent(HomeActivity.this, DriveActivity.class));

                break;

            case R.id.btn_asana:
                if (!NetworkUtil.isOnline(this)) {
                    Utility.showToast(this, "No internet available!");
                    return;
                }

                startActivity(new Intent(HomeActivity.this, AsanaLoginActivity.class));

                break;
            case R.id.btn_help:
                startActivity(new Intent(HomeActivity.this, TabbedActivity.class));

                break;

            case R.id.btn_anchor_tagging:
                if (!NetworkUtil.isOnline(this)) {
                    Utility.showToast(this, "No internet available!");
                    return;
                }
                if (SharedPreferencesUtil.getCurrentLoggedInUserWorkspaceId(this).equals("")) {
                    showAppCompatDialog("Please login to Asana !!", 3);
                } else if (SharedPreferencesUtil.getTeamNameForCreatingNewProject(this).equals("")) {
                    showAppCompatDialog("Please choose default team !!", 3);
                } else if (SharedPreferencesUtil.getLevyLabWorkspaceId(this).equals("")) {
                    showAppCompatDialog("Please choose organization for updating task on Asana !!", 3);
                } else {
                    startActivity(new Intent(HomeActivity.this, TagAnchorsActivity.class));
                }

                break;

        }

    }


    void checkDefaultSpreadSheetAvailability() {
        if (SharedPreferencesUtil.getDefaultSheetId(this).equals("")) {
            showAppCompatDialog("Please choose default google spreadsheet first !!", 1);
        } else if (SharedPreferencesUtil.getDefaultDriveFolderId(this).equals("")) {
            showAppCompatDialog("Please choose default google drive folder for uploading QR Photo !!", 2);
        } /*else if (SharedPreferencesUtil.getDefaultBluetoothSheet(this).equals("")) {
            startActivity(new Intent(HomeActivity.this, SpreadSheetListActivity.class));
        } */ else if (SharedPreferencesUtil.getCurrentLoggedInUserWorkspaceId(this).equals("")) {
            showAppCompatDialog("Please login to Asana !!", 3);
        } else if (SharedPreferencesUtil.getLevyLabWorkspaceId(this).equals("")) {
            showAppCompatDialog("Please choose organization for updating task on Asana !!", 3);
        } else {
            startActivity(new Intent(HomeActivity.this, ContinuousCaptureActivityNew.class));

        }
    }


    protected void showAppCompatDialog(String msg, int type) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setMessage(msg)
                .setTitle("Notice")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (type == 1) {
                            startActivity(new Intent(HomeActivity.this, SpreadSheetListActivity.class));
                        } else if (type == 2) {
                            startActivity(new Intent(HomeActivity.this, DriveActivity.class));
                        } else if (type == 3) {
                            startActivity(new Intent(HomeActivity.this, AsanaLoginActivity.class));
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .show();


    }

    @Override
    protected void onStop() {
        super.onStop();
        //  android.os.Process.killProcess(android.os.Process.myPid()); // stopping timertask
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.killAppFromBg();
    }

    public void CheckGpsStatus() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus) {
            //textview.setText("GPS Is Enabled");
        } else {
            // textview.setText("GPS Is Disabled");
          /*  Toast.makeText(getApplicationContext(), "Please enable Location!", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent1, GPS_REQUEST);*/

            enableGpsDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST) {
            CheckGpsStatus();
        } else if (requestCode == ENABLE_BT_REQUEST_ID) {
            if (resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
            }
        }
        stopService(new Intent(getApplicationContext(), BeaconScannerService.class));
        onResume();
       /* Intent stopIntent=new Intent(getApplicationContext(),BeaconScannerService.class);
        stopIntent.setAction("Stop");
        startService(stopIntent);*/
        //  stopService(new Intent(getApplicationContext(),BeaconScannerService.class));
    }


    private void enableGpsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable GPS");
        builder.setPositiveButton("OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent1, GPS_REQUEST);


            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    private void btDisabled() {
        Toast.makeText(this, "Sorry, Bluetooth needs to be turned ON for beacon scanner to work!", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        try {
            String strResponse = response.body().string();
            Log.e("APIResponse---->", strResponse);
            if (apiType == Constants.API_TYPE.REFRESH_TOKEN) {
                JSONObject jsonObject = new JSONObject(strResponse);
                String token = jsonObject.getString("access_token");
                String token_type = jsonObject.getString("token_type");
                SharedPreferencesUtil.setAuthToken(this, token_type + " " + token);

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Response<ResponseBody> response, Constants.API_TYPE apiType) {
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, Constants.API_TYPE apiType) {
    }
}
