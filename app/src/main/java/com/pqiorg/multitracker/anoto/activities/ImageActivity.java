package com.pqiorg.multitracker.anoto.activities;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
//import android.support.p003v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.util.Constants;*/
import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.sdk.util.Constants;
import com.rabbitmq.client.ConnectionFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;
//import p012uk.p013co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public String TAG = "ImageActivity";
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.bt_info) {
                Bitmap bitmap = ((BitmapDrawable) ImageActivity.this.ivImage.getDrawable()).getBitmap();
                bitmap.getWidth();
                String i = ImageActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("info: 해상도: ");
                sb.append(bitmap.getWidth());
                sb.append(" * ");
                sb.append(bitmap.getHeight());
                Log.e(i, sb.toString());
                Context applicationContext = ImageActivity.this.getApplicationContext();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(bitmap.getWidth());
                sb2.append(" * ");
                sb2.append(bitmap.getHeight());
                Toast.makeText(applicationContext, sb2.toString(), Toast.LENGTH_SHORT).show();
                try {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(Constants.DIRECTORY_PATH);
                    sb3.append("/20180420_185800.jpg");
                    new ExifInterface(sb3.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (id !=R.id.bt_save) {
                if (id ==R.id.bt_send) {
                    Toast.makeText(ImageActivity.this.getApplicationContext(), "preparing...", Toast.LENGTH_SHORT).show();
                } else if (id ==R.id.iv_img) {
                    if (ImageActivity.this.vTop.getVisibility() == View.VISIBLE) {
                        ImageActivity.this.vTop.setVisibility(View.GONE);
                        ImageActivity.this.vBottom.setVisibility(View.GONE);
                    } else if (ImageActivity.this.vTop.getVisibility() == View.GONE) {
                        ImageActivity.this.vTop.setVisibility(View.VISIBLE);
                        ImageActivity.this.vBottom.setVisibility(View.VISIBLE);
                    }
                }
            } else if (TextUtils.isEmpty(ImageActivity.this.mProdLink)) {
                Toast.makeText(ImageActivity.this.getApplicationContext(),R.string.txt_img_save_cannot, Toast.LENGTH_SHORT).show();
            } else {
                Picasso.get().load(ImageActivity.this.mProdLink).into(ImageActivity.this.getTarget());
            }
        }
    };
    private View btInfo;
    private View btSave;
    private View btSend;
    /* access modifiers changed from: private */
    public ImageView ivImage;
    /* access modifiers changed from: private */
    public String mProdLink;
    /* access modifiers changed from: private */
    public Handler mSaveCompleteHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            String str;
            if (message.what == 0) {
                str = ImageActivity.this.getString(R.string.txt_img_save_success);
                String str2 = (String) message.obj;
                ContentValues contentValues = new ContentValues();
                contentValues.put("_data", str2);
                contentValues.put("mime_type", "image/jpeg");
                ImageActivity.this.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                str = ImageActivity.this.getString(R.string.txt_img_save_fail);
            }
            Toast.makeText(ImageActivity.this.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }
    };
    /* access modifiers changed from: private */
    public TextView tvLoading;
    /* access modifiers changed from: private */
    public View vBottom;
    /* access modifiers changed from: private */
    public View vTop;

    /* access modifiers changed from: private */
    public Target getTarget() {
        return new Target() {
            public void onBitmapFailed(Exception exc, Drawable drawable) {
            }

            public void onBitmapLoaded(final Bitmap bitmap, LoadedFrom loadedFrom) {
                new Thread(new Runnable() {
                    public void run() {
                        if (ImageActivity.this.isAccessFolder()) {
                            int i = -1;
                            String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            StringBuilder sb = new StringBuilder();
                            sb.append(Constants.DIRECTORY_PATH);
                            sb.append(ConnectionFactory.DEFAULT_VHOST);
                            sb.append(format);
                            sb.append(".jpg");
                            String sb2 = sb.toString();
                            File file = new File(sb2);
                            try {
                                file.createNewFile();
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                i = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message obtain = Message.obtain();
                            obtain.what = i;
                            obtain.obj = sb2;
                            ImageActivity.this.mSaveCompleteHandler.sendMessage(obtain);
                        }
                    }
                }).start();
            }

            public void onPrepareLoad(Drawable drawable) {
            }
        };
    }

    /* access modifiers changed from: private */
    public boolean isAccessFolder() {
        String externalStorageState = Environment.getExternalStorageState();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isAccessFolder: external storage mounted state ? : ");
        sb.append(externalStorageState);
        Log.e(str, sb.toString());
        if (!externalStorageState.equals("mounted")) {
            return false;
        }
        File file = new File(Constants.DIRECTORY_PATH);
        String str2 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isAccessFolder: directory isExit ? ");
        sb2.append(file.exists());
        Log.e(str2, sb2.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        String str3 = this.TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("isAccessFolder: directory path ? ");
        sb3.append(file.getAbsolutePath());
        Log.e(str3, sb3.toString());
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int)R.layout.activity_image);
        DataVo dataVo = (DataVo) getIntent().getSerializableExtra("data_info");
        if (dataVo == null || TextUtils.isEmpty(dataVo.getCurl())) {
            Toast.makeText(getApplicationContext(),R.string.txt_no_link_data, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            this.mProdLink = dataVo.getCurl();
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mProdLink: ");
        sb.append(this.mProdLink);
        Log.e(str, sb.toString());
        this.vTop = findViewById(R.id.v_top);
        this.vBottom = findViewById(R.id.v_bottom);
        this.btSave = findViewById(R.id.bt_save);
        this.btSend = findViewById(R.id.bt_send);
        this.btInfo = findViewById(R.id.bt_info);
        this.ivImage = (ImageView) findViewById(R.id.iv_img);
        this.tvLoading = (TextView) findViewById(R.id.tv_loading);
        this.ivImage.setOnClickListener(this.btClickListener);
        this.btSave.setOnClickListener(this.btClickListener);
        this.btSend.setOnClickListener(this.btClickListener);
        this.btInfo.setOnClickListener(this.btClickListener);
        Picasso.get().load(this.mProdLink).fit().centerInside().error((int)R.drawable.no_image).into(this.ivImage, new Callback() {
            public void onError(Exception exc) {
                ImageActivity.this.tvLoading.setVisibility(View.VISIBLE);
            }

            public void onSuccess() {
                new PhotoViewAttacher(ImageActivity.this.ivImage);
                ImageActivity.this.tvLoading.setVisibility(View.GONE);
            }
        });
    }
}
