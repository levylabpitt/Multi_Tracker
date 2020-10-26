package com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
/*import com.anoto.adna.sdk.ADNASDK;
import com.anoto.adna.sdk.ADNASDK.ADNAInputImageColorSpace;
import com.anoto.adna.sdk.ADNASDK.ADNAStatus;
import com.anoto.adna.sdk.ADNASDK.GrayscalingMethod;
import com.anoto.adna.sdk.WebActivity;
import com.anoto.adna.sdk.camera.control.CameraView;
import com.anoto.adna.sdk.camera.control.Size;
import com.anoto.adna.sdk.camera.decoder.interfaces.DecoderEvent;
import com.anoto.adna.sdk.util.BasicUtil;
import com.anoto.adna.sdk.util.DevLog;*/
import com.pqiorg.multitracker.anoto.activities.sdk.ADNASDK;
import com.pqiorg.multitracker.anoto.activities.sdk.WebActivity;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.control.CameraView;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.control.Size;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.interfaces.DecoderEvent;

import org.opencv.imgproc.Imgproc;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
/*import org.opencv.imgproc.Imgproc;*/

public class ADNADecoder implements Runnable {
    public static final int AFP_NUMBER_OF_PAPER_SIZES = 7;
    public static final int AFP_START_SEGMENT = 1152;
    public static final int AFP_STOP_SEGMENT = 2256;
    public static final int BOOK_BITPOS = 12;
    public static final int PHYSICAL_COORD_MAX = 402653183;
    private static final double PREVIEW_FRAME_FRACTION_TO_CROP = 0.34d;
    public static final int SEGMENT_BITPOS = 40;
    public static final int SEGMENT_GRID_SIZE = 48;
    public static final int SEGMENT_MASK = 8388607;
    public static final int SEGMENT_SIZE_BITS = 23;
    public static final int SERIES_BITPOS = 52;
    public static final int SHELF_BITPOS = 24;
    private static final String TAG = "ADNADecoder";
    private static ADNADecoder instance;
    /* access modifiers changed from: private */
    public static ToneGenerator mDecodeSuccessTone;

    /* renamed from: a */
    int[][] f3108a = {new int[]{40, 2700, 2700}, new int[]{64, 8192, 8192}, new int[]{256, 1330, 2389}, new int[]{256, 1092, 2065}, new int[]{2517, 1666, 1621}, new int[]{32, 2164, 2852}, new int[]{128, 958, 2065}, new int[]{1024, 558, 357}, new int[]{32, 708, 916}, new int[]{32, 1344, 2399}, new int[]{256, 1354, 1202}, new int[]{256, 1213, 1070}, new int[]{32, 822, 1070}, new int[]{256, 708, 916}, new int[]{32, 404, 1004}, new int[]{128, 908, 1584}, new int[]{16, 431, 555}, new int[]{128, 1895, 1566}, new int[]{512, 8192, 8192}, new int[]{32, 798, 1046}, new int[]{256, 1344, 2346}, new int[]{1024, 512, 512}, new int[]{3114, 2693, 2226}, new int[]{256, 1344, 2345}, new int[]{32, 1202, 1354}, new int[]{128, 1068, 3077}, new int[]{256, 1330, 2601}, new int[]{32, 1770, 2709}, new int[]{32, 4264, 4952}, new int[]{32, 916, 1783}, new int[]{32, 1092, 2065}, new int[]{10, 3064, 4864}, new int[]{0, 0, 0}};

    /* renamed from: b */
    int[] f3109b = {1, 22, 19, 24, 21, 18, 32, 25, 22, 32, 32, 32, 32, 14, 5, 28, 15, 10, 5, 28, 7, 2, 2, 31, 0, 11, 18, 6, 6, 27, 17, 13, 8, 20, 12, 2, 16, 29, 12, 30, 9, 18, 3, 20, 23, 18, 18, 18, 2, 32, 2, 5, 2, 28, 2, 28, 5, 2, 2, 28, 26, 5, 28, 32, 32, 32, 32, 32, 32, 32, 28, 28, 2, 2, 5, 5, 5, 32, 32, 32, 32, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 4, 4, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 26, 5, 28, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 28, 28, 28, 28, 28, 28, 28, 28, 31, 31, 31, 31, 31, 31, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5, 5, 2, 2, 28, 28, 28, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 28, 28, 28, 28, 28, 28, 28, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};

    /* renamed from: c */
    int[][] f3110c = {new int[]{29, 108, 1064}, new int[]{31, 108, 422}, new int[]{33, 108, 776}, new int[]{37, 108, 1465}, new int[]{41, 108, 2044}, new int[]{46, Imgproc.COLOR_COLORCVT_MAX, 4031}, new int[]{48, 128, 8192}};

    /* renamed from: d */
    int f3111d;

    /* renamed from: e */
    int f3112e;

