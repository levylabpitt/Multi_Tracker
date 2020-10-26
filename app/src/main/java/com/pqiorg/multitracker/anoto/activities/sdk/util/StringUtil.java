package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.text.TextUtils;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {
    public static String formatDate(String str, String str2) {
        if (str.length() != 8) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 4));
        sb.append(str2);
        sb.append(str.substring(4, 6));
        sb.append(str2);
        sb.append(str.substring(6));
        return sb.toString();
    }

    public static String formatNumberComma(String str) {
        return new DecimalFormat("#,###").format((long) Integer.parseInt(str));
    }

    public static String getDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getDatetoDay(int i) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, i);
        return new SimpleDateFormat("yyyyMMdd").format(instance.getTime());
    }

    public static String getDatetoMonth(int i) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, i);
        return new SimpleDateFormat("yyyyMMdd").format(instance.getTime());
    }

    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static String getMonth() {
        return new SimpleDateFormat("MM").format(new Date());
    }

    public static String getMonth(String str) {
        return str.substring(4, 6);
    }

    public static String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static String getYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String getYear(String str) {
        return str.substring(0, 4);
    }

    public static boolean isNotNull(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean isNull(String str) {
        return str == null || "".equals(str);
    }

    public static String nvl(Object obj) {
        return nvl(obj, "");
    }

    public static String nvl(Object obj, String str) {
        return obj == null ? str : obj.toString();
    }

    public static String nvl(String str) {
        return nvl(str, "");
    }

    public static String nvl(String str, String str2) {
        return TextUtils.isEmpty(str) ? str2 : str;
    }

    public static boolean parseBoolean(String str) {
        return !isNull(str) && ServerProtocol.DIALOG_RETURN_SCOPES_TRUE.equalsIgnoreCase(str);
    }

    public static int parseInt(String str) {
        try {
            return Integer.parseInt(replaceNullToStr(str, AppEventsConstants.EVENT_PARAM_VALUE_NO));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static int parseInt(String str, int i) {
        try {
            return Integer.parseInt(replaceNullToStr(str, nvl((Object) Integer.valueOf(i))));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static long parseLong(String str) {
        try {
            return Long.parseLong(replaceNullToStr(str, AppEventsConstants.EVENT_PARAM_VALUE_NO));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static long parseLong(String str, long j) {
        try {
            return Long.parseLong(replaceNullToStr(str, nvl((Object) Long.valueOf(j))));
        } catch (Exception unused) {
            return 0;
        }
    }

    public static String replaceNullToStr(String str) {
        return (!isNull(str) && !"null".equalsIgnoreCase(str)) ? str : "";
    }

    public static String replaceNullToStr(String str, String str2) {
        return (!isNull(str) && !"null".equalsIgnoreCase(str)) ? str : str2;
    }

    public static String replaceString(String str, String[] strArr, String[] strArr2) {
        StringBuffer stringBuffer = new StringBuffer(str);
        if (!(str == null || "".equals(str) || strArr == null || strArr2 == null || strArr.length != strArr2.length)) {
            for (int i = 0; i < strArr.length; i++) {
                int i2 = 0;
                while (i2 != -1) {
                    i2 = stringBuffer.indexOf(strArr[i], i2);
                    if (i2 != -1) {
                        stringBuffer.delete(i2, strArr[i].length() + i2);
                        stringBuffer.insert(i2, strArr2[i]);
                        i2 = i2 + strArr2[i].length() + 1;
                    }
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String substrFromFinal(String str, String str2) {
        if (!isNotNull(str)) {
            return "";
        }
        if (isNotNull(str2)) {
            str = str.substring(str.indexOf(str2) + 1);
        }
        return str;
    }

    public static String substrFromStart(String str, String str2) {
        if (!isNotNull(str)) {
            return "";
        }
        if (isNotNull(str2)) {
            str = str.substring(0, str.lastIndexOf(str2));
        }
        return str;
    }

    public static String substrToFinal(String str, String str2) {
        if (!isNotNull(str)) {
            return "";
        }
        if (isNotNull(str2)) {
            str = str.substring(str.lastIndexOf(str2) + 1);
        }
        return str;
    }

    public static String substrToStart(String str, String str2) {
        if (!isNotNull(str)) {
            return "";
        }
        if (isNotNull(str2)) {
            str = str.substring(0, str.indexOf(str2));
        }
        return str;
    }

    public static String substring(String str, int i) {
        return (str == null || str.length() < i) ? str : str.substring(i);
    }

    public static String substring(String str, int i, int i2) {
        return (str == null || str.length() < i2) ? str : str.substring(i, i2);
    }
}
