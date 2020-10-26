package com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.decodeclient;

import org.json.JSONObject;

public class DecodeResult {

    /* renamed from: lX */
    public int f3114lX;

    /* renamed from: lY */
    public int f3115lY;
    public boolean success;

    public DecodeResult(boolean z, int i, int i2) {
        this.success = z;
        this.f3114lX = i;
        this.f3115lY = i2;
    }

    public JSONObject toJSON() {
        try {
            JSONObject put = new JSONObject().put("X", this.f3114lX).put("Y", this.f3115lY);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("Coordinate", put);
            return jSONObject;
        } catch (Exception unused) {
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(this.f3114lX);
        sb.append(",");
        sb.append(this.f3115lY);
        sb.append(")");
        return sb.toString();
    }
}
