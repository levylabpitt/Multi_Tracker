package com.pqiorg.multitracker.anoto.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
/*import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AppCompatActivity;*/
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.DateCalendarHelper;
import com.anoto.adna.util.MyAlertDialog;
import com.anoto.adna.util.MyProgress;
import com.anoto.adna.util.PermissionUtil;*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.appevents.AppEventsConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
import com.pqiorg.multitracker.anoto.activities.util.DateCalendarHelper;
import com.pqiorg.multitracker.anoto.activities.util.MyAlertDialog;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

import java.util.Calendar;

public class LinkRegActivity extends AppCompatActivity implements OnClickListener {
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    public static final String TAG = "LinkRegActivity";
    private static ImageView ivHistory;
    private static ImageView ivMypages;
    private static ImageView ivScan;
    private static ImageView ivSetting;
    private static int mCurTabPos;

    /* renamed from: A */
    String f2842A;

    /* renamed from: B */
    String f2843B;

    /* renamed from: C */
    String f2844C;

    /* renamed from: D */
    String f2845D;

    /* renamed from: E */
    String f2846E;

    /* renamed from: F */
    String f2847F;

    /* renamed from: G */
    int f2848G;

    /* renamed from: H */
    int f2849H;

    /* renamed from: I */
    int f2850I;

    /* renamed from: J */
    String f2851J;

    /* renamed from: K */
    String f2852K;

    /* renamed from: L */
    String f2853L;

    /* renamed from: M */
    String f2854M;

    /* renamed from: N */
    String f2855N;