    /* renamed from: f */
    int f3113f;
    private ADNASDK mADNASDK;
    private AtomicBoolean mAuthenticationComplete = new AtomicBoolean(false);
    private CameraView mCameraView;
    private Context mContext;
    private WeakReference<CoordinateCallback> mCoordinateCallback;
    private int mCropSize;
    private ADNASDK.ADNAStatus mDecodeStatus;
    private long[] mDecodedCoordinate = {0, 0, 0};
    private WeakReference<DecoderCallback> mDecoderCallback;
    private boolean mDecoderConfigured;
    private BlockingDeque<byte[]> mFrameBlockingQueue = new LinkedBlockingDeque();
    private byte[] mInputImageByteArray;
    private Size mPreviewFrameSize;
    private AtomicBoolean mProcessResponse = new AtomicBoolean(true);
    private SharedPreferences mSharedPref;
    private Thread mThread;
    private Vibrator mVibrator;

    public interface CoordinateCallback {
        void onCoordinateEvent(long j, long j2, String str);
    }

    public interface DecoderCallback {
        void onDecoderEvent(DecoderEvent decoderEvent);
    }

    public ADNADecoder(Context context, CameraView cameraView) {
        this.mContext = context;
        this.mCameraView = cameraView;
        this.mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        instance = this;
    }

    private void configureDecoder(int i, int i2) {
        this.mCropSize = (int) Math.round((i <= i2 ? (double) i : (double) i2) * PREVIEW_FRAME_FRACTION_TO_CROP);
        this.mCameraView.setCrosshairSize(this.mCropSize);
        this.mDecoderConfigured = true;
    }

    public static ADNADecoder getInstance() {
        return instance;
    }

