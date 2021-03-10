package com.pqiorg.multitracker.tag_anchor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pqiorg.multitracker.R;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.model.QRCode;
import com.synapse.model.create_project_by_workspace.CreateProjectByWorkspace;
import com.synapse.model.search_task.Datum;
import com.synapse.model.search_task.SearchTaskByWorkspace;
import com.synapse.network.APIError;
import com.synapse.network.Constants;
import com.synapse.network.RequestListener;
import com.synapse.network.RetrofitManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;


public class CreateNewAsanaProjectWorker extends Worker implements RequestListener {

    List<QRCode> QRCodeDataList = new ArrayList<>();
    String CurrentLoggedInUser_WorkspaceId = "", project_gid="";
    int taskSearchAPICount = 0, addTaskToProjectCount= 0 ;
    NotificationManager notificationManager;
    Integer notification_id = 324;


    public CreateNewAsanaProjectWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {

        Data inputData = getInputData();
        String[] QRTextArray = inputData.getStringArray("QRTextArray");

        for (int i = 0; i < QRTextArray.length; i++) {
            QRCodeDataList.add(new QRCode(QRTextArray[i], "", ""));
        }

        setForegroundAsync(createForegroundInfo());
        callApiToWriteDataOnAsana();

        return null;
    }

    @NonNull
    private ForegroundInfo createForegroundInfo() {
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = sendNotification("Processing data in background !!");

        return new ForegroundInfo(1, notification);
    }


