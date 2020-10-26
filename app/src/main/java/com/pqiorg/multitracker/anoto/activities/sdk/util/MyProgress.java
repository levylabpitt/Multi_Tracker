package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pqiorg.multitracker.R;


public class MyProgress extends Dialog {
    public static MyProgress mLoading;
    private Context context;

    public MyProgress(Context context2) {
        super(context2);
        this.context = context2;
        requestWindowFeature(1);
        setContentView(R.layout.dialog_my_progress);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        initView();
    }

    public static void hides() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
        mLoading = null;
    }

    private void initView() {
        ((ImageView) findViewById(R.id.img_android)).setAnimation(AnimationUtils.loadAnimation(this.context, R.anim.loading));
    }

    public static void shows(Context context2) {
        if (mLoading == null) {
            mLoading = new MyProgress(context2);
        }
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }
}
