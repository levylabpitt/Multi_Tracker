package com.pqiorg.multitracker.qr_scanner;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.pqiorg.multitracker.drive.MimeUtils;
import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.qr_scanner.intent_service.SaveWebRequestService;
import com.synapse.ProgressHUD;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.model.TaskData;
import com.synapse.model.Task_data;
import com.synapse.model.projetcs.GetProjectsResponse;
import com.synapse.model.search_task.Datum;
import com.synapse.model.search_task.SearchTaskByWorkspace;
import com.synapse.model.task_detail.CustomField;
import com.synapse.model.task_detail.TaskDetail;
import com.synapse.model.tasks.TasksResponse;
import com.synapse.model.update_task.UpdateTAskResponse;
import com.synapse.model.upload_attachment.UploadAttachmentsResponse;
import com.synapse.model.user_detail.UserDetailsResponse;
import com.synapse.model.user_detail.Workspace;
import com.synapse.network.APIError;
import com.synapse.network.Constants;
import com.synapse.network.RequestListener;
import com.synapse.network.RetrofitManager;
import com.synapse.service.BeaconScannerService;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Response;

import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;
import static com.facebook.GraphRequest.TAG;


/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivityNew extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private CaptureManager capture;
    int scannedQRCount = 0;
    List<Task_data> AsanaTaskDataList = new ArrayList<>();
    private final int RC_STORAGE = 1425;
    Bitmap bitmap;

    ProgressHUD mProgressHUD;

    @BindView(R.id.ll_Save_QR_Image)
    LinearLayout ll_Save_QR_Image;

    @BindView(R.id.barcodePreview)
    ImageView imageView;

    /************Beacon************/
    private FscBeaconApi fscBeaconApi;
    private static final int ENABLE_BT_REQUEST_ID = 1;
    private Activity activity;
    /************Beacon************/
    @BindView(R.id.Beacon_count)
    TextView Beacon_count;

    @BindView(R.id.QR_count)
    TextView QR_count;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();
            bitmap = result.getBitmapWithResultPoints(Color.YELLOW);
            scannedQRCount++;
            QR_count.setText("QR scanned: " + scannedQRCount);
            // if (Utility.QRAlreadyScanned(scannedDataList, result.getText())) return;
            if (Utility.QRAlreadyScanned(AsanaTaskDataList, result.getText())) return;
            //  scannedQRDataList.add(new ScannedData(result.getText(), Utility.getCurrentDate(), "", "", "", "",bitmap,""));
            //   QR_count.setText("QR scanned: " + scannedQRDataList.size());
            AsanaTaskDataList.add(new Task_data(result.getText(), Utility.getCurrentDate(), "", bitmap, "", "", "", "", "", "", "", "", "", ""));


        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.continuous_scan2);
        ButterKnife.bind(this);

        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        capture = new CaptureManager(this, barcodeView);
        capture.setShowMissingCameraPermissionDialog(false);
        beepManager = new BeepManager(this);
        requiresStoragePermission();
        Utility.deleteQRScannerFiles();

        if (fscBeaconApi != null) {
            Log.e("Set", fscBeaconApi.isConnected() + "");
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

    }

    @OnClick({R.id.btn_pause, R.id.btn_resume, R.id.btn_finish,
            R.id.btn_save, R.id.imgvw_cancel, R.id.imgvw_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pause:
                pause_scan();
                break;
            case R.id.btn_resume:
                resume_scan();
                break;
            case R.id.btn_finish:
                finish_scan();
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
        barcodeView.resume();
        //  startBeacon();
        initFeasyBeaconAPI();
        if (!Utility.isMyServiceRunning(BeaconScannerService.class, this)) {
            this.startService(new Intent(this, BeaconScannerService.class));
        }

    }

    void initFeasyBeaconAPI() {
        fscBeaconApi = FscBeaconApiImp.getInstance(activity);
        fscBeaconApi.initialize();
        fscBeaconApi.startScan(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        barcodeView.pause();
        //  pauseBeacon();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void pause_scan() {
        barcodeView.pause();
    }

    public void resume_scan() {
        barcodeView.resume();
    }

    public void finish_scan() {
        if (scannedQRCount == 0) {
            Toast.makeText(this, "No QR code has been scanned!", Toast.LENGTH_SHORT).show();
            return;
        }

        barcodeView.pause();
        uploadMultipleFilesOnGoogleDriveFolder();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @AfterPermissionGranted(RC_STORAGE)
    private void requiresStoragePermission() {
        String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,//};
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                android.Manifest.permission.BLUETOOTH_PRIVILEGED};
        if (EasyPermissions.hasPermissions(this, perms)) {
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "This app needs to access phone's internal storage, GPS, Bluetooth.",
                    RC_STORAGE, perms);
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case ENABLE_BT_REQUEST_ID:
                if (resultCode == Activity.RESULT_CANCELED) {
                    btDisabled();
                    return;
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
        // Toast.makeText(this, "Please allow the permissions", Toast.LENGTH_SHORT).show();
    }

    public void showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(ContinuousCaptureActivityNew.this, "", false);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public void hideprogressdialog() {
        try {
            if (mProgressHUD != null && mProgressHUD.isShowing()) {
                mProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    void uploadMultipleFilesOnGoogleDriveFolder() {
        showprogressdialog();
        for (int j = 0; j < AsanaTaskDataList.size(); j++) {
            Task_data scannedData = AsanaTaskDataList.get(j);
            String filePath = Utility.saveImage(scannedData.getBitmap());
            scannedData.setBitmapFilePath(filePath);
            AsanaTaskDataList.set(j, scannedData);
        }

        String timestampBeacon = Utility.getCurrentTimestamp();// timestamp for beacons should be same for all QR code scanned at a time
        long timestamp = 0;
        timestamp = Long.parseLong(Utility.getCurrentTimestamp());// timestamp for each QR code scanned must be unique
        List<TaskData> dataListWithoutBitmap = new ArrayList<>();
        for (Task_data task_data : AsanaTaskDataList) {
            dataListWithoutBitmap.add(new TaskData(task_data.getQrText(),
                    task_data.getDateTime(),
                    String.valueOf(++timestamp),
                    task_data.getBitmapFilePath(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "In Progress",
                    timestampBeacon,
                    false,
                    "",
                    ""));
        }
        hideprogressdialog();
        Toast.makeText(this, "Uploading started in background...", Toast.LENGTH_SHORT).show();

        Intent msgIntent = new Intent(ContinuousCaptureActivityNew.this, SaveWebRequestService.class);
        msgIntent.putExtra(SaveWebRequestService.QR_DATA_LIST, (Serializable) dataListWithoutBitmap);
        startService(msgIntent);
        finishActivity();


        //   driveUploadAPICount = 0;
        //   uploadFileOnGoogleDriveFolder(AsanaTaskDataList.get(driveUploadAPICount).getBitmapFilePath(), driveUploadAPICount);


    }

    public void finishActivity() {
        this.finish();
    }


    private void btDisabled() {
        Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        finishActivity();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Beacon_count.setText("Beacons found: " + message);
        }
    };


}