    private static void playTone(Context context, int i) {
        DevLog.defaultLogging("playTone");
        try {
            if (mDecodeSuccessTone == null) {
                mDecodeSuccessTone = new ToneGenerator(3, 100);
            }
            mDecodeSuccessTone.stopTone();
            mDecodeSuccessTone.startTone(i, 150);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    if (ADNADecoder.mDecodeSuccessTone != null) {
                        DevLog.defaultLogging("ToneGenerator released");
                        ADNADecoder.mDecodeSuccessTone.release();
                        ADNADecoder.mDecodeSuccessTone = null;
                    }
                }
            }, 200);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Exception while playing sound:");
            sb.append(e);
            DevLog.defaultLogging(sb.toString());
        }
    }

    private void processResponse(long j, long j2, String str) {
        Activity activity = BasicUtil.getActivity(this.mCameraView);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("pref_adna", 0);
        int i = sharedPreferences.getInt("pref_decode_mode", 0);
        StringBuilder sb = new StringBuilder();
        sb.append("Setting PREF_BEEP ");
        sb.append(i);
        DevLog.defaultLogging(sb.toString());
        switch (i) {
            case 0:
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("processResponse(MODE_AUTOMATIC): ");
                sb2.append(j);
                sb2.append(", ");
                sb2.append(j2);
                sb2.append(", ");
                sb2.append(str);
                Log.e(str2, sb2.toString());
                if (this.mCoordinateCallback.get() != null) {
                    ((CoordinateCallback) this.mCoordinateCallback.get()).onCoordinateEvent(j, j2, str);
                }
                return;
            case 1:
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("processResponse(MODE_TECHNICAL): ");
                sb3.append(j);
                sb3.append(", ");
                sb3.append(j2);
                sb3.append(", ");
                sb3.append(str);
                Log.e(str3, sb3.toString());
                showTechnical(j, j2, str);
                return;
            case 2:
                String string = sharedPreferences.getString("pref_manual_url", "http://www.anoto.com");
                String str4 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("processResponse(MODE_MANUAL): ");
                sb4.append(string);
                Log.e(str4, sb4.toString());
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("url", string);
                activity.startActivity(intent);
                return;
            default:
                return;
        }
    }

    private synchronized void returnResult(long j, long j2, String str) {
        if (this.mCoordinateCallback.get() != null) {
            ((CoordinateCallback) this.mCoordinateCallback.get()).onCoordinateEvent(j, j2, str);
        }
    }

    private void showTechnical(long j, long j2, String str) {
        this.mCameraView.showCoordinates(j, j2, str);
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public int mo10143a(int i, int i2) {
        return ((i2 >> 23) * 48) + (i >> 23);
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public String mo10144a(int i, int i2, int i3) {
        if (i > 402653183 || i2 > 402653183) {
            return null;
        }
        int a = mo10143a(i, i2);
        int i4 = i & SEGMENT_MASK;
        int i5 = i2 & SEGMENT_MASK;
        if (!mo10145b(i3, a).booleanValue()) {
            return null;
        }
        int i6 = i5 / this.f3113f;
        int i7 = this.f3113f;
        int i8 = i4 / (this.f3111d * this.f3112e);
        int i9 = (i4 - ((this.f3111d * i8) * this.f3112e)) / this.f3112e;
        int i10 = this.f3112e;
        StringBuilder sb = new StringBuilder();
        sb.append(i3);
        sb.append(".");
        sb.append(a);
        sb.append(".");
        sb.append(i6);
        sb.append(".");
        sb.append(i8);
        sb.append(".");
        sb.append(i9);
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: b */
    public Boolean mo10145b(int i, int i2) {
        if (i == 0) {
            if (i2 < 480) {
                int i3 = this.f3109b[i2];
                if (this.f3108a[i3][0] <= 0) {
                    return Boolean.valueOf(false);
                }
                this.f3111d = this.f3108a[i3][0];
                this.f3112e = this.f3108a[i3][1];
                this.f3113f = this.f3108a[i3][2];
                return Boolean.valueOf(true);
            } else if (i2 >= 1152 && i2 < 2256) {
                int i4 = i2 % 48;
                int i5 = 0;
                while (true) {
                    if (i5 >= 7) {
                        break;
                    } else if (i4 < this.f3110c[i5][0]) {
                        this.f3111d = this.f3110c[i5][1];
                        this.f3113f = this.f3110c[i5][2];
                        this.f3112e = this.f3110c[i5][2];
                        break;
                    } else {
                        i5++;
                    }
                }
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    public void decodePreviewFrame(byte[] bArr) {
        try {
            if (this.mFrameBlockingQueue.size() == 0) {
                this.mFrameBlockingQueue.putLast(Arrays.copyOf(bArr, bArr.length));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean getProcessAnotherResponse() {
        return this.mProcessResponse.get();
    }

    public void processAnotherResponse(boolean z) {
        this.mProcessResponse.set(z);
    }

    public void run() {
        ADNASDK adnasdk;
        ADNASDK.GrayscalingMethod grayscalingMethod;
        while (true) {
            try {
                this.mInputImageByteArray = (byte[]) this.mFrameBlockingQueue.takeFirst();
                if (System.currentTimeMillis() % 3 == 0) {
                    adnasdk = this.mADNASDK;
                    grayscalingMethod = ADNASDK.GrayscalingMethod.CYAN_YELLOW_DOT_GRAYSCALING;
                } else {
                    adnasdk = this.mADNASDK;
                    grayscalingMethod = ADNASDK.GrayscalingMethod.DEFAULT_GRAYSCALING;
                }
                adnasdk.setGrayscalingMethod(grayscalingMethod);
                if (!this.mDecoderConfigured) {
                    configureDecoder(this.mPreviewFrameSize.width, this.mPreviewFrameSize.height);
                }
                this.mDecodeStatus = this.mADNASDK.decode(this.mInputImageByteArray, ADNASDK.ADNAInputImageColorSpace.ADNA_COLOR_YUV_NV21, this.mPreviewFrameSize.width, this.mPreviewFrameSize.height, this.mCropSize, this.mDecodedCoordinate);
                if (this.mDecodeStatus == ADNASDK.ADNAStatus.ADNA_STATUS_OK) {
                    long j = this.mDecodedCoordinate[0];
                    long j2 = this.mDecodedCoordinate[1];
                    DevLog.defaultLogging("####################################### 111");
                    String a = mo10144a((int) this.mDecodedCoordinate[0], (int) this.mDecodedCoordinate[1], (int) this.mDecodedCoordinate[2]);
                    StringBuilder sb = new StringBuilder();
                    sb.append("PageAddress >>>>> ");
                    sb.append(a);
                    DevLog.defaultLogging(sb.toString());
                    DevLog.defaultLogging("####################################### 2222");
                    if (this.mProcessResponse.get()) {
                        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("pref_adna", 0);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Setting PREF_BEEP ");
                        sb2.append(sharedPreferences.getBoolean("pref_beep", true));
                        DevLog.defaultLogging(sb2.toString());
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Setting PREF_VIBRATION ");
                        sb3.append(sharedPreferences.getBoolean("pref_vibration", true));
                        DevLog.defaultLogging(sb3.toString());
                        if (sharedPreferences.getBoolean("pref_beep", true)) {
                            playTone(this.mContext, 44);
                        }
                        if (sharedPreferences.getBoolean("pref_vibration", true)) {
                            this.mVibrator.vibrate(150);
                        }
                        processResponse(j, j2, a);
                        getInstance().processAnotherResponse(false);
                    }
                }
                this.mInputImageByteArray = null;
            } catch (Exception  unused) {
                if (this.mInputImageByteArray != null) {
                    this.mInputImageByteArray = null;
                    return;
                }
                return;
            }
        }
    }

    public synchronized void setCoordinateCallback(CoordinateCallback coordinateCallback) {
        this.mCoordinateCallback = new WeakReference<>(coordinateCallback);
    }

    public synchronized void setDecoderCallback(DecoderCallback decoderCallback) {
        this.mDecoderCallback = new WeakReference<>(decoderCallback);
    }

    public void startADNADecoder(Size size) {
        this.mPreviewFrameSize = size;
        this.mADNASDK = new ADNASDK();
        this.mADNASDK.setGrayscalingMethod(ADNASDK.GrayscalingMethod.DEFAULT_GRAYSCALING);
        Thread thread = new Thread(this);
        this.mThread = thread;
        thread.start();
    }

    public void stopADNADecoder() {
        if (this.mThread != null) {
            this.mThread.interrupt();
            try {
                this.mThread.join();
                this.mThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mADNASDK = null;
        }
    }
}
