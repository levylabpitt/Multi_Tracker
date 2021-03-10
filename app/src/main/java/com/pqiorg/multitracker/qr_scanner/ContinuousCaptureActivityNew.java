package com.pqiorg.multitracker.qr_scanner;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.qr_scanner.intent_service.SaveAsanaDataWorker;
import com.room_db.Beacon;
import com.room_db.DatabaseClient;
import com.synapse.ProgressHUD;
import com.synapse.Utility;
import com.synapse.model.TaskData;
import com.synapse.model.Task_data;
import com.synapse.network.NetworkUtil;
import com.synapse.service.BeaconScannerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


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

    @BindView(R.id.webview)
    WebView webview;
    int URLCount = 0;
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
            Log.e("QR Scanned", result.getText());
            if (Utility.QRAlreadyScanned(AsanaTaskDataList, result.getText())) return;
            //  scannedQRDataList.add(new ScannedData(result.getText(), Utility.getCurrentDate(), "", "", "", "",bitmap,""));
            //   QR_count.setText("QR scanned: " + scannedQRDataList.size());
           ArrayList<String> dateAndTimestampList=Utility.getCurrentDateWithTimestamp();
           String currentDate="",currentTimestamp="";
            if(dateAndTimestampList.size()>0) currentDate=dateAndTimestampList.get(0);
            if(dateAndTimestampList.size()>1) currentTimestamp=dateAndTimestampList.get(1);

            AsanaTaskDataList.add(new Task_data(result.getText(), currentDate, currentTimestamp, bitmap, "", "", "", "", "", "", "", "", "", ""));


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
            this.startForegroundService(new Intent(this, BeaconScannerService.class));
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

        showprogressdialog();
        UnshortenURL();
        //saveQRBitmapToDeviceStorage();

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

    void saveQRBitmapToDeviceStorage() {
        //  showprogressdialog();

        for (int j = 0; j < AsanaTaskDataList.size(); j++) {
            Log.e("Debugging...",j +"Saving bitmap to local storage...");
            Task_data scannedData = AsanaTaskDataList.get(j);
            String filePath = Utility.saveImage(scannedData.getBitmap());
            scannedData.setBitmapFilePath(filePath);
            AsanaTaskDataList.set(j, scannedData);
        }


        String timestampBeacon = Utility.getCurrentTimestamp();// timestamp for beacons should be same for all QR code scanned at a time. This timestamp will be written to local database in QR table and Beacon table both so that corrosponding beacons can be found for a QR  with each Beacon. this timestamp will not be written in spreadsheet
    //    long timestamp = 0;
    //    timestamp = Long.parseLong(Utility.getCurrentTimestamp());// timestamp for each QR code scanned must be unique
        List<TaskData> dataListWithoutBitmap = new ArrayList<>();
        for (Task_data task_data : AsanaTaskDataList) {
            dataListWithoutBitmap.add(new TaskData(task_data.getQrText(),
                    task_data.getDateTime(),
                    task_data.getTimestamp(), // String.valueOf(++timestamp)
                    task_data.getBitmapFilePath(),
                    "",
                    "",
                    "",
                    task_data.getTaskId(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "In Progress",
                    timestampBeacon,  ///
                    false,
                    "",
                    "",
                    ""));
        }
        hideprogressdialog();

        if (!NetworkUtil.isOnline(this)) {
            Utility.showToast(this,"No internet available!");
            return;
        }

        new saveQRRecordsInLocalDBAsync(dataListWithoutBitmap).execute();


    }


    private void UnshortenURL() {

        if (URLCount >= AsanaTaskDataList.size()) {  // task complete
            webview.destroy();
            saveQRBitmapToDeviceStorage();
            return;
        }

        Log.e("Debugging...",URLCount +" URL Unshorten in Progress...");

        String url = AsanaTaskDataList.get(URLCount).getQrText();
        boolean isValidURL = Patterns.WEB_URL.matcher(url).matches();
        if (isValidURL &&  !(url.startsWith("http:") || (url.startsWith("https:")))) {
            url = "http://" + url;
        }

        if ( isValidURL && url.startsWith("https://app.asana.com") && URLCount < AsanaTaskDataList.size()) {
            String asanaURL = url.substring(url.lastIndexOf("/") + 1);
            AsanaTaskDataList.get(URLCount).setTaskId(asanaURL);
            URLCount++;
            UnshortenURL();
            return;
        }

       else if (!isValidURL && URLCount < AsanaTaskDataList.size()) {
            URLCount++;
            UnshortenURL();
            return;
        }


        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            // When finish loading page
            public void onPageFinished(WebView view, String url) {
                String expandedUrl = webview.getUrl();
                if (!expandedUrl.isEmpty() && expandedUrl.startsWith("https://app.asana.com")) {
                    webview.stopLoading();
                    if (expandedUrl.contains("/")) {
                        String asanaURL = expandedUrl.substring(expandedUrl.lastIndexOf("/") + 1);
                        AsanaTaskDataList.get(URLCount).setTaskId(asanaURL);
                        URLCount++;
                        UnshortenURL();
                    } else {
                        Log.e("Error", "Link not resolved to valid asana URL");
                    }
                }else if(expandedUrl.endsWith("404.html")){
                    URLCount++;
                    UnshortenURL();
                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                URLCount++;
                UnshortenURL();
            }
        });

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

    private class saveQRRecordsInLocalDBAsync extends AsyncTask<Void, Void, Void> {
        List<TaskData> dataListWithoutBitmap = new ArrayList<>();
        List<List<Object>> BluetoothBeaconDataList = new ArrayList<>();
        ArrayList<String> timestamps = new ArrayList<>();
        saveQRRecordsInLocalDBAsync(List<TaskData> dataListWithoutBitmap){
           this.dataListWithoutBitmap=dataListWithoutBitmap;
            BluetoothBeaconDataList = BeaconScannerService.getBeaconList();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveQRRecordsInLocalDB();
            saveBeaconRecordsInLocalDB();

            for(int i=0;i<dataListWithoutBitmap.size();i++){
                timestamps.add( dataListWithoutBitmap.get(i).getTimestamp());
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            String[] timestampsArr = timestamps.toArray(new String[timestamps.size()]);

            Data data = new Data.Builder()
                    .putStringArray("timestamps", timestampsArr)
                    .build();

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest oneTimeWorkRequest =
                    new OneTimeWorkRequest.Builder(SaveAsanaDataWorker.class)
                            .setInputData(data)
                            .setConstraints(constraints)
                            .build();

            WorkManager.getInstance(ContinuousCaptureActivityNew.this).enqueue(oneTimeWorkRequest);
           try { Toast.makeText(ContinuousCaptureActivityNew.this, "Uploading started in background...", Toast.LENGTH_SHORT).show(); }catch (Exception e){}
            finish();

        }

        void saveQRRecordsInLocalDB() {
            for (int i = 0; i < dataListWithoutBitmap.size(); i++) {
                TaskData task = dataListWithoutBitmap.get(i);
                try {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .insert(task);

                    Log.e("Debugging...Local DB Written:", " successfully");
                } catch (Exception e) {
                    Log.e("Debugging...Local DB Written:", " failed: " + e.getMessage());
                    Utility.ReportNonFatalError("saveQRRecordsInLocalDB", e.getMessage());
                }
            }
        }
        void saveBeaconRecordsInLocalDB() {
            if (BluetoothBeaconDataList == null || BluetoothBeaconDataList.isEmpty()) return;
            for (List<Object> beaconData : BluetoothBeaconDataList) {
                try {
                    Beacon beacon = new Beacon(String.valueOf(beaconData.get(0)),
                            String.valueOf(beaconData.get(2)),
                            String.valueOf(beaconData.get(1)),
                            String.valueOf(beaconData.get(3)),
                            String.valueOf(beaconData.get(4)),
                            String.valueOf(beaconData.get(5)),
                            String.valueOf(beaconData.get(6)),
                            String.valueOf(beaconData.get(7)),
                            dataListWithoutBitmap.get(0).getTimestampBeacon());// so that by using timestamp of a qr , corresponding beacons can be retrieved

                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .beaconDao()
                            .insert(beacon);
                } catch (Exception e) {
                    Utility.ReportNonFatalError("saveQRRecordsInLocalDB", e.getMessage());
                }
            }


        }


    }
}


