package com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingManager {
    private static final String API_CONTENT_ACCESS = "api/udrs/contents/access";
    private static final String API_CONTENT_LOG = "api/udrs/contents/log";
    private static final String API_DELETE_CONTENT_ACCESS = "api/udrs/contents/access";
    private static final String API_EUSR_LOGIN_EMAIL = "api/eusr/login";
    private static final String API_EUSR_PTRN_PAGE = "api/eusr/ptrn/page";
    private static final String API_EUSR_PTRN_PAGE_AREA = "api/eusr/ptrn/page/area";
    private static final String API_EUSR_SIGNUP_EMAIL = "api/eusr";
    private static final String API_EVENT_SCHEDULE = "api/comm/event_schedule/%s";
    private static final String API_NAMECARD = "api/comm/namecard/%s";
    private static final String API_SCAN = "api/udrs/scan";
    private static final String API_SCAN_PAGE_AREA = "api/eusr/ptrn/page/area/scan";
    private static final String API_SECURE_ATTACH = "api/comm/tape/attach";
    private static final String API_SECURE_SCAN = "api/udrs/scan";
    private static final String API_SECURE_WARN = "api/udrs/warn";
    private static final String API_VERSION = "api/comm/app/version/android";
    public static final boolean DEBUG = false;
    public static final String PREFERENCE_LOGIN = "preference_login";
    public static final String PREFERENCE_TOKEN = "preference_token";
    private static SettingManager instance;
    private static SharedPreferences mSharedPreferences;
    private String DEV_SERVER_URL = "http://192.168.10.145:5000";
    private String SERVER_URL = "http://www.a2big.com/release";
    private String mApiKey = "";
    private String mCity = "";
    private String mCountryCd = "";
    private String mDeviceId = "";
    private String mDeviceType = "Android";
    private String mLatitude = "37.5668367";
    private String mLongitude = "126.97857279999994";
    private boolean mOcrMode = false;
    private boolean mSaveAccessYn = false;
    private String mTimeZone = "";
    private String mZip = "";

    public static SettingManager getInstance() {
        if (instance == null) {
            instance = new SettingManager();
        }
        return instance;
    }

    public String getApiContentAccess() {
        return "api/udrs/contents/access";
    }

    public String getApiContentLog() {
        return API_CONTENT_LOG;
    }

    public String getApiDeleteContentAccess() {
        return "api/udrs/contents/access";
    }

    public String getApiEusrLoginEmail() {
        return API_EUSR_LOGIN_EMAIL;
    }

    public String getApiEusrPtrnPage() {
        return API_EUSR_PTRN_PAGE;
    }

    public String getApiEusrPtrnPageArea() {
        return API_EUSR_PTRN_PAGE_AREA;
    }

    public String getApiEusrSignupEmail() {
        return API_EUSR_SIGNUP_EMAIL;
    }

    public String getApiEventSchedule() {
        return API_EVENT_SCHEDULE;
    }

    public String getApiKey() {
        return this.mApiKey;
    }

    public String getApiNamecard() {
        return API_NAMECARD;
    }

    public String getApiScan() {
        return "api/udrs/scan";
    }

    public String getApiScanPageArea() {
        return API_SCAN_PAGE_AREA;
    }

    public String getApiSecureAttach() {
        return API_SECURE_ATTACH;
    }

    public String getApiSecureScan() {
        return "api/udrs/scan";
    }

    public String getApiSecureWarn() {
        return API_SECURE_WARN;
    }

    public String getApiVerson() {
        return API_VERSION;
    }

    public String getCity() {
        return this.mCity;
    }

    public String getCountryCd() {
        return this.mCountryCd;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public String getDeviceType() {
        return this.mDeviceType;
    }

    public String getLatitude() {
        return this.mLatitude;
    }

    public String getLongitude() {
        return this.mLongitude;
    }

    public boolean getOcrMode() {
        return this.mOcrMode;
    }

    public String getServerURL() {
        return this.SERVER_URL;
    }

    public SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreferences;
    }

    public String getTimeZone() {
        return this.mTimeZone;
    }

    public String getZip() {
        return this.mZip;
    }

    public boolean isDebugMode() {
        return false;
    }

    public void setApiKey(String str) {
        this.mApiKey = str;
    }

    public void setCity(String str) {
        this.mCity = str;
    }

    public void setCountryCd(String str) {
        this.mCountryCd = str;
    }

    public void setDeviceId(String str) {
        this.mDeviceId = str;
    }

    public void setDeviceType(String str) {
        this.mDeviceType = str;
    }

    public void setLatitude(String str) {
        this.mLatitude = str;
    }

    public void setLongitude(String str) {
        this.mLongitude = str;
    }

    public void setOcrMode(boolean z) {
        this.mOcrMode = z;
    }

    public void setSaveAccessYn(boolean z) {
        this.mSaveAccessYn = z;
    }

    public void setServerURL(String str) {
        this.SERVER_URL = str;
        this.DEV_SERVER_URL = str;
    }

    public void setTimeZone(String str) {
        this.mTimeZone = str;
    }

    public void setZip(String str) {
        this.mZip = str;
    }
}
