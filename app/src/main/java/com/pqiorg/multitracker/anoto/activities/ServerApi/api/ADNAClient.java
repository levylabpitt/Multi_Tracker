package com.pqiorg.multitracker.anoto.activities.ServerApi.api;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
/*import android.support.media.ExifInterface;
import android.util.Log;
import com.anoto.adna.ServerApi.api.connector.IConnector;
import com.anoto.adna.ServerApi.api.core.CoreAccessHelper;
import com.anoto.adna.ServerApi.api.manager.NetworkManager;
import com.anoto.adna.ServerApi.api.manager.ResponseManager;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.AddEusrPtrnPageObject;
import com.anoto.adna.ServerApi.api.object.AddEusrPtrnPageObject.AddEusrPtrnPageData;
import com.anoto.adna.ServerApi.api.object.ContentAccessObject;
import com.anoto.adna.ServerApi.api.object.ContentAccessObject.ContentAccessData;
import com.anoto.adna.ServerApi.api.object.ContentLogObject;
import com.anoto.adna.ServerApi.api.object.ContentLogObject.ContentLogData;
import com.anoto.adna.ServerApi.api.object.DataEventVo;
import com.anoto.adna.ServerApi.api.object.DataPatternAreaVo;
import com.anoto.adna.ServerApi.api.object.DataPatternVo;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.ServerApi.api.object.DeleteContentAccessObject;
import com.anoto.adna.ServerApi.api.object.EusrPtrnPageAreaObject;
import com.anoto.adna.ServerApi.api.object.EusrPtrnPageAreaObject.EusrPtrnPageAreaData;
import com.anoto.adna.ServerApi.api.object.EusrPtrnPageObject;
import com.anoto.adna.ServerApi.api.object.EusrPtrnPageObject.EusrPtrnPageData;
import com.anoto.adna.ServerApi.api.object.EventScheduleObject;
import com.anoto.adna.ServerApi.api.object.EventScheduleObject.EventData;
import com.anoto.adna.ServerApi.api.object.NameCardObject;
import com.anoto.adna.ServerApi.api.object.NameCardObject.NameCardData;
import com.anoto.adna.ServerApi.api.object.ScanObject;
import com.anoto.adna.ServerApi.api.object.ScanObject.ScanData;
import com.anoto.adna.ServerApi.api.object.ScanPageAreaDataVo;
import com.anoto.adna.ServerApi.api.object.ScanPageAreaObject;
import com.anoto.adna.ServerApi.api.object.ScanPageAreaObject.ScanPageAreaData;
import com.anoto.adna.ServerApi.api.object.SignupEusrEmailObject;
import com.anoto.adna.ServerApi.api.object.SignupEusrEmailObject.SignupEusrEmailData;
import com.anoto.adna.ServerApi.api.object.UpdEusrPtrnPageAreaObject;
import com.anoto.adna.ServerApi.api.object.UpdEusrPtrnPageAreaObject.UpdEusrPtrnPageAreaData;
import com.anoto.adna.ServerApi.api.object.VersionObject;
import com.anoto.adna.ServerApi.api.object.VersionObject.VersionData;
import com.anoto.adna.ServerApi.api.response.IResponseEvent;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.util.BasicUtil;
import com.anoto.adna.sdk.util.DevLog;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.games.GamesStatusCodes;*/
import androidx.exifinterface.media.ExifInterface;

import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.games.GamesStatusCodes;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.connector.IConnector;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.core.CoreAccessHelper;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.NetworkManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.ResponseManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.AddEusrPtrnPageObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ContentAccessObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ContentLogObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataEventVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataPatternAreaVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataPatternVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DeleteContentAccessObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.EusrPtrnPageAreaObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.EusrPtrnPageObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.EventScheduleObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.NameCardObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ScanObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ScanPageAreaDataVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ScanPageAreaObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.SignupEusrEmailObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.UpdEusrPtrnPageAreaObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.VersionObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.response.IResponseEvent;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.http.HttpHost;

public class ADNAClient {
    public static final int ERR_INVALID_JSON = 502;
    public static final int ERR_NETWORK = 500;
    public static final int ERR_NOT_PARSION_JSON = 503;
    public static final int ERR_SERVER = 501;
    public static final int MSG_ADD_EUSR_PTRN_PAGE = 17;
    public static final int MSG_CONTENT_ACCESS = 6;
    public static final int MSG_CONTENT_LOG = 5;
    public static final int MSG_DELETE_CONTENT_ACCESS = 7;
    public static final int MSG_EUSR_LOGIN = 20;
    public static final int MSG_EUSR_PTRN_PAGE = 8;
    public static final int MSG_EUSR_PTRN_PAGE_AREA = 9;
    public static final int MSG_EVENT_SCHEDULE = 3;
    public static final int MSG_NAMECARD = 2;
    public static final int MSG_SCAN = 4;
    public static final int MSG_SCAN_PAGE_AREA = 10;
    public static final int MSG_SCAN_PAGE_AREA_RESULT_EMPTY_OWNER = 101;
    public static final int MSG_SCAN_PAGE_AREA_RESULT_MATCH_OWNER = 103;
    public static final int MSG_SCAN_PAGE_AREA_RESULT_MISSMATCH_OWNER = 102;
    public static final int MSG_SIGNUP_EUSR_EMAIL = 19;
    public static final int MSG_UPD_EUSR_PTRN_PAGE_AREA = 18;
    public static final int MSG_VERSION = 1;
    public static final int SCAN_RESULT_EVENT_SCHEDULE = 16;
    public static final int SCAN_RESULT_IMAGE = 12;
    public static final int SCAN_RESULT_MP3 = 13;
    public static final int SCAN_RESULT_NAMECARD = 15;
    public static final int SCAN_RESULT_VIDEO = 14;
    public static final int SCAN_RESULT_WEB = 11;
    private static ADNAClient instance = null;
    public static double mLatitude = 37.5668367d;
    public static double mLongitude = 126.97857279999994d;
    private String TAG = "ADNAClient";

    /* renamed from: a */
    ADNAListener f2650a;
    private CoreAccessHelper mAccessor = null;
    private IConnector mConnector = null;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public NetworkManager mNetworkManager = null;
    /* access modifiers changed from: private */
    public ResponseManager mResponseManager;
    /* access modifiers changed from: private */
    public SettingManager mSettingManager = null;

    private class AddEusrPtrnPageResponseEvent implements IResponseEvent<Object> {
        private AddEusrPtrnPageResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new AddEusrPtrnPageResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class AddEusrPtrnPageResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2652a;

        /* renamed from: b */
        int f2653b;

        /* renamed from: c */
        String f2654c;

        /* renamed from: d */
        AddEusrPtrnPageObject.AddEusrPtrnPageData f2655d;

