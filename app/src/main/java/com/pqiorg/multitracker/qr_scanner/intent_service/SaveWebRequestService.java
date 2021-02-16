package com.pqiorg.multitracker.qr_scanner.intent_service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.pqiorg.multitracker.drive.MimeUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pqiorg.multitracker.R;
import com.room_db.Beacon;
import com.room_db.DatabaseClient;
import com.pqiorg.multitracker.spreadsheet.creater.SpreadSheetListActivity;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.model.TaskData;
import com.synapse.model.search_task.Datum;
import com.synapse.model.search_task.SearchTaskByWorkspace;
import com.synapse.model.task_detail.CustomField;
import com.synapse.model.task_detail.Tag;
import com.synapse.model.task_detail.TaskDetail;
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
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService;

/************************************** Note***************************************************************/
//Changes done in this file should also be done in UpdateWebRequestService file

public class SaveWebRequestService extends JobIntentService implements RequestListener {

    public static final String QR_DATA_LIST = "qr_data_list";
    List<TaskData> AsanaTaskDataList = new ArrayList<>();
    List<List<Object>> Bluetooth_dataList = new ArrayList<>();
    int taskDetailAPICount = 0, taskUpdateAPICount = 0, attachmentUploadAPICount = 0, feasybeaconTaskDetailAPICount = 0, feasybeaconTaskUpdateAPICount = 0, driveUploadAPICount = 0, taskSearchAPICount = 0;
   // final String LevyLabProject = "LevyLab AoT (all items)";

    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};
    //  private DriveServiceHelper mDriveServiceHelper;
    // GoogleAccountCredential mCredential;
    int strongestSignalBeaconRSSI = 0;
    String strongestSignalBeaconUUID = "";


    //    private RetrofitManager retrofitManager = RetrofitManager.getInstance();
    //  String LevyLab_project_gid = "";
    String LevyLab_workspace_gid = "";


    /*private Sheets mService = null;
    private Exception mLastError = null;
    private Context mContext;
*/


    long timestampBluetoothSheet = 0;
    String accountName = "";
    String updatedRangeOfDataWrittenInQRSheet = ""; // This is the range in which data has been written in spreadsheet.. this will be used for rewritting in

    // the same range to update status in spreadsheet
    public SaveWebRequestService() {
       // super("MyWebRequestService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        accountName = SharedPreferencesUtil.getAccountName(this);
        Utility.ReportNonFatalError("accountName", accountName);
        //  initDriveServiceHelper();
        //  initGoogleSheetServiceHelper();

    }