    /* renamed from: O */
    ADNAListener f2856O = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            Toast.makeText(LinkRegActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 18) {
                MyProgress.hides();
                DevLog.defaultLogging("updEusrPtrnPageArea onSuccess.");
                new MyAlertDialog(LinkRegActivity.this).customAlert(LinkRegActivity.this.getResources().getString(R.string.txt_registered));
            }
        }
    };
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            LinkRegActivity.this.goMainActivity(view.getId());
        }
    };
    private DateCalendarHelper dateHelper;

    /* renamed from: m */
    LinearLayout f2857m;
    private ADNAClient mApiClient;

    /* renamed from: n */
    Button f2858n;

    /* renamed from: o */
    Button f2859o;

    /* renamed from: p */
    TextView f2860p;

    /* renamed from: q */
    TextView f2861q;

    /* renamed from: r */
    TextView f2862r;

    /* renamed from: s */
    TextView f2863s;

    /* renamed from: t */
    EditText f2864t;

    /* renamed from: u */
    EditText f2865u;

    /* renamed from: v */
    Spinner f2866v;

    /* renamed from: w */
    Button f2867w;

    /* renamed from: x */
    View f2868x;

    /* renamed from: y */
    String f2869y;

    /* renamed from: z */
    String f2870z;

    /* access modifiers changed from: private */
    public void goMainActivity(int i) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("VIEW_MODE", i);
        startActivity(intent);
    }

    private void initADNA() {
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2856O);
    }

    private void init_bottomMenu() {
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
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.LinkRegActivity.selectNavigation(int):void");
    }

    /* access modifiers changed from: private */
    public void updLinkSticker() {
        if (!PermissionUtil.isNetworkConnect(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(this);
        if (this.mApiClient != null) {
            if (this.f2869y.equals("MyScanActivity") || this.f2869y.equals(MyStickerList2Activity.TAG)) {
                this.f2870z = "";
            }
            //this.mApiClient.updEusrPtrnPageArea(GlobalVar.USER_ACCOUNT, this.f2870z, this.f2842A, this.f2864t.getText().toString(), this.f2847F, this.f2865u.getText().toString(), this.f2862r.getText().toString().equals(getResources().getString(R.string.txt_none)) ? "" : this.f2862r.getText().toString());
            this.mApiClient.updEusrPtrnPageArea(GlobalVar.USER_ACCOUNT, this.f2870z, this.f2842A, this.f2864t.getText().toString(), this.f2847F, this.f2865u.getText().toString(), this.f2862r.getText().toString().equals(getResources().getString(R.string.txt_none)) ? "" : this.f2862r.getText().toString());
        }
    }

    public void confirmUpdLink() {
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
                LinkRegActivity.this.updLinkSticker();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void enableBottomMenu() {
        ivScan.setEnabled(true);
        ivHistory.setEnabled(true);
        ivSetting.setEnabled(true);
        ivMypages.setEnabled(true);
    }

    public void init() {
        LinearLayout linearLayout;
        int i;
        this.f2857m = (LinearLayout) findViewById(R.id.lay_index);
        this.f2858n = (Button) findViewById(R.id.btn_back);
        this.f2859o = (Button) findViewById(R.id.btn_link_reg);
        this.f2860p = (TextView) findViewById(R.id.txt_page_address);
        this.f2861q = (TextView) findViewById(R.id.txt_stickers_quantity);
        this.f2864t = (EditText) findViewById(R.id.edit_title);
        this.f2865u = (EditText) findViewById(R.id.edit_link_url);
        this.f2866v = (Spinner) findViewById(R.id.spinner_link_type);
        this.f2862r = (TextView) findViewById(R.id.txt_expire);
        this.f2863s = (TextView) findViewById(R.id.txt_index);
        this.f2867w = (Button) findViewById(R.id.btn_date_delete);
        this.f2868x = findViewById(R.id.line_index);
        this.f2858n.setOnClickListener(this);
        this.f2859o.setOnClickListener(this);
        this.f2862r.setOnClickListener(this);
        this.f2867w.setOnClickListener(this);
        if (!this.f2845D.equals("")) {
            linearLayout = this.f2857m;
            i = 0;
        } else {
            linearLayout = this.f2857m;
            i = 8;
        }
        linearLayout.setVisibility(i);
        this.f2868x.setVisibility(i);
        this.f2844C = this.f2870z.replace(",", "\n");
        this.f2860p.setText(this.f2844C);
        this.f2861q.setText(this.f2843B);
        TextView textView = this.f2863s;
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(this.f2845D);
        textView.setText(sb.toString());
        this.f2866v.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                LinkRegActivity linkRegActivity;
                String str;
                LinkRegActivity.this.f2846E = adapterView.getItemAtPosition(i).toString();
                if (i == 0) {
                    linkRegActivity = LinkRegActivity.this;
                    str = "01";
                } else if (i == 1) {
                    linkRegActivity = LinkRegActivity.this;
                    str = "03";
                } else if (i == 2) {
                    linkRegActivity = LinkRegActivity.this;
                    str = "04";
                } else if (i == 3) {
                    linkRegActivity = LinkRegActivity.this;
                    str = "05";
                } else if (i == 4) {
                    linkRegActivity = LinkRegActivity.this;
                    str = "06";
                } else if (i == 5) {
                    linkRegActivity = LinkRegActivity.this;
                    str = "07";
                } else {
                    return;
                }
                linkRegActivity.f2847F = str;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.btn_date_delete) {
            this.f2862r.setText(getResources().getString(R.string.txt_none));
        } else if (id != R.id.btn_link_reg) {
            if (id == R.id.txt_expire) {
                showDatePickerDialog();
            }
        } else if (this.f2865u.getText().toString().equals("")) {
            Toast.makeText(this, getResources().getString(R.string.txt_msg_input_link_url), Toast.LENGTH_SHORT).show();
        } else if (this.f2862r.getText().toString().equals(getResources().getString(R.string.txt_none)) || Integer.valueOf(this.f2855N).intValue() >= Integer.valueOf(this.f2854M).intValue()) {
            confirmUpdLink();
        } else {
            new MyAlertDialog(this).noCloseAlert(getResources().getString(R.string.txt_msg_expiration_date_false));
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_link_reg);
        init_bottomMenu();
        Intent intent = getIntent();
        this.f2870z = intent.getStringExtra("PAGE_ADDRESS");
        this.f2842A = intent.getStringExtra("PTRN_COORDS_ID");
        this.f2843B = intent.getStringExtra("QUANTITY");
        this.f2869y = intent.getStringExtra("PRE_PAGE");
        this.f2845D = intent.getStringExtra("INDEX_LIST");
        this.dateHelper = new DateCalendarHelper();
        initADNA();
        init();
        enableBottomMenu();
    }

    public void setToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog() {
        String str;
        String str2;
        Calendar instance = Calendar.getInstance();
        this.f2848G = instance.get(Calendar.YEAR);
        this.f2849H = instance.get(Calendar.MONTH);
        this.f2850I = instance.get(Calendar.DATE);
        if (this.f2849H < 10) {
            StringBuilder sb = new StringBuilder();
            sb.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            sb.append(String.valueOf(this.f2849H + 1));
            str = sb.toString();
        } else {
            str = String.valueOf(this.f2849H + 1);
        }
        if (this.f2850I < 10) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            sb2.append(String.valueOf(this.f2850I));
            str2 = sb2.toString();
        } else {
            str2 = String.valueOf(this.f2850I);
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(String.valueOf(this.f2848G));
        sb3.append(str);
        sb3.append(str2);
        this.f2854M = sb3.toString();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker, new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                LinkRegActivity linkRegActivity;
                String valueOf;
                LinkRegActivity linkRegActivity2;
                String valueOf2;
                LinkRegActivity.this.f2848G = i;
                LinkRegActivity.this.f2849H = i2 + 1;
                LinkRegActivity.this.f2850I = i3;
                LinkRegActivity.this.f2851J = String.valueOf(LinkRegActivity.this.f2848G);
                if (LinkRegActivity.this.f2849H < 10) {
                    linkRegActivity = LinkRegActivity.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                    sb.append(String.valueOf(LinkRegActivity.this.f2849H));
                    valueOf = sb.toString();
                } else {
                    linkRegActivity = LinkRegActivity.this;
                    valueOf = String.valueOf(LinkRegActivity.this.f2849H);
                }
                linkRegActivity.f2852K = valueOf;
                if (LinkRegActivity.this.f2850I < 10) {
                    linkRegActivity2 = LinkRegActivity.this;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                    sb2.append(String.valueOf(LinkRegActivity.this.f2850I));
                    valueOf2 = sb2.toString();
                } else {
                    linkRegActivity2 = LinkRegActivity.this;
                    valueOf2 = String.valueOf(LinkRegActivity.this.f2850I);
                }
                linkRegActivity2.f2853L = valueOf2;
                TextView textView = LinkRegActivity.this.f2862r;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(LinkRegActivity.this.f2851J);
                sb3.append("-");
                sb3.append(LinkRegActivity.this.f2852K);
                sb3.append("-");
                sb3.append(LinkRegActivity.this.f2853L);
                textView.setText(sb3.toString());
                LinkRegActivity linkRegActivity3 = LinkRegActivity.this;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(LinkRegActivity.this.f2851J);
                sb4.append(LinkRegActivity.this.f2852K);
                sb4.append(LinkRegActivity.this.f2853L);
                linkRegActivity3.f2855N = sb4.toString();
            }
        }, this.f2848G, this.f2849H, this.f2850I);
        datePickerDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        datePickerDialog.show();
    }
}
