package com.pqiorg.multitracker.anoto.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.FragmentTransaction;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AppCompatActivity;*/
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.ScanPageAreaDataVo;
import com.anoto.adna.ServerApi.api.object.ScanPageAreaObject.ScanPageAreaData;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.fragments.MypagesFragment;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.ADNACapture;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder.CoordinateCallback;
import com.anoto.adna.sdk.util.DevLog;*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.share.internal.ShareConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ScanPageAreaDataVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ScanPageAreaObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.fragments.MypagesFragment;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.ADNACapture;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.ADNADecoder;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
//import net.hockeyapp.android.CrashManager;

public class MyScanActivity extends AppCompatActivity implements ADNADecoder.CoordinateCallback {
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    private static ImageView ivHistory = null;
    private static ImageView ivMypages = null;
    private static ImageView ivScan = null;
    private static ImageView ivSetting = null;
    private static int mCurTabPos = 0;
    public static double mLatitude = 37.5668367d;
    public static double mLongitude = 126.97857279999994d;
    /* access modifiers changed from: private */
    public String SCAN_TYPE = "";
    /* access modifiers changed from: private */
    public String TAG = "MyScanActivity";
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            MyScanActivity.this.goMainActivity(view.getId());
        }
    };

    /* renamed from: m */
    GlobalVar f2902m;
   // BeaconReferenceApplication f2902m;

    /* access modifiers changed from: private */
    public ADNACapture mADNACaptureFrag;
    private ADNAClient mApiClient;
    private SettingManager mSettingManager;

    /* renamed from: n */
    int f2903n = 0;

    /* renamed from: o */
    TextView f2904o;

    /* renamed from: p */
    Button f2905p;

    /* renamed from: q */
    ADNAListener f2906q = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(i);
            sb.append(" ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error Type: ");
            sb2.append(i);
            sb2.append(" Error Message:");
            sb2.append(str);
            DevLog.defaultLogging(sb2.toString());
            if (i == 1403) {
                MyScanActivity.this.f2903n = 1;
                MyScanActivity.this.customAlert(MyScanActivity.this.getResources().getString(R.string.txt_scan_invalid_sticker));
                MyScanActivity.this.mADNACaptureFrag.scanRestart();
                return;
            }
            Toast.makeText(MyScanActivity.this.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }

        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceiveADNA(int r5, Object r6) {
            /*
                r4 = this;
                android.os.Handler r0 = new android.os.Handler
                android.os.Looper r1 = android.os.Looper.getMainLooper()
                r0.<init>(r1)
                com.anoto.adna.ServerApi.api.object.ScanPageAreaObject$ScanPageAreaData r6 = (com.anoto.adna.ServerApi.api.object.ScanPageAreaObject.ScanPageAreaData) r6
                r1 = 0
                switch(r5) {
                    case 101: goto L_0x004f;
                    case 102: goto L_0x0042;
                    case 103: goto L_0x0011;
                    default: goto L_0x0010;
                }
            L_0x0010:
                goto L_0x0067
            L_0x0011:
                java.lang.String r5 = "onReceiveADNA == "
                java.lang.String r3 = "MSG_SCAN_PAGE_AREA_RESULT_MATCH_OWNER"
                android.util.Log.d(r5, r3)
                com.anoto.adna.activities.MyScanActivity r5 = com.anoto.adna.activities.MyScanActivity.this
                java.lang.String r5 = r5.SCAN_TYPE
                java.lang.String r3 = com.anoto.adna.activities.fragments.MypagesFragment.SCAN_TYPE_ADD_FLAG
                boolean r5 = r5.equals(r3)
                if (r5 == 0) goto L_0x002c
                com.anoto.adna.activities.MyScanActivity$2$3 r5 = new com.anoto.adna.activities.MyScanActivity$2$3
                r5.<init>(r6)
                goto L_0x005b
            L_0x002c:
                com.anoto.adna.ServerApi.api.object.ScanPageAreaObject$Data r5 = r6.data
                java.util.ArrayList<com.anoto.adna.ServerApi.api.object.ScanPageAreaDataVo> r5 = r5.cid_list
                int r5 = r5.size()
                if (r5 <= 0) goto L_0x003c
                com.anoto.adna.activities.MyScanActivity r5 = com.anoto.adna.activities.MyScanActivity.this
                r5.goLinkDet(r6)
                goto L_0x005e
            L_0x003c:
                com.anoto.adna.activities.MyScanActivity r5 = com.anoto.adna.activities.MyScanActivity.this
                r5.goLinkReg(r6)
                goto L_0x005e
            L_0x0042:
                java.lang.String r5 = "onReceiveADNA == "
                java.lang.String r6 = "MSG_SCAN_PAGE_AREA_RESULT_MISSMATCH_OWNER"
                android.util.Log.d(r5, r6)
                com.anoto.adna.activities.MyScanActivity$2$2 r5 = new com.anoto.adna.activities.MyScanActivity$2$2
                r5.<init>()
                goto L_0x005b
            L_0x004f:
                java.lang.String r5 = "onReceiveADNA == "
                java.lang.String r3 = "MSG_SCAN_PAGE_AREA_RESULT_EMPTY_OWNER"
                android.util.Log.d(r5, r3)
                com.anoto.adna.activities.MyScanActivity$2$1 r5 = new com.anoto.adna.activities.MyScanActivity$2$1
                r5.<init>(r6)
            L_0x005b:
                r0.postDelayed(r5, r1)
            L_0x005e:
                com.anoto.adna.activities.MyScanActivity r5 = com.anoto.adna.activities.MyScanActivity.this
                com.anoto.adna.sdk.ADNACapture r5 = r5.mADNACaptureFrag
                r5.scanRestart()
            L_0x0067:
                com.anoto.adna.activities.MyScanActivity r5 = com.anoto.adna.activities.MyScanActivity.this
                r6 = 1
                r5.f2903n = r6
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.MyScanActivity.C05692.onReceiveADNA(int, java.lang.Object):void");
        }
    };
    private BroadcastReceiver screenOffBroadcastMyScan = new BroadcastReceiver() {
        private String screen_off = "android.intent.action.SCREEN_OFF";

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(this.screen_off)) {
                Log.e(MyScanActivity.this.TAG, "onReceive: PowerKey pressed");
                MyScanActivity.this.recreate();
            }
        }
    };

    private long getDate(String str) {
        long j;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        try {
            Calendar instance = Calendar.getInstance();
            instance.setTime(simpleDateFormat.parse(str));
            j = instance.getTimeInMillis();
            String str2 = "test";
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("time = ");
                sb.append(instance.getTimeInMillis());
                Log.i(str2, sb.toString());
                return j;
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                return j;
            }
        } catch (ParseException e2) {
           // e = e2;
            j = 0;
            e2.printStackTrace();
            return j;
        }
    }

    private void getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
        boolean isProviderEnabled2 = locationManager.isProviderEnabled("network");
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isGPSEnabled=");
        sb.append(isProviderEnabled);
        sb.append(" | isNetworkEnabled=");
        sb.append(isProviderEnabled2);
        Log.d(str, sb.toString());
        LocationListener r7 = new LocationListener() {
            public void onLocationChanged(Location location) {
                MyScanActivity.mLatitude = location.getLatitude();
                MyScanActivity.mLongitude = location.getLongitude();
                String c = MyScanActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("locationListener >>> lat=");
                sb.append(MyScanActivity.mLatitude);
                sb.append(", lon=");
                sb.append(MyScanActivity.mLongitude);
                Log.d(c, sb.toString());
            }

            public void onProviderDisabled(String str) {
            }

            public void onProviderEnabled(String str) {
            }

            public void onStatusChanged(String str, int i, Bundle bundle) {
            }
        };
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationManager locationManager2 = locationManager;
            LocationListener r6 = r7;
            locationManager2.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.0f, r6);
            locationManager2.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, r6);
            Location lastKnownLocation = locationManager.getLastKnownLocation("gps");
            if (lastKnownLocation != null) {
                mLatitude = lastKnownLocation.getLatitude();
                mLongitude = lastKnownLocation.getLongitude();
                String str2 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("lastKnownLocation >>> lat=");
                sb2.append(mLatitude);
                sb2.append(", lon=");
                sb2.append(mLongitude);
                Log.d(str2, sb2.toString());
            }
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
        this.mApiClient.setADNAListener(this.f2906q);
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
        //selectNavigation(3);
    }

    private void readCoordinate(String str, long j, long j2) {
        if (this.f2903n == 0) {
            this.mApiClient.getPageAreaScan(String.valueOf(j), String.valueOf(j2), str);
        }
    }

    private void registerBroadcast() {
        registerReceiver(this.screenOffBroadcastMyScan, new IntentFilter("android.intent.action.SCREEN_OFF"));
    }

    private void scanContent() {
        this.mADNACaptureFrag = new ADNACapture();
        getSupportFragmentManager().beginTransaction().replace(R.id.view_fragment_content, this.mADNACaptureFrag).addToBackStack(null).commitAllowingStateLoss();
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
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.MyScanActivity.selectNavigation(int):void");
    }

    private void unregisterBroadcast() {
        unregisterReceiver(this.screenOffBroadcastMyScan);
    }

    public void confirmAddSticker(final String str, final String str2) {
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
        textView.setText(R.string.txt_msg_scan_unregistered_sticker);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                MyScanActivity.this.f2903n = 0;
                MyScanActivity.this.mADNACaptureFrag.scanRestart();
                MyScanActivity.this.goAddPurchaseSticker(str, str2);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                MyScanActivity.this.f2903n = 0;
                MyScanActivity.this.mADNACaptureFrag.scanRestart();
            }
        });
        dialog.show();
    }

    public void customAlert(String str) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = getLayoutInflater().inflate(R.layout.custom_alret_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_ok);
        textView.setText(str);
        textView.setGravity(17);
        button.setText(R.string.txt_ok);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                MyScanActivity.this.f2903n = 0;
                MyScanActivity.this.mADNACaptureFrag.scanRestart();
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

    public void goAddPurchaseSticker(String str, String str2) {
        Intent intent = new Intent(this, AddStickersActivity.class);
        intent.putExtra("PAGE_ADDRESS", str);
        intent.putExtra("PTRN_COORDS_COUNT", str2);
        startActivity(intent);
    }

    public void goLinkDet(ScanPageAreaObject.ScanPageAreaData scanPageAreaData) {
        Intent intent = new Intent(this, LinkDetActivity.class);
        intent.putExtra("PAGE_ADDRESS", scanPageAreaData.data.page_address);
        intent.putExtra("INDEX", scanPageAreaData.data.index_in_page);
        intent.putExtra("REG_DATE", scanPageAreaData.data.reg_dt);
        intent.putExtra(ShareConstants.TITLE, scanPageAreaData.data.ptrn_coords_name);
        intent.putExtra("LINK_TYPE", ((ScanPageAreaDataVo) scanPageAreaData.data.cid_list.get(0)).getCtype());
        intent.putExtra("LINK_URL", ((ScanPageAreaDataVo) scanPageAreaData.data.cid_list.get(0)).getCurl());
        intent.putExtra("LINK_DATE", ((ScanPageAreaDataVo) scanPageAreaData.data.cid_list.get(0)).getLink_dt());
        intent.putExtra("EXPIRE", ((ScanPageAreaDataVo) scanPageAreaData.data.cid_list.get(0)).getLink_expire_dt());
        this.f2903n = 0;
        startActivity(intent);
    }

    public void goLinkReg(ScanPageAreaObject.ScanPageAreaData scanPageAreaData) {
        Intent intent = new Intent(this, LinkRegActivity.class);
        intent.putExtra("PAGE_ADDRESS", scanPageAreaData.data.page_address);
        intent.putExtra("PTRN_COORDS_ID", scanPageAreaData.data.ptrn_coords_id);
        intent.putExtra("QUANTITY", "1");
        intent.putExtra("INDEX_LIST", scanPageAreaData.data.index_in_page);
        intent.putExtra("PRE_PAGE", this.TAG);
        this.f2903n = 0;
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onCoordinateEvent(long j, long j2, String str) {
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCoordinateEvent: ");
        sb.append(j);
        sb.append(", ");
        sb.append(j2);
        sb.append(", ");
        sb.append(str);
        Log.d(str2, sb.toString());
        readCoordinate(str, j, j2);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        TextView textView;
        Resources resources;
        int i;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_scan);
        init_bottomMenu();
        synchronized (this) {
            getMyLocation();
        }
      //  this.f2902m = (GlobalVar) getApplicationContext();
        this.f2902m = (GlobalVar) getApplicationContext();
        initADNA();
        scanContent();
        registerBroadcast();
       // this.SCAN_TYPE = getIntent().getStringExtra("SCAN_TYPE");//nks
        this.SCAN_TYPE ="SCAN_TYPE";
        this.f2904o = (TextView) findViewById(R.id.txt_title);
        if (this.SCAN_TYPE.equals(MypagesFragment.SCAN_TYPE_ADD_FLAG)) {
            textView = this.f2904o;
            resources = getResources();
            i = R.string.txt_add_stickers;
        } else {
            textView = this.f2904o;
            resources = getResources();
            i = R.string.txt_stickers_link_registration;
        }
        textView.setText(resources.getString(i));
        this.f2905p = (Button) findViewById(R.id.btn_back);
        this.f2905p.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MyScanActivity.this.finish();
            }
        });
        enableBottomMenu();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        try {
            this.mADNACaptureFrag = new ADNACapture();
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.view_fragment_content, this.mADNACaptureFrag);
            beginTransaction.addToBackStack(null);
            beginTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
       // CrashManager.register(getApplicationContext());
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2906q);
        this.f2903n = 0;
    }
}
