package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.os.Environment;

public class Constants {
    public static final String COMPANY_ID = "company_id";
    public static final String DATA = "data";
    public static final String DATA_INFO = "data_info";
    public static final String DATA_INFO_ARR = "data_info_arr";
    public static final String DIRECTORY_PATH;
    public static final String FROM = "from";
    public static final String HOST = "host";
    public static final int LOADING_STYLE_BAR = 0;
    public static final int LOADING_STYLE_CIRCLE = 1;
    public static final int MODE_AUTOMATIC = 0;
    public static final int MODE_MANUAL = 2;
    public static final int MODE_TECHNICAL = 1;
    public static final String PREF_ABOUT_DONT_SHOW = "pref_about_dont_show";
    public static final String PREF_ADNA = "pref_adna";
    public static final String PREF_BEEP = "pref_beep";
    public static final String PREF_DECODE_MODE = "pref_decode_mode";
    public static final String PREF_FLASH_TORCH = "pref_flash_torch";
    public static final String PREF_GRAYSCALE_METHOD = "pref_grayscale_method";
    public static final String PREF_HISTORY_SAVE = "pref_history_save";
    public static final String PREF_MANUAL_URL = "pref_manual_url";
    public static final String PREF_VIBRATION = "pref_vibration";
    public static final String PREF_ZOOM_FACTOR = "pref_zoom_factor";
    public static final String RESULT = "result";
    public static final String TYPE_IMAGE = "03";
    public static final String TYPE_MP3 = "04";
    public static final String TYPE_NAME_CARD = "06";
    public static final String TYPE_SCHEDULE = "07";
    public static final int TYPE_SDCARD_CACHE = 1;
    public static final String TYPE_TEXT = "02";
    public static final int TYPE_URL_LINK = 0;
    public static final String TYPE_VIDEO = "05";
    public static final String TYPE_WEB = "01";
    public static final String URL = "url";
    public static final String VIRTUAL_HOST = "virtual_host";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Adna");
        DIRECTORY_PATH = sb.toString();
    }
}