        private AddEusrPtrnPageResponseTask() {
            this.f2652a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2652a = z;
            this.f2653b = i;
            this.f2654c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("AddEusrPtrnPageResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            AddEusrPtrnPageObject addEusrPtrnPageObject = new AddEusrPtrnPageObject();
            if (!addEusrPtrnPageObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2655d = addEusrPtrnPageObject.getAddEusrPtrnPageInfo();
                if (this.f2655d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2652a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2653b;
                str = this.f2654c;
            } else if (this.f2655d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(17, this.f2655d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2655d.result.code).intValue();
                str = this.f2655d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class ContentAccessResponseEvent implements IResponseEvent<Object> {
        private ContentAccessResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new ContentAccessResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class ContentAccessResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2658a = false;

        /* renamed from: b */
        int f2659b;

        /* renamed from: c */
        String f2660c;

        /* renamed from: d */
        ContentAccessObject.ContentAccessData f2661d;
        private ArrayList<DataVo> mScanItems;

        public ContentAccessResponseTask() {
            DevLog.defaultLogging("ContentAccessResponseTask() init....");
            this.mScanItems = new ArrayList<>();
        }

        /* JADX WARNING: Removed duplicated region for block: B:36:0x01d0 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        /*private void procAccessLogResponse(
                ContentAccessObject.ContentAccessData r9) {
            *//*
                r8 = this;
                r0 = 0
                r1 = 0
                r2 = 0
            L_0x0003:
                com.anoto.adna.ServerApi.api.object.ContentAccessObject$Data r3 = r9.data
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.DataVo> r3 = r3.contents_access_list
                int r3 = r3.size()
                if (r1 >= r3) goto L_0x01d4
                com.anoto.adna.ServerApi.api.object.ContentAccessObject$Data r3 = r9.data
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.DataVo> r3 = r3.contents_access_list
                java.lang.Object r3 = r3.get(r1)
                com.anoto.adna.ServerApi.api.object.DataVo r3 = (com.anoto.adna.ServerApi.api.object.DataVo) r3
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "[DEBUG] cids.... {\"cid\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCid()
                r4.append(r5)
                java.lang.String r5 = "\", \"cname\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCname()
                r4.append(r5)
                java.lang.String r5 = "\", \"ctype\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCtype()
                r4.append(r5)
                java.lang.String r5 = "\", \"ctype\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCtype()
                r4.append(r5)
                java.lang.String r5 = "\", \"curl\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCurl()
                r4.append(r5)
                java.lang.String r5 = "\"}"
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                java.lang.String r4 = r3.getCurl()
                android.net.Uri r4 = android.net.Uri.parse(r4)
                r4.getAuthority()
                r4.getPath()
                java.lang.String r5 = r4.getScheme()
                r4.getQueryParameterNames()
                java.lang.String r4 = r3.getCtype()
                int r4 = java.lang.Integer.parseInt(r4)
                r6 = 1
                if (r4 == r6) goto L_0x01cb
                r7 = 500(0x1f4, float:7.0E-43)
                switch(r4) {
                    case 3: goto L_0x01cb;
                    case 4: goto L_0x01cb;
                    case 5: goto L_0x01cb;
                    case 6: goto L_0x0124;
                    case 7: goto L_0x008b;
                    default: goto L_0x0089;
                }
            L_0x0089:
                goto L_0x01d0
            L_0x008b:
                java.lang.String r4 = r3.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                boolean r4 = r4.isOtherServer(r5)
                if (r4 == 0) goto L_0x00db
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x00d4
                com.anoto.adna.ServerApi.api.ADNAClient r2 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r2 = r2.mResponseManager
                java.lang.String r4 = r3.getCurl()
                java.lang.String r2 = r2.requestGetResponse(r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Other Server >>>> EventSchedule...Response "
            L_0x00c1:
                r4.append(r5)
                r4.append(r2)
                java.lang.String r4 = r4.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                boolean r2 = r8.procEvent(r3, r2)
                goto L_0x01d0
            L_0x00d4:
                java.lang.String r3 = "unconnected network"
                r8.setErr(r6, r7, r3)
                goto L_0x01d0
            L_0x00db:
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x00d4
                java.lang.String r2 = r3.getCurl()
                java.util.ArrayList r4 = new java.util.ArrayList
                r4.<init>()
                org.apache.http.message.BasicNameValuePair r5 = new org.apache.http.message.BasicNameValuePair
                java.lang.String r7 = "schedule_id"
                r5.<init>(r7, r2)
                r4.add(r5)
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.SettingManager r5 = r5.mSettingManager
                java.lang.String r5 = r5.getApiEventSchedule()
                java.lang.Object[] r6 = new java.lang.Object[r6]
                r6[r0] = r2
                java.lang.String r2 = java.lang.String.format(r5, r6)
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r5 = r5.mResponseManager
                java.lang.String r2 = r5.analyseGetResponse(r2, r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "EventSchedule...Response "
                goto L_0x00c1
            L_0x0124:
                java.lang.String r4 = r3.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                boolean r4 = r4.isOtherServer(r5)
                if (r4 == 0) goto L_0x0172
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x016c
                com.anoto.adna.ServerApi.api.ADNAClient r2 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r2 = r2.mResponseManager
                java.lang.String r4 = r3.getCurl()
                java.lang.String r2 = r2.requestGetResponse(r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Other Server >>>> NameCard...Response "
            L_0x015a:
                r4.append(r5)
                r4.append(r2)
                java.lang.String r4 = r4.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                boolean r2 = r8.procNameCard(r3, r2)
                goto L_0x01c2
            L_0x016c:
                java.lang.String r4 = "unconnected network"
                r8.setErr(r6, r7, r4)
                goto L_0x01c2
            L_0x0172:
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x016c
                com.anoto.adna.ServerApi.api.ADNAClient r2 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.SettingManager r2 = r2.mSettingManager
                java.lang.String r2 = r2.getApiNamecard()
                java.lang.Object[] r4 = new java.lang.Object[r6]
                java.lang.String r5 = r3.getCurl()
                r4[r0] = r5
                java.lang.String r2 = java.lang.String.format(r2, r4)
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r2)
                java.util.ArrayList r4 = new java.util.ArrayList
                r4.<init>()
                org.apache.http.message.BasicNameValuePair r5 = new org.apache.http.message.BasicNameValuePair
                java.lang.String r6 = "namecard_id"
                java.lang.String r7 = r3.getCurl()
                r5.<init>(r6, r7)
                r4.add(r5)
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r5 = r5.mResponseManager
                java.lang.String r2 = r5.analyseGetResponse(r2, r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = " NameCard...Response "
                goto L_0x015a
            L_0x01c2:
                java.lang.String r4 = r3.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                if (r2 != 0) goto L_0x01d0
            L_0x01cb:
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.DataVo> r4 = r8.mScanItems
                r4.add(r3)
            L_0x01d0:
                int r1 = r1 + 1
                goto L_0x0003
            L_0x01d4:
                return
            *//*
            throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.ADNAClient.ContentAccessResponseTask.procAccessLogResponse(com.anoto.adna.ServerApi.api.object.ContentAccessObject$ContentAccessData):void");
        }*/
        private void procAccessLogResponse(ContentAccessObject.ContentAccessData param1ContentAccessData) {
            Object object=null;
            int i = 0;
            boolean bool = false;
            while (i < param1ContentAccessData.data.contents_access_list.size()) {
                DataVo dataVo = param1ContentAccessData.data.contents_access_list.get(i);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[DEBUG] cids.... {\"cid\":\"");
                stringBuilder.append(dataVo.getCid());
                stringBuilder.append("\", \"cname\":\"");
                stringBuilder.append(dataVo.getCname());
                stringBuilder.append("\", \"ctype\":\"");
                stringBuilder.append(dataVo.getCtype());
                stringBuilder.append("\", \"ctype\":\"");
                stringBuilder.append(dataVo.getCtype());
                stringBuilder.append("\", \"curl\":\"");
                stringBuilder.append(dataVo.getCurl());
                stringBuilder.append("\"}");
                DevLog.defaultLogging(stringBuilder.toString());
                Uri uri = Uri.parse(dataVo.getCurl());
                uri.getAuthority();
                uri.getPath();
                String str = uri.getScheme();
                uri.getQueryParameterNames();
                int j = Integer.parseInt(dataVo.getCtype());
                Object object1 = object;
                if (j != 1) {
                    object1 = object;
                    switch (j) {
                        default:
                            object1 = object;
                            break;
                        case 7:
                            DevLog.defaultLogging(dataVo.toString());
                        case 6:
                            DevLog.defaultLogging(dataVo.toString());
                        case 3:
                        case 4:
                        case 5:
                            this.mScanItems.add(dataVo);
                            break;
                    }
                    continue;
                }
                i++;
                object = object1;
            }
        }


        private boolean procEvent(DataVo dataVo, String str) {
            int i;
            String str2;
            EventScheduleObject eventScheduleObject = new EventScheduleObject();
            if (!eventScheduleObject.onResponseListener(str)) {
                i = 502;
                str2 = "invalid JSON format";
            } else if (eventScheduleObject.getEventInfo() == null) {
                i = 503;
                str2 = "not parsing JSON";
            } else {
                for (int i2 = 0; i2 < eventScheduleObject.getEventInfo().data.event_list.size(); i2++) {
                    DataVo dataVo2 = new DataVo();
                    dataVo2.setAccess_no(dataVo.getAccess_no());
                    dataVo2.setDevice_id(dataVo.getDevice_id());
                    dataVo2.setDevice_type(dataVo.getDevice_type());
                    dataVo2.setCompany_id(dataVo.getCompany_id());
                    dataVo2.setCompany_name(dataVo.getCompany_name());
                    dataVo2.setLast_access_dt(dataVo.getLast_access_dt());
                    dataVo2.setLast_country_cd(dataVo.getLast_country_cd());
                    dataVo2.setLast_city(dataVo.getLast_city());
                    dataVo2.setCid(dataVo.getCid());
                    dataVo2.setCname(dataVo.getCname());
                    dataVo2.setCtype(dataVo.getCtype());
                    dataVo2.setCurl(dataVo.getCurl());
                    dataVo2.setEvent((DataEventVo) eventScheduleObject.getEventInfo().data.event_list.get(i2));
                    StringBuilder sb = new StringBuilder();
                    sb.append("Server >>>> EventSchedule...");
                    sb.append(i2);
                    sb.append(" ### ");
                    sb.append(dataVo2.getEvent().title);
                    DevLog.defaultLogging(sb.toString());
                    this.mScanItems.add(dataVo2);
                }
                return false;
            }
            setErr(true, i, str2);
            return true;
        }

        private boolean procNameCard(DataVo dataVo, String str) {
            int i;
            String str2;
            NameCardObject nameCardObject = new NameCardObject();
            if (!nameCardObject.onResponseListener(str)) {
                DevLog.defaultLogging("invalid JSON format");
                i = 502;
                str2 = "invalid JSON format";
            } else if (nameCardObject.getNameCardInfo() == null) {
                DevLog.defaultLogging("not parsing JSON");
                i = 503;
                str2 = "not parsing JSON";
            } else {
                dataVo.setNameCard(nameCardObject.getNameCardInfo().data);
                return false;
            }
            setErr(true, i, str2);
            return true;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2658a = z;
            this.f2659b = i;
            this.f2660c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int intValue;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("ContentAccessResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            ContentAccessObject contentAccessObject = new ContentAccessObject();
            if (!contentAccessObject.onResponseListener(strArr[0])) {
                intValue = 502;
                str = "invalid JSON format";
            } else {
                this.f2661d = contentAccessObject.getContentAccessInfo();
                if (this.f2661d == null) {
                    intValue = 503;
                    str = "not parsing JSON";
                } else if (this.f2661d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    procAccessLogResponse(this.f2661d);
                    return Boolean.valueOf(true);
                } else {
                    intValue = Integer.valueOf(this.f2661d.result.code).intValue();
                    str = this.f2661d.result.message;
                }
            }
            setErr(true, intValue, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            String dataVo;
            super.onPostExecute(bool);
            if (this.f2658a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2659b;
                str = this.f2660c;
            } else {
                for (int i = 0; i < this.mScanItems.size(); i++) {
                    DataVo dataVo2 = (DataVo) this.mScanItems.get(i);
                    if (dataVo2.getEvent() != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(dataVo2.toString());
                        sb.append(" ");
                        sb.append(dataVo2.getEvent().title);
                        dataVo = sb.toString();
                    } else {
                        dataVo = dataVo2.toString();
                    }
                    DevLog.defaultLogging(dataVo);
                }
                if (this.f2661d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    ADNAClient.this.f2650a.onReceiveADNA(6, this.mScanItems);
                    return;
                }
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2661d.result.code).intValue();
                str = this.f2661d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class ContentLogResponseEvent implements IResponseEvent<Object> {
        private ContentLogResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new ContentLogResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class ContentLogResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2664a;

        /* renamed from: b */
        int f2665b;

        /* renamed from: c */
        String f2666c;

        /* renamed from: d */
        ContentLogObject.ContentLogData f2667d;

        private ContentLogResponseTask() {
            this.f2664a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2664a = z;
            this.f2665b = i;
            this.f2666c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("ContentLogResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            ContentLogObject contentLogObject = new ContentLogObject();
            if (!contentLogObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2667d = contentLogObject.getContentLogInfo();
                if (this.f2667d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2664a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2665b;
                str = this.f2666c;
            } else if (this.f2667d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(5, this.f2667d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2667d.result.code).intValue();
                str = this.f2667d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class DeleteContentAccessResponseEvent implements IResponseEvent<Object> {
        private DeleteContentAccessResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new DeleteContentAccessResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class DeleteContentAccessResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2670a;

        /* renamed from: b */
        int f2671b;

        /* renamed from: c */
        String f2672c;

        /* renamed from: d */
        DeleteContentAccessObject.ContentAccessData f2673d;

        private DeleteContentAccessResponseTask() {
            this.f2670a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2670a = z;
            this.f2671b = i;
            this.f2672c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("DeleteContentAccessResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            DeleteContentAccessObject deleteContentAccessObject = new DeleteContentAccessObject();
            if (!deleteContentAccessObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2673d = deleteContentAccessObject.getContentAccessInfo();
                if (this.f2673d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2670a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2671b;
                str = this.f2672c;
            } else if (this.f2673d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(7, this.f2673d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2673d.result.code).intValue();
                str = this.f2673d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class EusrLoginResponseEvent implements IResponseEvent<Object> {
        private EusrLoginResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new EusrLoginResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class EusrLoginResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2676a;

        /* renamed from: b */
        int f2677b;

        /* renamed from: c */
        String f2678c;

        /* renamed from: d */
        SignupEusrEmailObject.SignupEusrEmailData f2679d;

        private EusrLoginResponseTask() {
            this.f2676a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2676a = z;
            this.f2677b = i;
            this.f2678c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("EusrLoginResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            SignupEusrEmailObject signupEusrEmailObject = new SignupEusrEmailObject();
            if (!signupEusrEmailObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2679d = signupEusrEmailObject.getSignupEusrEmailInfo();
                if (this.f2679d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2676a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2677b;
                str = this.f2678c;
            } else if (this.f2679d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(20, this.f2679d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2679d.result.code).intValue();
                str = this.f2679d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class EusrPtrnPageAreaResponseEvent implements IResponseEvent<Object> {
        private EusrPtrnPageAreaResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new EusrPtrnPageAreaResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class EusrPtrnPageAreaResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2682a = false;

        /* renamed from: b */
        int f2683b;

        /* renamed from: c */
        String f2684c;

        /* renamed from: d */
        EusrPtrnPageAreaObject.EusrPtrnPageAreaData f2685d;
        private ArrayList<DataPatternAreaVo> mPtrnItems;

        public EusrPtrnPageAreaResponseTask() {
            DevLog.defaultLogging("EusrPtrnPageAreaResponseTask() init....");
            this.mPtrnItems = new ArrayList<>();
        }

        private void procPtrnPageAreaLogResponse(EusrPtrnPageAreaObject.EusrPtrnPageAreaData eusrPtrnPageAreaData) {
            for (int i = 0; i < eusrPtrnPageAreaData.data.ptrn_area_list.size(); i++) {
                DataPatternAreaVo dataPatternAreaVo = (DataPatternAreaVo) eusrPtrnPageAreaData.data.ptrn_area_list.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("[DEBUG] pages.... {\"page_address\":\"");
                sb.append(dataPatternAreaVo.getPage_address());
                sb.append("\", \"ptrn_coords_id\":\"");
                sb.append(dataPatternAreaVo.getPtrn_coords_id());
                sb.append("\", \"index_in_page\":\"");
                sb.append(dataPatternAreaVo.getIndex_in_page());
                sb.append("\", \"reg_dt\":\"");
                sb.append(dataPatternAreaVo.getReg_dt());
                sb.append("\", \"curl\":\"");
                sb.append(dataPatternAreaVo.getCurl());
                sb.append("\"}");
                DevLog.defaultLogging(sb.toString());
            }
        }

        private void setErr(boolean z, int i, String str) {
            this.f2682a = z;
            this.f2683b = i;
            this.f2684c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int intValue;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("EusrPtrnPageAreaResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            EusrPtrnPageAreaObject eusrPtrnPageAreaObject = new EusrPtrnPageAreaObject();
            if (!eusrPtrnPageAreaObject.onResponseListener(strArr[0])) {
                intValue = 502;
                str = "invalid JSON format";
            } else {
                this.f2685d = eusrPtrnPageAreaObject.getEusrPtrnPageAreaDataInfo();
                if (this.f2685d == null) {
                    intValue = 503;
                    str = "not parsing JSON";
                } else {
                    if (!this.f2685d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                        intValue = Integer.valueOf(this.f2685d.result.code).intValue();
                        str = this.f2685d.result.message;
                    }
                    return Boolean.valueOf(true);
                }
            }
            setErr(true, intValue, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            String dataPatternAreaVo;
            super.onPostExecute(bool);
            if (this.f2682a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2683b;
                str = this.f2684c;
            } else {
                for (int i = 0; i < this.mPtrnItems.size(); i++) {
                    DataPatternAreaVo dataPatternAreaVo2 = (DataPatternAreaVo) this.mPtrnItems.get(i);
                    if (dataPatternAreaVo2.getPage_address() != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(dataPatternAreaVo2.toString());
                        sb.append(" ");
                        sb.append(dataPatternAreaVo2.getPage_address());
                        dataPatternAreaVo = sb.toString();
                    } else {
                        dataPatternAreaVo = dataPatternAreaVo2.toString();
                    }
                    DevLog.defaultLogging(dataPatternAreaVo);
                }
                if (this.f2685d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    ADNAClient.this.f2650a.onReceiveADNA(9, this.f2685d);
                    return;
                }
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2685d.result.code).intValue();
                str = this.f2685d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class EusrPtrnPageResponseEvent implements IResponseEvent<Object> {
        private EusrPtrnPageResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new EusrPtrnPageResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class EusrPtrnPageResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2688a = false;

        /* renamed from: b */
        int f2689b;

        /* renamed from: c */
        String f2690c;

        /* renamed from: d */
        EusrPtrnPageObject.EusrPtrnPageData f2691d;
        private ArrayList<DataPatternVo> mPtrnItems;

        public EusrPtrnPageResponseTask() {
            DevLog.defaultLogging("EusrPtrnPageResponseTask() init....");
            this.mPtrnItems = new ArrayList<>();
        }

        private void procPtrnPageLogResponse(EusrPtrnPageObject.EusrPtrnPageData eusrPtrnPageData) {
            for (int i = 0; i < eusrPtrnPageData.data.page_address_list.size(); i++) {
                DataPatternVo dataPatternVo = (DataPatternVo) eusrPtrnPageData.data.page_address_list.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("[DEBUG] page_address_list.... {\"page_address\":\"");
                sb.append(dataPatternVo.getPage_address());
                sb.append("\", \"area_cnt\":\"");
                sb.append(dataPatternVo.getArea_cnt());
                sb.append("\", \"usable_area_cnt\":\"");
                sb.append(dataPatternVo.getUsable_area_cnt());
                sb.append("\", \"reg_dt\":\"");
                sb.append(dataPatternVo.getReg_dt());
                sb.append("\"}");
                DevLog.defaultLogging(sb.toString());
            }
        }

        private void setErr(boolean z, int i, String str) {
            this.f2688a = z;
            this.f2689b = i;
            this.f2690c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int intValue;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("EusrPtrnPageResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            EusrPtrnPageObject eusrPtrnPageObject = new EusrPtrnPageObject();
            if (!eusrPtrnPageObject.onResponseListener(strArr[0])) {
                intValue = 502;
                str = "invalid JSON format";
            } else {
                this.f2691d = eusrPtrnPageObject.getEusrPtrnPageDataInfo();
                if (this.f2691d == null) {
                    intValue = 503;
                    str = "not parsing JSON";
                } else {
                    if (!this.f2691d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                        intValue = Integer.valueOf(this.f2691d.result.code).intValue();
                        str = this.f2691d.result.message;
                    }
                    return Boolean.valueOf(true);
                }
            }
            setErr(true, intValue, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            String dataPatternVo;
            super.onPostExecute(bool);
            if (this.f2688a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2689b;
                str = this.f2690c;
            } else {
                for (int i = 0; i < this.mPtrnItems.size(); i++) {
                    DataPatternVo dataPatternVo2 = (DataPatternVo) this.mPtrnItems.get(i);
                    if (dataPatternVo2.getPage_address() != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(dataPatternVo2.toString());
                        sb.append(" ");
                        sb.append(dataPatternVo2.getPage_address());
                        dataPatternVo = sb.toString();
                    } else {
                        dataPatternVo = dataPatternVo2.toString();
                    }
                    DevLog.defaultLogging(dataPatternVo);
                }
                if (this.f2691d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    ADNAClient.this.f2650a.onReceiveADNA(8, this.f2691d);
                    return;
                }
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2691d.result.code).intValue();
                str = this.f2691d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class EventScheduleResponseEvent implements IResponseEvent<Object> {
        private EventScheduleResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new EventScheduleResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class EventScheduleResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2694a;

        /* renamed from: b */
        int f2695b;

        /* renamed from: c */
        String f2696c;

        /* renamed from: d */
        EventScheduleObject.EventData f2697d;

        private EventScheduleResponseTask() {
            this.f2694a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2694a = z;
            this.f2695b = i;
            this.f2696c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            this.f2694a = false;
            StringBuilder sb = new StringBuilder();
            sb.append("EventScheduleResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            EventScheduleObject eventScheduleObject = new EventScheduleObject();
            if (!eventScheduleObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2697d = eventScheduleObject.getEventInfo();
                if (this.f2697d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2694a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2695b;
                str = this.f2696c;
            } else if (this.f2697d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(16, this.f2697d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2697d.result.code).intValue();
                str = this.f2697d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class NameCardResponseEvent implements IResponseEvent<Object> {
        private NameCardResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, "no response from server");
                return;
            }
            new NameCardResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class NameCardResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2700a;

        /* renamed from: b */
        int f2701b;

        /* renamed from: c */
        String f2702c;

        /* renamed from: d */
        NameCardObject.NameCardData f2703d;

        private NameCardResponseTask() {
            this.f2700a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2700a = z;
            this.f2701b = i;
            this.f2702c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("NameCardResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            NameCardObject nameCardObject = new NameCardObject();
            if (!nameCardObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2703d = nameCardObject.getNameCardInfo();
                if (this.f2703d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2700a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2701b;
                str = this.f2702c;
            } else if (this.f2703d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(15, this.f2703d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2703d.result.code).intValue();
                str = this.f2703d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class ScanPageAreaResponseEvent implements IResponseEvent<Object> {
        private ScanPageAreaResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, "no response from server");
                return;
            }
            new ScanPageAreaResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class ScanPageAreaResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2706a = false;

        /* renamed from: b */
        int f2707b;

        /* renamed from: c */
        String f2708c;

        /* renamed from: d */
        ScanPageAreaObject.ScanPageAreaData f2709d;
        private ArrayList<ScanPageAreaDataVo> mScanPageItems;

        public ScanPageAreaResponseTask() {
            DevLog.defaultLogging("ScanPageAreaResponseTask() init....");
            this.mScanPageItems = new ArrayList<>();
        }

        private void procScanPageAreaResponse(ScanPageAreaObject.ScanPageAreaData scanPageAreaData) {
            ADNAListener aDNAListener;
            int i;
            if (scanPageAreaData.data.owner_eusr_email.equals("")) {
                aDNAListener = ADNAClient.this.f2650a;
                i = 101;
           } else if (!GlobalVar.USER_ACCOUNT.equals(scanPageAreaData.data.owner_eusr_email)) {
           // } else if (!BeaconReferenceApplication.USER_ACCOUNT.equals(scanPageAreaData.data.owner_eusr_email)) {
                aDNAListener = ADNAClient.this.f2650a;
                i = 102;
            } else if (GlobalVar.USER_ACCOUNT.equals(scanPageAreaData.data.owner_eusr_email)) {
          //  } else if (BeaconReferenceApplication.USER_ACCOUNT.equals(scanPageAreaData.data.owner_eusr_email)) {
                aDNAListener = ADNAClient.this.f2650a;
                i = 103;
            } else {
                return;
            }
            aDNAListener.onReceiveADNA(i, scanPageAreaData);
        }

        private void setErr(boolean z, int i, String str) {
            this.f2706a = z;
            this.f2707b = i;
            this.f2708c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int intValue=0;
            String str="";
            this.f2706a = false;
            ScanPageAreaObject scanPageAreaObject = new ScanPageAreaObject();
            if (!scanPageAreaObject.onResponseListener(strArr[0])) {
                setErr(true, 502, "invalid JSON format");
            } else {
                try {
                    this.f2709d = scanPageAreaObject.getmScanPageAreaInfo();
                    if (this.f2709d == null) {
                        intValue = 503;
                        str = "not parsing JSON";
                    } else if (!this.f2709d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                        intValue = Integer.valueOf(this.f2709d.result.code).intValue();
                        str = this.f2709d.result.message;
                    }
                    setErr(true, intValue, str);
                } catch (Exception e) {
                    DevLog.defaultLogging(e.toString());
                    ADNAClient.this.f2650a.onFailedToReceiveADNA(GamesStatusCodes.STATUS_VIDEO_NOT_ACTIVE, "server is no response");
                }
            }
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            String scanPageAreaDataVo;
            super.onPostExecute(bool);
            DevLog.defaultLogging("ScanPageAreaResponseTask() onPostExecute....");
            for (int i = 0; i < this.mScanPageItems.size(); i++) {
                ScanPageAreaDataVo scanPageAreaDataVo2 = (ScanPageAreaDataVo) this.mScanPageItems.get(i);
                if (scanPageAreaDataVo2.getCid() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(scanPageAreaDataVo2.toString());
                    sb.append(" ");
                    sb.append(scanPageAreaDataVo2.getCid());
                    scanPageAreaDataVo = sb.toString();
                } else {
                    scanPageAreaDataVo = scanPageAreaDataVo2.toString();
                }
                DevLog.defaultLogging(scanPageAreaDataVo);
            }
            try {
                if (this.f2709d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    procScanPageAreaResponse(this.f2709d);
                } else {
                    ADNAClient.this.f2650a.onFailedToReceiveADNA(Integer.valueOf(this.f2709d.result.code).intValue(), this.f2709d.result.message);
                }
            } catch (Exception e) {
                Log.d("onPostExecute", e.getMessage().toString());
                DevLog.defaultLogging(e.toString());
                ADNAClient.this.f2650a.onFailedToReceiveADNA(GamesStatusCodes.STATUS_VIDEO_NOT_ACTIVE, "server is no response");
            }
        }
    }

    private class ScanResponseEvent implements IResponseEvent<Object> {
        private ScanResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, "no response from server");
                return;
            }
            new ScanResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class ScanResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2712a = false;

        /* renamed from: b */
        int f2713b;

        /* renamed from: c */
        String f2714c;

        /* renamed from: d */
        ScanObject.ScanData f2715d;
        private ArrayList<DataVo> mScanItems;

        public ScanResponseTask() {
            DevLog.defaultLogging("ScanResponseTask() init....");
            this.mScanItems = new ArrayList<>();
        }

        private boolean procEvent(DataVo dataVo, String str) {
            int i;
            String str2;
            EventScheduleObject eventScheduleObject = new EventScheduleObject();
            if (!eventScheduleObject.onResponseListener(str)) {
                i = 502;
                str2 = "invalid JSON format";
            } else if (eventScheduleObject.getEventInfo() == null) {
                i = 503;
                str2 = "not parsing JSON";
            } else {
                for (int i2 = 0; i2 < eventScheduleObject.getEventInfo().data.event_list.size(); i2++) {
                    DataVo dataVo2 = new DataVo();
                    dataVo2.setAccess_no(dataVo.getAccess_no());
                    dataVo2.setDevice_id(dataVo.getDevice_id());
                    dataVo2.setDevice_type(dataVo.getDevice_type());
                    dataVo2.setCompany_id(dataVo.getCompany_id());
                    dataVo2.setCompany_name(dataVo.getCompany_name());
                    dataVo2.setLast_access_dt(dataVo.getLast_access_dt());
                    dataVo2.setLast_country_cd(dataVo.getLast_country_cd());
                    dataVo2.setLast_city(dataVo.getLast_city());
                    dataVo2.setCid(dataVo.getCid());
                    dataVo2.setCname(dataVo.getCname());
                    dataVo2.setCtype(dataVo.getCtype());
                    dataVo2.setCurl(dataVo.getCurl());
                    dataVo2.setEvent((DataEventVo) eventScheduleObject.getEventInfo().data.event_list.get(i2));
                    StringBuilder sb = new StringBuilder();
                    sb.append("Server >>>> EventSchedule...");
                    sb.append(i2);
                    sb.append(" ### ");
                    sb.append(dataVo2.getEvent().title);
                    DevLog.defaultLogging(sb.toString());
                    this.mScanItems.add(dataVo2);
                }
                return false;
            }
            setErr(true, i, str2);
            return true;
        }

        private boolean procNameCard(DataVo dataVo, String str) {
            int i;
            String str2;
            NameCardObject nameCardObject = new NameCardObject();
            if (!nameCardObject.onResponseListener(str)) {
                DevLog.defaultLogging("invalid JSON format");
                i = 502;
                str2 = "invalid JSON format";
            } else if (nameCardObject.getNameCardInfo() == null) {
                DevLog.defaultLogging("not parsing JSON");
                i = 503;
                str2 = "not parsing JSON";
            } else {
                dataVo.setNameCard(nameCardObject.getNameCardInfo().data);
                return false;
            }
            setErr(true, i, str2);
            return true;
        }

        /* JADX WARNING: Removed duplicated region for block: B:36:0x01d0 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        /*private void procScanResponse(ScanObject.ScanData r9) {
            *//*
                r8 = this;
                r0 = 0
                r1 = 0
                r2 = 0
            L_0x0003:
                com.anoto.adna.ServerApi.api.object.ScanObject$Data r3 = r9.data
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.DataVo> r3 = r3.cid_list
                int r3 = r3.size()
                if (r1 >= r3) goto L_0x01d4
                com.anoto.adna.ServerApi.api.object.ScanObject$Data r3 = r9.data
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.DataVo> r3 = r3.cid_list
                java.lang.Object r3 = r3.get(r1)
                com.anoto.adna.ServerApi.api.object.DataVo r3 = (com.anoto.adna.ServerApi.api.object.DataVo) r3
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "[DEBUG] cids.... {\"cid\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCid()
                r4.append(r5)
                java.lang.String r5 = "\", \"cname\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCname()
                r4.append(r5)
                java.lang.String r5 = "\", \"ctype\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCtype()
                r4.append(r5)
                java.lang.String r5 = "\", \"ctype\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCtype()
                r4.append(r5)
                java.lang.String r5 = "\", \"curl\":\""
                r4.append(r5)
                java.lang.String r5 = r3.getCurl()
                r4.append(r5)
                java.lang.String r5 = "\"}"
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                java.lang.String r4 = r3.getCurl()
                android.net.Uri r4 = android.net.Uri.parse(r4)
                r4.getAuthority()
                r4.getPath()
                java.lang.String r5 = r4.getScheme()
                r4.getQueryParameterNames()
                java.lang.String r4 = r3.getCtype()
                int r4 = java.lang.Integer.parseInt(r4)
                r6 = 1
                if (r4 == r6) goto L_0x01cb
                r7 = 500(0x1f4, float:7.0E-43)
                switch(r4) {
                    case 3: goto L_0x01cb;
                    case 4: goto L_0x01cb;
                    case 5: goto L_0x01cb;
                    case 6: goto L_0x0124;
                    case 7: goto L_0x008b;
                    default: goto L_0x0089;
                }
            L_0x0089:
                goto L_0x01d0
            L_0x008b:
                java.lang.String r4 = r3.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                boolean r4 = r4.isOtherServer(r5)
                if (r4 == 0) goto L_0x00db
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x00d4
                com.anoto.adna.ServerApi.api.ADNAClient r2 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r2 = r2.mResponseManager
                java.lang.String r4 = r3.getCurl()
                java.lang.String r2 = r2.requestGetResponse(r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Other Server >>>> EventSchedule...Response "
            L_0x00c1:
                r4.append(r5)
                r4.append(r2)
                java.lang.String r4 = r4.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                boolean r2 = r8.procEvent(r3, r2)
                goto L_0x01d0
            L_0x00d4:
                java.lang.String r3 = "unconnected network"
                r8.setErr(r6, r7, r3)
                goto L_0x01d0
            L_0x00db:
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x00d4
                java.lang.String r2 = r3.getCurl()
                java.util.ArrayList r4 = new java.util.ArrayList
                r4.<init>()
                org.apache.http.message.BasicNameValuePair r5 = new org.apache.http.message.BasicNameValuePair
                java.lang.String r7 = "schedule_id"
                r5.<init>(r7, r2)
                r4.add(r5)
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.SettingManager r5 = r5.mSettingManager
                java.lang.String r5 = r5.getApiEventSchedule()
                java.lang.Object[] r6 = new java.lang.Object[r6]
                r6[r0] = r2
                java.lang.String r2 = java.lang.String.format(r5, r6)
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r5 = r5.mResponseManager
                java.lang.String r2 = r5.analyseGetResponse(r2, r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "EventSchedule...Response "
                goto L_0x00c1
            L_0x0124:
                java.lang.String r4 = r3.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                boolean r4 = r4.isOtherServer(r5)
                if (r4 == 0) goto L_0x0172
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x016c
                com.anoto.adna.ServerApi.api.ADNAClient r2 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r2 = r2.mResponseManager
                java.lang.String r4 = r3.getCurl()
                java.lang.String r2 = r2.requestGetResponse(r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Other Server >>>> NameCard...Response "
            L_0x015a:
                r4.append(r5)
                r4.append(r2)
                java.lang.String r4 = r4.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                boolean r2 = r8.procNameCard(r3, r2)
                goto L_0x01c2
            L_0x016c:
                java.lang.String r4 = "unconnected network"
                r8.setErr(r6, r7, r4)
                goto L_0x01c2
            L_0x0172:
                com.anoto.adna.ServerApi.api.ADNAClient r4 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.NetworkManager r4 = r4.mNetworkManager
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                android.content.Context r5 = r5.mContext
                boolean r4 = r4.isNetworkAvailable(r5)
                if (r4 == 0) goto L_0x016c
                com.anoto.adna.ServerApi.api.ADNAClient r2 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.SettingManager r2 = r2.mSettingManager
                java.lang.String r2 = r2.getApiNamecard()
                java.lang.Object[] r4 = new java.lang.Object[r6]
                java.lang.String r5 = r3.getCurl()
                r4[r0] = r5
                java.lang.String r2 = java.lang.String.format(r2, r4)
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r2)
                java.util.ArrayList r4 = new java.util.ArrayList
                r4.<init>()
                org.apache.http.message.BasicNameValuePair r5 = new org.apache.http.message.BasicNameValuePair
                java.lang.String r6 = "namecard_id"
                java.lang.String r7 = r3.getCurl()
                r5.<init>(r6, r7)
                r4.add(r5)
                com.anoto.adna.ServerApi.api.ADNAClient r5 = com.anoto.adna.ServerApi.api.ADNAClient.this
                com.anoto.adna.ServerApi.api.manager.ResponseManager r5 = r5.mResponseManager
                java.lang.String r2 = r5.analyseGetResponse(r2, r4)
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = " NameCard...Response "
                goto L_0x015a
            L_0x01c2:
                java.lang.String r4 = r3.toString()
                com.anoto.adna.sdk.util.DevLog.defaultLogging(r4)
                if (r2 != 0) goto L_0x01d0
            L_0x01cb:
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.DataVo> r4 = r8.mScanItems
                r4.add(r3)
            L_0x01d0:
                int r1 = r1 + 1
                goto L_0x0003
            L_0x01d4:
                return
            *//*
            throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.ADNAClient.ScanResponseTask.procScanResponse(com.anoto.adna.ServerApi.api.object.ScanObject$ScanData):void");
        }
        */
        private void procScanResponse(ScanObject.ScanData param1ScanData) {
            Object object=null;
            int i = 0;
            boolean bool = false;
            while (i < param1ScanData.data.cid_list.size()) {
                DataVo dataVo = param1ScanData.data.cid_list.get(i);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[DEBUG] cids.... {\"cid\":\"");
                stringBuilder.append(dataVo.getCid());
                stringBuilder.append("\", \"cname\":\"");
                stringBuilder.append(dataVo.getCname());
                stringBuilder.append("\", \"ctype\":\"");
                stringBuilder.append(dataVo.getCtype());
                stringBuilder.append("\", \"ctype\":\"");
                stringBuilder.append(dataVo.getCtype());
                stringBuilder.append("\", \"curl\":\"");
                stringBuilder.append(dataVo.getCurl());
                stringBuilder.append("\"}");
                DevLog.defaultLogging(stringBuilder.toString());
                Uri uri = Uri.parse(dataVo.getCurl());
                uri.getAuthority();
                uri.getPath();
                String str = uri.getScheme();
                uri.getQueryParameterNames();
                int j = Integer.parseInt(dataVo.getCtype());
                Object object1 = object;
                if (j != 1) {
                    object1 = object;
                    switch (j) {
                        default:
                            object1 = object;
                            break;
                        case 7:
                            DevLog.defaultLogging(dataVo.toString());
                        case 6:
                            DevLog.defaultLogging(dataVo.toString());
                        case 3:
                        case 4:
                        case 5:
                            this.mScanItems.add(dataVo);
                            break;
                    }
                    continue;
                }
                i++;
                object = object1;
            }
        }


        private void setErr(boolean z, int i, String str) {
            this.f2712a = z;
            this.f2713b = i;
            this.f2714c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int intValue=0;
            String str="";
            this.f2712a = false;
            ScanObject scanObject = new ScanObject();
            if (!scanObject.onResponseListener(strArr[0])) {
                setErr(true, 502, "invalid JSON format");
            } else {
                try {
                    this.f2715d = scanObject.getmScanInfo();
                    if (this.f2715d == null) {
                        intValue = 503;
                        str = "not parsing JSON";
                    } else if (this.f2715d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                        procScanResponse(this.f2715d);
                    } else {
                        intValue = Integer.valueOf(this.f2715d.result.code).intValue();
                        str = this.f2715d.result.message;
                    }
                    setErr(true, intValue, str);
                } catch (Exception e) {
                    DevLog.defaultLogging(e.toString());
                    ADNAClient.this.f2650a.onFailedToReceiveADNA(GamesStatusCodes.STATUS_VIDEO_NOT_ACTIVE, "server is no response");
                }
            }
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            String dataVo;
            super.onPostExecute(bool);
            DevLog.defaultLogging("ScanResponseTask() onPostExecute....");
            for (int i = 0; i < this.mScanItems.size(); i++) {
                DataVo dataVo2 = (DataVo) this.mScanItems.get(i);
                if (dataVo2.getEvent() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(dataVo2.toString());
                    sb.append(" ");
                    sb.append(dataVo2.getEvent().title);
                    dataVo = sb.toString();
                } else {
                    dataVo = dataVo2.toString();
                }
                DevLog.defaultLogging(dataVo);
            }
            try {
                if (this.f2715d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    ADNAClient.this.f2650a.onReceiveADNA(4, this.mScanItems);
                } else {
                    ADNAClient.this.f2650a.onFailedToReceiveADNA(Integer.valueOf(this.f2715d.result.code).intValue(), this.f2715d.result.message);
                }
            } catch (Exception e) {
                DevLog.defaultLogging(e.toString());
                ADNAClient.this.f2650a.onFailedToReceiveADNA(GamesStatusCodes.STATUS_VIDEO_NOT_ACTIVE, "server is no response");
            }
        }
    }

    private class SignupUserEmailResponseEvent implements IResponseEvent<Object> {
        private SignupUserEmailResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new SignupUserEmailResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class SignupUserEmailResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2718a;

        /* renamed from: b */
        int f2719b;

        /* renamed from: c */
        String f2720c;

        /* renamed from: d */
        SignupEusrEmailObject.SignupEusrEmailData f2721d;

        private SignupUserEmailResponseTask() {
            this.f2718a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2718a = z;
            this.f2719b = i;
            this.f2720c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("SignupUserEmailResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            SignupEusrEmailObject signupEusrEmailObject = new SignupEusrEmailObject();
            if (!signupEusrEmailObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2721d = signupEusrEmailObject.getSignupEusrEmailInfo();
                if (this.f2721d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2718a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2719b;
                str = this.f2720c;
            } else if (this.f2721d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(19, this.f2721d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2721d.result.code).intValue();
                str = this.f2721d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class UpdEusrPtrnPageAreaResponseEvent implements IResponseEvent<Object> {
        private UpdEusrPtrnPageAreaResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new UpdEusrPtrnPageAreaResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class UpdEusrPtrnPageAreaResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        boolean f2724a;

        /* renamed from: b */
        int f2725b;

        /* renamed from: c */
        String f2726c;

        /* renamed from: d */
        UpdEusrPtrnPageAreaObject.UpdEusrPtrnPageAreaData f2727d;

        private UpdEusrPtrnPageAreaResponseTask() {
            this.f2724a = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2724a = z;
            this.f2725b = i;
            this.f2726c = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("UpdEusrPtrnPageAreaResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            UpdEusrPtrnPageAreaObject updEusrPtrnPageAreaObject = new UpdEusrPtrnPageAreaObject();
            if (!updEusrPtrnPageAreaObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2727d = updEusrPtrnPageAreaObject.getUpdEusrPtrnPageAreaInfo();
                if (this.f2727d == null) {
                    i = 503;
                    str = "not parsing JSON";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2724a) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2725b;
                str = this.f2726c;
            } else if (this.f2727d.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(18, this.f2727d);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2727d.result.code).intValue();
                str = this.f2727d.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private class VersionResponseEvent implements IResponseEvent<Object> {
        private VersionResponseEvent() {
        }

        public void onResponse(Object obj) {
            if (obj == null) {
                ADNAClient.this.f2650a.onFailedToReceiveADNA(501, " no response from server");
                return;
            }
            new VersionResponseTask().execute(new String[]{obj.toString()});
        }
    }

    private class VersionResponseTask extends AsyncTask<String, Void, Boolean> {

        /* renamed from: a */
        VersionObject.VersionData f2730a;

        /* renamed from: b */
        boolean f2731b;

        /* renamed from: c */
        int f2732c;

        /* renamed from: d */
        String f2733d;

        private VersionResponseTask() {
            this.f2731b = false;
        }

        private void setErr(boolean z, int i, String str) {
            this.f2731b = z;
            this.f2732c = i;
            this.f2733d = str;
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(String... strArr) {
            int i;
            String str;
            this.f2731b = false;
            StringBuilder sb = new StringBuilder();
            sb.append("VersionResponseTask doInBackground.#");
            sb.append(strArr[0]);
            DevLog.defaultLogging(sb.toString());
            VersionObject versionObject = new VersionObject();
            if (!versionObject.onResponseListener(strArr[0])) {
                i = 502;
                str = "invalid JSON format";
            } else {
                this.f2730a = versionObject.getVersionData();
                if (this.f2730a == null) {
                    i = 503;
                    str = "Error Parsing json";
                }
                return Boolean.valueOf(true);
            }
            setErr(true, i, str);
            return Boolean.valueOf(true);
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void onPostExecute(Boolean bool) {
            ADNAListener aDNAListener;
            int intValue;
            String str;
            super.onPostExecute(bool);
            if (this.f2731b) {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = this.f2732c;
                str = this.f2733d;
            } else if (this.f2730a.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                ADNAClient.this.f2650a.onReceiveADNA(1, this.f2730a);
                return;
            } else {
                aDNAListener = ADNAClient.this.f2650a;
                intValue = Integer.valueOf(this.f2730a.result.code).intValue();
                str = this.f2730a.result.message;
            }
            aDNAListener.onFailedToReceiveADNA(intValue, str);
        }
    }

    private ADNAClient(Context context) {
        this.mContext = context;
        this.mNetworkManager = NetworkManager.getInstance();
        this.mSettingManager = SettingManager.getInstance();
        this.mConnector = getConnector();
        this.mResponseManager = ResponseManager.getInstance();
        init();
    }

    private void connectCoreEngine() {
        if (this.mAccessor == null) {
            synchronized (this) {
                if (this.mAccessor == null) {
                    this.mAccessor = new CoreAccessHelper();
                }
            }
        }
    }

    private String debugPringParms() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"latitude\":\"");
        sb.append(this.mSettingManager.getLatitude());
        sb.append("\", \"longitude\":\"");
        sb.append(this.mSettingManager.getLongitude());
        sb.append("\", \"device_id\":\"");
        sb.append(this.mSettingManager.getDeviceId());
        sb.append("\", \"device_type\":\"");
        sb.append(this.mSettingManager.getDeviceType());
        sb.append("\", \"country_cd\":\"");
        sb.append(this.mSettingManager.getCountryCd());
        sb.append("\", \"city\":\"");
        sb.append(this.mSettingManager.getCity());
        sb.append("\", \"timezone\":\"");
        sb.append(this.mSettingManager.getTimeZone());
        sb.append("\", \"zip\":\"");
        sb.append(this.mSettingManager.getZip());
        sb.append("\", \"save_access_yn\":\"");
        sb.append(getSaveAccessYn());
        sb.append("\"}");
        return sb.toString();
    }

    public static ADNAClient getInstance(Context context) {
        if (instance == null) {
            instance = new ADNAClient(context);
        }
        return instance;
    }

    private void init() {
        this.mSettingManager.setDeviceId(BasicUtil.getDeviceId(this.mContext));
        this.mSettingManager.setDeviceType(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
        this.mSettingManager.setTimeZone(TimeZone.getDefault().getID());
        this.mSettingManager.setCountryCd(this.mContext.getResources().getConfiguration().locale.getCountry());
        setLocation(mLatitude, mLongitude);
    }

    /* access modifiers changed from: private */
    public boolean isOtherServer(String str) {
        boolean z = false;
        if (str == null) {
            return false;
        }
        if (str.equals(HttpHost.DEFAULT_SCHEME_NAME) || str.equals("https")) {
            z = true;
        }
        return z;
    }

    public void addEusrPtrnPage(String str, String str2) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.addEusrPtrnPage(str, str2, new AddEusrPtrnPageResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void deleteContentAccess(String str, String str2) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.deleteContentAccess(str, str2, new DeleteContentAccessResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void eusrLogin(String str, String str2) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.eusrLogin(str, str2, new EusrLoginResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public ADNAListener getADNAListener() {
        return this.f2650a;
    }

    public IConnector getConnector() {
        if (this.mAccessor == null) {
            connectCoreEngine();
        }
        return this.mAccessor.getConnector();
    }

    public void getContentAccess(String str) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.contentAccess(str, new ContentAccessResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void getContentLog(String str, String str2, String str3, String str4) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.contentLog(str, str2, str3, str4, this.mSettingManager.getLatitude(), this.mSettingManager.getLongitude(), this.mSettingManager.getDeviceId(), this.mSettingManager.getDeviceType(), this.mSettingManager.getCountryCd(), this.mSettingManager.getCity(), this.mSettingManager.getTimeZone(), this.mSettingManager.getZip(), getSaveAccessYn(), new ContentLogResponseEvent());
            return;
        }
        this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
    }

    public void getEusrPtrnPage(String str) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.eusrPtrnPage(str, new EusrPtrnPageResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void getEusrPtrnPageArea(String str, String str2) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.eusrPtrnPageArea(str, str2, new EusrPtrnPageAreaResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void getEventSchedule(String str) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.event_schedule(str, new EventScheduleResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x00b5, code lost:
        r6.setNameCard(r1.getNameCardInfo().data);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0123, code lost:
        if (r1.getNameCardInfo() == null) goto L_0x00b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x00b0, code lost:
        if (r1.getNameCardInfo() == null) goto L_0x00b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x00b2, code lost:
        r0 = "not parsing JSON";
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public DataVo getNameCard(DataVo r6) {
        /*
            r5 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "[DEBUG] cids.... {\"cid\":\""
            r0.append(r1)
            java.lang.String r1 = r6.getCid()
            r0.append(r1)
            java.lang.String r1 = "\", \"cname\":\""
            r0.append(r1)
            java.lang.String r1 = r6.getCname()
            r0.append(r1)
            java.lang.String r1 = "\", \"ctype\":\""
            r0.append(r1)
            java.lang.String r1 = r6.getCtype()
            r0.append(r1)
            java.lang.String r1 = "\", \"ctype\":\""
            r0.append(r1)
            java.lang.String r1 = r6.getCtype()
            r0.append(r1)
            java.lang.String r1 = "\", \"curl\":\""
            r0.append(r1)
            java.lang.String r1 = r6.getCurl()
            r0.append(r1)
            java.lang.String r1 = "\"}"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            java.lang.String r0 = r6.getCurl()
            android.net.Uri r0 = android.net.Uri.parse(r0)
            r0.getAuthority()
            r0.getPath()
            java.lang.String r1 = r0.getScheme()
            r0.getQueryParameterNames()
            java.lang.String r0 = r6.getCtype()
            java.lang.Integer.parseInt(r0)
            java.lang.String r0 = r6.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            boolean r0 = r5.isOtherServer(r1)
            if (r0 == 0) goto L_0x00c5
            com.anoto.adna.ServerApi.api.manager.NetworkManager r0 = r5.mNetworkManager
            android.content.Context r1 = r5.mContext
            boolean r0 = r0.isNetworkAvailable(r1)
            if (r0 == 0) goto L_0x00bf
            com.anoto.adna.ServerApi.api.manager.ResponseManager r0 = r5.mResponseManager
            java.lang.String r1 = r6.getCurl()
            java.lang.String r0 = r0.requestGetResponse(r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Other Server >>>> NameCard...Response "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r1)
            com.anoto.adna.ServerApi.api.object.NameCardObject r1 = new com.anoto.adna.ServerApi.api.object.NameCardObject
            r1.<init>()
            boolean r0 = r1.onResponseListener(r0)
            if (r0 != 0) goto L_0x00ac
        L_0x00a9:
            java.lang.String r0 = "invalid JSON format"
            goto L_0x00c1
        L_0x00ac:
            com.anoto.adna.ServerApi.api.object.NameCardObject$NameCardData r0 = r1.getNameCardInfo()
            if (r0 != 0) goto L_0x00b5
        L_0x00b2:
            java.lang.String r0 = "not parsing JSON"
            goto L_0x00c1
        L_0x00b5:
            com.anoto.adna.ServerApi.api.object.NameCardObject$NameCardData r0 = r1.getNameCardInfo()
            com.anoto.adna.ServerApi.api.object.DataNameCardVo r0 = r0.data
            r6.setNameCard(r0)
            goto L_0x0126
        L_0x00bf:
            java.lang.String r0 = "unconnected network"
        L_0x00c1:
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            goto L_0x0126
        L_0x00c5:
            com.anoto.adna.ServerApi.api.manager.NetworkManager r0 = r5.mNetworkManager
            android.content.Context r1 = r5.mContext
            boolean r0 = r0.isNetworkAvailable(r1)
            if (r0 == 0) goto L_0x00bf
            com.anoto.adna.ServerApi.api.manager.SettingManager r0 = r5.mSettingManager
            java.lang.String r0 = r0.getApiNamecard()
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = 0
            java.lang.String r3 = r6.getCurl()
            r1[r2] = r3
            java.lang.String r0 = java.lang.String.format(r0, r1)
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            org.apache.http.message.BasicNameValuePair r2 = new org.apache.http.message.BasicNameValuePair
            java.lang.String r3 = "namecard_id"
            java.lang.String r4 = r6.getCurl()
            r2.<init>(r3, r4)
            r1.add(r2)
            com.anoto.adna.ServerApi.api.manager.ResponseManager r2 = r5.mResponseManager
            java.lang.String r0 = r2.analyseGetResponse(r0, r1)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = " NameCard...Response "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r1 = r1.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r1)
            com.anoto.adna.ServerApi.api.object.NameCardObject r1 = new com.anoto.adna.ServerApi.api.object.NameCardObject
            r1.<init>()
            boolean r0 = r1.onResponseListener(r0)
            if (r0 != 0) goto L_0x011f
            goto L_0x00a9
        L_0x011f:
            com.anoto.adna.ServerApi.api.object.NameCardObject$NameCardData r0 = r1.getNameCardInfo()
            if (r0 != 0) goto L_0x00b5
            goto L_0x00b2
        L_0x0126:
            java.lang.String r0 = r6.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.ADNAClient.getNameCard(com.anoto.adna.ServerApi.api.object.DataVo):com.anoto.adna.ServerApi.api.object.DataVo");
    }

    public void getNameCard(String str) {
        if (str == null) {
            DevLog.defaultLogging("Error....namecard_id is null ");
        } else if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.namecard(str, new NameCardResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void getPageAreaScan(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append("getPageAreaScan....");
        String str4 = str3;
        sb.append(str4);
        DevLog.defaultLogging(sb.toString());
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.scanPageArea(GlobalVar.USER_ACCOUNT, str, str2, str4, this.mSettingManager.getLatitude(), this.mSettingManager.getLongitude(), this.mSettingManager.getDeviceId(), this.mSettingManager.getDeviceType(), this.mSettingManager.getCountryCd(), this.mSettingManager.getCity(), this.mSettingManager.getTimeZone(), this.mSettingManager.getZip(), new ScanPageAreaResponseEvent());
          //  this.mConnector.scanPageArea(BeaconReferenceApplication.USER_ACCOUNT, str, str2, str4, this.mSettingManager.getLatitude(), this.mSettingManager.getLongitude(), this.mSettingManager.getDeviceId(), this.mSettingManager.getDeviceType(), this.mSettingManager.getCountryCd(), this.mSettingManager.getCity(), this.mSettingManager.getTimeZone(), this.mSettingManager.getZip(), new ScanPageAreaResponseEvent());
            return;
        }
        this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
    }

    public String getSaveAccessYn() {
        return this.mContext.getSharedPreferences("pref_adna", 0).getBoolean("pref_history_save", true) ? "Y" : "N";
    }

    public void getScan(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append("getScan....");
        String str4 = str3;
        sb.append(str4);
        DevLog.defaultLogging(sb.toString());
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.scan(str, str2, str4, this.mSettingManager.getLatitude(), this.mSettingManager.getLongitude(), this.mSettingManager.getDeviceId(), this.mSettingManager.getDeviceType(), this.mSettingManager.getCountryCd(), this.mSettingManager.getCity(), this.mSettingManager.getTimeZone(), this.mSettingManager.getZip(), getSaveAccessYn(), new ScanResponseEvent());
            return;
        }
        this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
    }

    public void getScan(String str, String str2, String str3, double d, double d2) {
        this.mSettingManager.setLatitude(String.valueOf(d));
        this.mSettingManager.setLongitude(String.valueOf(d2));
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.scan(str, str2, str3, this.mSettingManager.getLatitude(), this.mSettingManager.getLongitude(), this.mSettingManager.getDeviceId(), this.mSettingManager.getDeviceType(), this.mSettingManager.getCountryCd(), this.mSettingManager.getCity(), this.mSettingManager.getTimeZone(), this.mSettingManager.getZip(), getSaveAccessYn(), new ScanResponseEvent());
            return;
        }
        this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
    }

    public void getVersion() {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.version(new VersionResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void setADNAListener(ADNAListener aDNAListener) {
        try {
            this.f2650a = aDNAListener;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocation(double d, double d2) {
        String str="";
        String str2 = "";
        try {
            List fromLocation = new Geocoder(this.mContext, Locale.getDefault()).getFromLocation(d, d2, 1);
            if (fromLocation.size() > 0) {
                System.out.println(((Address) fromLocation.get(0)).getLocality());
            }
            str = ((Address) fromLocation.get(0)).getAdminArea();
            try {
                ((Address) fromLocation.get(0)).getAddressLine(0).toString();
                str2 = ((Address) fromLocation.get(0)).getCountryCode() != null ? ((Address) fromLocation.get(0)).getCountryCode() : "";
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                this.mSettingManager.setCountryCd(str2);
                this.mSettingManager.setCity(str);
                this.mSettingManager.setZip("");
                this.mSettingManager.setLatitude(String.valueOf(d));
                this.mSettingManager.setLongitude(String.valueOf(d2));
            }
        } catch (IOException e2) {
           // e = e2;
          //  str = null;
            e2.printStackTrace();
            this.mSettingManager.setCountryCd(str2);
            this.mSettingManager.setCity(str);
            this.mSettingManager.setZip("");
            this.mSettingManager.setLatitude(String.valueOf(d));
            this.mSettingManager.setLongitude(String.valueOf(d2));
        }
        this.mSettingManager.setCountryCd(str2);
        this.mSettingManager.setCity(str);
        this.mSettingManager.setZip("");
        this.mSettingManager.setLatitude(String.valueOf(d));
        this.mSettingManager.setLongitude(String.valueOf(d2));
    }

    public void signupUserEmail(String str, String str2, String str3) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.signupUserEmail(str, str2, str3, new SignupUserEmailResponseEvent());
        } else {
            this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
        }
    }

    public void updEusrPtrnPageArea(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        if (this.mNetworkManager.isNetworkAvailable(this.mContext)) {
            this.mConnector.updEusrPtrnPageArea(str, str2, str3, str4, str5, str6, str7, new UpdEusrPtrnPageAreaResponseEvent());
            return;
        }
        this.f2650a.onFailedToReceiveADNA(500, "unconnected network");
    }
}
