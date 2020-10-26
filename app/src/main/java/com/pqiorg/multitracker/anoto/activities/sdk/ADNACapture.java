package com.pqiorg.multitracker.anoto.activities.sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
/*import com.anoto.adna.sdk.camera.control.CameraApi;
import com.anoto.adna.sdk.camera.control.CameraApiException;
import com.anoto.adna.sdk.camera.control.CameraContract.Presenter;
import com.anoto.adna.sdk.camera.control.CameraView;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder.CoordinateCallback;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder.DecoderCallback;
import com.anoto.adna.sdk.camera.decoder.interfaces.CoordinateEvent;
import com.anoto.adna.sdk.camera.decoder.interfaces.DecoderEvent;
import com.anoto.adna.sdk.util.DevLog;*/
import androidx.fragment.app.Fragment;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.control.CameraApi;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.control.CameraContract;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.control.CameraView;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.control.CameraApiException;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.ADNADecoder;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.interfaces.CoordinateEvent;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.interfaces.DecoderEvent;

import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

public class ADNACapture extends Fragment implements CameraContract.Presenter, ADNADecoder.DecoderCallback {
    public static final String TAG = "ADNACapture";
    private ADNADecoder.CoordinateCallback listener;
    private ADNADecoder mADNADecoder;
    /* access modifiers changed from: private */
    public CameraApi mCameraApi;
    /* access modifiers changed from: private */
    public CameraView mCameraView;
    private AtomicBoolean mScanComplete = new AtomicBoolean(false);
    private String mServerAddr;

    public void changedCaptureView(int i) {
        DevLog.defaultLogging("changedCaptureView..............");
    }

    public void focusCamera() {
        try {
            if (this.mCameraApi != null) {
                this.mCameraApi.focusCamera();
            }
        } catch (CameraApiException e) {
            e.printStackTrace();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = (ADNADecoder.CoordinateCallback) getActivity();
            DevLog.defaultLogging("onAttach(Context context) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(getActivity().toString());
            sb.append(" must implement ADNACaptureListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_scan, viewGroup, false);
        if (this.mADNADecoder != null) {
            ADNADecoder.getInstance().processAnotherResponse(true);
        }
        this.mCameraView = (CameraView) inflate.findViewById(R.id.camera_view);
        this.mADNADecoder = new ADNADecoder(getContext(), this.mCameraView);
        Window window = getActivity().getWindow();
        LayoutParams attributes = window.getAttributes();
        attributes.screenBrightness = 1.0f;
        window.setAttributes(attributes);
        window.addFlags(128);
        return inflate;
    }

    public void onDecoderEvent(DecoderEvent decoderEvent) {
        JSONObject put=new JSONObject();
        JSONObject jSONObject=new JSONObject();
        String str;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onDecoderEvent: ");
        sb.append(decoderEvent.getType());
        Log.e(str2, sb.toString());
        String str3 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onDecoderEvent: ");
        sb2.append(decoderEvent.getMessage());
        Log.e(str3, sb2.toString());
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("onDecoderEvent: ");
        sb3.append(decoderEvent.toString());
        Log.e(str4, sb3.toString());
        int type = decoderEvent.getType();
        switch (type) {
            case 0:
                try {
                    put = new JSONObject().put("status", 0).put("status", "INVALID_APIKEY");
                    jSONObject = new JSONObject();
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case 1:
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                ADNACapture.this.scanRestart();
                            }
                        }, 4000);
                    }
                });
                this.mScanComplete.getAndSet(false);
                try {
                    put = new JSONObject().put("status", 1).put("status", decoderEvent.getMessage());
                    jSONObject = new JSONObject();
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case 2:
            case 4:
                return;
            case 3:
                CoordinateEvent coordinateEvent = (CoordinateEvent) decoderEvent;
                try {
                    put = new JSONObject().put("X", coordinateEvent.f3116x).put("Y", coordinateEvent.f3117y);
                    jSONObject = new JSONObject();
                    str = "Coordinate";
                    break;
                } catch (Exception unused) {
                    break;
                }
            default:
                switch (type) {
                    case 50:
                        break;
                    case 51:
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        ADNACapture.this.scanRestart();
                                    }
                                }, 4000);
                            }
                        });
                        break;
                    default:
                        return;
                }
                this.mScanComplete.getAndSet(false);
                return;
        }
     try{
        str = "Error";
        jSONObject.put(str, put);
    }catch (Exception e){
        e.getMessage();
    }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onPause() {
        super.onPause();
        stopCamera();
    }

    public void onResume() {
        super.onResume();
        if (this.mADNADecoder != null) {
            ADNADecoder.getInstance().processAnotherResponse(true);
        } else {
            this.mADNADecoder = new ADNADecoder(getContext(), this.mCameraView);
        }
        startCamera();
        StringBuilder sb = new StringBuilder();
        sb.append("ADNACapture onResume ().processAnotherResponse ..>>>> ");
        sb.append(ADNADecoder.getInstance().getProcessAnotherResponse());
        DevLog.defaultLogging(sb.toString());
        this.listener = (ADNADecoder.CoordinateCallback) getActivity();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void scanRestart() {
        DevLog.defaultLogging("scanRestart ..1111");
        DevLog.defaultLogging("SecureDecoder.getInstance().processAnotherResponse ..true");
        ADNADecoder.getInstance().processAnotherResponse(true);
    }

    public void showCoordinates(String str) {
    }

    public void showSettings() {
        this.mCameraView.showSettings();
    }

    public synchronized void startCamera() {
        try {
            if (this.mCameraApi != null) {
                this.mCameraApi.stopCamera();
            }
            this.mCameraApi = new CameraApi(getContext(), this.mADNADecoder);
            this.mCameraApi.startCamera();
            this.mCameraView.setPresenter(this);
            this.mCameraApi.addCameraSurface(this.mCameraView);
            this.mCameraApi.getCameraHandler().post(new Runnable() {
                public void run() {
                    ADNACapture.this.mCameraView.configureCameraTorch(ADNACapture.this.mCameraApi.getFlashSupported(), ADNACapture.this.mCameraApi.getFlashOn());
                }
            });
            this.mCameraApi.getCameraHandler().post(new Runnable() {
                public void run() {
                    try {
                        ADNACapture.this.mCameraApi.focusCamera();
                    } catch (CameraApiException e) {
                        e.printStackTrace();
                    }
                }
            });
            this.mADNADecoder.setDecoderCallback(this);
            this.mADNADecoder.setCoordinateCallback(this.listener);
            if (this.mScanComplete.get()) {
                this.mScanComplete.set(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public synchronized void stopCamera() {
        try {
            if (this.mCameraApi != null) {
                this.mCameraApi.stopCamera();
                this.mCameraApi.removeCameraSurface(this.mCameraView);
                this.mCameraView.setPresenter(null);
                this.mCameraApi = null;
            }
            if (!this.mScanComplete.get()) {
                this.mScanComplete.set(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void toggleCameraFlashTorch() {
        try {
            if (this.mCameraApi != null) {
                this.mCameraApi.toggleCameraFlashTorch();
            }
        } catch (CameraApiException e) {
            e.printStackTrace();
        }
    }
}
