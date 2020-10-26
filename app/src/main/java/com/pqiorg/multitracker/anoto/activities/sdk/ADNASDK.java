package com.pqiorg.multitracker.anoto.activities.sdk;

import android.widget.Toast;

import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;

//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

public final class ADNASDK {
    public static final int COORDINATE_S_INDEX = 2;
    public static final int COORDINATE_X_INDEX = 0;
    public static final int COORDINATE_Y_INDEX = 1;

    public enum ADNAInputImageColorSpace {
        ADNA_COLOR_RGB,
        ADNA_COLOR_YUV_NV21
    }

    public enum ADNAStatus {
        ADNA_STATUS_OK,
        ADNA_ERROR_DECODE_FAILED
    }

    public enum GrayscalingMethod {
        DEFAULT_GRAYSCALING,
        CYAN_YELLOW_DOT_GRAYSCALING
    }

    static {
        System.loadLibrary("adna-sdk");
      //  System.loadLibrary("libadna-sdk");
        try {
           initialize();

        }catch (Exception e){
            e.getMessage();
           // Toast.makeText(,"adna-sdk initialization error!",Toast.LENGTH_SHORT).show();
        }
    }

    protected static native void initialize();

    public native synchronized ADNAStatus decode(byte[] bArr, ADNAInputImageColorSpace aDNAInputImageColorSpace, int i, int i2, int i3, long[] jArr);

    public native synchronized String getPageAddress(long[] jArr);

    public native synchronized String getVersion();

    public native synchronized ADNAStatus setGrayscalingMethod(GrayscalingMethod grayscalingMethod);
}
