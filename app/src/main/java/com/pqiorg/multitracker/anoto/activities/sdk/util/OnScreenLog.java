package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

public class OnScreenLog {
    private static Activity activity = null;
    /* access modifiers changed from: private */
    public static int cntClicks = 0;
    private static int logCount = 0;
    private static int logCountMax = 30;
    private static String[] logs = new String[logCountMax];
    /* access modifiers changed from: private */
    public static int timeoutTime = 1000;
    private static TextView tvLog = null;
    /* access modifiers changed from: private */
    public static boolean visibility = false;

    /* renamed from: a */
    Handler f3121a = new Handler();

    /* renamed from: b */
    Runnable f3122b = new Runnable() {
        public void run() {
            OnScreenLog.cntClicks = 0;
        }
    };
    /* access modifiers changed from: private */
    public int maxClicks = 5;

    public OnScreenLog() {
    }

    /* JADX WARNING: type inference failed for: r1v6, types: [android.widget.LinearLayout] */
    /* JADX WARNING: type inference failed for: r5v1, types: [android.widget.RelativeLayout] */
    /* JADX WARNING: type inference failed for: r5v2, types: [android.view.View] */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: type inference failed for: r5v8, types: [android.widget.RelativeLayout] */
    /* JADX WARNING: type inference failed for: r1v7 */
    /* JADX WARNING: type inference failed for: r1v9, types: [android.widget.LinearLayout] */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public OnScreenLog(Activity r5, int r6) {
        /*
            r4 = this;
            r4.<init>()
            r0 = 5
            r4.maxClicks = r0
            android.os.Handler r0 = new android.os.Handler
            r0.<init>()
            r4.f3121a = r0
            com.anoto.adna.sdk.util.OnScreenLog$2 r0 = new com.anoto.adna.sdk.util.OnScreenLog$2
            r0.<init>()
            r4.f3122b = r0
            activity = r5
            android.widget.TextView r0 = new android.widget.TextView
            android.content.Context r1 = r5.getApplicationContext()
            r0.<init>(r1)
            tvLog = r0
            android.widget.TextView r0 = tvLog
            android.text.method.ScrollingMovementMethod r1 = new android.text.method.ScrollingMovementMethod
            r1.<init>()
            r0.setMovementMethod(r1)
            java.lang.String r0 = "Log is working"
            r4.maintainLog(r0)
            android.widget.TextView r0 = tvLog
            android.widget.RelativeLayout$LayoutParams r1 = new android.widget.RelativeLayout$LayoutParams
            r2 = -1
            r3 = -2
            r1.<init>(r2, r3)
            r0.setLayoutParams(r1)
            android.widget.TextView r0 = tvLog
            r1 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r0.setTextColor(r1)
            android.widget.TextView r0 = tvLog
            r1 = -3355444(0xffffffffffcccccc, float:NaN)
            r0.setBackgroundColor(r1)
            android.widget.TextView r0 = tvLog
            r1 = 1053609165(0x3ecccccd, float:0.4)
            r0.setAlpha(r1)
            r0 = 0
            android.view.View r1 = r5.findViewById(r6)     // Catch:{ ClassCastException -> 0x005b }
            android.widget.LinearLayout r1 = (android.widget.LinearLayout) r1     // Catch:{ ClassCastException -> 0x005b }
            goto L_0x005c
        L_0x005b:
            r1 = r0
        L_0x005c:
            android.view.View r5 = r5.findViewById(r6)     // Catch:{ ClassCastException -> 0x0063 }
            android.widget.RelativeLayout r5 = (android.widget.RelativeLayout) r5     // Catch:{ ClassCastException -> 0x0063 }
            goto L_0x0064
        L_0x0063:
            r5 = r0
        L_0x0064:
            if (r1 == 0) goto L_0x006d
            android.widget.TextView r5 = tvLog
            r1.addView(r5)
            r5 = r1
            goto L_0x0076
        L_0x006d:
            if (r5 == 0) goto L_0x0075
            android.widget.TextView r6 = tvLog
            r5.addView(r6)
            goto L_0x0076
        L_0x0075:
            r5 = r0
        L_0x0076:
            if (r5 == 0) goto L_0x0080
            com.anoto.adna.sdk.util.OnScreenLog$1 r6 = new com.anoto.adna.sdk.util.OnScreenLog$1
            r6.<init>()
            r5.setOnTouchListener(r6)
        L_0x0080:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.sdk.util.OnScreenLog.<init>(android.app.Activity, int):void");
    }

    /* renamed from: a */
    static /* synthetic */ int m2104a() {
        int i = cntClicks;
        cntClicks = i + 1;
        return i;
    }

    public static int getLogCountMax() {
        return logCountMax;
    }

    private void maintainLog(String str) {
        StringBuilder sb;
        String str2;
        String str3 = "";
        if (logCount < logCountMax) {
            logCount++;
        }
        for (int i = logCount - 1; i > 0; i--) {
            logs[i] = logs[i - 1];
        }
        logs[0] = str;
        for (int i2 = 0; i2 < logCount; i2++) {
            if (i2 < logCount - 1) {
                sb = new StringBuilder();
                sb.append(str3);
                sb.append(logs[i2]);
                str2 = System.getProperty("line.separator");
            } else {
                sb = new StringBuilder();
                sb.append(str3);
                str2 = logs[i2];
            }
            sb.append(str2);
            str3 = sb.toString();
        }
        tvLog.setText(str3);
    }

    public static void setLogCountMax(int i) {
        logCountMax = i;
        logs = new String[i];
    }

    public void clearLog() {
        for (int i = 0; i < logCount; i++) {
            logs[i] = "";
        }
        logCount = 0;
        tvLog.setText("");
    }

    public int getMaxClicks() {
        return this.maxClicks;
    }

    public void log(int i) {
        maintainLog(String.valueOf(i));
    }

    public void log(String str) {
        maintainLog(str);
    }

    public void log(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte append : bArr) {
            sb.append(append);
            sb.append("-");
        }
        maintainLog(sb.toString());
    }

    public void log(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        for (int append : iArr) {
            sb.append(append);
            sb.append("-");
        }
        maintainLog(sb.toString());
    }

    public void setLogVisible(boolean z) {
        TextView textView;
        int i;
        if (z) {
            textView = tvLog;
            i = 0;
        } else {
            textView = tvLog;
            i = 4;
        }
        textView.setVisibility(i);
        visibility = z;
    }

    public void setMaxClicks(int i) {
        this.maxClicks = i;
    }
}
