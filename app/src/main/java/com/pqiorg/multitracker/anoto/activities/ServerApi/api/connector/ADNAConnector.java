package com.pqiorg.multitracker.anoto.activities.ServerApi.api.connector;

/*import com.anoto.adna.ServerApi.api.core.ITaskHandler;
import com.anoto.adna.ServerApi.api.core.ITaskType;
import com.anoto.adna.ServerApi.api.core.SharedTask;
import com.anoto.adna.ServerApi.api.manager.NetworkManager;
import com.anoto.adna.ServerApi.api.manager.ResponseManager;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.response.IResponseEvent;
import com.anoto.adna.sdk.util.DevLog;*/
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.core.ITaskHandler;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.core.ITaskType;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.core.SharedTask;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.NetworkManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.ResponseManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.response.IResponseEvent;
import com.rabbitmq.client.ConnectionFactory;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import org.apache.http.message.BasicNameValuePair;

public class ADNAConnector implements IConnector, ITaskHandler {
    private static final String TAG = "ADNAConnector";
    private NetworkManager mNetworkManager;
    private ResponseManager mResponseManager;
    private SettingManager mSettingManager;
    private SharedTask mSharedTask = null;

    public static class TaskParam {
        private List<? extends Object> mParameterValue = null;
        private IResponseEvent mResponse = null;

        public TaskParam(List<? extends Object> list, IResponseEvent iResponseEvent) {
            this.mParameterValue = list;
            this.mResponse = iResponseEvent;
        }

        public List<? extends Object> getParameterValue() {
            return this.mParameterValue;
        }

        public IResponseEvent getResponse() {
            return this.mResponse;
        }
    }

    public enum TaskType implements ITaskType {
        VERSION,
        NAMECARD,
        NAMECARD_OTHER_SERVER,
        EVENT_SCHEDULE,
        EVENT_SCHEDULE_OTHER_SERVER,
        SCAN,
        CONTENT_LOG,
        CONTENT_ACCESS,
        DELETE_CONTENT_ACCESS,
        SECURE_SCAN,
        SECURE_REGISTER,
        EUSR_PTRN_PAGE,
        EUSR_PTRN_PAGE_AREA,
        SCAN_PAGE_AREA,
        ADD_EUSR_PTRN_PAGE,
        UPD_EUSR_PTRN_PAGE_AREA,
        SIGNUP_EUSR_EMAIL,
        EUSR_LOGIN
    }

    public ADNAConnector(SharedTask sharedTask) {
        if (sharedTask == null) {
            DevLog.LoggingError(TAG, "SharedTask is null");
            throw new InvalidParameterException();
        }
        this.mSharedTask = sharedTask;
        this.mNetworkManager = NetworkManager.getInstance();
        this.mResponseManager = ResponseManager.getInstance();
        this.mSettingManager = SettingManager.getInstance();
    }

