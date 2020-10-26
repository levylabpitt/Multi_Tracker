package com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.interfaces;

public class DecoderEvent {
    public static final int COORDINATE = 3;
    public static final int DECODE_DONE = 50;
    public static final int DECODE_RESET = 51;
    public static final int INVALID_APIKEY = 0;
    public static final int INVALID_IMAGE_FORMAT = 2;
    public static final int LOOKUP_FAILURE = 1;
    public static final int PRODUCT = 4;
    private String mMessage;
    private int mType;

    /* renamed from: x */
    public int f3118x;

    /* renamed from: y */
    public int f3119y;

    public DecoderEvent(int i) {
        this.mType = i;
    }

    public DecoderEvent(int i, String str) {
        this.mType = i;
        this.mMessage = str;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public int getType() {
        return this.mType;
    }
}