/*
    void initDriveServiceHelper() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null) {
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, getString(R.string.app_name)));
        }
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }
    void initGoogleSheetServiceHelper() {
        String accountName = SharedPreferencesUtil.getAccountName(this);
        Utility.ReportNonFatalError("accountName", accountName);
        if (accountName != null) {
            mCredential.setSelectedAccountName(accountName);
        }

    }
*/

    @Override
    protected void onHandleWork(Intent intent) {
        AsanaTaskDataList = (List<TaskData>) intent.getSerializableExtra(QR_DATA_LIST);
        Bluetooth_dataList = BeaconScannerService.getBeaconList();
        timestampBluetoothSheet = Long.parseLong(AsanaTaskDataList.get(AsanaTaskDataList.size() - 1).getTimestamp());
        new writeQRRecordsInSpreadsheet().execute();
        new saveQRRecordsInLocalDBAsync().execute();

        uploadFileOnGoogleDriveFolder(AsanaTaskDataList.get(driveUploadAPICount).getBitmapFilePath(), driveUploadAPICount);

    }


    void uploadFileOnGoogleDriveFolder(String filePath, int index) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        DriveServiceHelper mDriveServiceHelper = null;
        if (account != null) {
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, getString(R.string.app_name)));
        }
        if (mDriveServiceHelper == null) {
            addErrorDetail(index, "Can't upload file in google drive folder. Reason: DriveServiceHelper object is null"); //Log error here
            return;
        }

        String fileExt = "";
        if (filePath.contains(".")) {
            fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        File file = new File(filePath);
        String mime_type = MimeUtils.guessMimeTypeFromExtension(fileExt);

        String folderId = SharedPreferencesUtil.getDefaultDriveFolderId(this);
        mDriveServiceHelper.uploadFile(file, mime_type, folderId)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Log.e("Debugging..., ","photo:" + index+"uploaded successfully");
                        AsanaTaskDataList.get(index).setGdriveFileId(googleDriveFileHolder.getId());
                        AsanaTaskDataList.get(index).setGdriveFileParentId(folderId);
                        continueUploadFileOnGoogleDrive();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Debugging...photo:" + index, "uploaded failed: " + e.getMessage());
                        Utility.ReportNonFatalError("uploadFileOnGoogleDriveFolder", e.getMessage());
                        addErrorDetail(index, "Failed uploading file in google drive folder."); //Log error here
                        continueUploadFileOnGoogleDrive();
                    }
                });
    }

    void continueUploadFileOnGoogleDrive() {
        driveUploadAPICount++;
        if (driveUploadAPICount < AsanaTaskDataList.size()) {
            uploadFileOnGoogleDriveFolder(AsanaTaskDataList.get(driveUploadAPICount).getBitmapFilePath(), driveUploadAPICount);
        } else {
            //   getGoogleDriveFileThumbnails(); // should not use because thumbnail link works only for few days
            for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                AsanaTaskDataList.get(i).setGdriveFileThumbnail("Parent_Id=" + AsanaTaskDataList.get(i).getGdriveFileParentId() + "," + "File_Id=" + AsanaTaskDataList.get(i).getGdriveFileId());
            }
            new UpdateStatusInQRSheetNew().execute();
            // new writeQRRecordsInSpreadsheet().execute();
            callApiToWriteDataOnAsana();

        }
    }

    private void getGoogleDriveFileThumbnails() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        DriveServiceHelper mDriveServiceHelper = null;
        if (account != null) {
            mDriveServiceHelper = new DriveServiceHelper(getGoogleDriveService(getApplicationContext(), account, getString(R.string.app_name)));
        }
        if (mDriveServiceHelper == null) {
            //Log error here
            return;
        }

        String parentFolderid = SharedPreferencesUtil.getDefaultDriveFolderId(this);
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

                        new writeQRRecordsInSpreadsheet().execute();


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

    private void callApiToWriteDataOnAsana() {
        //    String user_gid = SharedPreferencesUtil.getAsanaUserId(this);
        //   String workspace_gid = SharedPreferencesUtil.getAsanaWorkspaceId(this);
        LevyLab_workspace_gid = SharedPreferencesUtil.getLevyLabWorkspaceId(this);
        // LevyLab_project_gid = SharedPreferencesUtil.getLevyLabProjectId(this);
        Utility.ReportNonFatalError("AsanaTaskDataList", AsanaTaskDataList.toString());
        Log.e("AsanaTaskDataList", AsanaTaskDataList.toString());
        if (LevyLab_workspace_gid.equals("")) {
            hitAPIGetAsanaUserDetails();
        } else {
            taskSearchAPICount = 0;
            hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());
        }
    }

    private void hitAPIGetAsanaUserDetails() {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.getUserDetails(this, this, Constants.API_TYPE.GET_USER_DETAILS, false);
    }

    private void hitAPISearchTaskByWorkspace(String workspace_gid, String search_task) {

        boolean isValidURL = Patterns.WEB_URL.matcher(search_task).matches();
        if (isValidURL) {
            taskSearchAPICount++;
            if (taskSearchAPICount < AsanaTaskDataList.size()) {
                hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());
            } else {
                taskDetailAPICount = 0;
                hitAPIGetTaskDetails(AsanaTaskDataList.get(taskDetailAPICount).getTaskId());
            }
            return;
        }
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.searchTaskByWorkspace(this, this, Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE, workspace_gid, search_task, false);
    }

    private void hitAPIGetTaskDetails(String task_gid) {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.getTaskDetails(this, this, Constants.API_TYPE.TASK_DETAILS, task_gid, false);
    }

    private void hitAPIGetFeasybeaconTaskDetails(String task_gid) {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.getTaskDetails(this, SaveWebRequestService.this, Constants.API_TYPE.FEASYBEACON_TASK_DETAILS, task_gid, false);
    }

    private void hitAPIUpdateTask(String task_gid, JsonObject input) {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.updateTask1(this, this, Constants.API_TYPE.UPDATE_TASK, task_gid, input, false);
    }

    private void hitAPIUpdateFeasybeaconTask(String task_gid, JsonObject input) {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.updateTask1(this, this, Constants.API_TYPE.UPDATE_FEASYBEACON_TASK, task_gid, input, false);
    }

    private void hitAPIUploadAttachments(String task_gid, MultipartBody.Part IMAGE) {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.uploadAttachment(this, this, Constants.API_TYPE.UPLOAD_ATTACHMENTS, IMAGE, task_gid, false);
    }

    void logResponseToFirebaseConsole(Response<ResponseBody> response) {
        try {

            String url = "", headers = "", strResponse = "";
            if (response != null && response.body() != null) {
                strResponse = response.body().toString();
            }
            if (response != null && response.raw() != null && response.raw().request() != null && response.raw().request().url() != null) {
                url = response.raw().request().url().toString();
            }
            if (response != null && response.raw() != null && response.raw().request() != null && response.raw().request().headers() != null) {
                headers = response.raw().request().headers().toString();
            }
            Utility.ReportNonFatalError("onSuccess----", "<-url->\n " + url + " <-headers->\n " + headers + " <-Response->\n " + strResponse);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, Constants.API_TYPE apiType) {

        //OKHttp 2.4..the response body (response.body().string()) is a one-shot value that may be consumed only once
        //response.body().toString(): This returns your object in string format.
        //response.body().string(): This returns your response.

        try {

            if (apiType == Constants.API_TYPE.GET_USER_DETAILS) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        UserDetailsResponse userDetailsResponse = new Gson().fromJson(strResponse, UserDetailsResponse.class);
                        List<Workspace> workspaces = userDetailsResponse.getData().getWorkspaces();
                        if (workspaces.size() > 0) {
                            for (int i = 0; i < workspaces.size(); i++) {
                                if (workspaces.get(i).getName().equals("levylab.org")) {
                                    LevyLab_workspace_gid = workspaces.get(i).getGid();
                                    SharedPreferencesUtil.setLevyLabWorkspaceId(this, workspaces.get(i).getGid());
                                    taskSearchAPICount = 0;
                                    hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                                    break;
                                } else if (i == workspaces.size() - 1) {
                                    Utility.ReportNonFatalError("onSuccess", "'Levy Lab' workspace not found.\n Please get access of this project for updating info on Asana.");
                                }
                            }
                        }
                    } else {
                        // Log Error here
                        addErrorDetail(0, "Response of GET_ASANA_USER_DETAILS API  is empty!");
                    }

                } catch (Exception e) {
                    // Log Error here
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    addErrorDetail(0, "Error in processing response of GET_ASANA_USER_DETAILS API");
                }

            } else if (apiType == Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        SearchTaskByWorkspace searchTaskResponse = new Gson().fromJson(strResponse, SearchTaskByWorkspace.class);
                        if (searchTaskResponse.getData() == null || searchTaskResponse.getData().isEmpty()) {
                            Utility.ReportNonFatalError("onSuccess", "No matching task found for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                            addErrorDetail(taskSearchAPICount, "No matching task found ");
                        } else {
                            //   Utility.showToast(this, " matching tasks found for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                            TaskData task_data = AsanaTaskDataList.get(taskSearchAPICount);
                            List<Datum> listMatchingTasks = searchTaskResponse.getData();
                            for (int j = 0; j < listMatchingTasks.size(); j++) {
                                Datum matchingTask = listMatchingTasks.get(j);
                                if (matchingTask.getResourceType().equals("task") && matchingTask.getName().equals(task_data.getQrText())) { //// TODO matching criteria will be changed later
                                    task_data.setTaskId(matchingTask.getGid());
                                    AsanaTaskDataList.set(taskSearchAPICount, task_data);
                                    break;
                                } else if (j == listMatchingTasks.size() - 1) {
                                    /*     Utility.showToast(this, "No task can be matched for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());*/
                                    Utility.ReportNonFatalError("onSuccess", "No task can be matched for " + AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                                    addErrorDetail(taskSearchAPICount, "No Task can be matched.");
                                }
                            }
                        }
                    } else {
                        // Log Error here
                        addErrorDetail(taskSearchAPICount, "Response of SEARCH_TASK_BY_WORKSPACE API  is empty!");
                    }
                } catch (Exception e) {
                    // Log Error here
                    addErrorDetail(taskSearchAPICount, "Error in processing response of SEARCH_TASK_BY_WORKSPACE API!");
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                }
                taskSearchAPICount++;
                if (taskSearchAPICount < AsanaTaskDataList.size()) {
                    hitAPISearchTaskByWorkspace(LevyLab_workspace_gid, AsanaTaskDataList.get(taskSearchAPICount).getQrText());
                } else {
                    taskDetailAPICount = 0;
                    hitAPIGetTaskDetails(AsanaTaskDataList.get(taskDetailAPICount).getTaskId());
                }
            } else if (apiType == Constants.API_TYPE.TASK_DETAILS) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        TaskDetail taskDetailsResponse = new Gson().fromJson(strResponse, TaskDetail.class);
                        List<CustomField> fieldList = taskDetailsResponse.getData().getCustomFields();
                        if (fieldList == null) return;
                        String beacon1_gid = "", beacon1_RSSI_gid = "", beacon1_URL = "", feasybeacon_task_gid = "", nearAnchor_gid = "", barcode = "";
                        boolean isAnchor = false;

                        // Check if this Qr code is an Anchor or not
                        List<Tag> tagList = taskDetailsResponse.getData().getTags();
                        for (Tag tag : tagList) {
                            if (String.valueOf(tag.getName()).equalsIgnoreCase("Anchor")) {
                                isAnchor = true;
                                break;
                            }
                        }

                        // get data from custom fields
                        for (int i = 0; i < fieldList.size(); i++) {
                            CustomField customField = fieldList.get(i);
                            if (customField.getName().equalsIgnoreCase("Beacon1")) {
                                beacon1_gid = customField.getGid();
                            } else if (customField.getName().equalsIgnoreCase("Beacon1 RSSI")) {
                                beacon1_RSSI_gid = customField.getGid();
                            } else if (customField.getName().equalsIgnoreCase("Beacon1 URL")) {
                                beacon1_URL = customField.getTextValue();
                                if (beacon1_URL != null && !beacon1_URL.isEmpty() && beacon1_URL.contains("/")) {
                                    feasybeacon_task_gid = beacon1_URL.substring(beacon1_URL.lastIndexOf("/") + 1);
                                } else if (beacon1_URL == null) beacon1_URL = "";
                            } else if (customField.getName().equalsIgnoreCase("Barcode")) {
                                barcode = customField.getTextValue();
                            } else if (customField.getName().equalsIgnoreCase("Near Anchor")) {
                                nearAnchor_gid = customField.getGid();
                            }
                        }

                        TaskData task_data = AsanaTaskDataList.get(taskDetailAPICount);
                        task_data.setBeacon1_gid(beacon1_gid);
                        task_data.setBeacon1_RSSI_gid(beacon1_RSSI_gid);
                        task_data.setBeacon1_URL(beacon1_URL);
                        task_data.setFeasybeacon_task_gid(feasybeacon_task_gid);
                        task_data.setAnchor(isAnchor);
                        task_data.setBarcode(barcode);
                        task_data.setNearAnchor_gid(nearAnchor_gid);
                        AsanaTaskDataList.set(taskDetailAPICount, task_data);
                    } else {
                        // Log Error here
                        addErrorDetail(taskDetailAPICount, "Response of TASK_DETAILS API  is empty!");
                    }
                } catch (Exception e) {
                    // Log Error here
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    addErrorDetail(taskDetailAPICount, "Error in processing response of API TASK_DETAILS API");
                }

                taskDetailAPICount++;
                if (taskDetailAPICount < AsanaTaskDataList.size()) {
                    hitAPIGetTaskDetails(AsanaTaskDataList.get(taskDetailAPICount).getTaskId());
                } else {
                    //  taskDetailAPICount = 0;
                    feasybeaconTaskDetailAPICount = 0;
                    callAPI_getFeasyBeaconTaskDetails();
                }


            } else if (apiType == Constants.API_TYPE.FEASYBEACON_TASK_DETAILS) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
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
                            TaskData task_data = AsanaTaskDataList.get(i);
                            if (task_data.getFeasybeacon_task_gid().equals(taskDetailsResponse.getData().getGid())) {
                                task_data.setFeasybeacon_UUID_gid(feasybeacon_UUID_gid);
                                AsanaTaskDataList.set(feasybeaconTaskDetailAPICount, task_data);
                                break;
                            }
                        }
                    } else {
                        // Log Error here
                        addErrorDetail(feasybeaconTaskDetailAPICount, "Response of FEASYBEACON_TASK_DETAILS API is empty");
                    }
                } catch (Exception e) {
                    // Log Error here
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    addErrorDetail(feasybeaconTaskDetailAPICount, "Error in processing response of FEASYBEACON_TASK_DETAILS API");
                }


                feasybeaconTaskDetailAPICount++;
                callAPI_getFeasyBeaconTaskDetails();


            } else if (apiType == Constants.API_TYPE.UPDATE_TASK) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        UpdateTAskResponse updateTAskResponse = new Gson().fromJson(strResponse, UpdateTAskResponse.class);
                    } else {
                        // Log Error here
                        addErrorDetail(taskUpdateAPICount, "Response of UPDATE_TASK API  is empty!");
                    }
                } catch (Exception e) {
                    // Log Error here
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    addErrorDetail(taskUpdateAPICount, "Error in processing response of UPDATE_TASK API");
                }
                taskUpdateAPICount++;
                if (taskUpdateAPICount < AsanaTaskDataList.size()) {
                    // updateTask();
                    updateTask_v2();
                } else {
                    //taskUpdateAPICount = 0;
                    feasybeaconTaskUpdateAPICount = 0;
                    callAPI_UpdateFeasybeaconTask();
                }


            } else if (apiType == Constants.API_TYPE.UPDATE_FEASYBEACON_TASK) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        UpdateTAskResponse updateTAskResponse = new Gson().fromJson(strResponse, UpdateTAskResponse.class);
                    } else {
                        // Log Error here
                        addErrorDetail(feasybeaconTaskUpdateAPICount, "Response of UPDATE_FEASYBEACON_TASK API  is empty!");
                    }
                } catch (Exception e) {
                    // Log Error here
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    addErrorDetail(feasybeaconTaskUpdateAPICount, "Error in processing response of UPDATE_FEASYBEACON_TASK API");
                }

                feasybeaconTaskUpdateAPICount++;
                callAPI_UpdateFeasybeaconTask();

            } else if (apiType == Constants.API_TYPE.UPLOAD_ATTACHMENTS) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        UploadAttachmentsResponse uploadAttachmentsResponse = new Gson().fromJson(strResponse, UploadAttachmentsResponse.class);
                        AsanaTaskDataList.get(attachmentUploadAPICount).setStatus("Completed");
                    } else {
                        // Log Error here
                        AsanaTaskDataList.get(attachmentUploadAPICount).setStatus("Failed");
                        addErrorDetail(attachmentUploadAPICount, "Response of UPLOAD_ATTACHMENTS API  is empty!");
                    }
                } catch (Exception e) {
                    // Log Error here
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    addErrorDetail(attachmentUploadAPICount, "Error in processing response of UPLOAD_ATTACHMENTS API!");

                }
                attachmentUploadAPICount++;
                if (attachmentUploadAPICount < AsanaTaskDataList.size()) {
                    hitAPIUploadAttachments(AsanaTaskDataList.get(attachmentUploadAPICount).getTaskId(), getMultipartImage(AsanaTaskDataList.get(attachmentUploadAPICount).getBitmapFilePath()));
                } else {
                    // new UpdateStatusInQRSheet().execute();
                    new UpdateStatusInQRSheetNew().execute();
                    new updateStatusInLocalDBAsync().execute();
                }


            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailure(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        if (response != null && response.errorBody() != null) {
            Utility.ReportNonFatalError("onFailure", "API-->" + apiType + " Error-->" + response.errorBody().toString());
        }
        onSuccess(response, apiType); // So that api call flow will not break due to a failed api call
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, Constants.API_TYPE apiType) {
        if (response != null && response.errorBody() != null) {
            Utility.ReportNonFatalError("onApiException", "API-->" + apiType + " Error-->" + response.errorBody().toString());
        }
        onSuccess(response, apiType);   // So that api call flow will not break due to a failed api call
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
            //  updateTask();
            updateTask_v2();

        }
    }

    void updateTask() {
        //if AsanaTaskDataList has an Anchor. Update NearAnchor URL to normal task containing no Anchor tag

        JsonObject input;
        if (AsanaTaskDataList.get(taskUpdateAPICount).isAnchor()) {
            input = Utility.getJSONForUpdatingAnchorTask(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI);
        } else {
            String nearAnchorURL = Utility.getNearAnchorURL(AsanaTaskDataList);
            input = Utility.getJSONForUpdatingNormalTask(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI, AsanaTaskDataList.get(taskUpdateAPICount).getNearAnchor_gid(), nearAnchorURL);
        }


        hitAPIUpdateTask(AsanaTaskDataList.get(taskUpdateAPICount).getTaskId(), input);
    }

    void updateTask_v1() {
        JsonObject input;
        if (AsanaTaskDataList.get(taskUpdateAPICount).isAnchor() && AsanaTaskDataList.get(taskUpdateAPICount).getTaskId().equals(Utility.getLastScannedAnchorTaskID(AsanaTaskDataList))) {
            // find logic here- https://app.asana.com/0/1190079917488218/1197159241924214
            String nearAnchorURL = Utility.getNearAnchorURL_v1(AsanaTaskDataList.get(taskUpdateAPICount));
            input = Utility.getJSONForUpdatingAnchorTask_v1(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI, AsanaTaskDataList.get(taskUpdateAPICount).getNearAnchor_gid(), nearAnchorURL);
        } else {
            input = Utility.getJSONForUpdatingNormalTask_v1(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI);
        }
        hitAPIUpdateTask(AsanaTaskDataList.get(taskUpdateAPICount).getTaskId(), input);
    }

    void updateTask_v2() {
        JsonObject input;
        if (AsanaTaskDataList.get(taskUpdateAPICount).isAnchor()) {
            input = Utility.getJSONForUpdatingAnchorTask_v2(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI);
        } else {
            int recentScannedAnchorPosition = Utility.getRecentlyScannedAnchorPosition(AsanaTaskDataList, taskUpdateAPICount);
            if (recentScannedAnchorPosition == -1) { // means no anchor was scanned before this QR(Task)
                input = Utility.getJSONForUpdatingAnchorTask_v2(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI);
            } else {
                String nearAnchorURL = Utility.getNearAnchorURL_v2(AsanaTaskDataList.get(recentScannedAnchorPosition).getBarcode());
                input = Utility.getJSONForUpdatingOrdinaryTask_v2(AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_gid(), AsanaTaskDataList.get(taskUpdateAPICount).getBeacon1_RSSI_gid(), strongestSignalBeaconUUID, strongestSignalBeaconRSSI, AsanaTaskDataList.get(taskUpdateAPICount).getNearAnchor_gid(), nearAnchorURL);
            }
        }

        if (input.size() != 0) {
            hitAPIUpdateTask(AsanaTaskDataList.get(taskUpdateAPICount).getTaskId(), input);
        } else if (taskUpdateAPICount < AsanaTaskDataList.size()) {
            taskUpdateAPICount++;
            updateTask_v2();
        } else {
            feasybeaconTaskUpdateAPICount = 0;
            callAPI_UpdateFeasybeaconTask();
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
            RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);
            surveyImage = MultipartBody.Part.createFormData("file", fileName, requestBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return surveyImage;
    }

    private class writeQRRecordsInSpreadsheet extends AsyncTask<Void, Void, Integer> {
        private Sheets mService = null;
        private Exception mLastError = null;

        @Override
        protected void onPreExecute() {

        }

        writeQRRecordsInSpreadsheet() {
            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            }

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                /************************Preparing data for QR Sheet*******************************/
                List<List<Object>> QR_dataList = new ArrayList<>();
                for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                    try {
                        List<Object> list1 = new ArrayList<>();
                        list1.add(AsanaTaskDataList.get(i).getDateTime());
                        //     list1.add(scannedDataList.get(i).getTimestamp());
                        //  String timestamp = Utility.getCurrentTimestamp();
                        list1.add(AsanaTaskDataList.get(i).getTimestamp());
                        //  list1.add(currentTimestamp);
                        //   AsanaTaskDataList.get(i).setTimestamp(timestamp);
                        list1.add(AsanaTaskDataList.get(i).getQrText());
                        list1.add(AsanaTaskDataList.get(i).getGdriveFileThumbnail());
                        list1.add(AsanaTaskDataList.get(i).getStatus()); // for status of updating spreadsheets and asana tasks
                        list1.add(AsanaTaskDataList.get(i).getErrors()); // for updating error details in 'errors' column in QR spreadsheet
                        QR_dataList.add(list1);
                    } catch (Exception e) {
                    }
                }

                /************************Preparing data for Bluetooth Sheet*******************************/
                //updating timestamp in bluetooth sheet
                //  List<List<Object>> Bluetooth_dataList = BeaconScannerService.getBeaconList();
                List<List<Object>> BluetoothBeacons_dataList = new ArrayList<>();
                BluetoothBeacons_dataList.addAll(Bluetooth_dataList);
                for (int i = 0; i < BluetoothBeacons_dataList.size(); i++) {
                    try {
                        List<Object> list1 = BluetoothBeacons_dataList.get(i);
                        // if(list1.size()>2) list1.set(2,currentTimestamp);
                        if (list1.size() > 4) {
                            int RSSI = 0;
                            try {
                                RSSI = Integer.parseInt(String.valueOf(list1.get(3)));
                            } catch (Exception e) {
                            }
                            if (strongestSignalBeaconRSSI == 0) {
                                strongestSignalBeaconRSSI = RSSI;
                                strongestSignalBeaconUUID = String.valueOf(list1.get(4));
                            } else if (RSSI > strongestSignalBeaconRSSI) {
                                strongestSignalBeaconRSSI = RSSI;
                                strongestSignalBeaconUUID = String.valueOf(list1.get(4));
                            }
                        }

                        // list1.set(2, Utility.getCurrentTimestamp()); // for showing in bluetooth sheet only
                        list1.set(2, ++timestampBluetoothSheet); // to nearly match with QRSheetTimestamp
                    } catch (Exception e) {
                    }


                }
                if (strongestSignalBeaconUUID == null) strongestSignalBeaconUUID = "";

                /************************Ready to Write data to Sheets*******************************/
                writeToQRSheet(QR_dataList);
                writeToBLUETOOTHSheet(BluetoothBeacons_dataList);

            } catch (Exception e) {
                mLastError = e;
                Utility.ReportNonFatalError("WriteToSheetTask", e.getMessage());
                addErrorDetail(0, "Error in writing data to spreadsheet!");
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                } else {
                    Log.e("Debugging...", "Error_Log_WriteToSheetTask:" + mLastError.getMessage());
                }

                return 0;
            }
            return 1;
        }


        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Log.e("Debugging...QR Sheet Written:" , " successfully");
            }else {
                Log.e("Debugging...QR Sheet Written:" , " failed");
            }

        }


        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                Utility.ReportNonFatalError("onCancelled", mLastError.getMessage());
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                }
            }
        }

        private void writeToQRSheet(List<List<Object>> QR_dataList) throws
                IOException, GeneralSecurityException {
            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(SaveWebRequestService.this);
            String range = com.synapse.Constants.SheetOne;
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();

            //  Utility.ReportNonFatalError("writeToQRSheet1", "1");
            if (QR_dataList.size() > 0) {
                requestBody.setValues(QR_dataList);
                Sheets.Spreadsheets.Values.Append request =
                        mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
                request.setValueInputOption(valueInputOption);
                AppendValuesResponse response = request.execute();
                updatedRangeOfDataWrittenInQRSheet = response.getUpdates().getUpdatedRange();
                //  Utility.ReportNonFatalError("writeToQRSheet2", response.toString());
                Log.e(this.toString(), response.toString());
            }
        }

        private void writeToBLUETOOTHSheet(List<List<Object>> Bluetooth_dataList) throws
                IOException, GeneralSecurityException {
            // String spreadsheetId = SharedPreferencesUtil.getDefaultBluetoothSheet(ContinuousCaptureActivity.this);
            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(SaveWebRequestService.this);
            String range = com.synapse.Constants.SheetTwo;
            ;// name of sheet and range
            //   Utility.ReportNonFatalError("Check", "3");
            String valueInputOption = "USER_ENTERED";
            ValueRange requestBody = new ValueRange();
            requestBody.setValues(Bluetooth_dataList);
            Sheets.Spreadsheets.Values.Append request =
                    mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
            request.setValueInputOption(valueInputOption);
            AppendValuesResponse response = request.execute();
            // Utility.ReportNonFatalError("Check3", response.toString());
            Log.e(this.toString(), response.toString());

        }


    }

    private class UpdateStatusInQRSheet extends AsyncTask<Void, Void, Integer> {
        private Sheets mService = null;
        private Exception mLastError = null;


        UpdateStatusInQRSheet() {
            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            }

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName(getResources().getString(R.string.app_name))

                    .build();

        }

        @Override
        protected Integer doInBackground(Void... params) {


            for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                try {
                    String timestamp = AsanaTaskDataList.get(i).getTimestamp();
                    String position = findCellPositionInSheet(timestamp);
                    String status = AsanaTaskDataList.get(i).getStatus();
                    updateStatusQRSheet(position, status);
                } catch (Exception e) {
                    mLastError = e;
                    Utility.ReportNonFatalError("WriteToSheetTask", e.getMessage());
                    addErrorDetail(0, "Error in updating status to spreadsheet!");
                    if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    } else {
                        Log.e(this.toString(), "Error_Log_WriteToSheetTask:" + mLastError.getMessage());
                    }
                    Log.e(this.toString(), e + "");
                    return 0;
                }
            }

            return 1;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Integer result) {

        }


        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                Utility.ReportNonFatalError("onCancelled", mLastError.getMessage());
            }
        }

        private void updateStatusQRSheet(String cellPosition, String status) throws IOException {
            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(SaveWebRequestService.this);
            String range = com.synapse.Constants.SheetOne + "!" + cellPosition;
            String valueInputOption = "USER_ENTERED";
            // ValueRange requestBody = new ValueRange();
            List<List<Object>> dataList = new ArrayList<>();
            List<Object> list = new ArrayList<>();
            list.add(status);
            dataList.add(list);

            //  requestBody.setValues(dataList);

          /*  Sheets.Spreadsheets.Values.Append request =
                    mService.spreadsheets().values().append(spreadsheetId, range, requestBody);
            request.setValueInputOption(valueInputOption);
             AppendValuesResponse response = request.execute();
            */

            ValueRange valueRange = new ValueRange();
            valueRange.setValues(dataList);
            UpdateValuesResponse response = mService.spreadsheets().values().update(spreadsheetId, range, valueRange)
                    .setValueInputOption(valueInputOption)
                    .execute();


            Utility.ReportNonFatalError("UpdateValuesResponse", response.toString());
            Log.e(this.toString(), response.toString());

        }

        private String findCellPositionInSheet(String searchText) throws IOException {

            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(SaveWebRequestService.this);
            String range = SpreadSheetListActivity.SheetOne;// name of sheet!range
            Sheets.Spreadsheets.Values.Get request =
                    mService.spreadsheets().values().get(spreadsheetId, range);

            ValueRange response = request.execute();
            Log.e(this.toString(), response.toPrettyString());


            List<List<Object>> values = response.getValues();
            List<List<Object>> columns = new ArrayList<>();
            if (values != null) columns.addAll(values);

            int columnNum = 4, rowNum = 0;
            Boolean found = false;
            for (int i = 0; i < columns.size(); i++) {
                if (found) break;
                List<Object> row = columns.get(i);
                for (int j = 0; j < row.size(); j++) {
                    /*if (i == 0) {
                        columnNum = row.indexOf("Status"); // get 'Status' column number
                        break;
                    }*/
                    if (row.contains(searchText)) {
                        rowNum = i;
                        found = true;
                        break;
                    }
                }

            }


            return Utility.getCellByIndex(columnNum, rowNum);

        }

    }

    private class UpdateStatusInQRSheetNew extends AsyncTask<Void, Void, Integer> {
        private Sheets mService = null;
        private Exception mLastError = null;

        @Override
        protected void onPreExecute() {
        }

        UpdateStatusInQRSheetNew() {
            GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            }

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new Sheets.Builder(
                    transport, jsonFactory, mCredential)
                    .setApplicationName(getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                /************************Preparing data for QR Sheet*******************************/
                List<List<Object>> QR_dataList = new ArrayList<>();
                for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                    try {
                        List<Object> list1 = new ArrayList<>();
                        list1.add(AsanaTaskDataList.get(i).getDateTime());
                        list1.add(AsanaTaskDataList.get(i).getTimestamp());
                        list1.add(AsanaTaskDataList.get(i).getQrText());
                        list1.add(AsanaTaskDataList.get(i).getGdriveFileThumbnail());
                        list1.add(AsanaTaskDataList.get(i).getStatus()); // for status of updating spreadsheets and asana tasks
                        list1.add(AsanaTaskDataList.get(i).getErrors()); // for updating error details in 'errors' column in QR spreadsheet
                        QR_dataList.add(list1);
                    } catch (Exception e) {
                    }
                }

                /************************Ready to Write data to Sheets*******************************/
                updateToQRSheet(QR_dataList);


            } catch (Exception e) {
                mLastError = e;
                Utility.ReportNonFatalError("OverwriteToRange", e.getMessage());
                addErrorDetail(0, "Error in overwriting data to spreadsheet!");
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                } else {
                    Log.e("Debugging...", "Error_Log_overwriting:" + mLastError.getMessage());
                }

                return 0;
            }
            return 1;
        }


        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Log.e("Debugging...QR Sheet Updated:" , " successfully");
            }else {
                Log.e("Debugging...QR Sheet Updated:" , " failed");
            }
        }


        @Override
        protected void onCancelled() {
        }

        private void updateToQRSheet(List<List<Object>> QR_dataList) throws IOException {
            String spreadsheetId = SharedPreferencesUtil.getDefaultSheetId(SaveWebRequestService.this);
            String range = updatedRangeOfDataWrittenInQRSheet;
            String valueInputOption = "USER_ENTERED";
            ValueRange valueRange = new ValueRange();
            valueRange.setValues(QR_dataList);
            UpdateValuesResponse response = mService.spreadsheets().values().update(spreadsheetId, range, valueRange)
                    .setValueInputOption(valueInputOption)
                    .execute();
            Log.e(this.toString(), response.toString());


        }


    }

    private class updateStatusInLocalDBAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected Void doInBackground(Void... params) {
            updateQRStatusInLocalDB();
            List<TaskData> s = getAllQRRecordsFromLocalStorage();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

        }

        void updateQRStatusInLocalDB() {

            for (TaskData task : AsanaTaskDataList) {
                try {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .updateStatus(task.getTimestamp(), task.getTaskId(), task.getFeasybeacon_UUID_gid(), task.getFeasybeacon_task_gid(), task.getGdriveFileId(), task.getGdriveFileParentId(), task.getGdriveFileThumbnail(), task.getBeacon1_RSSI_gid(), task.getBeacon1_URL(), task.getBeacon1_gid(), task.getStatus());
                    Log.e("Debugging...Local DB Updated:" , " successfully");
                } catch (Exception e) {
                    Log.e("Debugging...Local DB Updated:" , " failed" +e.getMessage());
                    Utility.ReportNonFatalError("updateQRStatusInLocalDB", e.getMessage());
                    addErrorDetail(0, "Error in updating status to spreadsheet!");
                }

            }




        }

    }

    private class saveQRRecordsInLocalDBAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveQRRecordsInLocalDB();
            saveBeaconRecordsInLocalDB();
            List<TaskData> QRRecords = getAllQRRecordsFromLocalStorage();
            List<Beacon> beacons = getAllBeaconsRecordsFromLocalStorage();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
        }

        void saveQRRecordsInLocalDB() {
            for (int i = 0; i < AsanaTaskDataList.size(); i++) {
                TaskData task = AsanaTaskDataList.get(i);
                try {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .insert(task);

                    Log.e("Debugging...Local DB Written:" , " successfully");
                } catch (Exception e) {
                    Log.e("Debugging...Local DB Written:" , " failed: "+e.getMessage());
                    addErrorDetail(i, "Error in inserting QR record in local db");
                    Utility.ReportNonFatalError("saveQRRecordsInLocalDB", e.getMessage());
                }
            }
        }

        void saveBeaconRecordsInLocalDB() {
            if (Bluetooth_dataList == null || Bluetooth_dataList.isEmpty()) return;
            for (List<Object> beaconData : Bluetooth_dataList) {
                try {
                    Beacon beacon = new Beacon(String.valueOf(beaconData.get(0)),
                            AsanaTaskDataList.get(0).getTimestampBeacon(), // so that by using timestamp of a qr , corresponding beacons can be retrieved
                            String.valueOf(beaconData.get(1)),
                            String.valueOf(beaconData.get(3)),
                            String.valueOf(beaconData.get(4)),
                            String.valueOf(beaconData.get(5)),
                            String.valueOf(beaconData.get(6)),
                            String.valueOf(beaconData.get(7)));

                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .beaconDao()
                            .insert(beacon);
                } catch (Exception e) {
                    Utility.ReportNonFatalError("saveQRRecordsInLocalDB", e.getMessage());
                }
            }


        }


    }

    List<TaskData> getAllQRRecordsFromLocalStorage() {
        return DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .taskDao()
                .getAll();

    }

    List<Beacon> getAllBeaconsRecordsFromLocalStorage() {
        return DatabaseClient
                .getInstance(getApplicationContext())
                .getAppDatabase()
                .beaconDao()
                .getAll();

    }

    void addErrorDetail(int TaskPos, String errorDetail) {
        String prevErrors = AsanaTaskDataList.get(TaskPos).getErrors();
        if (prevErrors.isEmpty()) {
            AsanaTaskDataList.get(TaskPos).setErrors(prevErrors + errorDetail);
        } else {
            AsanaTaskDataList.get(TaskPos).setErrors(prevErrors + "\n" + errorDetail);
        }

    }

}



