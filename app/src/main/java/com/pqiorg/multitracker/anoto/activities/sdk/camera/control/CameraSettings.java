package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

import android.content.Context;
import java.util.List;

public abstract class CameraSettings {

    /* renamed from: a */
    protected Context f3079a;

    /* renamed from: b */
    protected CameraConfigFeatures f3080b;

    /* renamed from: c */
    protected CameraControlFeatures f3081c;

    public static class CameraConfigFeatures {
        public List<String> focusModes;
        public List<int[]> supportedPreviewFrameFPSRanges;
        public List<Size> supportedPreviewSizes;
        public List<Integer> zoomRatios;
    }

    public static class CameraControlFeatures {
        public boolean continuousFocusSupported;
        public boolean flashTorchOn;
        public boolean flashTorchSupported;
        public boolean manualFocusEnabled;
        public boolean manualFocusSupported;
        public int maxZoom;
        public int minZoom;
        public int selectedPreviewFormat;
        public int selectedZoom;
        public float selectedZoomFactor;
        public Size seletedPreviewSize;
        public boolean zoomSupported;
    }

    public CameraSettings(Context context) {
        this.f3079a = context;
    }

    public boolean getFlashOn() {
        return this.f3081c.flashTorchOn;
    }

    public boolean getFlashSupported() {
        return this.f3081c.flashTorchSupported;
    }
}
