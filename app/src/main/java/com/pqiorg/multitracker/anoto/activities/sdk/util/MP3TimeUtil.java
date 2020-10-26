package com.pqiorg.multitracker.anoto.activities.sdk.util;

import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rabbitmq.client.ConnectionFactory;
/*import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rabbitmq.client.ConnectionFactory;*/

public class MP3TimeUtil {
    public int getProgressPercentage(long j, long j2) {
        Double.valueOf(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
        return Double.valueOf((((double) ((long) ((int) (j / 1000)))) / ((double) ((long) ((int) (j2 / 1000))))) * 100.0d).intValue();
    }

    public String milliSecondsToTimer(long j) {
        StringBuilder sb;
        String str;
        String str2 = "";
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / ConnectionFactory.DEFAULT_CONNECTION_TIMEOUT;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(i);
            sb2.append(":");
            str2 = sb2.toString();
        }
        if (i3 < 10) {
            sb = new StringBuilder();
            str = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        } else {
            sb = new StringBuilder();
            str = "";
        }
        sb.append(str);
        sb.append(i3);
        String sb3 = sb.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(i2);
        sb4.append(":");
        sb4.append(sb3);
        return sb4.toString();
    }

    public int progressToTimer(int i, int i2) {
        return ((int) ((((double) i) / 100.0d) * ((double) (i2 / 1000)))) * 1000;
    }
}
