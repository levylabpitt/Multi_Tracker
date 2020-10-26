package com.pqiorg.multitracker.anoto.activities.sdk.camera.control;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
/*import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.p000v4.view.ViewCompat;*/
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.ADNADecoder;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
/*
import com.anoto.adna.sdk.C0635R;
import com.anoto.adna.sdk.camera.control.CameraContract.Presenter;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder;
import com.anoto.adna.sdk.util.BasicUtil;
*/

public class CameraView extends RelativeLayout {
    private static final String TAG = "CameraView";

    /* renamed from: a */
    FrameLayout f3082a;

    /* renamed from: b */
    ImageButton f3083b;

    /* renamed from: c */
    ToggleButton f3084c;

    /* renamed from: d */
    FloatingActionButton f3085d;

    /* renamed from: e */
    ImageView f3086e;

    /* renamed from: f */
    TextView f3087f;

    /* renamed from: g */
    TextView f3088g;

    /* renamed from: h */
    TextView f3089h;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public CameraContract.Presenter mPresenter;

    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: private */
    public void hideCameraPreview() {
        this.f3082a.setVisibility(View.INVISIBLE);
        this.f3086e.setVisibility(View.INVISIBLE);
        this.f3084c.setVisibility(View.INVISIBLE);
    }

    private void init() {
        inflate(getContext(), R.layout.camera_view_content, this);
        this.f3082a = (FrameLayout) findViewById(R.id.preview);
        this.f3082a.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    CameraView.this.mPresenter.focusCamera();
                }
                return true;
            }
        });
        this.f3083b = (ImageButton) findViewById(R.id.ibtnSettings);
        this.f3083b.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CameraView.this.mPresenter.showSettings();
            }
        });
        this.f3084c = (ToggleButton) findViewById(R.id.tbtnFlashTorch);
        this.f3084c.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CameraView.this.mPresenter.toggleCameraFlashTorch();
            }
        });
        this.f3084c = (ToggleButton) findViewById(R.id.tbtnFlashTorch);
        this.f3087f = (TextView) findViewById(R.id.tvCoordinateX);
        this.f3088g = (TextView) findViewById(R.id.tvCoordinateY);
        this.f3089h = (TextView) findViewById(R.id.tvCoordinatePage);
        this.f3086e = (ImageView) findViewById(R.id.ivCrosshair);
        this.f3085d = (FloatingActionButton) findViewById(R.id.button_return);
        this.f3085d.setVisibility(View.INVISIBLE);
        this.f3085d.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CameraView.this.showCameraPreview();
                ADNADecoder.getInstance().processAnotherResponse(true);
            }
        });
        setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    /* access modifiers changed from: private */
    public void showCameraPreview() {
        this.f3082a.setVisibility(View.VISIBLE);
        this.f3086e.setVisibility(View.VISIBLE);
        this.f3084c.setVisibility(View.VISIBLE);
        this.f3087f.setVisibility(View.INVISIBLE);
        this.f3088g.setVisibility(View.INVISIBLE);
        this.f3089h.setVisibility(View.INVISIBLE);
        this.f3085d.setVisibility(View.INVISIBLE);
    }

    private void startActivity(Intent intent) {
        BasicUtil.getActivity(this).startActivity(intent);
    }

    public void addCameraSurface(final View view) {
        this.mHandler.post(new Runnable() {
            public void run() {
                CameraView.this.f3082a.addView(view);
            }
        });
    }

    public void configureCameraTorch(final boolean z, final boolean z2) {
        this.mHandler.post(new Runnable() {
            public void run() {
                CameraView.this.f3084c.setVisibility(z ? View.VISIBLE : View.INVISIBLE);
                CameraView.this.f3084c.setChecked(z2);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void removeCameraSurface(final View view) {
        this.mHandler.post(new Runnable() {
            public void run() {
                CameraView.this.f3082a.removeView(view);
            }
        });
    }

    public void setCrosshairSize(final int i) {
        if (this.f3082a != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CameraView.this.f3086e.getLayoutParams().height = i;
                    CameraView.this.f3086e.getLayoutParams().width = i;
                    CameraView.this.f3086e.requestLayout();
                    CameraView.this.f3086e.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void setPresenter(@NonNull CameraContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void showCoordinates(long j, long j2, String str) {
        Handler handler = this.mHandler;
        final long j3 = j;
        final long j4 = j2;
        final String str2 = str;
        Runnable  r1 = new Runnable() {
            public void run() {
                CameraView.this.hideCameraPreview();
                if (CameraView.this.f3087f.getVisibility() == View.INVISIBLE) {
                    CameraView.this.f3087f.setVisibility(View.VISIBLE);
                }
                TextView textView = CameraView.this.f3087f;
                StringBuilder sb = new StringBuilder();
                sb.append("X: ");
                sb.append(j3);
                textView.setText(sb.toString());
                if (CameraView.this.f3088g.getVisibility() == View.INVISIBLE) {
                    CameraView.this.f3088g.setVisibility(View.VISIBLE);
                }
                TextView textView2 = CameraView.this.f3088g;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Y: ");
                sb2.append(j4);
                textView2.setText(sb2.toString());
                if (CameraView.this.f3089h.getVisibility() == View.INVISIBLE) {
                    CameraView.this.f3089h.setVisibility(View.VISIBLE);
                }
                TextView textView3 = CameraView.this.f3089h;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("P: ");
                sb3.append(str2);
                textView3.setText(sb3.toString());
                if (CameraView.this.f3085d.getVisibility() == INVISIBLE) {
                    CameraView.this.f3085d.setVisibility(VISIBLE);
                }
            }
        };
        handler.post(r1);
    }

    public void showSettings() {
        this.mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(CameraView.this.getContext(), "preparing...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
