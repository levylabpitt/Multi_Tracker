package com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.interfaces;

public class ProductEvent extends DecoderEvent {
    public String jsonString;

    public ProductEvent(String str) {
        super(4);
        this.jsonString = str;
    }
}