    Notification sendNotification(String content) {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                // .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setTicker(getApplicationContext().getString(R.string.app_name))
                .setContentText(content)
                .setPriority(PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        notificationManager.notify(notification_id, notification);

        return notification;
    }


    private void callApiToWriteDataOnAsana() {
        CurrentLoggedInUser_WorkspaceId = SharedPreferencesUtil.getCurrentLoggedInUserWorkspaceId(getApplicationContext());
        taskSearchAPICount = 0;
        if(QRCodeDataList.size()>0)
        hitAPISearchTaskByWorkspace(CurrentLoggedInUser_WorkspaceId, QRCodeDataList.get(taskSearchAPICount).getQrText());

    }

    private void hitAPISearchTaskByWorkspace(String workspace_gid, String search_task) {
        // " Task and project must be in same workspace."

        boolean isValidURL = Patterns.WEB_URL.matcher(search_task).matches();
        if (isValidURL && search_task.startsWith("https://app.asana.com") && search_task.contains("/")) {
            String taskId = search_task.substring(search_task.lastIndexOf("/") + 1);
            QRCode qrCode = QRCodeDataList.get(taskSearchAPICount);
            qrCode.setTaskId(taskId);

            taskSearchAPICount++;
            if (taskSearchAPICount < QRCodeDataList.size()) {
                hitAPISearchTaskByWorkspace(CurrentLoggedInUser_WorkspaceId, QRCodeDataList.get(taskSearchAPICount).getQrText());
            } else {
                hitAPICreateProject();
            }
            return;
        }

        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.searchTaskByWorkspace(this, getApplicationContext(), Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE, workspace_gid, search_task, false);
    }
    private void hitAPICreateProject() {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.createProject(this, getApplicationContext(), Constants.API_TYPE.CREATE_PROJECT_IN_WORKSPACE, CurrentLoggedInUser_WorkspaceId, getJSONObjectToCreateProject(), false);
    }
    private void hitAPIAddTaskToProject(String task_gid) {
        RetrofitManager retrofitManager = RetrofitManager.getInstance();
        retrofitManager.addTaskToProject(this, getApplicationContext(), Constants.API_TYPE.ADD_TASK_TO_PROJECT, task_gid, getJSONObjectToAddTaskToProject(), false);
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
            if (apiType == Constants.API_TYPE.SEARCH_TASK_BY_WORKSPACE) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        SearchTaskByWorkspace searchTaskResponse = new Gson().fromJson(strResponse, SearchTaskByWorkspace.class);
                        if (searchTaskResponse.getData() == null || searchTaskResponse.getData().isEmpty()) {
                            Utility.ReportNonFatalError("onSuccess", "No matching task found for " + QRCodeDataList.get(taskSearchAPICount).getQrText());
                        } else {
                            QRCode task_data = QRCodeDataList.get(taskSearchAPICount);
                            List<Datum> listMatchingTasks = searchTaskResponse.getData();
                            for (int j = 0; j < listMatchingTasks.size(); j++) {
                                Datum matchingTask = listMatchingTasks.get(j);
                                if (matchingTask.getResourceType().equals("task") && matchingTask.getName().equals(task_data.getQrText())) { //// TODO matching criteria will be changed later
                                    task_data.setTaskId(matchingTask.getGid());
                                    QRCodeDataList.set(taskSearchAPICount, task_data);
                                    break;
                                } else if (j == listMatchingTasks.size() - 1) {
                                    Utility.ReportNonFatalError("onSuccess", "No task can be matched for " + QRCodeDataList.get(taskSearchAPICount).getQrText());
                                }
                            }
                        }
                    } else {
                        // Log Error here
                        //  addErrorDetail(taskSearchAPICount, "Response of SEARCH_TASK_BY_WORKSPACE API  is empty!");
                    }
                } catch (Exception e) {
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                }
                taskSearchAPICount++;
                if (taskSearchAPICount < QRCodeDataList.size()) {
                    hitAPISearchTaskByWorkspace(CurrentLoggedInUser_WorkspaceId, QRCodeDataList.get(taskSearchAPICount).getQrText());
                } else {
                    hitAPICreateProject();
                }
            } else if (apiType == Constants.API_TYPE.CREATE_PROJECT_IN_WORKSPACE) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                        CreateProjectByWorkspace createProjectByWorkspace = new Gson().fromJson(strResponse, CreateProjectByWorkspace.class);
                        if (createProjectByWorkspace.getData()==null || createProjectByWorkspace.getData().getPermalinkUrl()==null) {
                            Utility.ReportNonFatalError("Error in creating project-" + apiType, "");
                           // sendNotification("Error in creating project!!");
                        } else {
                            project_gid=createProjectByWorkspace.getData().getGid();
                            Utility.ReportNonFatalError("Created project-" , createProjectByWorkspace.getData().getPermalinkUrl());
                            addTaskToProjectCount = 0;
                            hitAPIAddTaskToProject(QRCodeDataList.get(addTaskToProjectCount).getTaskId());
                        }
                    }

                } catch (Exception e) {
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    sendNotification("Error in processing data !!");
                }

            }else if (apiType == Constants.API_TYPE.ADD_TASK_TO_PROJECT) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        String strResponse = response.body().string();
                        logResponseToFirebaseConsole(response);
                    }else {
                        Utility.ReportNonFatalError("Error in adding task " + QRCodeDataList.get(taskSearchAPICount).getTaskId() +" To Project " +project_gid, QRCodeDataList.get(taskSearchAPICount).getQrText());
                    }

                } catch (Exception e) {
                    Utility.ReportNonFatalError("Exception-" + apiType, e.getMessage());
                    sendNotification("Error in processing data !!");
                }
                addTaskToProjectCount++;
                if (addTaskToProjectCount < QRCodeDataList.size()) {
                    hitAPIAddTaskToProject(QRCodeDataList.get(addTaskToProjectCount).getTaskId());
                }else {
                    sendNotification("Project created and tasks added successfully !!");
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

    public JsonObject getJSONObjectToCreateProject() {
        String team_gid = SharedPreferencesUtil.getTeamIdForCreatingNewProject(getApplicationContext());
        String project_name = Utility.getProjectName(getApplicationContext());
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("archived", false);
            obj.addProperty("color", "light-green");
            obj.addProperty("default_view", "list");
            //obj.addProperty("due_date", null);
            //obj.addProperty("due_on", null);
            obj.addProperty("is_template", false);
            obj.addProperty("name", project_name);
            obj.addProperty("notes", "");
            obj.addProperty("owner", "me");
            obj.addProperty("public", false);
            // obj.addProperty("start_on", null);
            obj.addProperty("team", team_gid);

            obj3.add("data", obj);


        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }

    public JsonObject getJSONObjectToAddTaskToProject() {
        JsonObject obj3 = new JsonObject();
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("project", project_gid);
            obj3.add("data", obj);
        } catch (Exception e) {
            e.getCause();
        }
        return obj3;
    }
}


