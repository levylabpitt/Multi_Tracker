package com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
//import com.anoto.adna.sdk.util.DevLog;
import com.rabbitmq.client.ConnectionFactory;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class NetworkManager {
    private static final int CANCEL_TIMEOUT = 1;
    private static final String DEV_SERVER_URL = "";
    private static final int HARD_TIMEOUT = 10000;
    private static final String SERVER_URL = "";
    private static final int SOCKET_TIMEOUT = 10000;
    private static final String TAG = "NetworkManager";
    private static final int TIMEOUT = 0;
    private static NetworkManager instance;
    private boolean isDev;
    /* access modifiers changed from: private */
    public HttpClient mClient;
    private CookieStore mCookieStore;
    private Handler mHandler;
    private HttpContext mLocalContext;
    private String mServerUrl;
    private SettingManager mSettingManager;
    /* access modifiers changed from: private */
    public boolean mTimeoutFlag;

    class LogoutAsync extends AsyncTask<Void, Void, Boolean> {
        LogoutAsync() {
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public Boolean doInBackground(Void... voidArr) {
            boolean z;
            if (NetworkManager.this.mClient.getConnectionManager() != null) {
                NetworkManager.this.mClient.getConnectionManager().shutdown();
                z = true;
            } else {
                z = false;
            }
            return Boolean.valueOf(z);
        }
    }

    public NetworkManager() {
        this.isDev = true;
        this.mClient = null;
        this.mCookieStore = new BasicCookieStore();
        this.mLocalContext = new BasicHttpContext();
        this.mTimeoutFlag = false;
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 0:
                        NetworkManager.this.mTimeoutFlag = true;
                        break;
                    case 1:
                        if (hasMessages(0)) {
                            removeMessages(0);
                            return;
                        }
                        break;
                    default:
                        return;
                }
            }
        };
        this.mSettingManager = null;
        this.mSettingManager = SettingManager.getInstance();
    }

    private HttpEntity get(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mServerUrl);
        sb.append(ConnectionFactory.DEFAULT_VHOST);
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Request(GET) >>");
        sb3.append(sb2);
        DevLog.defaultLogging(sb3.toString());
        HttpGet httpGet = new HttpGet(sb2);
        httpGet.addHeader("apikey", this.mSettingManager.getApiKey());
        try {
            HttpResponse execute = this.mClient.execute(httpGet);
            execute.getStatusLine().getStatusCode();
            return execute.getEntity();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
            return null;
        } catch (IOException e3) {
            e3.printStackTrace();
            return null;
        } catch (IllegalStateException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public boolean connect(String str) {
        this.mServerUrl = str;
        try {
            TrustManager r0 = new TrustManager() {
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext.getInstance(SSLSocketFactory.TLS).init(null, new TrustManager[]{r0}, null);
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, (SocketFactory) new PlainSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", (SocketFactory) socketFactory, 443));
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            basicHttpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            basicHttpParams.setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));
            HttpProtocolParams.setUserAgent(basicHttpParams, "http.agent");
            this.mClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
            this.mClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, Boolean.valueOf(true));
            setTimeout(10000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        new LogoutAsync().execute(new Void[0]);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0063, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0064, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0067, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0068, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0069, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006c, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006e, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0071, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0072, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0073, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0076, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0077, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0078, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007b, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0063 A[ExcHandler: Exception (r0v7 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:1:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068 A[ExcHandler: IllegalStateException (r0v6 'e' java.lang.IllegalStateException A[CUSTOM_DECLARE]), Splitter:B:1:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0072 A[ExcHandler: ClientProtocolException (r0v4 'e' org.apache.http.client.ClientProtocolException A[CUSTOM_DECLARE]), Splitter:B:1:0x0035] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0077 A[ExcHandler: UnsupportedEncodingException (r0v3 'e' java.io.UnsupportedEncodingException A[CUSTOM_DECLARE]), Splitter:B:1:0x0035] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
   /* public String getAndGetRequest(String r5) {
        *//*
            r4 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Request(GET) >>"
            r0.append(r1)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            org.apache.http.client.methods.HttpGet r0 = new org.apache.http.client.methods.HttpGet
            r0.<init>(r5)
            java.lang.String r5 = "apikey"
            com.anoto.adna.ServerApi.api.manager.SettingManager r1 = r4.mSettingManager
            java.lang.String r1 = r1.getApiKey()
            r0.addHeader(r5, r1)
            r5 = 0
            r4.mTimeoutFlag = r5
            android.os.Handler r1 = r4.mHandler
            r2 = 1
            r1.sendEmptyMessage(r2)
            android.os.Handler r1 = r4.mHandler
            r2 = 10000(0x2710, double:4.9407E-320)
            r1.sendEmptyMessageDelayed(r5, r2)
            r5 = 0
            org.apache.http.client.HttpClient r1 = r4.mClient     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            org.apache.http.HttpResponse r1 = r1.execute(r0)     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            org.apache.http.HttpEntity r1 = r1.getEntity()     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            boolean r2 = r4.mTimeoutFlag     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            if (r2 == 0) goto L_0x0049
            if (r0 == 0) goto L_0x0048
            r0.abort()     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
        L_0x0048:
            return r5
        L_0x0049:
            if (r1 == 0) goto L_0x0062
            java.lang.String r0 = org.apache.http.util.EntityUtils.toString(r1)     // Catch:{ ParseException -> 0x005e, IOException -> 0x0059, UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            r1.consumeContent()     // Catch:{ ParseException -> 0x005e, IOException -> 0x0059, UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            boolean r1 = r4.mTimeoutFlag     // Catch:{ ParseException -> 0x005e, IOException -> 0x0059, UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            if (r1 == 0) goto L_0x0057
            return r5
        L_0x0057:
            r5 = r0
            return r5
        L_0x0059:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
            return r5
        L_0x005e:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x0077, ClientProtocolException -> 0x0072, IOException -> 0x006d, IllegalStateException -> 0x0068, Exception -> 0x0063 }
        L_0x0062:
            return r5
        L_0x0063:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x0068:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x006d:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x0072:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x0077:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        *//*
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.manager.NetworkManager.getAndGetRequest(java.lang.String):java.lang.String");
    }

    *//* JADX WARNING: Code restructure failed: missing block: B:14:0x006f, code lost:
        r0 = move-exception;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.printStackTrace();
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:17:0x0073, code lost:
        return null;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:18:0x0074, code lost:
        r0 = move-exception;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:19:0x0075, code lost:
        r0.printStackTrace();
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:21:0x0079, code lost:
        r0 = move-exception;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:22:0x007a, code lost:
        r0.printStackTrace();
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:23:0x007d, code lost:
        return null;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:24:0x007e, code lost:
        r0 = move-exception;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:25:0x007f, code lost:
        r0.printStackTrace();
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:26:0x0082, code lost:
        return null;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:30:0x0088, code lost:
        r0 = move-exception;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:31:0x0089, code lost:
        r0.printStackTrace();
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:32:0x008c, code lost:
        return null;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:33:0x008d, code lost:
        r0 = move-exception;
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:34:0x008e, code lost:
        r0.printStackTrace();
     *//*
    *//* JADX WARNING: Code restructure failed: missing block: B:35:0x0091, code lost:
        return null;
     *//*
    *//* JADX WARNING: Failed to process nested try/catch *//*
    *//* JADX WARNING: Removed duplicated region for block: B:21:0x0079 A[ExcHandler: Exception (r0v8 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:1:0x004b] *//*
    *//* JADX WARNING: Removed duplicated region for block: B:24:0x007e A[ExcHandler: IllegalStateException (r0v7 'e' java.lang.IllegalStateException A[CUSTOM_DECLARE]), Splitter:B:1:0x004b] *//*
    *//* JADX WARNING: Removed duplicated region for block: B:30:0x0088 A[ExcHandler: ClientProtocolException (r0v5 'e' org.apache.http.client.ClientProtocolException A[CUSTOM_DECLARE]), Splitter:B:1:0x004b] *//*
    *//* JADX WARNING: Removed duplicated region for block: B:33:0x008d A[ExcHandler: UnsupportedEncodingException (r0v4 'e' java.io.UnsupportedEncodingException A[CUSTOM_DECLARE]), Splitter:B:1:0x004b] *//*
    *//* Code decompiled incorrectly, please refer to instructions dump. *//*
    public String getAndGetString(String r5) {
        *//*
            r4 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r4.mServerUrl
            r0.append(r1)
            java.lang.String r1 = "/"
            r0.append(r1)
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Request(GET) >>"
            r0.append(r1)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            org.apache.http.client.methods.HttpGet r0 = new org.apache.http.client.methods.HttpGet
            r0.<init>(r5)
            java.lang.String r5 = "apikey"
            com.anoto.adna.ServerApi.api.manager.SettingManager r1 = r4.mSettingManager
            java.lang.String r1 = r1.getApiKey()
            r0.addHeader(r5, r1)
            r5 = 0
            r4.mTimeoutFlag = r5
            android.os.Handler r1 = r4.mHandler
            r2 = 1
            r1.sendEmptyMessage(r2)
            android.os.Handler r1 = r4.mHandler
            r2 = 10000(0x2710, double:4.9407E-320)
            r1.sendEmptyMessageDelayed(r5, r2)
            r5 = 0
            org.apache.http.client.HttpClient r1 = r4.mClient     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            org.apache.http.HttpResponse r1 = r1.execute(r0)     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            org.apache.http.HttpEntity r1 = r1.getEntity()     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            boolean r2 = r4.mTimeoutFlag     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            if (r2 == 0) goto L_0x005f
            if (r0 == 0) goto L_0x005e
            r0.abort()     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
        L_0x005e:
            return r5
        L_0x005f:
            if (r1 == 0) goto L_0x0078
            java.lang.String r0 = org.apache.http.util.EntityUtils.toString(r1)     // Catch:{ ParseException -> 0x0074, IOException -> 0x006f, UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            r1.consumeContent()     // Catch:{ ParseException -> 0x0074, IOException -> 0x006f, UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            boolean r1 = r4.mTimeoutFlag     // Catch:{ ParseException -> 0x0074, IOException -> 0x006f, UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            if (r1 == 0) goto L_0x006d
            return r5
        L_0x006d:
            r5 = r0
            return r5
        L_0x006f:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
            return r5
        L_0x0074:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x008d, ClientProtocolException -> 0x0088, IOException -> 0x0083, IllegalStateException -> 0x007e, Exception -> 0x0079 }
        L_0x0078:
            return r5
        L_0x0079:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x007e:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x0083:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x0088:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        L_0x008d:
            r0 = move-exception
            r0.printStackTrace()
            return r5
        *//*
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.manager.NetworkManager.getAndGetString(java.lang.String):java.lang.String");
    }*/

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ae, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00af, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b2, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b3, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b4, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b7, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b8, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00b9, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00bc, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00bd, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00be, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00c1, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c2, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c3, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c6, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00ae A[ExcHandler: Exception (r5v8 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:7:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b3 A[ExcHandler: IllegalStateException (r5v7 'e' java.lang.IllegalStateException A[CUSTOM_DECLARE]), Splitter:B:7:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00bd A[ExcHandler: ClientProtocolException (r5v5 'e' org.apache.http.client.ClientProtocolException A[CUSTOM_DECLARE]), Splitter:B:7:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c2 A[ExcHandler: UnsupportedEncodingException (r5v4 'e' java.io.UnsupportedEncodingException A[CUSTOM_DECLARE]), Splitter:B:7:0x0080] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
/*    public String getAndGetString(String r4, List<org.apache.http.NameValuePair> r5) {
        *//*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r3.mServerUrl
            r0.append(r1)
            java.lang.String r1 = "/"
            r0.append(r1)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            java.lang.String r0 = "NetworkManager"
            com.anoto.adna.sdk.util.DevLog.Logging(r0, r4)
            if (r5 == 0) goto L_0x004b
            java.lang.String r0 = "utf-8"
            java.lang.String r5 = org.apache.http.client.utils.URLEncodedUtils.format(r5, r0)
            java.lang.String r0 = "?"
            boolean r0 = r4.endsWith(r0)
            if (r0 != 0) goto L_0x003c
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r4)
            java.lang.String r4 = "?"
            r0.append(r4)
            java.lang.String r4 = r0.toString()
        L_0x003c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r4)
            r0.append(r5)
            java.lang.String r4 = r0.toString()
        L_0x004b:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r0 = "Request(GET) >># "
            r5.append(r0)
            r5.append(r4)
            java.lang.String r5 = r5.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r5)
            org.apache.http.client.methods.HttpGet r5 = new org.apache.http.client.methods.HttpGet
            r5.<init>(r4)
            java.lang.String r4 = "apikey"
            com.anoto.adna.ServerApi.api.manager.SettingManager r0 = r3.mSettingManager
            java.lang.String r0 = r0.getApiKey()
            r5.addHeader(r4, r0)
            r4 = 0
            r3.mTimeoutFlag = r4
            android.os.Handler r0 = r3.mHandler
            r1 = 1
            r0.sendEmptyMessage(r1)
            android.os.Handler r0 = r3.mHandler
            r1 = 10000(0x2710, double:4.9407E-320)
            r0.sendEmptyMessageDelayed(r4, r1)
            r4 = 0
            org.apache.http.client.HttpClient r0 = r3.mClient     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            org.apache.http.HttpResponse r0 = r0.execute(r5)     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            org.apache.http.HttpEntity r0 = r0.getEntity()     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            boolean r1 = r3.mTimeoutFlag     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            if (r1 == 0) goto L_0x0094
            if (r5 == 0) goto L_0x0093
            r5.abort()     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
        L_0x0093:
            return r4
        L_0x0094:
            if (r0 == 0) goto L_0x00ad
            java.lang.String r5 = org.apache.http.util.EntityUtils.toString(r0)     // Catch:{ ParseException -> 0x00a9, IOException -> 0x00a4, UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            r0.consumeContent()     // Catch:{ ParseException -> 0x00a9, IOException -> 0x00a4, UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            boolean r0 = r3.mTimeoutFlag     // Catch:{ ParseException -> 0x00a9, IOException -> 0x00a4, UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            if (r0 == 0) goto L_0x00a2
            return r4
        L_0x00a2:
            r4 = r5
            return r4
        L_0x00a4:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
            return r4
        L_0x00a9:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x00c2, ClientProtocolException -> 0x00bd, IOException -> 0x00b8, IllegalStateException -> 0x00b3, Exception -> 0x00ae }
        L_0x00ad:
            return r4
        L_0x00ae:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00b3:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00b8:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00bd:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00c2:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        *//*
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.manager.NetworkManager.getAndGetString(java.lang.String, java.util.List):java.lang.String");
    }*/


    public String getAndGetRequest(String paramString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request(GET) >>");
        stringBuilder.append(paramString);
        DevLog.defaultLogging(stringBuilder.toString());
        HttpGet httpGet = new HttpGet(paramString);
        httpGet.addHeader("apikey", this.mSettingManager.getApiKey());
        this.mTimeoutFlag = false;
        this.mHandler.sendEmptyMessage(1);
        this.mHandler.sendEmptyMessageDelayed(0, 10000L);
        try {
            HttpEntity httpEntity = this.mClient.execute((HttpUriRequest)httpGet).getEntity();
            if (this.mTimeoutFlag) {
                if (httpGet != null)
                    httpGet.abort();
                return null;
            }
            if (httpEntity != null)
                try {
                    String str = EntityUtils.toString(httpEntity);
                    httpEntity.consumeContent();
                    boolean bool = this.mTimeoutFlag;
                    return bool ? null : str;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                    return null;
                }
            return null;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        } catch (ClientProtocolException clientProtocolException) {
            clientProtocolException.printStackTrace();
            return null;
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        } catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }


    public String getAndGetString(String paramString) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mServerUrl);
        stringBuilder.append("/");
        stringBuilder.append(paramString);
        paramString = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append("Request(GET) >>");
        stringBuilder.append(paramString);
        DevLog.defaultLogging(stringBuilder.toString());
        HttpGet httpGet = new HttpGet(paramString);
        httpGet.addHeader("apikey", this.mSettingManager.getApiKey());
        this.mTimeoutFlag = false;
        this.mHandler.sendEmptyMessage(1);
        this.mHandler.sendEmptyMessageDelayed(0, 10000L);
        try {
            HttpEntity httpEntity = this.mClient.execute((HttpUriRequest)httpGet).getEntity();
            if (this.mTimeoutFlag) {
                if (httpGet != null)
                    httpGet.abort();
                return null;
            }
            if (httpEntity != null)
                try {
                    String str = EntityUtils.toString(httpEntity);
                    httpEntity.consumeContent();
                    boolean bool = this.mTimeoutFlag;
                    return bool ? null : str;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                    return null;
                }
            return null;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        } catch (ClientProtocolException clientProtocolException) {
            clientProtocolException.printStackTrace();
            return null;
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        } catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getAndGetString(String paramString, List<NameValuePair> paramList) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.mServerUrl);
        stringBuilder2.append("/");
        stringBuilder2.append(paramString);
        paramString = stringBuilder2.toString();
        DevLog.Logging("NetworkManager", paramString);
        String str = paramString;
        if (paramList != null) {
            str = URLEncodedUtils.format(paramList, "utf-8");
            String str1 = paramString;
            if (!paramString.endsWith("?")) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(paramString);
                stringBuilder3.append("?");
                str1 = stringBuilder3.toString();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str1);
            stringBuilder.append(str);
            str = stringBuilder.toString();
        }
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Request(GET) >># ");
        stringBuilder1.append(str);
        DevLog.defaultLogging(stringBuilder1.toString());
        HttpGet httpGet = new HttpGet(str);
        httpGet.addHeader("apikey", this.mSettingManager.getApiKey());
        this.mTimeoutFlag = false;
        this.mHandler.sendEmptyMessage(1);
        this.mHandler.sendEmptyMessageDelayed(0, 10000L);
        try {
            HttpEntity httpEntity = this.mClient.execute((HttpUriRequest)httpGet).getEntity();
            if (this.mTimeoutFlag) {
                if (httpGet != null)
                    httpGet.abort();
                return null;
            }
            if (httpEntity != null)
                try {
                    String str1 = EntityUtils.toString(httpEntity);
                    httpEntity.consumeContent();
                    boolean bool = this.mTimeoutFlag;
                    return bool ? null : str1;
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                    return null;
                }
            return null;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        } catch (ClientProtocolException clientProtocolException) {
            clientProtocolException.printStackTrace();
            return null;
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        } catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String getCookie(String str) {
        List cookies = this.mCookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = (Cookie) cookies.get(i);
            if (cookie.getName().equals(str)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String getServerMode() {
        if (this.mSettingManager.getServerURL() != null) {
            return this.mSettingManager.getServerURL();
        }
        boolean z = this.isDev;
        return "";
    }

    public String getServerUrl() {
        return this.mServerUrl;
    }

    public int getWifiSignalStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int i = 0;
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            for (ScanResult scanResult : wifiManager.getScanResults()) {
                if (scanResult.BSSID.equals(wifiManager.getConnectionInfo().getBSSID())) {
                    int calculateSignalLevel = (WifiManager.calculateSignalLevel(wifiManager.getConnectionInfo().getRssi(), scanResult.level) * 100) / scanResult.level;
                    if (calculateSignalLevel >= 100) {
                        i = 4;
                    } else if (calculateSignalLevel >= 75) {
                        i = 3;
                    } else if (calculateSignalLevel >= 50) {
                        i = 2;
                    } else if (calculateSignalLevel >= 25) {
                        i = 1;
                    }
                }
            }
        }
        return i;
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED || connectivityManager.getNetworkInfo(0).getState() == State.CONNECTED;
        } catch (NullPointerException unused) {
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0098, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x009c, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009e, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a2, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a3, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a6, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a7, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a8, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ab, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b1, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00b2, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b5, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b6, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00b7, code lost:
        r5.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ba, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a2 A[ExcHandler: Exception (r5v9 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:7:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00a7 A[ExcHandler: IllegalStateException (r5v8 'e' java.lang.IllegalStateException A[CUSTOM_DECLARE]), Splitter:B:7:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b1 A[ExcHandler: ClientProtocolException (r5v6 'e' org.apache.http.client.ClientProtocolException A[CUSTOM_DECLARE]), Splitter:B:7:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00b6 A[ExcHandler: UnsupportedEncodingException (r5v5 'e' java.io.UnsupportedEncodingException A[CUSTOM_DECLARE]), Splitter:B:7:0x0074] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String performDELETE(String r4, List<org.apache.http.NameValuePair> r5) {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r3.mServerUrl
            r0.append(r1)
            java.lang.String r1 = "/"
            r0.append(r1)
            r0.append(r4)
            java.lang.String r4 = r0.toString()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Request(DELETE) >>"
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            com.anoto.adna.sdk.util.DevLog.defaultLogging(r0)
            if (r5 == 0) goto L_0x0053
            boolean r0 = r5.isEmpty()
            if (r0 == 0) goto L_0x0033
            goto L_0x0053
        L_0x0033:
            org.apache.http.client.methods.HttpDelete r0 = new org.apache.http.client.methods.HttpDelete
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            r4 = 63
            r1.append(r4)
            java.lang.String r4 = "UTF-8"
            java.lang.String r4 = org.apache.http.client.utils.URLEncodedUtils.format(r5, r4)
            r1.append(r4)
            java.lang.String r4 = r1.toString()
            r0.<init>(r4)
            goto L_0x0058
        L_0x0053:
            org.apache.http.client.methods.HttpDelete r0 = new org.apache.http.client.methods.HttpDelete
            r0.<init>(r4)
        L_0x0058:
            java.lang.String r4 = "apikey"
            com.anoto.adna.ServerApi.api.manager.SettingManager r5 = r3.mSettingManager
            java.lang.String r5 = r5.getApiKey()
            r0.addHeader(r4, r5)
            r4 = 0
            r3.mTimeoutFlag = r4
            android.os.Handler r5 = r3.mHandler
            r1 = 1
            r5.sendEmptyMessage(r1)
            android.os.Handler r5 = r3.mHandler
            r1 = 10000(0x2710, double:4.9407E-320)
            r5.sendEmptyMessageDelayed(r4, r1)
            r4 = 0
            org.apache.http.client.HttpClient r5 = r3.mClient     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            org.apache.http.HttpResponse r5 = r5.execute(r0)     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            org.apache.http.HttpEntity r5 = r5.getEntity()     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            boolean r1 = r3.mTimeoutFlag     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            if (r1 == 0) goto L_0x0088
            if (r0 == 0) goto L_0x0087
            r0.abort()     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
        L_0x0087:
            return r4
        L_0x0088:
            if (r5 == 0) goto L_0x00a1
            java.lang.String r0 = org.apache.http.util.EntityUtils.toString(r5)     // Catch:{ ParseException -> 0x009d, IOException -> 0x0098, UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            r5.consumeContent()     // Catch:{ ParseException -> 0x009d, IOException -> 0x0098, UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            boolean r5 = r3.mTimeoutFlag     // Catch:{ ParseException -> 0x009d, IOException -> 0x0098, UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            if (r5 == 0) goto L_0x0096
            return r4
        L_0x0096:
            r4 = r0
            return r4
        L_0x0098:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
            return r4
        L_0x009d:
            r5 = move-exception
            r5.printStackTrace()     // Catch:{ UnsupportedEncodingException -> 0x00b6, ClientProtocolException -> 0x00b1, IOException -> 0x00ac, IllegalStateException -> 0x00a7, Exception -> 0x00a2 }
        L_0x00a1:
            return r4
        L_0x00a2:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00a7:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00ac:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00b1:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        L_0x00b6:
            r5 = move-exception
            r5.printStackTrace()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.ServerApi.api.manager.NetworkManager.performDELETE(java.lang.String, java.util.List):java.lang.String");
    }

    public String postAndGetString(String str, List<NameValuePair> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mServerUrl);
        sb.append(ConnectionFactory.DEFAULT_VHOST);
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Request(POST) >>");
        sb3.append(sb2);
        DevLog.defaultLogging(sb3.toString());
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        try {
            HttpParams params = defaultHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPost httpPost = new HttpPost(sb2);
            httpPost.addHeader("apikey", this.mSettingManager.getApiKey());
            if (list != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            }
            return EntityUtils.toString(defaultHttpClient.execute(httpPost).getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String putString(String str, List<NameValuePair> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mServerUrl);
        sb.append(ConnectionFactory.DEFAULT_VHOST);
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Request(POST) >>");
        sb3.append(sb2);
        DevLog.defaultLogging(sb3.toString());
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        try {
            HttpParams params = defaultHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            HttpPut httpPut = new HttpPut(sb2);
            httpPut.addHeader("apikey", this.mSettingManager.getApiKey());
            if (list != null) {
                httpPut.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            }
            return EntityUtils.toString(defaultHttpClient.execute(httpPut).getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setServerMode(boolean z) {
        this.isDev = z;
    }

    public void setTimeout(int i) {
        if (this.mClient != null) {
            HttpParams params = this.mClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, i);
            HttpConnectionParams.setSoTimeout(params, i);
        }
    }
}
