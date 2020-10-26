package com.pqiorg.multitracker.anoto.activities.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.pqiorg.multitracker.R;


public class MyAlertDialog {
    /* access modifiers changed from: private */
    public Activity activity;

    public MyAlertDialog(Activity activity2) {
        this.activity = activity2;
    }

    public void customAlert(String str) {
        final Dialog dialog = new Dialog(this.activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = this.activity.getLayoutInflater().inflate(R.layout.custom_alret_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_ok);
        textView.setText(str);
        textView.setGravity(17);
        button.setText(R.string.txt_ok);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                MyAlertDialog.this.activity.finish();
            }
        });
        dialog.show();
    }

    public void noCloseAlert(String str) {
        final Dialog dialog = new Dialog(this.activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = this.activity.getLayoutInflater().inflate(R.layout.custom_alret_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_ok);
        textView.setText(str);
        textView.setGravity(17);
        button.setText(R.string.txt_ok);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
