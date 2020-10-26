package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;

import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.ADNADecoder;
//import com.anoto.adna.sdk.camera.decoder.ADNADecoder;

public class CameraPreviewCallback implements PreviewCallback, Runnable {
    private final String TAG = "CameraPreviewCallback";
    private ADNADecoder mADNADecoder;
    private Camera mCamera;
    private byte[] mPreviewFrameBuffer;
    private Size mPreviewFrameSize;
    private Thread mThread;

    public void onPreviewFrame(byte[] bArr, Camera camera) {
        synchronized (this) {
            notify();
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                try {
                    synchronized (this) {
                        wait();
                        this.mADNADecoder.decodePreviewFrame(this.mPreviewFrameBuffer);
                    }
                    this.mCamera.addCallbackBuffer(this.mPreviewFrameBuffer);
                } catch (InterruptedException unused) {
                    return;
                }
            }
            throw new InterruptedException();
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void startPreviewFrame(Camera camera, ADNADecoder aDNADecoder, Size size, int i) {
        stopPreviewFrame();
        this.mCamera = camera;
        this.mPreviewFrameSize = size;
        this.mCamera.setPreviewCallbackWithBuffer(this);
        this.mPreviewFrameBuffer = new byte[(((size.width * size.height) * ImageFormat.getBitsPerPixel(i)) / 8)];
        this.mCamera.addCallbackBuffer(this.mPreviewFrameBuffer);
        this.mADNADecoder = aDNADecoder;
        this.mADNADecoder.startADNADecoder(size);
        Thread thread = new Thread(this);
        this.mThread = thread;
        thread.start();
        this.mCamera.startPreview();
    }

    public void stopPreviewFrame() {
        if (this.mThread != null) {
            this.mCamera.stopPreview();
            this.mCamera.setPreviewCallbackWithBuffer(null);
            this.mPreviewFrameBuffer = null;
            this.mThread.interrupt();
            try {
                this.mThread.join();
                this.mThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.mADNADecoder != null) {
            this.mADNADecoder.stopADNADecoder();
            this.mADNADecoder = null;
        }
    }
}
