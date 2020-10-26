package com.pqiorg.multitracker.anoto.activities;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
//import android.support.p003v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.MyProgress;
import com.anoto.adna.util.PermissionUtil;*/
import androidx.appcompat.app.AppCompatActivity;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    public static final String TAG = "LoginActivity";
    private static ImageView ivHistory;
    private static ImageView ivMypages;
    private static ImageView ivScan;
    private static ImageView ivSetting;
    public static LoginActivity loginActivity;
    private static int mCurTabPos;
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            LoginActivity.this.goMainActivity(view.getId());
        }
    };

    /* renamed from: m */
    EditText f2881m;
    private ADNAClient mApiClient;

    /* renamed from: n */
    EditText f2882n;

    /* renamed from: o */
    Button f2883o;

    /* renamed from: p */
    Button f2884p;

    /* renamed from: q */
    TextView f2885q;

    /* renamed from: r */
    ADNAListener f2886r = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 20) {
                MyProgress.hides();
                DevLog.defaultLogging("eusr Login onSuccess.");
                LoginActivity.this.setEusrAccountInfo(LoginActivity.this.f2881m.getText().toString());
                LoginActivity.this.goMainActivity(R.id.bt_mypages);
            }
        }
    };

    private void eusrLogin(String str, String str2) {
        if (!PermissionUtil.isNetworkConnect(this)) {
            Toast.makeText(this, R.string.txt_network_disconnected,  Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(this);
        if (this.mApiClient != null) {
            this.mApiClient.eusrLogin(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void goMainActivity(int i) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("VIEW_MODE", i);
        startActivity(intent);
    }

    private void initADNA() {
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2886r);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0015, code lost:
        r4.setImageResource(r3);
        r4 = ivHistory;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x001a, code lost:
        r4.setImageResource(r2);
        r4 = ivSetting;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x001f, code lost:
        r4.setImageResource(r1);
        r4 = ivMypages;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0024, code lost:
        r4.setImageResource(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0027, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void selectNavigation(int r4) {
        /*
            mCurTabPos = r4
            int r4 = mCurTabPos
            r0 = 2131231144(0x7f0801a8, float:1.807836E38)
            r1 = 2131231166(0x7f0801be, float:1.8078405E38)
            r2 = 2131230998(0x7f080116, float:1.8078065E38)
            r3 = 2131230955(0x7f0800eb, float:1.8077977E38)
            switch(r4) {
                case 0: goto L_0x0058;
                case 1: goto L_0x004d;
                case 2: goto L_0x003d;
                case 3: goto L_0x0028;
                default: goto L_0x0013;
            }
        L_0x0013:
            android.widget.ImageView r4 = ivScan
        L_0x0015:
            r4.setImageResource(r3)
            android.widget.ImageView r4 = ivHistory
        L_0x001a:
            r4.setImageResource(r2)
            android.widget.ImageView r4 = ivSetting
        L_0x001f:
            r4.setImageResource(r1)
            android.widget.ImageView r4 = ivMypages
        L_0x0024:
            r4.setImageResource(r0)
            return
        L_0x0028:
            android.widget.ImageView r4 = ivScan
            r4.setImageResource(r3)
            android.widget.ImageView r4 = ivHistory
            r4.setImageResource(r2)
            android.widget.ImageView r4 = ivSetting
            r4.setImageResource(r1)
            android.widget.ImageView r4 = ivMypages
            r0 = 2131231145(0x7f0801a9, float:1.8078363E38)
            goto L_0x0024
        L_0x003d:
            android.widget.ImageView r4 = ivScan
            r4.setImageResource(r3)
            android.widget.ImageView r4 = ivHistory
            r4.setImageResource(r2)
            android.widget.ImageView r4 = ivSetting
            r1 = 2131231167(0x7f0801bf, float:1.8078407E38)
            goto L_0x001f
        L_0x004d:
            android.widget.ImageView r4 = ivScan
            r4.setImageResource(r3)
            android.widget.ImageView r4 = ivHistory
            r2 = 2131230999(0x7f080117, float:1.8078067E38)
            goto L_0x001a
        L_0x0058:
            android.widget.ImageView r4 = ivScan
            r3 = 2131230956(0x7f0800ec, float:1.807798E38)
            goto L_0x0015
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.LoginActivity.selectNavigation(int):void");
    }

    public void enableBottomMenu() {
        ivScan.setEnabled(true);
        ivHistory.setEnabled(true);
        ivSetting.setEnabled(true);
        ivMypages.setEnabled(true);
    }

    public void init() {
        this.f2883o = (Button) findViewById(R.id.btn_back);
        this.f2881m = (EditText) findViewById(R.id.edit_id);
        this.f2882n = (EditText) findViewById(R.id.edit_pwd);
        this.f2884p = (Button) findViewById(R.id.btn_login);
        this.f2885q = (TextView) findViewById(R.id.txt_new_account);
        this.f2883o.setOnClickListener(this);
        this.f2884p.setOnClickListener(this);
        this.f2885q.setOnClickListener(this);
        ivScan = (ImageView) findViewById(R.id.iv_scan);
        ivHistory = (ImageView) findViewById(R.id.iv_history);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        ivMypages = (ImageView) findViewById(R.id.iv_mypages);
        ivScan.setEnabled(false);
        ivHistory.setEnabled(false);
        ivSetting.setEnabled(false);
        ivMypages.setEnabled(false);
        findViewById(R.id.bt_scan).setOnClickListener(this.btClickListener);
        findViewById(R.id.bt_history).setOnClickListener(this.btClickListener);
        findViewById(R.id.bt_setting).setOnClickListener(this.btClickListener);
        findViewById(R.id.bt_mypages).setOnClickListener(this.btClickListener);
        selectNavigation(3);
    }

    public void onClick(View view) {
        String string;
        Resources resources;
        int i;
        int id = view.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.btn_login) {
            if (this.f2881m.getText().toString().equals("")) {
                resources = getResources();
                i = R.string.txt_hint_input_id;
            } else if (this.f2882n.getText().toString().equals("")) {
                resources = getResources();
                i = R.string.txt_hint_input_pwd;
            } else if (BasicUtil.checkEmail(this, this.f2881m.getText().toString())) {
                try {
                    MessageDigest instance = MessageDigest.getInstance("SHA-256");
                    instance.update(this.f2882n.getText().toString().getBytes());
                    byte[] digest = instance.digest();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (byte b : digest) {
                        stringBuffer.append(Integer.toString((b & 255) + 256, 16).substring(1));
                    }
                    eusrLogin(this.f2881m.getText().toString(), stringBuffer.toString());
                    return;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                string = getString(R.string.txt_not_valid_email);
                Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
                return;
            }
            string = resources.getString(i);
            Toast.makeText(this, string,  Toast.LENGTH_SHORT).show();
            return;
        } else if (id == R.id.txt_new_account) {
            startActivity(new Intent(this, SignupActivity.class));
        } else {
            return;
        }
        overridePendingTransition(0, 0);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        loginActivity = this;
        init();
        initADNA();
        enableBottomMenu();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0 && i == 4) {
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        initADNA();
    }

    public void setEusrAccountInfo(String str) {
        try {
          //  GlobalVar.USER_ACCOUNT = str;
            GlobalVar.USER_ACCOUNT = str;
            Editor edit = getSharedPreferences("eusr_info", 0).edit();
            edit.putString("user_email", str);
            edit.commit();
        } catch (Exception unused) {
        }
    }
}
