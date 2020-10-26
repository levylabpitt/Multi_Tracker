package com.pqiorg.multitracker.anoto.activities;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.Contacts;
/*import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentTransaction;
import android.support.p003v7.app.AppCompatActivity;*/
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.DataNameCardVo.Addr;
import com.anoto.adna.ServerApi.api.object.DataNameCardVo.Tel;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.fragments.HistoryFragment;
import com.anoto.adna.activities.fragments.MypagesFragment;
import com.anoto.adna.activities.fragments.SettingFragment;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.ADNACapture;
import com.anoto.adna.sdk.camera.decoder.ADNADecoder.CoordinateCallback;
import com.anoto.adna.sdk.util.DevLog;*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataNameCardVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.fragments.HistoryFragment;
import com.pqiorg.multitracker.anoto.activities.fragments.MypagesFragment;
import com.pqiorg.multitracker.anoto.activities.fragments.SettingFragment;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.ADNACapture;
import com.pqiorg.multitracker.anoto.activities.sdk.camera.decoder.ADNADecoder;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
//import net.hockeyapp.android.CrashManager;

public class MainActivity extends AppCompatActivity implements ADNADecoder.CoordinateCallback {
    private static final int BACK_KEY_TIMEOUT = 2;
    private static final int MILLS_IN_SEC = 1000;
    private static final int MSG_TIMER_EXPIRED = 1;
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    private static ImageView ivHistory = null;
    private static ImageView ivMypages = null;
    /* access modifiers changed from: private */
    public static ImageView ivScan = null;
    private static ImageView ivSetting = null;
    private static int mCurTabPos = 0;
    public static double mLatitude = 37.5668367d;
    public static double mLongitude = 126.97857279999994d;
    public static MainActivity mainActivity;
    /* access modifiers changed from: private */
    public String TAG = "MainActivity";
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            MainActivity.this.setFragmentPage(view.getId());
        }
    };

    /* renamed from: m */
    LinearLayout f2893m;
    /* access modifiers changed from: private */
    public ADNACapture mADNACaptureFrag;
    private ADNAClient mApiClient;
    private long mCurTimeInMillis = 0;
    /* access modifiers changed from: private */
    public boolean mIsBackKeyPressed = false;
    private SettingManager mSettingManager;
    private final Handler mTimerHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                MainActivity.this.mIsBackKeyPressed = false;
            }
        }
    };

    /* renamed from: n */
    ADNAListener f2894n = new ADNAListener() {
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
            Toast.makeText(MainActivity.this.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            MainActivity.this.mADNACaptureFrag.scanRestart();
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 4) {
                new ArrayList();
                ArrayList arrayList = (ArrayList) obj;
                if (arrayList.size() > 1) {
                    MainActivity.this.onScanMultiData(arrayList);
                } else if (arrayList.size() == 1) {
                    DataVo dataVo = (DataVo) arrayList.get(0);
                    String ctype = dataVo.getCtype();
                    if (ctype.equals("02")) {
                        MainActivity.this.onScanResultText(dataVo);
                    } else if (ctype.equals("03")) {
                        MainActivity.this.onScanResultImage(dataVo);
                    } else if (ctype.equals("04")) {
                        MainActivity.this.onScanResultMP3(dataVo);
                    } else if (ctype.equals("05")) {
                        MainActivity.this.onScanResultVideo(dataVo);
                    } else if (ctype.equals("01")) {
                        MainActivity.this.onScanResultWeb(dataVo);
                    } else if (ctype.equals("06")) {
                        MainActivity.this.onScanResultNameCard(dataVo);
                    } else if (ctype.equals("07")) {
                        MainActivity.this.onScanResultSchedule(dataVo);
                    }
                }
                MainActivity.this.mADNACaptureFrag.scanRestart();
            }
        }
    };
    private float orgBrightness = 0.5f;
    private LayoutParams params;
    private BroadcastReceiver screenOffBroadcast = new BroadcastReceiver() {
        private String screen_off = "android.intent.action.SCREEN_OFF";

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(this.screen_off)) {
                Log.e(MainActivity.this.TAG, "onReceive: PowerKey pressed");
                try {
                    if (((BitmapDrawable) MainActivity.ivScan.getDrawable()).getBitmap().equals(((BitmapDrawable) MainActivity.this.getResources().getDrawable(R.drawable.ic_camera_50_on)).getBitmap())) {
                        Log.e(MainActivity.this.TAG, "onReceive: scanFragment ON");
                        MainActivity.this.mADNACaptureFrag = new ADNACapture();
                        FragmentTransaction beginTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        beginTransaction.replace(R.id.view_fragment_content, MainActivity.this.mADNACaptureFrag);
                        beginTransaction.addToBackStack(null);
                        beginTransaction.commit();
                        return;
                    }
                    Log.e(MainActivity.this.TAG, "onReceive: scanFragment OFF");
                } catch (Exception unused) {
                }
            }
        }
    };

    /* access modifiers changed from: private */
    public void addContact(DataVo dataVo, Bitmap bitmap) {
        String str="";
        ArrayList arrayList = new ArrayList();
        Intent intent = new Intent("android.intent.action.INSERT", Contacts.CONTENT_URI);
        ContentValues contentValues = new ContentValues();
        contentValues.put("mimetype", "vnd.android.cursor.item/organization");
        contentValues.put("data1", dataVo.getNameCard().getOrg());
        arrayList.add(contentValues);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("mimetype", "vnd.android.cursor.item/email_v2");
        contentValues2.put("data2", Integer.valueOf(0));
        contentValues2.put("data3", dataVo.getNameCard().getTitle());
        contentValues2.put("data1", dataVo.getNameCard().getEmail());
        arrayList.add(contentValues2);
        for (int i = 0; i < dataVo.getNameCard().tel.size(); i++) {
            DataNameCardVo.Tel tel = (DataNameCardVo.Tel) dataVo.getNameCard().tel.get(i);
            switch (i) {
                case 0:
                    intent.putExtra("phone_type", tel.tel_type);
                    str = "phone";
                    break;
                case 1:
                    intent.putExtra("secondary_phone_type", tel.tel_type);
                    str = "secondary_phone";
                    break;
            }
            intent.putExtra(str, tel.tel_no);
        }
        if (bitmap != null) {
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("mimetype", "vnd.android.cursor.item/photo");
            contentValues3.put("data15", bitmapToByteArray(bitmap));
            arrayList.add(contentValues3);
            intent.putParcelableArrayListExtra("data", arrayList);
        }
        intent.putExtra("postal", ((DataNameCardVo.Addr) dataVo.getNameCard().addr.get(0)).addr_text);
        intent.putExtra("postal_type", ((DataNameCardVo.Addr) dataVo.getNameCard().addr.get(0)).addr_label);
        StringBuilder sb = new StringBuilder();
        sb.append(dataVo.getNameCard().getFname());
        sb.append(" ");
        sb.append(dataVo.getNameCard().getName());
        intent.putExtra("name", sb.toString());
        intent.putParcelableArrayListExtra("data", arrayList);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void addEvent(String str, String str2, String str3, long j, long j2) {
        Intent putExtra = new Intent("android.intent.action.INSERT").setData(Events.CONTENT_URI).putExtra("title", str).putExtra("eventLocation", str2).putExtra("beginTime", j).putExtra("endTime", j2).putExtra("description", str3);
        if (putExtra.resolveActivity(getPackageManager()) != null) {
            putExtra.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(putExtra);
        }
    }

    public static Bitmap getBitmapFromURL(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (IOException unused) {
            return null;
        }
    }

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

    private void getExtrasViewMode(Bundle bundle) {
        int i;
        if (bundle == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                i = extras.getInt("VIEW_MODE");
            } else {
                return;
            }
        } else {
            try {
                i = ((Integer) bundle.getSerializable("VIEW_MODE")).intValue();
            } catch (Exception unused) {
                setFragmentPage(0);
                return;
            } catch (Throwable th) {
                setFragmentPage(0);
                throw th;
            }
        }
        setFragmentPage(i);
    }

    private void getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
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
                MainActivity.mLatitude = location.getLatitude();
                MainActivity.mLongitude = location.getLongitude();
                String a = MainActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("locationListener >>> lat=");
                sb.append(MainActivity.mLatitude);
                sb.append(", lon=");
                sb.append(MainActivity.mLongitude);
                Log.d(a, sb.toString());
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
            locationManager2.requestLocationUpdates("gps", 0, 0.0f, r6);
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

    private void initADNA() {
        String string = getResources().getString(R.string.adna_app_id);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setApiKey(string);
    }

    private void readCoordinate(long j, long j2, String str) {
        this.mApiClient.getScan(String.valueOf(j), String.valueOf(j2), str);
    }

    private void registerBroadcast() {
        registerReceiver(this.screenOffBroadcast, new IntentFilter("android.intent.action.SCREEN_OFF"));
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
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.MainActivity.selectNavigation(int):void");
    }

    /* access modifiers changed from: private */
    public void setFragmentPage(int i) {
        Fragment fragment;
        int i2=0;
        if (i == R.id.bt_history) {
            this.f2893m.setVisibility(View.GONE);
            setOrgBrightness(this.orgBrightness);
            fragment = new HistoryFragment();
            i2 = 1;
        } else if (i != R.id.bt_mypages) {
            if (i == R.id.bt_scan) {
                this.f2893m.setVisibility(View.VISIBLE);
                if (!((BitmapDrawable) ivScan.getDrawable()).getBitmap().equals(((BitmapDrawable) getResources().getDrawable(R.drawable.ic_camera_50_on)).getBitmap())) {
                    fragment = new ADNACapture();
                    this.mADNACaptureFrag = (ADNACapture) fragment;
                    this.mApiClient.setADNAListener(this.f2894n);
                    selectNavigation(0);
                    setOrgBrightness(1.0f);
                } else {
                    return;
                }
            } else if (i != R.id.bt_setting) {
                fragment = null;
            } else {
                this.f2893m.setVisibility(View.GONE);
                setOrgBrightness(this.orgBrightness);
                fragment = new SettingFragment();
                i2 = 2;
            }
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.view_fragment_content, fragment);
            beginTransaction.addToBackStack(null);
            beginTransaction.commit();
        } else {
            this.f2893m.setVisibility(View.GONE);
            setOrgBrightness(this.orgBrightness);
           // if (GlobalVar.USER_ACCOUNT.equals("")) {
                if (GlobalVar.USER_ACCOUNT.equals("")) {
                fragment = new MypagesFragment();
            } else {
                startActivity(new Intent(this, MyStickerList1Activity.class));
                overridePendingTransition(0, 0);
                fragment = null;
            }
            i2 = 3;
        }
        selectNavigation(i2);
        try {
            FragmentTransaction beginTransaction2 = getSupportFragmentManager().beginTransaction();
            beginTransaction2.replace(R.id.view_fragment_content, fragment);
            beginTransaction2.addToBackStack(null);
            beginTransaction2.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOrgBrightness(float f) {
        this.params.screenBrightness = f;
        getWindow().setAttributes(this.params);
    }

    private void startTimer() {
        this.mTimerHandler.sendEmptyMessageDelayed(1, 2000);
    }

    private void unregisterBroadcast() {
        unregisterReceiver(this.screenOffBroadcast);
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo9933c() {
        if (!this.mIsBackKeyPressed) {
            this.mIsBackKeyPressed = true;
            this.mCurTimeInMillis = Calendar.getInstance().getTimeInMillis();
            startTimer();
            Toast.makeText(this, R.string.txt_exit, Toast.LENGTH_SHORT).show();
            return;
        }
        this.mIsBackKeyPressed = false;
        if (Calendar.getInstance().getTimeInMillis() <= this.mCurTimeInMillis + 2000) {
            ActivityCompat.finishAffinity(this);
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
    }

    public void getEusrAccountInfo() {
      //  GlobalVar.USER_ACCOUNT = getSharedPreferences("eusr_info", 0).getString("user_email", "");
        GlobalVar.USER_ACCOUNT = getSharedPreferences("eusr_info", 0).getString("user_email", "");
    }

    public void onBackPressed() {
        mo9933c();
    }

    public void onCoordinateEvent(long j, long j2, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("onCoordinateEvent: ");
        sb.append(j);
        sb.append(", ");
        sb.append(j2);
        sb.append(", ");
        sb.append(str);
        DevLog.defaultLogging(sb.toString());
        readCoordinate(j, j2, str);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        mainActivity = this;
        try {
            this.params = getWindow().getAttributes();
            this.orgBrightness = this.params.screenBrightness;
        } catch (Exception unused) {
        }
        synchronized (this) {
            getMyLocation();
        }
        this.f2893m = (LinearLayout) findViewById(R.id.lay_titlebar);
        ivScan = (ImageView) findViewById(R.id.iv_scan);
        ivHistory = (ImageView) findViewById(R.id.iv_history);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        ivMypages = (ImageView) findViewById(R.id.iv_mypages);
        findViewById(R.id.bt_scan).setOnClickListener(this.btClickListener);
        findViewById(R.id.bt_history).setOnClickListener(this.btClickListener);
        findViewById(R.id.bt_setting).setOnClickListener(this.btClickListener);
        findViewById(R.id.bt_mypages).setOnClickListener(this.btClickListener);
        scanContent();
        registerBroadcast();
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2894n);
        selectNavigation(0);
        getEusrAccountInfo();
        getExtrasViewMode(bundle);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    public void onFailedToReceiveADNA(int i, String str) {
        DevLog.defaultLogging("onFailedToReceiveADNA....");
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        if (mCurTabPos == 0) {
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
    }

    public void onResume() {
        super.onResume();
     //   CrashManager.register(getApplicationContext());
        this.mApiClient = ADNAClient.getInstance(this);
        try {
            this.params = getWindow().getAttributes();
            this.orgBrightness = this.params.screenBrightness;
        } catch (Exception unused) {
        }
        setOrgBrightness(this.orgBrightness);
    }

    public void onScanMultiData(ArrayList<DataVo> arrayList) {
        Intent intent = new Intent(this, DataListActivity.class);
        intent.putExtra("company_id", ((DataVo) arrayList.get(0)).getCompany_id());
        intent.putExtra("data_info_arr", arrayList);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onScanResultImage(DataVo dataVo) {
        DevLog.defaultLogging("onScanResultImage....");
        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
        intent.putExtra("data_info", dataVo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onScanResultMP3(DataVo dataVo) {
        DevLog.defaultLogging("onScanResultMP3....");
        Intent intent = new Intent(getApplicationContext(), MP3Activity.class);
        intent.putExtra("data_info", dataVo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onScanResultNameCard(final DataVo dataVo) {
        DevLog.defaultLogging("onScanResultNameCard....");
        Picasso.get().load(dataVo.getNameCard().getPhoto()).into((Target) new Target() {
            public void onBitmapFailed(Exception exc, Drawable drawable) {
                DevLog.defaultLogging("onBitmapFailed...");
                MainActivity.this.addContact(dataVo, null);
            }

            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
                DevLog.defaultLogging("loaded bitmap is here (bitmap");
                MainActivity.this.addContact(dataVo, bitmap);
            }

            public void onPrepareLoad(Drawable drawable) {
            }
        });
    }

    public void onScanResultSchedule(DataVo dataVo) {
        DevLog.defaultLogging("onScanResultSchedule....");
        addEvent(dataVo.getEvent().title, dataVo.getEvent().event_loc, dataVo.getEvent().description, getDate(dataVo.getEvent().start_dt), getDate(dataVo.getEvent().end_dt));
    }

    public void onScanResultText(DataVo dataVo) {
        DevLog.defaultLogging("onScanResultText....");
        Intent intent = new Intent(getApplicationContext(), TextActivity.class);
        intent.putExtra("data_info", dataVo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onScanResultVideo(DataVo dataVo) {
        DevLog.defaultLogging("onScanResultVideo....");
        Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
        intent.putExtra("data_info", dataVo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onScanResultWeb(DataVo dataVo) {
        DevLog.defaultLogging("onScanResultWeb....");
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("data_info", dataVo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }
}
