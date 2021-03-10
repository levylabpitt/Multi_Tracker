package com.pqiorg.multitracker.tag_anchor;

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

import com.synapse.ProgressHUD;
import com.synapse.Utility;
import com.synapse.model.QRCode;
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
public class TagAnchorsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private CaptureManager capture;
    int scannedQRCount = 0;

    private final int RC_STORAGE = 1425;
    Bitmap bitmap;

    ProgressHUD mProgressHUD;

    @BindView(R.id.QR_count)
    TextView QR_count;

    @BindView(R.id.webview)
    WebView webview;
    int URLCount = 0;

    List<QRCode> QRCodeDataList = new ArrayList<>();


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
            Log.e("QR Scanned", result.getText());
            if (Utility.QRAlreadyScannedNew(QRCodeDataList, result.getText())) return;
            QRCodeDataList.add(new QRCode(result.getText(),"",""));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.continuous_scan3);
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


    }

    @OnClick({R.id.btn_pause, R.id.btn_resume, R.id.btn_finish})
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

    }
    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        barcodeView.pause();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
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
    }

    private void UnshortenURL() {
        if (URLCount >= QRCodeDataList.size()) {  // task complete
            webview.destroy();
            prepareDataForBackgroundProcessing();
            return;
        }

        Log.e("Debugging...",URLCount +" URL Unshorten in Progress...");

        String url = QRCodeDataList.get(URLCount).getQrText();
        boolean isValidURL = Patterns.WEB_URL.matcher(url).matches();
        if (isValidURL &&  !(url.startsWith("http:") || (url.startsWith("https:")))) {
            url = "http://" + url;
        }

        if ( isValidURL && url.startsWith("https://app.asana.com") && URLCount < QRCodeDataList.size()) {
            QRCodeDataList.get(URLCount).setExpendedURL(url);
            URLCount++;
            UnshortenURL();
            return;
        }

       else if (!isValidURL && URLCount < QRCodeDataList.size()) {
            QRCodeDataList.get(URLCount).setExpendedURL(url);
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

                    QRCodeDataList.get(URLCount).setExpendedURL(expandedUrl);
                    URLCount++;
                    UnshortenURL();
                    /*if (expandedUrl.contains("/")) {
                        String asanaURL = expandedUrl.substring(expandedUrl.lastIndexOf("/") + 1);
                        QRCodeDataList.get(URLCount).setTaskId(asanaURL);
                        URLCount++;
                        UnshortenURL();
                    } else {
                        Log.e("Error", "Link not resolved to valid asana URL");
                    }

                    */

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

    private void prepareDataForBackgroundProcessing(){
        ArrayList<String> expandedURLList=new ArrayList<>();


        for(QRCode qrCode : QRCodeDataList) {
            if(qrCode.getExpendedURL() !=null && !qrCode.getExpendedURL().isEmpty())
            expandedURLList.add(qrCode.getExpendedURL());
        }
        String[] expandedURArr = expandedURLList.toArray(new String[expandedURLList.size()]);


        Data data = new Data.Builder()
                .putStringArray("QRTextArray", expandedURArr)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(CreateNewAsanaProjectWorker.class)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .build();

        hideprogressdialog();

        WorkManager.getInstance(TagAnchorsActivity.this).enqueue(oneTimeWorkRequest);
        try { Toast.makeText(TagAnchorsActivity.this, "Uploading started in background...", Toast.LENGTH_SHORT).show(); }catch (Exception e){}
        finish();

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

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {

        // Toast.makeText(this, "Please allow the permissions", Toast.LENGTH_SHORT).show();
    }

    public void showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(TagAnchorsActivity.this, "", false);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}


