package com.pqiorg.multitracker.anoto.activities;

import android.os.Bundle;
//import android.support.p003v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
//import com.anoto.adna.C0524R;
//import com.anoto.adna.ServerApi.api.object.DataVo;

public class TextActivity extends AppCompatActivity {
    private String TAG = "TextActivity";
    private DataVo mDataInfo;
    private String mProdLink;

    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getExtras() != null) {
            TextUtils.isEmpty(getIntent().getStringExtra("from"));
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_text);
        this.mDataInfo = (DataVo) getIntent().getSerializableExtra("data_info");
        if (this.mDataInfo == null || TextUtils.isEmpty(this.mDataInfo.getCurl())) {
            Toast.makeText(getApplicationContext(), R.string.txt_no_link_data,  Toast.LENGTH_SHORT).show();
            finish();
        } else {
            this.mProdLink = this.mDataInfo.getCurl();
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mProdLink: ");
        sb.append(this.mProdLink);
        Log.e(str, sb.toString());
    }
}
