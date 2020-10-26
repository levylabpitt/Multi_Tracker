package com.pqiorg.multitracker.anoto.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;

import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
import com.pqiorg.multitracker.anoto.activities.util.MyAlertDialog;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;


public class AddStickersActivity extends AppCompatActivity implements OnClickListener {
    public static final String TAG = "AddStickersActivity";

    /* renamed from: m */
    TextView f2797m;
    private ADNAClient mApiClient;

    /* renamed from: n */
    TextView f2798n;

    /* renamed from: o */
    Button f2799o;

    /* renamed from: p */
    Button f2800p;

    /* renamed from: q */
    String f2801q;

    /* renamed from: r */
    String f2802r;

    /* renamed from: s */
    ADNAListener f2803s = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            Toast.makeText(AddStickersActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 17) {
                MyProgress.hides();
                DevLog.defaultLogging("addEusrPtrnPage onSuccess.");
                new MyAlertDialog(AddStickersActivity.this).customAlert(AddStickersActivity.this.getResources().getString(R.string.txt_registered));
            }
        }
    };

    /* access modifiers changed from: private */
    public void addMyStickers() {
        if (!PermissionUtil.isNetworkConnect(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(this);
        if (this.mApiClient != null) {
            this.mApiClient.addEusrPtrnPage(GlobalVar.USER_ACCOUNT, this.f2801q);
          //  this.mApiClient.addEusrPtrnPage(BeaconReferenceApplication.USER_ACCOUNT, this.f2801q);

        }
    }

    private void initADNA() {
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2803s);
    }

    public void confirmAddSticker() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = getLayoutInflater().inflate(R.layout.custom_confirm_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_ok);
        Button button2 = (Button) inflate.findViewById(R.id.btn_cancel);
        button.setText(R.string.txt_ok);
        button.setBackgroundColor(Color.parseColor("#bdbdbd"));
        button2.setText(R.string.txt_no);
        button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        textView.setText(R.string.txt_msg_reg_link);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                AddStickersActivity.this.addMyStickers();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void init() {
        this.f2797m = (TextView) findViewById(R.id.txt_pageaddress);
        this.f2798n = (TextView) findViewById(R.id.txt_stickers_quantity);
        this.f2799o = (Button) findViewById(R.id.btn_back);
        this.f2800p = (Button) findViewById(R.id.btn_add_stickers);
        this.f2799o.setOnClickListener(this);
        this.f2800p.setOnClickListener(this);
        this.f2797m.setText(this.f2801q);
        this.f2798n.setText(this.f2802r);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_stickers /*2131296334*/:
                confirmAddSticker();
                return;
            case R.id.btn_back /*2131296335*/:
                finish();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_add_stickers);
        Intent intent = getIntent();
        this.f2801q = intent.getStringExtra("PAGE_ADDRESS");
        this.f2802r = intent.getStringExtra("PTRN_COORDS_COUNT");
        initADNA();
        init();
    }
}
