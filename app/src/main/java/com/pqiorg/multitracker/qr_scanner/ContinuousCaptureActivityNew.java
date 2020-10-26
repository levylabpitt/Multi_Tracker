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

//import com.pqiorg.multitracker.feasybeacon.Activity.SetActivity;


/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivityNew extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        RequestListener {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private CaptureManager capture;

    int scannedQRCount = 0;

    //  List<ScannedData> scannedQRDataList = new ArrayList<>(); // can have duplicate # Client Requirement
    List<Task_data> AsanaTaskDataList = new ArrayList<>();           // can't have duplicate
    int strongestSignalBeaconRSSI = 0;
    String strongestSignalBeaconUUID;
    private final int RC_STORAGE = 1425;
    Bitmap bitmap;
    //  List<BitmapData> bitmapList = new ArrayList<>();
    //   private ArrayList<BluetoothDeviceWrapper> mDevices = new ArrayList<BluetoothDeviceWrapper>();


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

            //  Utility.addData(result.getText(), AsanaTaskDataList);

            //Added preview of scanned barcode
        /*    if (ll_Save_QR_Image.getVisibility() != View.VISIBLE)
                ll_Save_QR_Image.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));*/

            //  bitmapList.add(new BitmapData(result.getText(), bitmap));
            // saveQRImageOnLocalStorage(bitmap);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    /**************************Write to spreadsheet***********************************************/
    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    ProgressHUD mProgressHUD;

    /*************************************************************************/

    @BindView(R.id.ll_Save_QR_Image)
    LinearLayout ll_Save_QR_Image;

    @BindView(R.id.barcodePreview)
    ImageView imageView;


    private GoogleSignInClient mGoogleSignInClient;
    private DriveServiceHelper mDriveServiceHelper;
    private static final int REQUEST_CODE_SIGN_IN = 100;
    //String filePath = "";
    //  List<String> listFilePath = new ArrayList<>();
    /************Beacon************/
    private FscBeaconApi fscBeaconApi;
    private static final int ENABLE_BT_REQUEST_ID = 1;
    // private Handler handler = new Handler();
    //   private static Timer timerUI;
    //   private static TimerTask timerTask;
    Queue<BluetoothDeviceWrapper> deviceQueue = new LinkedList<BluetoothDeviceWrapper>();
    private Activity activity;

    /************Beacon************/
    @BindView(R.id.Beacon_count)
    TextView Beacon_count;

    @BindView(R.id.QR_count)
    TextView QR_count;


    private RetrofitManager retrofitManager = RetrofitManager.getInstance();
    int limit = 100;
    String LevyLab_project_gid = "", LevyLab_workspace_gid = "";
    String offset = "";


    // int Count_Image_Uploaded = 0;

    int taskDetailAPICount = 0, taskUpdateAPICount = 0, attachmentUploadAPICount = 0, feasybeaconTaskDetailAPICount = 0, feasybeaconTaskUpdateAPICount = 0, driveUploadAPICount = 0, taskSearchAPICount = 0;

    final String LevyLabProject = "LevyLab AoT (all items)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.continuous_scan2);
        ButterKnife.bind(this);
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        barcodeView = findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        capture = new CaptureManager(this, barcodeView);
        capture.setShowMissingCameraPermissionDialog(false);
        beepManager = new BeepManager(this);
        requiresStoragePermission();
        getResultsFromApi();

        Utility.deleteQRScannerFiles();


        /********************Beacon***********************/
        if (fscBeaconApi != null) {
            Log.e("Set", fscBeaconApi.isConnected() + "");
        }
        /********************Beacon***********************/


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

    }

    private void callApiToWriteDataOnAsana() {
       // String authToken = SharedPreferencesUtil.getAuthToken(this);
      //  String refreshToken = SharedPreferencesUtil.getRefreshToken(this);
     //   String user_gid = SharedPreferencesUtil.getAsanaUserId(this);
     //   String workspace_gid = SharedPreferencesUtil.getAsanaWorkspaceId(this);
        LevyLab_workspace_gid = SharedPreferencesUtil.getLevyLabWorkspaceId(this);
        LevyLab_project_gid = SharedPreferencesUtil.getLevyLabProjectId(this);
        // if (LevyLab_project_gid.equals("")) {
        if (LevyLab_workspace_gid.equals("")) {
            hitAPIGetAsanaUserDetails();
        } else {
            //hitAPIGetTasksByProject(Levy_Lab_project_gid, limit);
            taskSearchAPICount = 0;
            hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());
        }
    }

    private void hitAPIGetAsanaUserDetails() {
        retrofitManager.getUserDetails(this, this, Constants.API_TYPE.GET_USER_DETAILS, true);
    }

 /*   private void hitAPIGetTeamsForUser() {
        String user_gid = SharedPreferencesUtil.getAsanaUserId(this);
        String workspaceId_gid = SharedPreferencesUtil.getAsanaWorkspaceId(this);
        retrofitManager.getTeamsForUser(this, this, Constants.API_TYPE.GET_TEAMS_FOR_USER, user_gid, workspaceId_gid, true);
    }*/

  /*  private void hitAPIGetProjects() {
        retrofitManager.getProjects(this, this, Constants.API_TYPE.GET_PROJECTS, true);
    }*/

    private void hitAPIGetProjectsByWorkspace(String workspace_gid) {
        retrofitManager.getProjectsByWorkspace(this, this, Constants.API_TYPE.GET_PROJECTS_BY_WORKSPACE, workspace_gid, true);
    }


    private void hitAPISearchProjectByWorkspace(String workspace_gid, String search_project) {
        retrofitManager.searchProjectByWorkspace(this, this, Constants.API_TYPE.SEARCH_PROJECT_BY_WORKSPACE, workspace_gid, search_project, true);
    }

    private void hitAPISearchTaskByWorkspace(String workspace_gid, String search_task) {
        retrofitManager.searchTaskByWorkspace(this, this, Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE, workspace_gid, search_task, true);
    }
    private void hitAPISearchTaskByWorkspaceAssignedToMe(String workspace_gid, String search_task) {
        retrofitManager.searchTaskByWorkspaceAssignedToMe(this, this, Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE, workspace_gid, search_task, true);
    }


    private void hitAPIGetTasksByProject(String project_gid, int limit) {
        retrofitManager.getTasksByProject(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.GET_TASKS_BY_PROJECT, project_gid, limit, true);
    }

    private void hitAPIGetNextTasksByProject(String project_gid, int limit, String offset) {
        retrofitManager.getNextTasksByProject(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.GET_NEXT_TASKS_BY_PROJECT, project_gid, limit, offset, true);
    }

    private void hitAPIGetTaskDetails(String task_gid) {
        retrofitManager.getTaskDetails(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.TASK_DETAILS, task_gid, true);
    }

    private void hitAPIGetFeasybeaconTaskDetails(String task_gid) {
        retrofitManager.getTaskDetails(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.FEASYBEACON_TASK_DETAILS, task_gid, true);
    }

    private void hitAPIUpdateTask(String task_gid, JsonObject input) {
        retrofitManager.updateTask1(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.UPDATE_TASK, task_gid, input, true);
    }

    private void hitAPIUpdateFeasybeaconTask(String task_gid, JsonObject input) {
        retrofitManager.updateTask1(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.UPDATE_FEASYBEACON_TASK, task_gid, input, true);
    }

    private void hitAPIUploadAttachments(String task_gid, MultipartBody.Part IMAGE) {
        retrofitManager.uploadAttachment(this, ContinuousCaptureActivityNew.this, Constants.API_TYPE.UPLOAD_ATTACHMENTS, IMAGE, task_gid, true);
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
          /*  case R.id.btn_save:
                Save(view);
                break;
            case R.id.imgvw_save:
                Save(view);
                break;
            case R.id.imgvw_cancel:
                Cancel();
                break;*/


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

/*    void pauseBeacon() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timerUI != null) {
            timerUI.cancel();
            timerUI.purge();
            timerUI = null;
        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        /*
        Intent i2 = new Intent(this, ChatHeadService.class);
        stopService(i2);
    */

    }

    @Override
    protected void onStop() {
        super.onStop();
        // pauseBeacon();
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
        Toast.makeText(this, "Uploading started in background...", Toast.LENGTH_SHORT).show();
        barcodeView.pause();
        uploadMultipleFilesOnGoogleDriveFolder();

    }

    /*  protected void Save(View view) {
          barcodeView.pause();
          String driveFolderID = SharedPreferencesUtil.getDefaultDriveFolder(this);
          if (driveFolderID.equals("")) {
              Toast.makeText(this, "Please choose default Google Drive folder first!", Toast.LENGTH_SHORT).show();
          } else {
              if (bitmap != null) {
                  filePath = Utility.saveImage(bitmap);
                  uploadFileOnGoogleDriveFolder(filePath, 0);

              }
          }


      }

      protected void Cancel() {
          barcodeView.resume();
          ll_Save_QR_Image.setVisibility(View.INVISIBLE);
      }

      public void triggerScan(View view) {
          barcodeView.decodeSingle(callback);
      }
  */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    /**************************Write to spreadsheet***********************************************/

    public void getResultsFromApi() {
        String accountName = SharedPreferencesUtil.getAccountName(this);
        Utility.ReportNonFatalError("getResultsFromApi", accountName);
        if (accountName != null) {
            mCredential.setSelectedAccountName(accountName);
        }

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.e(this.toString(), "No network connection available.");
        }
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


    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = SharedPreferencesUtil.getAccountName(this);
            Utility.ReportNonFatalError("chooseAccount", accountName);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
                Utility.ReportNonFatalError("startActivityForResult", REQUEST_ACCOUNT_PICKER + "");
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
            Utility.ReportNonFatalError("EasyPermissions.requestPermissions", "This app needs to access your Google account (via Contacts)");
        }
    }


    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.e(this.toString(), "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.");
                    Utility.ReportNonFatalError("REQUEST_GOOGLE_PLAY_SERVICES", "This app requires Google Play Services.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {

                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Utility.ReportNonFatalError("REQUEST_ACCOUNT_PICKER", accountName);
                    if (accountName != null) {
                        SharedPreferencesUtil.setAccountName(ContinuousCaptureActivityNew.this, accountName);
                        mCredential.setSelectedAccountName(accountName);
                        // getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();

                }
                break;

            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK && data != null) {
                    handleSignInResult(data);
                }
                break;
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
        //   onResume();
        // getResultsFromApi();
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
        // Toast.makeText(this, "Please allow the permissions", Toast.LENGTH_SHORT).show();
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ContinuousCaptureActivityNew.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }


    private class WriteToSheetTask extends AsyncTask<Void, Void, Integer> {
        private Sheets mService = null;
        private Exception mLastError = null;
        private Context mContext;

        WriteToSheetTask(GoogleAccountCredential credential, Context context) {

            mContext = context;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .setApplicationName("")
                    .build();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {

                //updating timestamp of qr sheet
                String currentTimestamp = Utility.getCurrentTimestamp();
                List<List<Object>> QR_dataList = new ArrayList<>();
                for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                    List<Object> list1 = new ArrayList<>();
                    list1.add(AsanaTaskDataList.get(i).getDateTime());
                    //     list1.add(scannedDataList.get(i).getTimestamp());
                    String timestamp = Utility.getCurrentTimestamp();
                    list1.add(timestamp);
                    //  list1.add(currentTimestamp);
                    AsanaTaskDataList.get(i).setTimestamp(timestamp);
                    list1.add(AsanaTaskDataList.get(i).getQrText());
                    list1.add(AsanaTaskDataList.get(i).getGdriveFileThumbnail());
                    list1.add("In Progress"); // for status of updating spreadsheets and asana tasks
                    QR_dataList.add(list1);
                }

                //updating timestamp in bluetooth sheet
                List<List<Object>> Bluetooth_dataList = BeaconScannerService.getBeaconList();
                for (int i = 0; i < Bluetooth_dataList.size(); i++) {
                    List<Object> list1 = Bluetooth_dataList.get(i);
                    // if(list1.size()>2) list1.set(2,currentTimestamp);
                    if (list1.size() > 4) {
                        if (strongestSignalBeaconRSSI == 0) {
                            strongestSignalBeaconRSSI = (Integer) list1.get(3);
                            strongestSignalBeaconUUID = String.valueOf(list1.get(4));
                        } else if ((Integer) list1.get(3) > strongestSignalBeaconRSSI) {
                            strongestSignalBeaconRSSI = (Integer) list1.get(3);
                            strongestSignalBeaconUUID = String.valueOf(list1.get(4));
                        }

                        list1.set(2, Utility.getCurrentTimestamp());
                    }
                }


                writeToQRSheet(QR_dataList);
                writeToBLUETOOTHSheet(Bluetooth_dataList);

            } catch (Exception e) {
                hideprogressdialog();
                mLastError = e;
                Utility.ReportNonFatalError("WriteToSheetTask", e.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);
                } else {
                    Log.e(this.toString(), "Error_Log_WriteToSheetTask:" + mLastError.getMessage());
                    Utility.ReportNonFatalError("WriteToSheetTask", mLastError.getMessage());
                    //  Toast.makeText(WriteDataToSheet.this, "Error!", Toast.LENGTH_SHORT).show();
                    return 0;
                }
                Log.e(this.toString(), e + "");
            }
            return 1;
        }

        @Override
        protected void onPreExecute() {

            // showprogressdialog();
        }

        @Override
        protected void onPostExecute(Integer result) {

           /* if (result == 0) {  // TODO uncomment later
                Toast.makeText(ContinuousCaptureActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            } else {
                callApiToWriteDataOnAsana();
            }*/

            callApiToWriteDataOnAsana();


        }


        @Override
        protected void onCancelled() {

            // mProgressBar.setVisibility(View.GONE);
            hideprogressdialog();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            REQUEST_AUTHORIZATION);
                } else {
                    // mTextView.setText("The following error occurred:\n"+ mLastError.getMessage());
                    Toast.makeText(ContinuousCaptureActivityNew.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.e(this.toString(), "Error_Log:" + mLastError.getMessage());
                    Utility.ReportNonFatalError("onCancelled", mLastError.getMessage());

                }
            } else {
                //  mTextView.setText("Request cancelled.");
                Toast.makeText(ContinuousCaptureActivityNew.this, "Cancelled!", Toast.LENGTH_SHORT).show();
            }
        }


        private void writeToQRSheet(List<List<Object>> QR_dataList) throws IOException, GeneralSecurityException {
            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(ContinuousCaptureActivityNew.this);
            String range = com.synapse.Constants.SheetOne;
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();
            // requestBodyData.add(data1);
            // requestBody.setValues(requestBodyData);

            Utility.ReportNonFatalError("Check", "1");

            if (QR_dataList.size() > 0) {
                requestBody.setValues(QR_dataList);
                Sheets.Spreadsheets.Values.Append request =
                        mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
                request.setValueInputOption(valueInputOption);
                AppendValuesResponse response = request.execute();
                Utility.ReportNonFatalError("Check2", response.toString());
                Log.e(this.toString(), response.toString());
            }
        }

        private void writeToBLUETOOTHSheet(List<List<Object>> Bluetooth_dataList) throws IOException, GeneralSecurityException {
            // String spreadsheetId = SharedPreferencesUtil.getDefaultBluetoothSheet(ContinuousCaptureActivity.this);
            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(ContinuousCaptureActivityNew.this);

            String range = com.synapse.Constants.SheetTwo;
            ;// name of sheet and range
            Utility.ReportNonFatalError("Check", "3");
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();

            requestBody.setValues(Bluetooth_dataList);
            Sheets.Spreadsheets.Values.Append request =
                    mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
            request.setValueInputOption(valueInputOption);
            AppendValuesResponse response = request.execute();
            Utility.ReportNonFatalError("Check3", response.toString());
            Log.e(this.toString(), response.toString());

        }

     /*   private void createSpreadSheetForBeaconScanner(String SpreadSheetTitle) throws IOException, GeneralSecurityException {
            Spreadsheet requestBody = new Spreadsheet()
                    .setProperties(new SpreadsheetProperties()
                            .setTitle(SpreadSheetTitle));
            Spreadsheet response = mService.spreadsheets().create(requestBody)
                    .setFields("spreadsheetId")
                    .execute();

            // if (Utility.isSpreadsheetListEmpty(ContinuousCaptureActivity.this)) {
            SharedPreferencesUtil.setDefaultBluetoothSheet(ContinuousCaptureActivity.this, response.getSpreadsheetId());
            //}

            writeTitleToBluetoothBeaconSheet();

        }
*/
       /* private void writeTitleToBluetoothBeaconSheet() throws IOException, GeneralSecurityException {
            String spreadsheetId = SharedPreferencesUtil.getDefaultBluetoothSheet(ContinuousCaptureActivity.this);
            //  String range = "Sheet1!A1:X1";//
            String range = "Sheet1";//
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();
            List<Object> data1 = new ArrayList<>();

            data1.add("datetime");
            data1.add("Timestamp");
            data1.add("Name");
            data1.add("RSSI");
            data1.add("UUID");
            data1.add("Major");
            data1.add("Minor");
            data1.add("Battery(%)");


            List<List<Object>> requestBodyData = new ArrayList<>();
            requestBodyData.add(data1);
            requestBody.setValues(requestBodyData);


            Sheets.Spreadsheets.Values.Append request =
                    mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
            request.setValueInputOption(valueInputOption);

            AppendValuesResponse response = request.execute();
            Log.e(this.toString(), response.toString());

        }*/
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

    /**************************Write to spreadsheet***********************************************/

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account == null) {
            signIn();
        } else {
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, getString(R.string.app_name)));

        }
    }

    private void signIn() {
        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE, new Scope("https://www.googleapis.com/auth/spreadsheets"))
                        .requestEmail()
                        .build();
        return GoogleSignIn.getClient(getApplicationContext(), signInOptions);
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.d(TAG, "Signed in as " + googleSignInAccount.getEmail());
                        mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), googleSignInAccount, getString(R.string.app_name)));
                        Toast.makeText(getApplicationContext(), "Sign in Success!", Toast.LENGTH_SHORT).show();

                        Utility.ReportNonFatalError("handleSignInResult", googleSignInAccount.getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Sign in Failed!", Toast.LENGTH_SHORT).show();
                        Log.e(this.toString(), "Error_Log_handleSignInResult:" + e.getMessage());
                        Utility.ReportNonFatalError("handleSignInResult", e.getMessage());
                    }
                });
    }

    void uploadMultipleFilesOnGoogleDriveFolder() {
        //  showprogressdialog();
        for (int j = 0; j < AsanaTaskDataList.size(); j++) {
            Task_data scannedData = AsanaTaskDataList.get(j);
            String filePath = Utility.saveImage(scannedData.getBitmap());
            scannedData.setBitmapFilePath(filePath);
            AsanaTaskDataList.set(j, scannedData);
        }

        String timestampBeacon=Utility.getCurrentTimestamp();// timestamp for beacons should be same for all QR code scanned at a time
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


        Intent msgIntent = new Intent(ContinuousCaptureActivityNew.this, SaveWebRequestService.class);
        msgIntent.putExtra(SaveWebRequestService.QR_DATA_LIST, (Serializable) dataListWithoutBitmap);
        startService(msgIntent);
        finishActivity();


        //   driveUploadAPICount = 0;
        //   uploadFileOnGoogleDriveFolder(AsanaTaskDataList.get(driveUploadAPICount).getBitmapFilePath(), driveUploadAPICount);


    }

    void uploadFileOnGoogleDriveFolder(String filePath, int index) {
        // showprogressdialog();
        if (mDriveServiceHelper == null) {
            return;
        }

        String fileExt = "";
        if (filePath.contains(".")) {
            fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        File file = new File(filePath);
        String mime_type = MimeUtils.guessMimeTypeFromExtension(fileExt);

        String folderId = SharedPreferencesUtil.getDefaultDriveFolderId(ContinuousCaptureActivityNew.this);
        mDriveServiceHelper.uploadFile(file, mime_type, folderId)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        AsanaTaskDataList.get(index).setGdriveFileId(googleDriveFileHolder.getId());
                        AsanaTaskDataList.get(index).setGdriveFileParentId(folderId);
                        driveUploadAPICount++;
                        if (driveUploadAPICount < AsanaTaskDataList.size()) {
                            uploadFileOnGoogleDriveFolder(AsanaTaskDataList.get(driveUploadAPICount).getBitmapFilePath(), driveUploadAPICount);
                        } else {
                            viewFileFolder();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(getApplicationContext(), "Failed, can't upload on Google Drive folder! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        hideprogressdialog();
                        //barcodeView.resume();
                        Log.e(this.toString(), "Error_Log_uploadFileOnGoogleDriveFolder:" + e.getMessage());
                        Utility.ReportNonFatalError("uploadFileOnGoogleDriveFolder", e.getMessage());
                    }
                });
    }

    private void viewFileFolder() {
        if (mDriveServiceHelper == null) {
            return;
        }

        String parentFolderid = SharedPreferencesUtil.getDefaultDriveFolderId(ContinuousCaptureActivityNew.this);
        mDriveServiceHelper.queryFiles(parentFolderid)
                .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
                    @Override
                    public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
                        Gson gson = new Gson();
                        String userJson = gson.toJson(googleDriveFileHolders);
                        Type userListType = new TypeToken<ArrayList<GoogleDriveFileHolder>>() {
                        }.getType();
                        ArrayList<GoogleDriveFileHolder> list = gson.fromJson(userJson, userListType);
                        for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                            for (int j = 0; j < list.size(); j++) {
                                if (AsanaTaskDataList.get(i).getGdriveFileId().equals(list.get(j).getId())) {
                                    AsanaTaskDataList.get(i).setGdriveFileThumbnail(list.get(j).getThumbnailLink() + "," + AsanaTaskDataList.get(i).getGdriveFileParentId() + "," + AsanaTaskDataList.get(i).getGdriveFileId());
                                    break;
                                }
                            }
                        }

                        new WriteToSheetTask(mCredential, ContinuousCaptureActivityNew.this).execute();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(this.toString(), "Error_Log_viewFileFolder:" + e.getMessage());
                        Utility.ReportNonFatalError("viewFileFolder", e.getMessage());

                    }
                });
    }

    /***************************Scanning beacon******************************************************************/

    public void finishActivity() {
        this.finish();
    }

    public Queue<BluetoothDeviceWrapper> getDeviceQueue() {
        return deviceQueue;
    }

   /* public FscBeaconApi getFscBeaconApi() {
        return fscBeaconApi;
    }*/

  /*  public Handler getHandler() {
        return handler;
    }*/

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

    @Override
    public void onSuccess(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        try {
            String strResponse = response.body().string();
            Log.e("APIResponse---->", response.body().toString());
            if (apiType == Constants.API_TYPE.GET_USER_DETAILS) {
                String url = response.raw().request().url().toString();
                String headers = response.raw().request().headers().toString();
                Utility.ReportNonFatalError("onSuccess----", "<-url->\n " + url + " <-headers->\n " + headers + " <-Response->\n " + strResponse);
                //  Gson gson = new Gson();
                // Type listType = new TypeToken<List<Template>>() {}.getType();
                UserDetailsResponse userDetailsResponse = new Gson().fromJson(strResponse, UserDetailsResponse.class);
                String user_gid = userDetailsResponse.getData().getGid();
               // SharedPreferencesUtil.setAsanaUserId(this, user_gid);
                List<Workspace> workspaces = userDetailsResponse.getData().getWorkspaces();
                if (workspaces.size() > 0) {
                    String workspace_gid = workspaces.get(0).getGid();
                   // SharedPreferencesUtil.setAsanaWorkspaceId(this, workspace_gid);
                }
                for (int i = 0; i < workspaces.size(); i++) {
                    if (workspaces.get(i).getName().equals("levylab.org")) {
                        LevyLab_workspace_gid = workspaces.get(i).getGid();
                        SharedPreferencesUtil.setLevyLabWorkspaceId(this, workspaces.get(i).getGid());
                        //  hitAPIGetProjectsByWorkspace(workspaces.get(i).getGid());
                        taskSearchAPICount = 0;
                        hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());

                        break;
                    } else if (i == workspaces.size() - 1) {
                        Toast.makeText(this, "'Levy Lab' workspace not found.\n Please get access of this project for updating info on Asana. ", Toast.LENGTH_SHORT).show();
                    }
                }
            }/* else if (apiType == Constants.API_TYPE.GET_TEAMS) {
                TeamsByOrganization teamsByOrganization = new Gson().fromJson(strResponse, TeamsByOrganization.class);
                List<Datum> data = teamsByOrganization.getData();

            } else if (apiType == Constants.API_TYPE.GET_PROJECTS) {
                GetProjectsResponse projectsResponse = new Gson().fromJson(strResponse, GetProjectsResponse.class);
                List<com.synapse.model.projetcs.Datum> data = projectsResponse.getData();

            }*/ else if (apiType == Constants.API_TYPE.GET_PROJECTS_BY_WORKSPACE) {
                GetProjectsResponse projectsResponse = new Gson().fromJson(strResponse, GetProjectsResponse.class);
                List<com.synapse.model.projetcs.Datum> projectsList = projectsResponse.getData();
                for (com.synapse.model.projetcs.Datum project : projectsList) {
                    if (project.getName().equals(LevyLabProject)) {
                        LevyLab_project_gid = project.getGid();
                        SharedPreferencesUtil.setLevyLabProjectId(this, LevyLab_project_gid);
                        hitAPIGetTasksByProject(LevyLab_project_gid, limit);
                        break;
                    }
                }
            } else if (apiType == Constants.API_TYPE.GET_TASKS_BY_PROJECT) {
                getTasksResponse(strResponse);
            } else if (apiType == Constants.API_TYPE.GET_NEXT_TASKS_BY_PROJECT) {
                getTasksResponse(strResponse);
            } /*else if (apiType == Constants.API_TYPE.SEARCH_PROJECT_BY_WORKSPACE) {// not required
                SearchProjectByWorkspace searchProjectResponse = new Gson().fromJson(strResponse, SearchProjectByWorkspace.class);
            }*/ else if (apiType == Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE) {
                SearchTaskByWorkspace searchTaskResponse = new Gson().fromJson(strResponse, SearchTaskByWorkspace.class);
                if (searchTaskResponse.getData() == null || searchTaskResponse.getData().isEmpty()) {
                    Utility.showToast(this, "No matching task found for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                } else {
                    //  Utility.showToast(this, "Multiple matching tasks found for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                    Task_data task_data = AsanaTaskDataList.get(taskSearchAPICount);
                    List<Datum> listMatchingTasks = searchTaskResponse.getData();
                    for (int j = 0; j < listMatchingTasks.size(); j++) {
                        Datum matchingTask = listMatchingTasks.get(j);
                        if (matchingTask.getResourceType().equals("task") && matchingTask.getName().equals(task_data.getQrText())) { //// TODO matching criteria will be changed later
                            task_data.setTaskId(matchingTask.getGid());
                            AsanaTaskDataList.set(taskSearchAPICount, task_data);
                            break;
                        } else if (j == listMatchingTasks.size() - 1) {
                            Utility.showToast(this, "No task can be matched for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                        }
                    }
                }
                taskSearchAPICount++;
                if (taskSearchAPICount < AsanaTaskDataList.size()) {
                    hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                } else {
                    taskDetailAPICount = 0;
                    hitAPIGetTaskDetails(AsanaTaskDataList.get(taskDetailAPICount).getTaskId());
                }
            } else if (apiType == Constants.API_TYPE.TASK_DETAILS) {
                TaskDetail taskDetailsResponse = new Gson().fromJson(strResponse, TaskDetail.class);
                List<CustomField> fieldList = taskDetailsResponse.getData().getCustomFields();
                if (fieldList == null) return;
                String beacon1_gid = "", beacon1_RSSI_gid = "", beacon1_URL = "", feasybeacon_task_gid = "";
                // String beacon_UUID = "A0:U5:9F:B0:6C:4G";
                // int beacon_RSSI = -187;
                for (int i = 0; i < fieldList.size(); i++) {
                    CustomField customField = fieldList.get(i);
                    if (customField.getName().equals("Beacon1")) {
                        beacon1_gid = customField.getGid();
                    } else if (customField.getName().equals("Beacon1 RSSI")) {
                        beacon1_RSSI_gid = customField.getGid();
                    } else if (customField.getName().equals("Beacon1 URL")) {
                        beacon1_URL = customField.getTextValue();
                        if (beacon1_URL != null && !beacon1_URL.isEmpty() && beacon1_URL.contains("/")) {
                            feasybeacon_task_gid = beacon1_URL.substring(beacon1_URL.lastIndexOf("/") + 1);
                        }

                    }

                }

                Task_data task_data = AsanaTaskDataList.get(taskDetailAPICount);
                task_data.setBeacon1_gid(beacon1_gid);
                task_data.setBeacon1_RSSI_gid(beacon1_RSSI_gid);
                task_data.setBeacon1_URL(beacon1_URL);
                task_data.setFeasybeacon_task_gid(feasybeacon_task_gid);

                AsanaTaskDataList.set(taskDetailAPICount, task_data);
                taskDetailAPICount++;
                if (taskDetailAPICount < AsanaTaskDataList.size()) {
                    hitAPIGetTaskDetails(AsanaTaskDataList.get(taskDetailAPICount).getTaskId());
                } else {

                    //  taskDetailAPICount = 0;
                    feasybeaconTaskDetailAPICount = 0;
                    // hitAPIGetFeasybeaconTaskDetails(AsanaTaskDataList.get(feasybeaconTaskDetailAPICount).getFeasybeacon_task_gid());
                    callAPI_getFeasyBeaconTaskDetails();

                }


            } else if (apiType == Constants.API_TYPE.FEASYBEACON_TASK_DETAILS) {


                TaskDetail taskDetailsResponse = new Gson().fromJson(strResponse, TaskDetail.class);
                List<CustomField> fieldList = taskDetailsResponse.getData().getCustomFields();
                if (fieldList == null) return;
                String feasybeacon_UUID_gid = "";

                for (int i = 0; i < fieldList.size(); i++) {
                    CustomField customField = fieldList.get(i);
                    if (customField.getName().equals("UUID")) {
                        feasybeacon_UUID_gid = customField.getGid();
                        break;
                    }
                }
                for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                    Task_data task_data = AsanaTaskDataList.get(i);
                    if (task_data.getFeasybeacon_task_gid().equals(taskDetailsResponse.getData().getGid())) {
                        task_data.setFeasybeacon_UUID_gid(feasybeacon_UUID_gid);
                        AsanaTaskDataList.set(feasybeaconTaskDetailAPICount, task_data);
                        break;
                    }
                }

                feasybeaconTaskDetailAPICount++;

                callAPI_getFeasyBeaconTaskDetails();


            } else if (apiType == Constants.API_TYPE.UPDATE_TASK) {
                UpdateTAskResponse updateTAskResponse = new Gson().fromJson(strResponse, UpdateTAskResponse.class);
                taskUpdateAPICount++;
                if (taskUpdateAPICount < AsanaTaskDataList.size()) {
                    JsonObject input = getJSONObject(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI);
                    hitAPIUpdateTask(AsanaTaskDataList.get(taskUpdateAPICount).getTaskId(), input);
                } else {

                    //taskUpdateAPICount = 0;
                    feasybeaconTaskUpdateAPICount = 0;
                    callAPI_UpdateFeasybeaconTask();

                }


            } else if (apiType == Constants.API_TYPE.UPDATE_FEASYBEACON_TASK) {
                feasybeaconTaskUpdateAPICount++;
                UpdateTAskResponse updateTAskResponse = new Gson().fromJson(strResponse, UpdateTAskResponse.class);

                callAPI_UpdateFeasybeaconTask();

            } else if (apiType == Constants.API_TYPE.UPLOAD_ATTACHMENTS) {
                UploadAttachmentsResponse uploadAttachmentsResponse = new Gson().fromJson(strResponse, UploadAttachmentsResponse.class);
                attachmentUploadAPICount++;
                if (attachmentUploadAPICount < AsanaTaskDataList.size()) {
                    hitAPIUploadAttachments(AsanaTaskDataList.get(attachmentUploadAPICount).getTaskId(), getMultipartImage(AsanaTaskDataList.get(attachmentUploadAPICount).getBitmapFilePath()));
                } else {
                    hideprogressdialog();
                    finish();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callAPI_UpdateFeasybeaconTask() {
        // because it is possible that Feasybeacon_UUID_gid may OR MAY NOT be available
        while (feasybeaconTaskUpdateAPICount < AsanaTaskDataList.size()) {
            String Feasybeacon_UUID_gid = AsanaTaskDataList.get(feasybeaconTaskUpdateAPICount).getFeasybeacon_UUID_gid();
            if (Feasybeacon_UUID_gid == null || Feasybeacon_UUID_gid.isEmpty()) {
                feasybeaconTaskUpdateAPICount++;
            } else {
                break;
            }
        }
        if (feasybeaconTaskUpdateAPICount < AsanaTaskDataList.size()) {
            JsonObject input = getJSONObjectFeasybeacon(AsanaTaskDataList.get(feasybeaconTaskUpdateAPICount).getFeasybeacon_UUID_gid(), strongestSignalBeaconUUID);
            hitAPIUpdateFeasybeaconTask(AsanaTaskDataList.get(feasybeaconTaskUpdateAPICount).getFeasybeacon_task_gid(), input);
        } else {
            attachmentUploadAPICount = 0;
            hitAPIUploadAttachments(AsanaTaskDataList.get(attachmentUploadAPICount).getTaskId(), getMultipartImage(AsanaTaskDataList.get(attachmentUploadAPICount).getBitmapFilePath()));
        }
    }

    public void callAPI_getFeasyBeaconTaskDetails() {
        // because it is possible that Feasybeacon_task_gid may OR MAY NOT be available
        while (feasybeaconTaskDetailAPICount < AsanaTaskDataList.size()) {
            String feasybeacon_task_gid = AsanaTaskDataList.get(feasybeaconTaskDetailAPICount).getFeasybeacon_task_gid();
            if (feasybeacon_task_gid == null || feasybeacon_task_gid.isEmpty()) {
                feasybeaconTaskDetailAPICount++;
            } else {
                break;
            }
        }
        if (feasybeaconTaskDetailAPICount < AsanaTaskDataList.size()) {
            hitAPIGetFeasybeaconTaskDetails(AsanaTaskDataList.get(feasybeaconTaskDetailAPICount).getFeasybeacon_task_gid());
        } else {
            taskUpdateAPICount = 0;
            JsonObject input = getJSONObject(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI);
            hitAPIUpdateTask(AsanaTaskDataList.get(taskUpdateAPICount).getTaskId(), input);
        }
    }

    void getTasksResponse(String strResponse) {
        TasksResponse tasksResponse = new Gson().fromJson(strResponse, TasksResponse.class);
        List<com.synapse.model.tasks.Datum> tasksList = tasksResponse.getData();

        for (int j = 0; j < AsanaTaskDataList.size(); j++) {
            Task_data task_data = AsanaTaskDataList.get(j);
            if (!task_data.getTaskId().equals("")) continue;
            for (int i = 0; i < tasksList.size(); i++) {
                com.synapse.model.tasks.Datum task = tasksList.get(i);
                if (task.getName().equals(task_data.getQrText())) {  // TODO matching criteria will be changed later
                    task_data.setTaskId(task.getGid());
                    AsanaTaskDataList.set(j, task_data);
                    break;
                }
            }
        }

        if (Utility.AllQRMatchedWithTask(AsanaTaskDataList))  //All qr text matched with task and corresponding  task_id saved successfully
        {
            if (AsanaTaskDataList.size() < 1) return;
            taskDetailAPICount = 0;
            hitAPIGetTaskDetails(AsanaTaskDataList.get(taskDetailAPICount).getTaskId());
        } else if (tasksResponse.getNextPage() != null && tasksResponse.getNextPage().getOffset() != null) {
            offset = tasksResponse.getNextPage().getOffset();
            hitAPIGetNextTasksByProject(LevyLab_project_gid, limit, offset);  // still all QR text not matched with a task
        } else {
            Utility.showToast(this, "No matching task found for QR: " + Utility.getNonMatchedQR(AsanaTaskDataList));
        }


    }

    @Override
    public void onFailure(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        try {
            hideprogressdialog();
            Log.e("onFailure---->", String.valueOf(apiType));
            Toast.makeText(this, "onFailure--" + String.valueOf(apiType), Toast.LENGTH_SHORT).show();
            if (apiType.equals(Constants.API_TYPE.GET_USER_DETAILS)) {
                if (response.code() == 403) {
                    Toast.makeText(this, "Access denied!\nPlease reauthorize and allow access to Asana projects", Toast.LENGTH_SHORT).show();
                }
            }


        } catch (Exception e) {
            e.getCause();
        }
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, Constants.API_TYPE apiType) {
        Utility.ReportNonFatalError("onApiException", response.errorBody().toString());
        Log.e("onApiException---->", String.valueOf(apiType));
        hideprogressdialog();
        Toast.makeText(this, "onApiException--" + String.valueOf(apiType), Toast.LENGTH_SHORT).show();
    }


    public JsonObject getJSONObject(String beacon1_gid, String beacon1_RSSI_gid, String UUID, int RSSI) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(beacon1_gid, UUID);
            obj.addProperty(beacon1_RSSI_gid, RSSI);
            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }

    public JsonObject getJSONObjectFeasybeacon(String feasybeacon_UUID_gid, String UUID) {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty(feasybeacon_UUID_gid, UUID);
            JsonObject obj2 = new JsonObject();
            obj2.add("custom_fields", obj);
            obj3.add("data", obj2);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }


    public MultipartBody.Part getMultipartImage(String filePath) {
        String mimeType = Utility.getMimeTypeFile(filePath);
        MultipartBody.Part surveyImage = null;
        try {
            File file = new File(filePath);
            String fileName = file.getName();
            // ProgressRequestBody surveyBody = new ProgressRequestBody(file, this);
            // RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
            surveyImage = MultipartBody.Part.createFormData("file", fileName, requestBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return surveyImage;
    }

}


