package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

/*import android.support.p000v4.app.NotificationCompat;*/

import androidx.core.app.NotificationCompat;

public abstract class AbstractObject {

    /* renamed from: a */
    protected String f2742a = "data";

    /* renamed from: b */
    protected String f2743b = "success";

    /* renamed from: c */
    protected String f2744c = NotificationCompat.CATEGORY_MESSAGE;

    /* renamed from: d */
    protected String f2745d = "code";

    /* renamed from: e */
    protected String f2746e = "results";

    /* renamed from: f */
    protected String f2747f = "errors";

    /* renamed from: g */
    protected String f2748g = "";

    /* renamed from: h */
    protected int f2749h;

    public int getCode() {
        return this.f2749h;
    }

    public String getErrorMessage() {
        return this.f2748g;
    }

    public abstract boolean onResponseListener(String str);
}
