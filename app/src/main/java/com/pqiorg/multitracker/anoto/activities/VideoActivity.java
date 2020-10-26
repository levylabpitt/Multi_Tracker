package com.pqiorg.multitracker.anoto.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
//import android.support.p003v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.util.BasicUtil;*/
import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.rabbitmq.client.ConnectionFactory;
//import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadRequest.Priority;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;
import java.io.File;

public class VideoActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "VideoActivity";
    private int downloadId;
    private DataVo mDataInfo;
    private ThinDownloadManager mDownloadManager = new ThinDownloadManager();
    private GestureDetector mGestureDetector;
    private String mProdLink;
    /* access modifiers changed from: private */
    public VideoView mVideoView;
    /* access modifiers changed from: private */
    public ProgressDialog pDialog;
    /* access modifiers changed from: private */
    public TextView tvFileName;
    private OnErrorListener videoErrorListener = new OnErrorListener() {
        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            Log.d("video", "setOnErrorListener ");
            return false;
        }
    };

    private class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            StringBuilder sb = new StringBuilder();
            sb.append("Tapped at: (");
            sb.append(x);
            sb.append(",");
            sb.append(y);
            sb.append(")");
            Log.d("Double Tap", sb.toString());
            if (VideoActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                VideoActivity.this.tvFileName.setVisibility(View.GONE);
                VideoActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return true;
            }
            if (VideoActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                VideoActivity.this.tvFileName.setVisibility(View.VISIBLE);
                VideoActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            return true;
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }
    }

    private void downloadFile(String str, final String str2) {
        showDialog(0);
        Uri parse = Uri.parse(str);
        Uri parse2 = Uri.parse(str2);
        DownloadRequest downloadRequest = new DownloadRequest(parse);
      //  downloadRequest.addCustomHeader("Auth-Token", "YourTokenApiKey");
       // downloadRequest.setRetryPolicy(new DefaultRetryPolicy());
        downloadRequest.setDestinationURI(parse2).setPriority(Priority.HIGH);
        downloadRequest.setDownloadListener(new DownloadStatusListener() {
            public void onDownloadComplete(int i) {
                try {
                    Log.e(VideoActivity.this.TAG, "onDownloadComplete: 다운로드 성공");
                    VideoActivity.this.removeDialog(0);
                    Toast.makeText(VideoActivity.this.getApplicationContext(), "Success Download",  Toast.LENGTH_SHORT).show();
                    VideoActivity.this.playVideo(1, str2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onDownloadFailed(int i, int i2, String str) {
                try {
                    Log.e(VideoActivity.this.TAG, "onDownloadFailed: 다운로드 실패");
                    VideoActivity.this.removeDialog(0);
                    VideoActivity videoActivity = VideoActivity.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Fail Download: ");
                    sb.append(str);
                    BasicUtil.showAlertDialog(videoActivity, sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onProgress(int i2, long totalBytes, long downloadedBytes, int progress) {
                String c = VideoActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onProgress: 다운로드 중...");
                sb.append(progress);
                Log.e(c, sb.toString());
                VideoActivity.this.pDialog.setProgress(progress);
            }
        });
        this.downloadId = this.mDownloadManager.add(downloadRequest);
    }

    /* access modifiers changed from: private */
    public void playVideo(int i, String str) {
        switch (i) {
            case 0:
                this.mVideoView.setVideoURI(Uri.parse(str));
                break;
            case 1:
                this.mVideoView.setVideoPath(str);
                break;
        }
        this.mVideoView.setMediaController(new MediaController(this) {
            public void hide() {
                super.hide();
                VideoActivity.this.tvFileName.setVisibility(View.GONE);
            }

            public void show() {
                super.show();
                VideoActivity.this.tvFileName.setVisibility(View.VISIBLE);
            }
        });
        this.mVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoActivity.this.mVideoView.seekTo(View.VISIBLE);
                VideoActivity.this.mVideoView.start();
            }
        });
    }

    private void prepareFile() {
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
            playVideo(1, sb2);
        } else {
            downloadFile(this.mProdLink, sb2);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getExtras() != null) {
            TextUtils.isEmpty(getIntent().getStringExtra("from"));
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (configuration.orientation == 1) {
            getWindow().clearFlags(1024);
            return;
        }
        if (configuration.orientation == 2) {
            getWindow().addFlags(1024);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video);
        this.mDataInfo = (DataVo) getIntent().getSerializableExtra("data_info");
        if (this.mDataInfo == null || TextUtils.isEmpty(this.mDataInfo.getCurl())) {
            Toast.makeText(getApplicationContext(), R.string.txt_no_link_data,  Toast.LENGTH_SHORT).show();
            finish();
        } else {
            this.mProdLink = this.mDataInfo.getCurl();
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mProdLink: ");
            sb.append(this.mProdLink);
            Log.e(str, sb.toString());
            String str2 = "https://youtu.be/";
            String str3 = "https://www.youtube.com/watch?v=";
            if (this.mProdLink.contains(str2)) {
                this.mProdLink = this.mProdLink.replace(str2, str3);
            }
        }
        String str4 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mProdLink: ");
        sb2.append(this.mProdLink);
        Log.e(str4, sb2.toString());
        this.mGestureDetector = new GestureDetector(this, new GestureListener());
        this.tvFileName = (TextView) findViewById(R.id.tv_file_name);
        this.mVideoView = (VideoView) findViewById(R.id.vv_video);
        this.mVideoView.setOnErrorListener(this.videoErrorListener);
        prepareFile();
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
    public void onDestroy() {
        super.onDestroy();
        if (this.mVideoView != null) {
            this.mVideoView.pause();
            this.mVideoView.stopPlayback();
            this.mVideoView = null;
        }
        if (this.mDownloadManager != null) {
            this.mDownloadManager.release();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.mDownloadManager != null) {
            try {
                this.mDownloadManager.pause(this.downloadId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
