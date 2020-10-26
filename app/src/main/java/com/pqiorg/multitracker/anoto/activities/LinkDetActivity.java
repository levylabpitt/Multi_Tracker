package com.pqiorg.multitracker.anoto.activities;

import android.content.Intent;
import android.os.Bundle;
//import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import com.anoto.adna.C0524R;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.ShareConstants;
import com.pqiorg.multitracker.R;

public class LinkDetActivity extends AppCompatActivity {
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    private static ImageView ivHistory;
    private static ImageView ivMypages;
    private static ImageView ivScan;
    private static ImageView ivSetting;
    private static int mCurTabPos;

    /* renamed from: A */
    String f2820A;

    /* renamed from: B */
    String f2821B;

    /* renamed from: C */
    String f2822C;

    /* renamed from: D */
    String f2823D;
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            LinkDetActivity.this.goMainActivity(view.getId());
        }
    };

    /* renamed from: m */
    Button f2824m;

    /* renamed from: n */
    Button f2825n;

    /* renamed from: o */
    TextView f2826o;

    /* renamed from: p */
    TextView f2827p;

    /* renamed from: q */
    TextView f2828q;

    /* renamed from: r */
    TextView f2829r;

    /* renamed from: s */
    TextView f2830s;

    /* renamed from: t */
    TextView f2831t;

    /* renamed from: u */
    TextView f2832u;

    /* renamed from: v */
    TextView f2833v;

    /* renamed from: w */
    String f2834w;

    /* renamed from: x */
    String f2835x;

    /* renamed from: y */
    String f2836y;

    /* renamed from: z */
    String f2837z;

    /* access modifiers changed from: private */
    public void goMainActivity(int i) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("VIEW_MODE", i);
        startActivity(intent);
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
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.LinkDetActivity.selectNavigation(int):void");
    }

    public void enableBottomMenu() {
        ivScan.setEnabled(true);
        ivHistory.setEnabled(true);
        ivSetting.setEnabled(true);
        ivMypages.setEnabled(true);
    }

    public void goLinkPage() {
        Intent intent = new Intent(this, LinkWebViewActivity.class);
        intent.putExtra("LINK_URL", this.f2821B);
        startActivity(intent);
    }

    public void init() {
        this.f2824m = (Button) findViewById(R.id.btn_back);
        this.f2825n = (Button) findViewById(R.id.btn_ok);
        this.f2826o = (TextView) findViewById(R.id.txt_page_address);
        this.f2827p = (TextView) findViewById(R.id.txt_index);
        this.f2828q = (TextView) findViewById(R.id.txt_reg_date);
        this.f2829r = (TextView) findViewById(R.id.txt_title);
        this.f2830s = (TextView) findViewById(R.id.txt_link_type);
        this.f2831t = (TextView) findViewById(R.id.txt_link_url);
        this.f2832u = (TextView) findViewById(R.id.txt_url_link_date);
        this.f2833v = (TextView) findViewById(R.id.txt_expire);
        this.f2824m.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LinkDetActivity.this.finish();
            }
        });
        this.f2825n.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LinkDetActivity.this.finish();
            }
        });
        this.f2831t.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LinkDetActivity.this.goLinkPage();
            }
        });
        setStickerInfo();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String str="";
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_link_det);
        init_bottomMenu();
        Intent intent = getIntent();
        this.f2834w = intent.getStringExtra("PAGE_ADDRESS");
        this.f2835x = intent.getStringExtra("INDEX");
        this.f2836y = intent.getStringExtra("REG_DATE");
        this.f2837z = intent.getStringExtra(ShareConstants.TITLE);
        this.f2820A = intent.getStringExtra("LINK_TYPE");
        this.f2821B = intent.getStringExtra("LINK_URL");
        this.f2822C = intent.getStringExtra("LINK_DATE");
        this.f2823D = intent.getStringExtra("EXPIRE");
        if (this.f2820A.equals("01")) {
            str = "Web";
        } else if (this.f2820A.equals("03")) {
            str = "Image";
        } else if (this.f2820A.equals("04")) {
            str = "MP3";
        } else if (this.f2820A.equals("05")) {
            str = "Video";
        } else if (this.f2820A.equals("06")) {
            str = "Name Card";
        } else {
            if (this.f2820A.equals("07")) {
                str = AppEventsConstants.EVENT_NAME_SCHEDULE;
            }
            init();
            enableBottomMenu();
        }
        this.f2820A = str;
        init();
        enableBottomMenu();
    }

    public void setStickerInfo() {
        this.f2826o.setText(this.f2834w);
        TextView textView = this.f2827p;
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(this.f2835x);
        textView.setText(sb.toString());
        this.f2828q.setText(this.f2836y);
        this.f2829r.setText(this.f2837z);
        this.f2830s.setText(this.f2820A);
        this.f2831t.setText(this.f2821B);
        this.f2832u.setText(this.f2822C);
        this.f2833v.setText(this.f2823D);
    }
}
