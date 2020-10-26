package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;


/*import com.anoto.adna.sdk.camera.control.CameraSettings.CameraConfigFeatures;
import com.anoto.adna.sdk.camera.control.CameraSettings.CameraControlFeatures;*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Config {
    private CameraSettings.CameraConfigFeatures mCameraConfigFeatures;
    private CameraSettings.CameraControlFeatures mCameraControlFeatures;
    private Context mContext;
    private Parameters mParameters;

    private void setFlashTorch() {
        boolean z = false;
        if (this.mParameters.getFlashMode() != null) {
            this.mCameraControlFeatures.flashTorchSupported = true;
            z = this.mContext.getSharedPreferences("pref_adna", 0).getBoolean("pref_flash_torch", false);
            if (z) {
                this.mParameters.setFlashMode("torch");
            }
        }
        this.mCameraControlFeatures.flashTorchOn = z;
    }

    private void setFocusMode() {
        this.mCameraConfigFeatures.focusModes = this.mParameters.getSupportedFocusModes();
        this.mCameraControlFeatures.manualFocusSupported = this.mCameraConfigFeatures.focusModes.contains("auto");
        if (this.mCameraConfigFeatures.focusModes.contains("continuous-picture")) {
            this.mParameters.setFocusMode("continuous-picture");
            this.mCameraControlFeatures.continuousFocusSupported = true;
            this.mCameraControlFeatures.manualFocusEnabled = false;
            return;
        }
        if (this.mCameraControlFeatures.manualFocusSupported) {
            this.mParameters.setFocusMode("auto");
            this.mCameraControlFeatures.continuousFocusSupported = false;
            this.mCameraControlFeatures.manualFocusEnabled = true;
        }
    }

    private void setFrameRate() {
        this.mCameraConfigFeatures.supportedPreviewFrameFPSRanges = this.mParameters.getSupportedPreviewFpsRange();
        int[] iArr = (int[]) this.mCameraConfigFeatures.supportedPreviewFrameFPSRanges.get(0);
        for (int i = 1; i < this.mCameraConfigFeatures.supportedPreviewFrameFPSRanges.size(); i++) {
            int[] iArr2 = (int[]) this.mCameraConfigFeatures.supportedPreviewFrameFPSRanges.get(i);
            if (iArr2[0] * iArr2[1] > iArr[0] * iArr[1]) {
                iArr = iArr2;
            }
        }
        this.mParameters.setPreviewFpsRange(iArr[0], iArr[1]);
    }

    private void setPreviewSize(Context context) {
        List<Size> supportedPreviewSizes = this.mParameters.getSupportedPreviewSizes();
        this.mCameraConfigFeatures.supportedPreviewSizes = new ArrayList();

        /////////////////////
        for (Size size : supportedPreviewSizes) {
            this.mCameraConfigFeatures.supportedPreviewSizes.add(new com.pqiorg.multitracker.anoto.activities.sdk.camera.control.Size(size.width,size.height));
        }
        com.pqiorg.multitracker.anoto.activities.sdk.camera.control.Size size2 =  this.mCameraConfigFeatures.supportedPreviewSizes.get(0);

        for (int i = 1; i < this.mCameraConfigFeatures.supportedPreviewSizes.size(); i++) {

            if (( this.mCameraConfigFeatures.supportedPreviewSizes.get(i)).width * ( this.mCameraConfigFeatures.supportedPreviewSizes.get(i)).height > size2.width * size2.height)
            {

                size2 =  this.mCameraConfigFeatures.supportedPreviewSizes.get(i);
            }
        }
        this.mCameraControlFeatures.seletedPreviewSize = size2;
       ////////////////
        /*  android.util.Size size=getLargestAvailableSize(context);
        this.mCameraControlFeatures.seletedPreviewSize =new com.pqiorg.multitracker.sdk.camera.control.Size(size.getWidth(),size.getHeight());
         */
       ///////////////////

        this.mCameraControlFeatures.selectedPreviewFormat = this.mParameters.getPreviewFormat();
        this.mParameters.setPreviewSize(this.mCameraControlFeatures.seletedPreviewSize.width, this.mCameraControlFeatures.seletedPreviewSize.height);
    }

    private void setZoom() {
        this.mCameraControlFeatures.zoomSupported = this.mParameters.isZoomSupported();
        if (this.mCameraControlFeatures.zoomSupported) {
            this.mCameraConfigFeatures.zoomRatios = this.mParameters.getZoomRatios();
            int maxZoom = this.mParameters.getMaxZoom();
            int i = 0;
            this.mCameraControlFeatures.minZoom = 0;
            this.mCameraControlFeatures.maxZoom = maxZoom;
            int i2 = this.mContext.getSharedPreferences("pref_adna", 0).getInt("pref_zoom_factor", this.mCameraControlFeatures.minZoom);
            float f = 0.0f;
            while (true) {
                if (i >= maxZoom) {
                    break;
                }
                f = (float) ((Integer) this.mCameraConfigFeatures.zoomRatios.get(i)).intValue();
                if (f >= 200.0f) {
                    f /= 100.0f;
                    i2 = i;
                    break;
                }
                i++;
            }
            this.mCameraControlFeatures.selectedZoom = i2;
            this.mCameraControlFeatures.selectedZoomFactor = f;
            this.mParameters.setZoom(this.mCameraControlFeatures.selectedZoom);
        }
    }

    public boolean ConfigureCamera(Context context, Parameters parameters, CameraSettings.CameraConfigFeatures cameraConfigFeatures, CameraSettings.CameraControlFeatures cameraControlFeatures) {
        this.mContext = context;
        this.mParameters = parameters;
        this.mCameraConfigFeatures = cameraConfigFeatures;
        this.mCameraControlFeatures = cameraControlFeatures;
        setPreviewSize(context);
        setFocusMode();
        setFrameRate();
        setFlashTorch();
        return true;
    }



    private android.util.Size getLargestAvailableSize(Context activity) {

        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                android.util.Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());

                return largest;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.

        }
        return null;
    }
    static class CompareSizesByArea implements Comparator<android.util.Size> {

        @Override
        public int compare(android.util.Size lhs, android.util.Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }
}
