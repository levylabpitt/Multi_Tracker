package com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.interfaces;

public class CoordinateEvent extends DecoderEvent {

    /* renamed from: x */
    public int f3116x;

    /* renamed from: y */
    public int f3117y;

    public CoordinateEvent(int i, int i2) {
        super(3);
        this.f3116x = i;
        this.f3117y = i2;
    }
}