    private Object addEusrPtrnPage(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 2) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("page_address", (String) list.get(1)));
        DevLog.defaultLogging(this.mSettingManager.getApiEusrPtrnPage());
        return this.mResponseManager.analysePostResponse(this.mSettingManager.getApiEusrPtrnPage(), arrayList);
    }

    private Object contentAccess(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("device_id", (String) list.get(0)));
        DevLog.defaultLogging(this.mSettingManager.getApiContentAccess());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiContentAccess(), arrayList);
    }

    private Object contentLog(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 13) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("latitude", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("longitude", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("company_id", (String) list.get(2)));
        arrayList.add(new BasicNameValuePair("device_id", (String) list.get(3)));
        arrayList.add(new BasicNameValuePair("device_type", (String) list.get(4)));
        arrayList.add(new BasicNameValuePair("country_cd", (String) list.get(5)));
        arrayList.add(new BasicNameValuePair("city", (String) list.get(6)));
        arrayList.add(new BasicNameValuePair("timezone", (String) list.get(7)));
        arrayList.add(new BasicNameValuePair("zip", (String) list.get(8)));
        arrayList.add(new BasicNameValuePair("save_access_yn", (String) list.get(9)));
        arrayList.add(new BasicNameValuePair("cid", (String) list.get(10)));
        arrayList.add(new BasicNameValuePair("ctype", (String) list.get(11)));
        arrayList.add(new BasicNameValuePair("curl", (String) list.get(12)));
        DevLog.defaultLogging(this.mSettingManager.getApiContentLog());
        return this.mResponseManager.analysePostResponse(this.mSettingManager.getApiContentLog(), arrayList);
    }

    private Object deleteContentAccess(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 2) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("device_id", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("access_no_list", (String) list.get(1)));
        DevLog.defaultLogging(this.mSettingManager.getApiDeleteContentAccess());
        return this.mResponseManager.analyseDeleteResponse(this.mSettingManager.getApiDeleteContentAccess(), arrayList);
    }

    private Object eusrLogin(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 2) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("eusr_pw", (String) list.get(1)));
        StringBuilder sb = new StringBuilder();
        sb.append(this.mSettingManager.getApiEusrLoginEmail());
        sb.append(ConnectionFactory.DEFAULT_VHOST);
        sb.append((String) list.get(0));
        DevLog.defaultLogging(sb.toString());
        ResponseManager responseManager = this.mResponseManager;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mSettingManager.getApiEusrLoginEmail());
        sb2.append(ConnectionFactory.DEFAULT_VHOST);
        sb2.append((String) list.get(0));
        return responseManager.analysePostResponse(sb2.toString(), arrayList);
    }

    private Object eusrPtrnPage(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        DevLog.defaultLogging(this.mSettingManager.getApiEusrPtrnPage());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiEusrPtrnPage(), arrayList);
    }

    private Object eusrPtrnPageArea(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 2) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("page_address_list", (String) list.get(1)));
        DevLog.defaultLogging(this.mSettingManager.getApiEusrPtrnPageArea());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiEusrPtrnPageArea(), arrayList);
    }

    private Object event_schedule(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 1) {
            return null;
        }
        String str = (String) list.get(0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("schedule_id", str));
        String format = String.format(this.mSettingManager.getApiEventSchedule(), new Object[]{str});
        DevLog.defaultLogging(format);
        return this.mResponseManager.analyseGetResponse(format, arrayList);
    }

    private Object event_schedule_other_server(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 1) {
            return null;
        }
        return this.mResponseManager.requestGetResponse((String) list.get(0));
    }

    private Object namecard(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 1) {
            return null;
        }
        String str = (String) list.get(0);
        String format = String.format(this.mSettingManager.getApiNamecard(), new Object[]{str});
        DevLog.defaultLogging(format);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("namecard_id", (String) list.get(0)));
        return this.mResponseManager.analyseGetResponse(format, arrayList);
    }

    private Object namecard_other_server(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 1) {
            return null;
        }
        return this.mResponseManager.requestGetResponse((String) list.get(0));
    }

    private Object scan(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 12) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("scan_x", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("scan_y", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("page_address", (String) list.get(2)));
        arrayList.add(new BasicNameValuePair("latitude", (String) list.get(3)));
        arrayList.add(new BasicNameValuePair("longitude", (String) list.get(4)));
        arrayList.add(new BasicNameValuePair("device_id", (String) list.get(5)));
        arrayList.add(new BasicNameValuePair("device_type", (String) list.get(6)));
        arrayList.add(new BasicNameValuePair("country_cd", (String) list.get(7)));
        arrayList.add(new BasicNameValuePair("city", (String) list.get(8)));
        arrayList.add(new BasicNameValuePair("timezone", (String) list.get(9)));
        arrayList.add(new BasicNameValuePair("zip", (String) list.get(10)));
        arrayList.add(new BasicNameValuePair("save_access_yn", (String) list.get(11)));
        DevLog.defaultLogging(this.mSettingManager.getApiScan());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiScan(), arrayList);
    }

    private Object scanPageArea(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 12) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("scan_x", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("scan_y", (String) list.get(2)));
        arrayList.add(new BasicNameValuePair("page_address", (String) list.get(3)));
        arrayList.add(new BasicNameValuePair("latitude", (String) list.get(4)));
        arrayList.add(new BasicNameValuePair("longitude", (String) list.get(5)));
        arrayList.add(new BasicNameValuePair("device_id", (String) list.get(6)));
        arrayList.add(new BasicNameValuePair("device_type", (String) list.get(7)));
        arrayList.add(new BasicNameValuePair("country_cd", (String) list.get(8)));
        arrayList.add(new BasicNameValuePair("city", (String) list.get(9)));
        arrayList.add(new BasicNameValuePair("timezone", (String) list.get(10)));
        arrayList.add(new BasicNameValuePair("zip", (String) list.get(11)));
        DevLog.defaultLogging(this.mSettingManager.getApiScanPageArea());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiScanPageArea(), arrayList);
    }

    private Object secure_register(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 6) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("work_id", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("tape_no", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("attach_latitude", (String) list.get(2)));
        arrayList.add(new BasicNameValuePair("attach_longitude", (String) list.get(3)));
        arrayList.add(new BasicNameValuePair("attach_picture_base64", (String) list.get(4)));
        arrayList.add(new BasicNameValuePair("attach_comments", (String) list.get(5)));
        DevLog.defaultLogging("secure_register...mResponseManager");
        DevLog.defaultLogging(this.mSettingManager.getApiSecureAttach());
        return this.mResponseManager.analysePostResponse(this.mSettingManager.getApiSecureAttach(), arrayList);
    }

    private Object secure_scan(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 6) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("page_address", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("scan_ptn_x", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("scan_ptn_y", (String) list.get(2)));
        arrayList.add(new BasicNameValuePair("scan_latitude", (String) list.get(3)));
        arrayList.add(new BasicNameValuePair("scan_longitude", (String) list.get(4)));
        arrayList.add(new BasicNameValuePair("device_id", (String) list.get(5)));
        DevLog.defaultLogging(this.mSettingManager.getApiSecureScan());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiSecureScan(), arrayList);
    }

    private Object signupEusrEmail(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 3) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("eusr_pw", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("auth_site", (String) list.get(2)));
        StringBuilder sb = new StringBuilder();
        sb.append(this.mSettingManager.getApiEusrSignupEmail());
        sb.append(ConnectionFactory.DEFAULT_VHOST);
        sb.append((String) list.get(0));
        DevLog.defaultLogging(sb.toString());
        ResponseManager responseManager = this.mResponseManager;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mSettingManager.getApiEusrSignupEmail());
        sb2.append(ConnectionFactory.DEFAULT_VHOST);
        sb2.append((String) list.get(0));
        return responseManager.analysePostResponse(sb2.toString(), arrayList);
    }

    private Object updEusrPtrnPageArea(List<? extends Object> list) throws UnsupportedEncodingException {
        if (list == null || list.size() != 7) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("eusr_email", (String) list.get(0)));
        arrayList.add(new BasicNameValuePair("page_address_list", (String) list.get(1)));
        arrayList.add(new BasicNameValuePair("ptrn_coords_id_list", (String) list.get(2)));
        arrayList.add(new BasicNameValuePair("ptrn_coords_name", (String) list.get(3)));
        arrayList.add(new BasicNameValuePair("ctype", (String) list.get(4)));
        arrayList.add(new BasicNameValuePair("curl", (String) list.get(5)));
        arrayList.add(new BasicNameValuePair("usable_duration", (String) list.get(6)));
        DevLog.defaultLogging(this.mSettingManager.getApiEusrPtrnPage());
        return this.mResponseManager.analysePutResponse(this.mSettingManager.getApiEusrPtrnPageArea(), arrayList);
    }

    private Object version(List<? extends Object> list) throws UnsupportedEncodingException {
        DevLog.defaultLogging(this.mSettingManager.getApiVerson());
        return this.mResponseManager.analyseGetResponse(this.mSettingManager.getApiVerson());
    }

    public void addEusrPtrnPage(String str, String str2, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.ADD_EUSR_PTRN_PAGE, new TaskParam(Arrays.asList(new String[]{str, str2}), iResponseEvent));
    }

    public void contentAccess(String str, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.CONTENT_ACCESS, new TaskParam(Arrays.asList(new String[]{str}), iResponseEvent));
    }

    public void contentLog(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.CONTENT_LOG, new TaskParam(Arrays.asList(new String[]{str5, str6, str, str7, str8, str9, str10, str11, str12, str13, str2, str3, str4}), iResponseEvent));
    }

    public void deleteContentAccess(String str, String str2, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.DELETE_CONTENT_ACCESS, new TaskParam(Arrays.asList(new String[]{str, str2}), iResponseEvent));
    }

    public void eusrLogin(String str, String str2, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.EUSR_LOGIN, new TaskParam(Arrays.asList(new String[]{str, str2}), iResponseEvent));
    }

    public void eusrPtrnPage(String str, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.EUSR_PTRN_PAGE, new TaskParam(Arrays.asList(new String[]{str}), iResponseEvent));
    }

    public void eusrPtrnPageArea(String str, String str2, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.EUSR_PTRN_PAGE_AREA, new TaskParam(Arrays.asList(new String[]{str, str2}), iResponseEvent));
    }

    public void event_schedule(String str, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.EVENT_SCHEDULE, new TaskParam(Arrays.asList(new String[]{str}), iResponseEvent));
    }

    public void event_schedule_other_server(String str, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.EVENT_SCHEDULE_OTHER_SERVER, new TaskParam(Arrays.asList(new String[]{str}), iResponseEvent));
    }

    public void getScanCoordinate(String str, String str2, String str3, String str4, String str5, String str6, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.SECURE_SCAN, new TaskParam(Arrays.asList(new String[]{str, str2, str3, str4, str5, str6}), iResponseEvent));
    }

    public void handleTask(ITaskType iTaskType, Object obj) throws IllegalArgumentException {
        Object obj2;
        if (!(iTaskType instanceof TaskType)) {
            throw new IllegalArgumentException("pTaskType should be one of TaskTypes which is defined previous");
        } else if (!(obj instanceof TaskParam)) {
            throw new IllegalArgumentException("pParam should be TaskParam type");
        } else {
            TaskParam taskParam = (TaskParam) obj;
            TaskType taskType = (TaskType) iTaskType;
            try {
                switch (taskType) {

                    case VERSION:
                            obj2 = version(taskParam.getParameterValue());
                        break;
                    case NAMECARD:
                        obj2 = namecard(taskParam.getParameterValue());
                        break;
                    case NAMECARD_OTHER_SERVER:
                        obj2 = namecard_other_server(taskParam.getParameterValue());
                        break;
                    case EVENT_SCHEDULE:
                        obj2 = event_schedule(taskParam.getParameterValue());
                        break;
                    case EVENT_SCHEDULE_OTHER_SERVER:
                        obj2 = event_schedule_other_server(taskParam.getParameterValue());
                        break;
                    case SCAN:
                        DevLog.defaultLogging("handleTask scan...");
                        obj2 = scan(taskParam.getParameterValue());
                        break;
                    case CONTENT_LOG:
                        obj2 = contentLog(taskParam.getParameterValue());
                        break;
                    case CONTENT_ACCESS:
                        obj2 = contentAccess(taskParam.getParameterValue());
                        break;
                    case DELETE_CONTENT_ACCESS:
                        obj2 = deleteContentAccess(taskParam.getParameterValue());
                        break;
                    case SECURE_SCAN:
                        DevLog.defaultLogging("handleTask secure_scan...");
                        obj2 = secure_scan(taskParam.getParameterValue());
                        break;
                    case SECURE_REGISTER:
                        DevLog.defaultLogging("handleTask secure_register...");
                        obj2 = secure_register(taskParam.getParameterValue());
                        break;
                    case EUSR_PTRN_PAGE:
                        obj2 = eusrPtrnPage(taskParam.getParameterValue());
                        break;
                    case EUSR_PTRN_PAGE_AREA:
                        obj2 = eusrPtrnPageArea(taskParam.getParameterValue());
                        break;
                    case SCAN_PAGE_AREA:
                        DevLog.defaultLogging("handleTask scan page area...");
                        obj2 = scanPageArea(taskParam.getParameterValue());
                        break;
                    case ADD_EUSR_PTRN_PAGE:
                        DevLog.defaultLogging("handleTask add eurn ptrn page...");
                        obj2 = addEusrPtrnPage(taskParam.getParameterValue());
                        break;
                    case UPD_EUSR_PTRN_PAGE_AREA:
                        DevLog.defaultLogging("handleTask upd eusr ptrn page area...");
                        obj2 = updEusrPtrnPageArea(taskParam.getParameterValue());
                        break;
                    case SIGNUP_EUSR_EMAIL:
                        DevLog.defaultLogging("handleTask signup eusr email...");
                        obj2 = signupEusrEmail(taskParam.getParameterValue());
                        break;
                    case EUSR_LOGIN:
                        DevLog.defaultLogging("handleTask eusr login...");
                        obj2 = eusrLogin(taskParam.getParameterValue());
                        break;
                    default:
                        obj2 = null;
                        break;
                }
                if (!(taskParam == null || taskParam.getResponse() == null)) {
                    taskParam.getResponse().onResponse(obj2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot call task type: ");
                sb.append(taskType.name());
                DevLog.LoggingError(str, sb.toString());
                if (!(taskParam == null || taskParam.getResponse() == null)) {
                    taskParam.getResponse().onResponse(null);
                }
            } catch (Throwable th) {
                if (!(taskParam == null || taskParam.getResponse() == null)) {
                    taskParam.getResponse().onResponse(null);
                }
                throw th;
            }
        }
    }

    public void namecard(String str, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.NAMECARD, new TaskParam(Arrays.asList(new String[]{str}), iResponseEvent));
    }

    public void namecard_other_server(String str, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.NAMECARD_OTHER_SERVER, new TaskParam(Arrays.asList(new String[]{str}), iResponseEvent));
    }

    public void scan(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.SCAN, new TaskParam(Arrays.asList(new String[]{str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12}), iResponseEvent));
    }

    public void scanPageArea(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.SCAN_PAGE_AREA, new TaskParam(Arrays.asList(new String[]{str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12}), iResponseEvent));
    }

    public void sendTapeRegister(String str, String str2, String str3, String str4, String str5, String str6, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.SECURE_REGISTER, new TaskParam(Arrays.asList(new String[]{str, str2, str3, str4, str5, str6}), iResponseEvent));
    }

    public void signupUserEmail(String str, String str2, String str3, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.SIGNUP_EUSR_EMAIL, new TaskParam(Arrays.asList(new String[]{str, str2, str3}), iResponseEvent));
    }

    public void updEusrPtrnPageArea(String str, String str2, String str3, String str4, String str5, String str6, String str7, IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.UPD_EUSR_PTRN_PAGE_AREA, new TaskParam(Arrays.asList(new String[]{str, str2, str3, str4, str5, str6, str7}), iResponseEvent));
    }

    public void version(IResponseEvent<Object> iResponseEvent) {
        this.mSharedTask.addTask(this, TaskType.VERSION, new TaskParam(null, iResponseEvent));
    }
}
