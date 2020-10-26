package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.SurfaceHolder;

import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.ADNADecoder;

import java.io.IOException;
/*
import com.anoto.adna.sdk.camera.control.CameraSettings.CameraConfigFeatures;
import com.anoto.adna.sdk.camera.control.CameraSettings.CameraControlFeatures;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder;
*/

public class CameraApi extends CameraSettings {
    private static final String TAG = "CameraApi";
    private ADNADecoder mADNADecoder;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    /* access modifiers changed from: private */
    public Camera mCamera;
    private CameraPreviewCallback mCameraPreviewCallback;
    /* access modifiers changed from: private */
    public PreviewSurfaceView mCameraSurface;
    private CameraView mCameraView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mDefaultCameraId;
    /* access modifiers changed from: private */
    public Parameters mParameters;
    /* access modifiers changed from: private */
    public Handler mResetContinuousFocusHandler = new Handler();
    /* access modifiers changed from: private */
    public Runnable mResetContinuousFocusRunnable;

    public CameraApi(Context context, ADNADecoder aDNADecoder) {
        super(context);
        this.mContext = context;
        this.mADNADecoder = aDNADecoder;
        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        int i = 0;
        while (true) {
            if (i >= numberOfCameras) {
                break;
            }
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 0) {
                this.mDefaultCameraId = i;
                break;
            }
            i++;
        }
        this.mCameraPreviewCallback = new CameraPreviewCallback();
    }

    private int getDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(this.mDefaultCameraId, cameraInfo);
        int i = 0;
        switch (((Activity) this.mContext).getWindowManager().getDefaultDisplay().getRotation()) {
            case 1:
                i = 90;
                break;
            case 2:
                i = 180;
                break;
            case 3:
                i = 270;
                break;
        }
        return (cameraInfo.facing == 1 ? 360 - ((cameraInfo.orientation + i) % 360) : (cameraInfo.orientation - i) + 360) % 360;
    }

    /* access modifiers changed from: private */
    public void runAutoFocus() {
        if (!this.f3081c.continuousFocusSupported) {
            try {
                this.mCamera.cancelAutoFocus();
            } catch (RuntimeException unused) {
            }
            if (this.mResetContinuousFocusRunnable != null) {
                this.mResetContinuousFocusHandler.removeCallbacks(this.mResetContinuousFocusRunnable);
                this.mResetContinuousFocusRunnable = null;
            }
            if (this.f3081c.manualFocusSupported && !this.f3081c.manualFocusEnabled) {
                this.mParameters.setFocusMode("auto");
                try {
                    this.mCamera.setParameters(this.mParameters);
                } catch (RuntimeException unused2) {
                    return;
                }
            }
            try {
                this.mCamera.autoFocus(new AutoFocusCallback() {
                    public void onAutoFocus(boolean z, Camera camera) {
                        CameraApi.this.mResetContinuousFocusRunnable = new Runnable() {
                            public void run() {
                                CameraApi.this.runAutoFocus();
                            }
                        };
                        CameraApi.this.mResetContinuousFocusHandler.postDelayed(CameraApi.this.mResetContinuousFocusRunnable, z ? 2000 : 500);
                    }
                });
            } catch (RuntimeException unused3) {
            }
        }
    }

    private void startBackgroundThread() {
        this.mBackgroundThread = new HandlerThread("CameraBackground");
        this.mBackgroundThread.start();
        this.mBackgroundHandler = new Handler(this.mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        this.mBackgroundThread.quit();
        try {
            this.mBackgroundThread.join();
            this.mBackgroundThread = null;
            this.mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addCameraSurface(final CameraView cameraView) {
        this.mCameraView = cameraView;
        synchronized (this.mBackgroundThread) {
            this.mBackgroundHandler.post(new Runnable() {
                public void run() {
                    CameraApi.this.mCameraSurface = new PreviewSurfaceView(CameraApi.this.mContext, CameraApi.this);
                    cameraView.addCameraSurface(CameraApi.this.mCameraSurface);
                }
            });
        }
    }

    public synchronized void focusCamera() throws CameraApiException {
        if (this.mCamera != null && this.f3081c.manualFocusSupported) {
            this.mResetContinuousFocusRunnable = new Runnable() {
                public void run() {
                    CameraApi.this.runAutoFocus();
                }
            };
            this.mResetContinuousFocusHandler.postDelayed(this.mResetContinuousFocusRunnable, 2000);
        }
    }

    public Handler getCameraHandler() {
        return this.mBackgroundHandler;
    }

    public float getMeasureSpec(Size size, int i, int i2) {
        float f;
        int i3;
        if (this.mCamera != null) {
            if (this.f3081c.seletedPreviewSize.height >= this.f3081c.seletedPreviewSize.width) {
                f = (float) this.f3081c.seletedPreviewSize.height;
                i3 = this.f3081c.seletedPreviewSize.width;
            } else {
                f = (float) this.f3081c.seletedPreviewSize.width;
                i3 = this.f3081c.seletedPreviewSize.height;
            }
            float f2 = f / ((float) i3);
            size.width = i;
            size.height = (int) (((float) i) * f2);
            return f2;
        }
        size.width = i;
        size.height = i2;
        return 0.0f;
    }

    public void removeCameraSurface(final CameraView cameraView) {
        this.mCameraView = null;
        if (this.mBackgroundThread != null) {
            synchronized (this.mBackgroundThread) {
                this.mBackgroundHandler.post(new Runnable() {
                    public void run() {
                        cameraView.removeCameraSurface(CameraApi.this.mCameraSurface);
                        CameraApi.this.mCameraSurface = null;
                    }
                });
            }
        }
    }

    public void startCamera() throws CameraApiException {
        if (this.mBackgroundThread == null) {
            startBackgroundThread();
        }
        synchronized (this.mBackgroundThread) {
            this.mBackgroundHandler.post(new Runnable() {
                public void run() {
                    try {
                        CameraApi.this.mCamera = Camera.open(CameraApi.this.mDefaultCameraId);
                        CameraApi.this.f3080b = new CameraConfigFeatures();
                        CameraApi.this.f3081c = new CameraControlFeatures();
                        new Config().ConfigureCamera(CameraApi.this.mContext, CameraApi.this.mParameters = CameraApi.this.mCamera.getParameters(), CameraApi.this.f3080b, CameraApi.this.f3081c);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }
    }

    public void stopCamera() throws CameraApiException {
        stopBackgroundThread();
        this.mCameraPreviewCallback.stopPreviewFrame();
        this.mCamera.release();
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    /*public void surfaceAvailable(android.view.SurfaceHolder r5) throws java.io.IOException {
        *//*
            r4 = this;
            android.hardware.Camera r0 = r4.mCamera
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            com.anoto.adna.sdk.camera.control.CameraPreviewCallback r0 = r4.mCameraPreviewCallback     // Catch:{ Exception -> 0x000a }
            r0.stopPreviewFrame()     // Catch:{ Exception -> 0x000a }
        L_0x000a:
            android.hardware.Camera r0 = r4.mCamera     // Catch:{ RuntimeException -> 0x0030 }
            android.hardware.Camera$Parameters r1 = r4.mParameters     // Catch:{ RuntimeException -> 0x0030 }
            r0.setParameters(r1)     // Catch:{ RuntimeException -> 0x0030 }
            android.hardware.Camera r0 = r4.mCamera     // Catch:{ RuntimeException -> 0x0030 }
            int r1 = r4.getDisplayOrientation()     // Catch:{ RuntimeException -> 0x0030 }
            r0.setDisplayOrientation(r1)     // Catch:{ RuntimeException -> 0x0030 }
            android.hardware.Camera r0 = r4.mCamera     // Catch:{ RuntimeException -> 0x0030 }
            r0.setPreviewDisplay(r5)     // Catch:{ RuntimeException -> 0x0030 }
            com.anoto.adna.sdk.camera.control.CameraPreviewCallback r5 = r4.mCameraPreviewCallback     // Catch:{ RuntimeException -> 0x0030 }
            android.hardware.Camera r0 = r4.mCamera     // Catch:{ RuntimeException -> 0x0030 }
            com.anoto.adna.sdk.camera.decoder.ADNADecoder r1 = r4.mADNADecoder     // Catch:{ RuntimeException -> 0x0030 }
            com.anoto.adna.sdk.camera.control.CameraSettings$CameraControlFeatures r2 = r4.f3081c     // Catch:{ RuntimeException -> 0x0030 }
            com.anoto.adna.sdk.camera.control.Size r2 = r2.seletedPreviewSize     // Catch:{ RuntimeException -> 0x0030 }
            com.anoto.adna.sdk.camera.control.CameraSettings$CameraControlFeatures r3 = r4.f3081c     // Catch:{ RuntimeException -> 0x0030 }
            int r3 = r3.selectedPreviewFormat     // Catch:{ RuntimeException -> 0x0030 }
            r5.startPreviewFrame(r0, r1, r2, r3)     // Catch:{ RuntimeException -> 0x0030 }
        L_0x0030:
            return
        *//*
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.sdk.camera.control.CameraApi.surfaceAvailable(android.view.SurfaceHolder):void");
    }*/
    public void surfaceAvailable(SurfaceHolder paramSurfaceHolder) throws IOException {
        if (this.mCamera == null)
            return;
        try {
            this.mCameraPreviewCallback.stopPreviewFrame();
            try {
                this.mCamera.setParameters(this.mParameters);
                this.mCamera.setDisplayOrientation(getDisplayOrientation());
                this.mCamera.setPreviewDisplay(paramSurfaceHolder);
                this.mCameraPreviewCallback.startPreviewFrame(this.mCamera, this.mADNADecoder, this.f3081c.seletedPreviewSize, this.f3081c.selectedPreviewFormat);
                return;
            } catch (RuntimeException runtimeException) {}
        } catch (Exception exception) {
            try {
                this.mCamera.setParameters(this.mParameters);
                this.mCamera.setDisplayOrientation(getDisplayOrientation());
                this.mCamera.setPreviewDisplay(paramSurfaceHolder);
                this.mCameraPreviewCallback.startPreviewFrame(this.mCamera, this.mADNADecoder, this.f3081c.seletedPreviewSize, this.f3081c.selectedPreviewFormat);
                return;
            } catch (RuntimeException runtimeException1) {}
        }
    }
    public void toggleCameraFlashTorch() throws CameraApiException {
        if (this.mCamera != null) {
            String flashMode = this.mParameters.getFlashMode();
            if (flashMode != null) {
                if (flashMode.equals("off")) {
                    this.mParameters.setFlashMode("torch");
                    this.f3081c.flashTorchOn = true;
                } else {
                    this.mParameters.setFlashMode("off");
                    this.f3081c.flashTorchOn = false;
                }
                this.mCamera.setParameters(this.mParameters);
                Editor edit = this.mContext.getSharedPreferences("pref_adna", 0).edit();
                edit.putBoolean("pref_flash_torch", this.f3081c.flashTorchOn);
                edit.commit();
            }
        }
    }
}
