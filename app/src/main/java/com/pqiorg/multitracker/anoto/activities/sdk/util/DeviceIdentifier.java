package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.rabbitmq.client.AMQP;

import java.lang.reflect.InvocationTargetException;

import org.apache.http.HttpStatus;

public final class DeviceIdentifier {
    private static final String ANDROID_ID_BUG_MSG = "The device suffers from the Android ID bug - its ID is the emulator ID : 9774d56d682e549c";
    private static volatile String uuid;

    public static class DeviceIDException extends Exception {
        private static final String NO_ANDROID_ID = "Could not retrieve a device ID";
        private static final long serialVersionUID = -8083699995384519417L;

        public DeviceIDException() {
            super(NO_ANDROID_ID);
        }

        public DeviceIDException(String str) {
            super(str);
        }

        public DeviceIDException(Throwable th) {
            super(NO_ANDROID_ID, th);
        }
    }

    public static final class DeviceIDNotUniqueException extends DeviceIDException {
        private static final long serialVersionUID = -8940090896069484955L;

        public DeviceIDNotUniqueException() {
            super(DeviceIdentifier.ANDROID_ID_BUG_MSG);
        }
    }

    private enum IDs {
        TELEPHONY_ID {
            /* access modifiers changed from: 0000 */
            public String getId(Context context) {

                String deviceId="";
                  try {
                      TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                      if (telephonyManager == null) {
                          IDs.m2103w("Telephony Manager not available");
                          return null;
                      }
                      DeviceIdentifier.assertPermission(context, "android.permission.READ_PHONE_STATE");
                      deviceId = telephonyManager.getDeviceId();
                  }catch (SecurityException e){
                      e.getCause();
                  }
                    return deviceId;

            }
        },
        ANDROID_ID {
            /* access modifiers changed from: 0000 */
            public String getId(Context context) throws DeviceIDException {
                String string = Secure.getString(context.getContentResolver(), "android_id");
                if (!IDs.BUGGY_ANDROID_ID.equals(string)) {
                    return string;
                }
                IDs.m2102e(DeviceIdentifier.ANDROID_ID_BUG_MSG);
                throw new DeviceIDNotUniqueException();
            }
        },
        WIFI_MAC {
            /* access modifiers changed from: 0000 */
            public String getId(Context context) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager == null) {
                    IDs.m2103w("Wifi Manager not available");
                    return null;
                }
                DeviceIdentifier.assertPermission(context, "android.permission.ACCESS_WIFI_STATE");
                return wifiManager.getConnectionInfo().getMacAddress();
            }
        },
        BLUETOOTH_MAC {
            /* access modifiers changed from: 0000 */
            public String getId(Context context) {
                BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter == null) {
                    IDs.m2103w("Bluetooth Adapter not available");
                    return null;
                }
                DeviceIdentifier.assertPermission(context, "android.permission.BLUETOOTH");
                return defaultAdapter.getAddress();
            }
        };

        static final String BUGGY_ANDROID_ID = "9774d56d682e549c";
        private static final String TAG = "DeviceIdentifier$IDs";

        /* access modifiers changed from: private */
        /* renamed from: e */
        public static void m2102e(String str) {
            Log.e(TAG, str);
        }

        /* access modifiers changed from: private */
        /* renamed from: w */
        public static void m2103w(String str) {
            Log.w(TAG, str);
        }

        /* access modifiers changed from: 0000 */
        public abstract String getId(Context context) throws DeviceIDException;
    }

    private DeviceIdentifier() {
    }

    /* access modifiers changed from: private */
    public static void assertPermission(Context context, String str) {
        if (context.getPackageManager().checkPermission(str, context.getPackageName()) != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Permission ");
            sb.append(str);
            sb.append(" is required");
            throw new SecurityException(sb.toString());
        }
    }

    @SuppressLint({"PrivateApi"})
    private static String getCDMACountryIso() {
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            switch (Integer.parseInt(((String) cls.getMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{"ro.cdma.home.operator.numeric"})).substring(0, 3))) {
                case HttpStatus.SC_NO_CONTENT /*204*/:
                    return "NL";
                case 232:
                    return "AT";
                case 247:
                    return "LV";
                case 255:
                    return "UA";
              //  case CallbackHandler.MSG_ROUTE_SELECTED /*262*/:
              //      return "DE";
                case 283:
                    return "AM";
                case 310:
                    return "US";
                case AMQP.CONTENT_TOO_LARGE /*311*/:
                    return "US";
                case AMQP.NO_ROUTE /*312*/:
                    return "US";
                case 316:
                    return "US";
                case 330:
                    return "PR";
                case 414:
                    return "MM";
                case 434:
                    return "UZ";
                case 450:
                    return "KR";
                case 455:
                    return "MO";
                case 460:
                    return "CN";
                case 619:
                    return "SL";
                case 634:
                    return "SD";
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | NullPointerException | InvocationTargetException unused) {
        }
        return null;
    }

    public static String getDeviceCountryCode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            String simCountryIso = telephonyManager.getSimCountryIso();
            if (simCountryIso != null && simCountryIso.length() == 2) {
                return simCountryIso.toLowerCase();
            }
            String cDMACountryIso = telephonyManager.getPhoneType() == 2 ? getCDMACountryIso() : telephonyManager.getNetworkCountryIso();
            if (cDMACountryIso != null && cDMACountryIso.length() == 2) {
                return cDMACountryIso.toLowerCase();
            }
        }
        String country = (VERSION.SDK_INT >= 24 ? context.getResources().getConfiguration().getLocales().get(0) : context.getResources().getConfiguration().locale).getCountry();
        return (country == null || country.length() != 2) ? "us" : country.toLowerCase();
    }

    public static String getDeviceId(Context context) {
        return BasicUtil.getDeviceId(context);
    }

    public static String getDeviceIdentifier(Context context, boolean z) throws DeviceIDException {
        String str = uuid;
        if (str != null) {
            return str;
        }
        synchronized (DeviceIdentifier.class) {
            String str2 = uuid;
            if (str2 != null) {
                return str2;
            }
            for (IDs id : IDs.values()) {
                try {
                    String id2 = id.getId(context);
                    uuid = id2;
                    str2 = id2;
                } catch (DeviceIDNotUniqueException e) {
                    if (!z) {
                        throw new DeviceIDException((Throwable) e);
                    }
                }
                if (str2 != null) {
                    return str2;
                }
            }
            throw new DeviceIDException();
        }
    }}

