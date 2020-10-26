package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.io.IOException;

public class PreviewSurfaceView extends SurfaceView implements Callback {
    private static final String TAG = "PreviewSurfaceView";
    private CameraApi mCameraApi;
    private Size mMeasureSpec = new Size();

    public PreviewSurfaceView(Context context, CameraApi cameraApi) {
        super(context);
        this.mCameraApi = cameraApi;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setType(3);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        this.mCameraApi.getMeasureSpec(this.mMeasureSpec, resolveSize(getSuggestedMinimumWidth(), i), resolveSize(getSuggestedMinimumHeight(), i2));
        setMeasuredDimension(this.mMeasureSpec.width, this.mMeasureSpec.height);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (surfaceHolder.getSurface() != null) {
            try {
                this.mCameraApi.surfaceAvailable(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
