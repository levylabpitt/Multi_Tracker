package com.pqiorg.multitracker.anoto.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//import android.support.p003v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.R;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.MP3TimeUtil;
import com.rabbitmq.client.ConnectionFactory;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadRequest.Priority;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;*/
import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MP3TimeUtil;
import com.rabbitmq.client.ConnectionFactory;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

public class MP3Activity extends AppCompatActivity implements OnCompletionListener, OnSeekBarChangeListener {
    /* access modifiers changed from: private */
    public String TAG = "MP3Activity";
    private OnClickListener btClickListener = new OnClickListener() {
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0029, code lost:
            r3.seekTo(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x002c, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onClick(android.view.View r3) {
            /*
                r2 = this;
                com.anoto.adna.activities.MP3Activity r0 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r0 = r0.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                int r0 = r0.getCurrentPosition()     // Catch:{ Exception -> 0x00ad }
                int r3 = r3.getId()     // Catch:{ Exception -> 0x00ad }
                switch(r3) {
                    case 2131296320: goto L_0x007a;
                    case 2131296321: goto L_0x0035;
                    case 2131296322: goto L_0x0012;
                    default: goto L_0x0011;
                }     // Catch:{ Exception -> 0x00ad }
            L_0x0011:
                return
            L_0x0012:
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                int r3 = r3.seekBackwardTime     // Catch:{ Exception -> 0x00ad }
                int r3 = r0 - r3
                if (r3 < 0) goto L_0x002d
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                com.anoto.adna.activities.MP3Activity r1 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                int r1 = r1.seekBackwardTime     // Catch:{ Exception -> 0x00ad }
                int r0 = r0 - r1
            L_0x0029:
                r3.seekTo(r0)     // Catch:{ Exception -> 0x00ad }
                return
            L_0x002d:
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                r0 = 0
                goto L_0x0029
            L_0x0035:
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                boolean r3 = r3.isPlaying()     // Catch:{ Exception -> 0x00ad }
                if (r3 == 0) goto L_0x005f
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                if (r3 == 0) goto L_0x00ad
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                r3.pause()     // Catch:{ Exception -> 0x00ad }
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.widget.ImageView r3 = r3.btPlay     // Catch:{ Exception -> 0x00ad }
                r0 = 2131230827(0x7f08006b, float:1.8077718E38)
            L_0x005b:
                r3.setImageResource(r0)     // Catch:{ Exception -> 0x00ad }
                return
            L_0x005f:
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                if (r3 == 0) goto L_0x00ad
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                r3.start()     // Catch:{ Exception -> 0x00ad }
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.widget.ImageView r3 = r3.btPlay     // Catch:{ Exception -> 0x00ad }
                r0 = 2131230826(0x7f08006a, float:1.8077716E38)
                goto L_0x005b
            L_0x007a:
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                int r3 = r3.seekForwardTime     // Catch:{ Exception -> 0x00ad }
                int r3 = r3 + r0
                com.anoto.adna.activities.MP3Activity r1 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r1 = r1.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                int r1 = r1.getDuration()     // Catch:{ Exception -> 0x00ad }
                if (r3 > r1) goto L_0x009b
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                com.anoto.adna.activities.MP3Activity r1 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                int r1 = r1.seekForwardTime     // Catch:{ Exception -> 0x00ad }
                int r0 = r0 + r1
                goto L_0x0029
            L_0x009b:
                com.anoto.adna.activities.MP3Activity r3 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r3 = r3.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                com.anoto.adna.activities.MP3Activity r0 = com.anoto.adna.activities.MP3Activity.this     // Catch:{ Exception -> 0x00ad }
                android.media.MediaPlayer r0 = r0.mMediaPlayer     // Catch:{ Exception -> 0x00ad }
                int r0 = r0.getDuration()     // Catch:{ Exception -> 0x00ad }
                goto L_0x0029
            L_0x00ad:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.MP3Activity.C05591.onClick(android.view.View):void");
        }
    };
    private ImageView btNextTrack;
    /* access modifiers changed from: private */
    public ImageView btPlay;
    private ImageView btPrevTrack;
    private int downloadId;
    private DataVo mDataInfo;
    private ThinDownloadManager mDownloadManager = new ThinDownloadManager();
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    private String mProdLink;
    /* access modifiers changed from: private */
    public SeekBar mSeekBar;
    private Runnable mUpdateTimeAndProgressTask = new Runnable() {
        public void run() {
            long duration = (long) MP3Activity.this.mMediaPlayer.getDuration();
            long currentPosition = (long) MP3Activity.this.mMediaPlayer.getCurrentPosition();
            int progressPercentage = MP3Activity.this.utils.getProgressPercentage(currentPosition, duration);
            TextView f = MP3Activity.this.tvTotalDuration;
            StringBuilder sb = new StringBuilder();
            sb.append(MP3Activity.this.utils.milliSecondsToTimer(duration));
            sb.append("");
            f.setText(sb.toString());
            TextView g = MP3Activity.this.tvCurrentDuration;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(MP3Activity.this.utils.milliSecondsToTimer(currentPosition));
            sb2.append("");
            g.setText(sb2.toString());
            MP3Activity.this.mSeekBar.setProgress(progressPercentage);
            MP3Activity.this.mHandler.postDelayed(this, 100);
        }
    };
    /* access modifiers changed from: private */
    public ProgressDialog pDialog;
    /* access modifiers changed from: private */
    public int seekBackwardTime = 5000;
    /* access modifiers changed from: private */
    public int seekForwardTime = 5000;
    /* access modifiers changed from: private */
    public TextView tvCurrentDuration;
    private TextView tvFileName;
    /* access modifiers changed from: private */
    public TextView tvTotalDuration;
    /* access modifiers changed from: private */
    public MP3TimeUtil utils = new MP3TimeUtil();

    private void downloadFile(String str, final String str2) {
        showDialog(0);
        Uri parse = Uri.parse(str);
        Uri parse2 = Uri.parse(str2);
        DownloadRequest downloadRequest = new DownloadRequest(parse);
       // downloadRequest.addCustomHeader("Auth-Token", "YourTokenApiKey");
      //  downloadRequest.setRetryPolicy(new DefaultRetryPolicy());
        downloadRequest.setDestinationURI(parse2).setPriority(DownloadRequest.Priority.HIGH);
        downloadRequest.setDownloadListener(new DownloadStatusListener() {
            public void onDownloadComplete(int i) {
                Log.e(MP3Activity.this.TAG, "onDownloadComplete: 다운로드 성공");
                MP3Activity.this.dismissDialog(0);
                Toast.makeText(MP3Activity.this.getApplicationContext(), "Success Download", Toast.LENGTH_SHORT).show();
                MP3Activity.this.playMP3(str2);
            }

            public void onDownloadFailed(int i, int i2, String str) {
                try {
                    Log.e(MP3Activity.this.TAG, "onDownloadFailed: 다운로드 실패");
                    MP3Activity.this.dismissDialog(0);
                    MP3Activity mP3Activity = MP3Activity.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Fail Download: ");
                    sb.append(str);
                    BasicUtil.showAlertDialog(mP3Activity, sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

           /* @Override
            public void onProgress(int i, long j, int i2) {
                String j3 = MP3Activity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onProgress: 다운로드 중...");
                sb.append(i2);
                Log.e(j3, sb.toString());
                MP3Activity.this.pDialog.setProgress(i2);
            }*/

            @Override
            public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
                String j3 = MP3Activity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onProgress: 다운로드 중...");
                sb.append(progress);
                Log.e(j3, sb.toString());
                MP3Activity.this.pDialog.setProgress(progress);
            }
        });
        this.downloadId = this.mDownloadManager.add(downloadRequest);
    }

    private void prepareFile() {
        synchronized (this) {
            String[] split = this.mProdLink.split(ConnectionFactory.DEFAULT_VHOST);
            String str = split[split.length - 1];
            this.tvFileName.setTag(this.mProdLink);
            this.tvFileName.setText(str);
            StringBuilder sb = new StringBuilder();
            sb.append(getExternalCacheDir().toString());
            sb.append(ConnectionFactory.DEFAULT_VHOST);
            sb.append(str);
            String sb2 = sb.toString();
            if (new File(sb2).exists()) {
                playMP3(sb2);
            } else {
                downloadFile(this.mProdLink, sb2);
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        this.btPlay.setImageResource(R.drawable.bt_play);
        this.mHandler.removeCallbacks(this.mUpdateTimeAndProgressTask);
        this.mMediaPlayer.seekTo(this.utils.progressToTimer(0, this.mMediaPlayer.getDuration()));
        updatePlayInfo();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_mp3);
        this.mDataInfo = (DataVo) getIntent().getSerializableExtra("data_info");
        if (this.mDataInfo == null || TextUtils.isEmpty(this.mDataInfo.getCurl())) {
            Toast.makeText(getApplicationContext(), R.string.txt_no_link_data, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            this.mProdLink = this.mDataInfo.getCurl();
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mProdLink: ");
        sb.append(this.mProdLink);
        Log.e(str, sb.toString());
        this.tvFileName = (TextView) findViewById(R.id.tv_file_name);
        this.tvCurrentDuration = (TextView) findViewById(R.id.tv_time1);
        this.tvTotalDuration = (TextView) findViewById(R.id.tv_time2);
        this.mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        this.btPrevTrack = (ImageView) findViewById(R.id.bt_prev_track);
        this.btPlay = (ImageView) findViewById(R.id.bt_play);
        this.btNextTrack = (ImageView) findViewById(R.id.bt_next_track);
        this.btPrevTrack.setOnClickListener(this.btClickListener);
        this.btPlay.setOnClickListener(this.btClickListener);
        this.btNextTrack.setOnClickListener(this.btClickListener);
        this.mSeekBar.setOnSeekBarChangeListener(this);
    }

    /* access modifiers changed from: protected */
    public Dialog onCreateDialog(int i) {
        this.pDialog = null;
        switch (i) {
            case 0:
                this.pDialog = new ProgressDialog(this);
                this.pDialog.setMessage("Downloading, please wait...");
                this.pDialog.setIndeterminate(false);
                this.pDialog.setMax(100);
                this.pDialog.setProgressStyle(1);
                this.pDialog.setCancelable(false);
                break;
            case 1:
                this.pDialog = new ProgressDialog(this);
                this.pDialog.setProgressStyle(0);
                this.pDialog.setCancelable(true);
                break;
            default:
                return this.pDialog;
        }
        this.pDialog.show();
        return this.pDialog;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        try {
            this.mHandler.removeCallbacks(this.mUpdateTimeAndProgressTask);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        } catch (Exception unused) {
        }
        super.onPause();
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnCompletionListener(this);
        prepareFile();
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.mHandler.removeCallbacks(this.mUpdateTimeAndProgressTask);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        this.mHandler.removeCallbacks(this.mUpdateTimeAndProgressTask);
        this.mMediaPlayer.seekTo(this.utils.progressToTimer(seekBar.getProgress(), this.mMediaPlayer.getDuration()));
        updatePlayInfo();
    }

    public void playMP3(String str) {
        synchronized (this) {
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("playMP3: ");
            sb.append(str);
            Log.e(str2, sb.toString());
            try {
                if (TextUtils.isEmpty(str)) {
                    BasicUtil.showAlertDialog(this, "Can't read the file.");
                } else {
                    this.mMediaPlayer.setDataSource(str);
                    this.mMediaPlayer.prepare();
                    this.mMediaPlayer.start();
                }
                this.btPlay.setImageResource(R.drawable.bt_pause);
                this.mSeekBar.setProgress(0);
                this.mSeekBar.setMax(100);
                updatePlayInfo();
            } catch (Exception e) {
                e.printStackTrace();
                BasicUtil.showAlertDialog(this, "Can't read the file.");
            }
        }
    }

    public void updatePlayInfo() {
        this.mHandler.postDelayed(this.mUpdateTimeAndProgressTask, 100);
    }
}
