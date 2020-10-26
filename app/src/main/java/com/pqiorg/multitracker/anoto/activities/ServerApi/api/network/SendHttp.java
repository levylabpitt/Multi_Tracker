package com.pqiorg.multitracker.anoto.activities.ServerApi.api.network;

import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.internal.http.HttpHeaders;

public class SendHttp {

    /* renamed from: a */
    static ThreadPolicy f2739a = null;

    /* renamed from: b */
    static HttpClient f2740b = null;

    /* renamed from: is */
    public static InputStream f2741is = null;
    public static JSONObject jObj = null;
    public static String json = "";

    public static JSONObject deleteHttp(String str, List<NameValuePair> list) throws Exception {
        try {
            if (f2739a == null) {
                f2739a = new Builder().permitAll().build();
            }
            String format = URLEncodedUtils.format(list, "UTF-8");
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("?");
            sb.append(format);
            HttpDelete httpDelete = new HttpDelete(sb.toString());
            httpDelete.setHeader("Content-Type", "application/json");
            httpDelete.setHeader(HttpHeaders.ACCEPT, "JSON");
            HttpEntity entity = defaultHttpClient.execute(httpDelete).getEntity();
            if (entity != null) {
                jObj = new JSONObject(EntityUtils.toString(entity));
                return jObj;
            }
        } catch (Exception e) {
            Log.d("Delete 통신에러", e.getMessage().toString());
        }
        return jObj;
    }

