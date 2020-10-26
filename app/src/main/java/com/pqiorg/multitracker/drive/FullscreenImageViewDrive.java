
package com.pqiorg.multitracker.drive;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pqiorg.multitracker.R;
import com.synapse.ProgressHUD;


//import planet.info.skyline.R;
//import planet.info.skyline.old_activity.BaseActivity;

public class FullscreenImageViewDrive extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    ProgressHUD mProgressHUD;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_view);
        showprogressdialog();
        // initialize the view and the gesture detector
        mImageView = findViewById(R.id.imageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        setToolbar();
        final String uri = getIntent().getStringExtra("uri");


        //    mImageView.setImageURI(Uri.parse(uri));


        Glide.with(this).load(uri).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                hideprogressdialog();
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                hideprogressdialog();
                return false;
            }
        })
                .into(mImageView);


    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        final String name = getIntent().getStringExtra("name");
        // mTitle.setText(Utility.getLimitedCharFromString(name));
        mTitle.setText(name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    // this redirects all touch events in the activity to the gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        // when a scale gesture is detected, use it to resize the image
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            return true;
        }
    }
    public void showprogressdialog() {
        try {
            mProgressHUD = ProgressHUD.show(FullscreenImageViewDrive.this, "", false);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void hideprogressdialog() {
        try {
            if (mProgressHUD.isShowing()) {
                mProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