    public static JSONObject getHttp(String str, List<NameValuePair> list) throws Exception {
        try {
            if (f2739a == null) {
                f2739a = new Builder().permitAll().build();
            }
            String format = URLEncodedUtils.format(list, "UTF-8");
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("?");
            sb.append(format);
            HttpGet httpGet = new HttpGet(sb.toString());
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader(HttpHeaders.ACCEPT, "JSON");
            HttpEntity entity = defaultHttpClient.execute(httpGet).getEntity();
            if (entity != null) {
                jObj = new JSONObject(EntityUtils.toString(entity));
                return jObj;
            }
        } catch (Exception e) {
            Log.d("Get 통신에러", e.getMessage().toString());
        }
        return jObj;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00b2, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2.getMessage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e3, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00e4, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00e5, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00e6, code lost:
        throw r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00e3 A[ExcHandler: HttpHostConnectException (r2v2 'e' org.apache.http.conn.HttpHostConnectException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00e5 A[ExcHandler: ConnectTimeoutException (r2v1 'e' org.apache.http.conn.ConnectTimeoutException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static JSONObject postHttp(String r2, List<org.apache.http.NameValuePair> r3) throws Exception {
        /*
            com.anoto.adna.ServerApi.api.manager.SettingManager r0 = com.anoto.adna.ServerApi.api.manager.SettingManager.getInstance()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            android.os.StrictMode$ThreadPolicy r1 = f2739a     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            if (r1 != 0) goto L_0x0017
            android.os.StrictMode$ThreadPolicy$Builder r1 = new android.os.StrictMode$ThreadPolicy$Builder     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r1.<init>()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            android.os.StrictMode$ThreadPolicy$Builder r1 = r1.permitAll()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            android.os.StrictMode$ThreadPolicy r1 = r1.build()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            f2739a = r1     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
        L_0x0017:
            android.os.StrictMode$ThreadPolicy r1 = f2739a     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            android.os.StrictMode.setThreadPolicy(r1)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.net.URI r1 = new java.net.URI     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r1.<init>(r2)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.client.HttpClient r2 = f2740b     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            if (r2 != 0) goto L_0x002c
            org.apache.http.impl.client.DefaultHttpClient r2 = new org.apache.http.impl.client.DefaultHttpClient     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r2.<init>()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            f2740b = r2     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
        L_0x002c:
            org.apache.http.client.methods.HttpPost r2 = new org.apache.http.client.methods.HttpPost     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r2.<init>(r1)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r1 = "apikey"
            java.lang.String r0 = r0.getApiKey()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r2.addHeader(r1, r0)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r0 = "Connection"
            java.lang.String r1 = "Keep-Alive"
            r2.setHeader(r0, r1)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r0 = "Accept-Charset"
            java.lang.String r1 = "UTF-8"
            r2.setHeader(r0, r1)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.client.entity.UrlEncodedFormEntity r0 = new org.apache.http.client.entity.UrlEncodedFormEntity     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r1 = "UTF-8"
            r0.<init>(r3, r1)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r2.setEntity(r0)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.client.HttpClient r3 = f2740b     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.params.HttpParams r3 = r3.getParams()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r0 = "http.protocol.version"
            org.apache.http.HttpVersion r1 = org.apache.http.HttpVersion.HTTP_1_1     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r3.setParameter(r0, r1)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r0 = 5000(0x1388, float:7.006E-42)
            org.apache.http.params.HttpConnectionParams.setConnectionTimeout(r3, r0)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.params.HttpConnectionParams.setSoTimeout(r3, r0)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.client.HttpClient r3 = f2740b     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            org.apache.http.HttpResponse r2 = r3.execute(r2)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            if (r2 != 0) goto L_0x0074
            java.lang.String r3 = "postHttp httpError,,,,,"
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r3)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
        L_0x0074:
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            org.apache.http.HttpEntity r2 = r2.getEntity()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.io.InputStream r2 = r2.getContent()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.lang.String r1 = "utf-8"
            r0.<init>(r2, r1)     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            r3.<init>(r0)     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            r2.<init>()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
        L_0x008d:
            java.lang.String r0 = r3.readLine()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            if (r0 == 0) goto L_0x00a8
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            r1.<init>()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            r1.append(r0)     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.lang.String r0 = "\n"
            r1.append(r0)     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            r2.append(r0)     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            goto L_0x008d
        L_0x00a8:
            r3 = 0
            json = r3     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            json = r2     // Catch:{ Exception -> 0x00b2, ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3 }
            goto L_0x00b6
        L_0x00b2:
            r2 = move-exception
            r2.getMessage()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
        L_0x00b6:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00c0 }
            java.lang.String r3 = json     // Catch:{ JSONException -> 0x00c0 }
            r2.<init>(r3)     // Catch:{ JSONException -> 0x00c0 }
            jObj = r2     // Catch:{ JSONException -> 0x00c0 }
            goto L_0x00d9
        L_0x00c0:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r3.<init>()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r0 = "Error parsing data "
            r3.append(r0)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r2 = r2.toString()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            r3.append(r2)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            java.lang.String r2 = r3.toString()     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r2)     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
        L_0x00d9:
            org.json.JSONObject r2 = jObj     // Catch:{ ConnectTimeoutException -> 0x00e5, HttpHostConnectException -> 0x00e3, Exception -> 0x00dc }
            return r2
        L_0x00dc:
            r2 = move-exception
            r2.getMessage()
            org.json.JSONObject r2 = jObj
            return r2
        L_0x00e3:
            r2 = move-exception
            throw r2
        L_0x00e5:
            r2 = move-exception
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.network.SendHttp.postHttp(java.lang.String, java.util.List):org.json.JSONObject");
    }

    public static JSONObject postHttpCommon(String str) {
        try {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
            URI uri = new URI(str);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Connection", HTTP.CONN_KEEP_ALIVE);
            httpPost.setHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8");
            HttpParams params = defaultHttpClient.getParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            if (execute == null) {
                Log.e("httpError", "통신에러");
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(execute.getEntity().getContent(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(readLine);
                    sb2.append("\n");
                    sb.append(sb2.toString());
                }
                json = null;
                json = sb.toString();
            } catch (Exception e) {
                e.getMessage();
            }
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e2) {
                String str2 = "JSON Parser";
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Error parsing data ");
                sb3.append(e2.toString());
                Log.e(str2, sb3.toString());
            }
            return jObj;
        } catch (Exception unused) {
            return jObj;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00a7, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2.getMessage();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00da, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00db, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00dc, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00dd, code lost:
        throw r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00da A[ExcHandler: HttpHostConnectException (r2v2 'e' org.apache.http.conn.HttpHostConnectException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00dc A[ExcHandler: ConnectTimeoutException (r2v1 'e' org.apache.http.conn.ConnectTimeoutException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static JSONObject putHttp(String r2, List<org.apache.http.NameValuePair> r3) throws Exception {
        /*
            android.os.StrictMode$ThreadPolicy r0 = f2739a     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            if (r0 != 0) goto L_0x0013
            android.os.StrictMode$ThreadPolicy$Builder r0 = new android.os.StrictMode$ThreadPolicy$Builder     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r0.<init>()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            android.os.StrictMode$ThreadPolicy$Builder r0 = r0.permitAll()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            android.os.StrictMode$ThreadPolicy r0 = r0.build()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            f2739a = r0     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
        L_0x0013:
            android.os.StrictMode$ThreadPolicy r0 = f2739a     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            android.os.StrictMode.setThreadPolicy(r0)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.net.URI r0 = new java.net.URI     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r0.<init>(r2)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.client.HttpClient r2 = f2740b     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            if (r2 != 0) goto L_0x0028
            org.apache.http.impl.client.DefaultHttpClient r2 = new org.apache.http.impl.client.DefaultHttpClient     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r2.<init>()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            f2740b = r2     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
        L_0x0028:
            org.apache.http.client.methods.HttpPut r2 = new org.apache.http.client.methods.HttpPut     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r2.<init>(r0)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r0 = "Connection"
            java.lang.String r1 = "Keep-Alive"
            r2.setHeader(r0, r1)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r0 = "Accept-Charset"
            java.lang.String r1 = "UTF-8"
            r2.setHeader(r0, r1)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.client.entity.UrlEncodedFormEntity r0 = new org.apache.http.client.entity.UrlEncodedFormEntity     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r1 = "UTF-8"
            r0.<init>(r3, r1)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r2.setEntity(r0)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.client.HttpClient r3 = f2740b     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.params.HttpParams r3 = r3.getParams()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r0 = "http.protocol.version"
            org.apache.http.HttpVersion r1 = org.apache.http.HttpVersion.HTTP_1_1     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r3.setParameter(r0, r1)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r0 = 10000(0x2710, float:1.4013E-41)
            org.apache.http.params.HttpConnectionParams.setConnectionTimeout(r3, r0)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.params.HttpConnectionParams.setSoTimeout(r3, r0)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.client.HttpClient r3 = f2740b     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            org.apache.http.HttpResponse r2 = r3.execute(r2)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            if (r2 != 0) goto L_0x0069
            java.lang.String r3 = "httpError"
            java.lang.String r0 = "통신에러"
            android.util.Log.e(r3, r0)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
        L_0x0069:
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            org.apache.http.HttpEntity r2 = r2.getEntity()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.io.InputStream r2 = r2.getContent()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.lang.String r1 = "utf-8"
            r0.<init>(r2, r1)     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            r3.<init>(r0)     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            r2.<init>()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
        L_0x0082:
            java.lang.String r0 = r3.readLine()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            if (r0 == 0) goto L_0x009d
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            r1.<init>()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            r1.append(r0)     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.lang.String r0 = "\n"
            r1.append(r0)     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            r2.append(r0)     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            goto L_0x0082
        L_0x009d:
            r3 = 0
            json = r3     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            json = r2     // Catch:{ Exception -> 0x00a7, ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da }
            goto L_0x00ab
        L_0x00a7:
            r2 = move-exception
            r2.getMessage()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
        L_0x00ab:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00b5 }
            java.lang.String r3 = json     // Catch:{ JSONException -> 0x00b5 }
            r2.<init>(r3)     // Catch:{ JSONException -> 0x00b5 }
            jObj = r2     // Catch:{ JSONException -> 0x00b5 }
            goto L_0x00d0
        L_0x00b5:
            r2 = move-exception
            java.lang.String r3 = "JSON Parser"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r0.<init>()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r1 = "Error parsing data "
            r0.append(r1)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r2 = r2.toString()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            r0.append(r2)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            java.lang.String r2 = r0.toString()     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            android.util.Log.e(r3, r2)     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
        L_0x00d0:
            org.json.JSONObject r2 = jObj     // Catch:{ ConnectTimeoutException -> 0x00dc, HttpHostConnectException -> 0x00da, Exception -> 0x00d3 }
            return r2
        L_0x00d3:
            r2 = move-exception
            r2.getMessage()
            org.json.JSONObject r2 = jObj
            return r2
        L_0x00da:
            r2 = move-exception
            throw r2
        L_0x00dc:
            r2 = move-exception
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.network.SendHttp.putHttp(java.lang.String, java.util.List):org.json.JSONObject");
    }
}
