package com.pqiorg.multitracker.anoto.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
/*import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.DataPatternVo;
import com.anoto.adna.ServerApi.api.object.EusrPtrnPageObject.EusrPtrnPageData;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.fragments.MypagesFragment;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.MyProgress;
import com.anoto.adna.util.PermissionUtil;*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.appevents.AppEventsConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataPatternVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.EusrPtrnPageObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.fragments.MypagesFragment;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;
import com.rabbitmq.client.ConnectionFactory;
import java.util.ArrayList;

public class MyStickerList1Activity extends AppCompatActivity implements OnClickListener {
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    public static final String TAG = "MyStickerList1Activity";
    private static ImageView ivHistory;
    private static ImageView ivMypages;
    private static ImageView ivScan;
    private static ImageView ivSetting;
    private static int mCurTabPos;
    /* access modifiers changed from: private */
    public String SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            MyStickerList1Activity.this.goMainActivity(view.getId());
        }
    };

    /* renamed from: m */
    MainActivity f2925m = MainActivity.mainActivity;
    /* access modifiers changed from: private */
    public RecyclerAdapter mAdapter;
    private ADNAClient mApiClient;
    /* access modifiers changed from: private */
    public ArrayList<DataPatternVo> mDataPtrnInfoArr = new ArrayList<>();
    private SettingManager mSettingManager;
    /* access modifiers changed from: private */
    public int multiSelectEnd = -1;
    /* access modifiers changed from: private */
    public Boolean multiSelectMode = Boolean.valueOf(false);
    /* access modifiers changed from: private */
    public int multiSelectStart = -1;

    /* renamed from: n */
    LinearLayout f2926n;

    /* renamed from: o */
    Button f2927o;

    /* renamed from: p */
    Button f2928p;

    /* renamed from: q */
    Button f2929q;

    /* renamed from: r */
    TextView f2930r;
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            MyStickerList1Activity.this.getMyPatternList();
            MyStickerList1Activity.this.vSwipeRefresh.setRefreshing(false);
        }
    };

    /* renamed from: s */
    ADNAListener f2931s = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            Toast.makeText(MyStickerList1Activity.this, str,  Toast.LENGTH_SHORT).show();
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 8) {
                EusrPtrnPageObject.EusrPtrnPageData eusrPtrnPageData = (EusrPtrnPageObject.EusrPtrnPageData) obj;
                MyStickerList1Activity.this.mDataPtrnInfoArr.clear();
                MyProgress.hides();
                DevLog.defaultLogging("My Stickers List1 onSuccess.");
                MyStickerList1Activity.this.sSelectedItems.clear();
                try {
                    MyStickerList1Activity.this.mDataPtrnInfoArr = eusrPtrnPageData.data.page_address_list;
                    for (int i2 = 0; i2 < MyStickerList1Activity.this.mDataPtrnInfoArr.size(); i2++) {
                        MyStickerList1Activity.this.sSelectedItems.add(i2, Boolean.valueOf(false));
                    }
                } catch (Exception e) {
                    e.getMessage().toString();
                }
                MyStickerList1Activity.this.mAdapter.notifyDataSetChanged();
                MyStickerList1Activity.this.mAdapter.getItemCount();
            }
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<Boolean> sSelectedItems = new ArrayList<>();
    /* access modifiers changed from: private */
    public int selectCnt = 0;
    private Context thiz = null;
    /* access modifiers changed from: private */
    public RecyclerView vList;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout vSwipeRefresh;

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
        private LayoutInflater inflater;
        /* access modifiers changed from: private */
        public Context sContext;

        class ItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener {

            /* renamed from: m */
            LinearLayout f2940m;

            /* renamed from: n */
            TextView f2941n;

            /* renamed from: o */
            TextView f2942o;

            public ItemViewHolder(View view) {
                super(view);
                this.f2940m = (LinearLayout) view.findViewById(R.id.v_item);
                this.f2941n = (TextView) view.findViewById(R.id.txt_page_address);
                this.f2942o = (TextView) view.findViewById(R.id.txt_reg_date);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
            }

            public void onClick(View view) {
                if (MyStickerList1Activity.this.SELECT_MODE.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    MyStickerList1Activity.this.goMyStickerlist2(((DataPatternVo) MyStickerList1Activity.this.mDataPtrnInfoArr.get(getAdapterPosition())).getPage_address());
                    return;
                }
                if (MyStickerList1Activity.this.SELECT_MODE.equals("1")) {
                    if (this.f2940m.isSelected()) {
                        MyStickerList1Activity.this.sSelectedItems.set(getAdapterPosition(), Boolean.valueOf(false));
                        this.f2940m.setSelected(false);
                    } else {
                        MyStickerList1Activity.this.sSelectedItems.set(getAdapterPosition(), Boolean.valueOf(true));
                        this.f2940m.setSelected(true);
                    }
                    MyStickerList1Activity.this.multiSelectMode = Boolean.valueOf(false);
                    MyStickerList1Activity.this.mAdapter.notifyDataSetChanged();
                }
            }

            public boolean onLongClick(View view) {
                if (MyStickerList1Activity.this.SELECT_MODE.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    MyStickerList1Activity.this.SELECT_MODE = "1";
                    MyStickerList1Activity.this.sSelectedItems.set(getAdapterPosition(), Boolean.valueOf(true));
                    this.f2940m.setSelected(true);
                    MyStickerList1Activity.this.f2928p.setBackgroundResource(R.drawable.new_blue);
                    MyStickerList1Activity.this.f2928p.setEnabled(true);
                    MyStickerList1Activity.this.f2929q.setBackgroundResource(R.drawable.new_grey);
                    MyStickerList1Activity.this.f2929q.setEnabled(false);
                    MyStickerList1Activity.this.f2926n.setBackgroundColor(MyStickerList1Activity.this.getResources().getColor(R.color.bg_green));
                    MyStickerList1Activity.this.multiSelectMode = Boolean.valueOf(true);
                    MyStickerList1Activity.this.multiSelectStart = getAdapterPosition();
                    return true;
                }
                MyStickerList1Activity.this.multiSelectEnd = getAdapterPosition();
                if (MyStickerList1Activity.this.multiSelectStart >= 0) {
                    if (MyStickerList1Activity.this.multiSelectEnd > MyStickerList1Activity.this.multiSelectStart) {
                        for (int g = MyStickerList1Activity.this.multiSelectStart; g < MyStickerList1Activity.this.multiSelectEnd + 1; g++) {
                            MyStickerList1Activity.this.sSelectedItems.set(g, Boolean.valueOf(true));
                        }
                    } else if (MyStickerList1Activity.this.multiSelectStart <= MyStickerList1Activity.this.multiSelectEnd && MyStickerList1Activity.this.multiSelectStart == MyStickerList1Activity.this.multiSelectEnd) {
                        MyStickerList1Activity.this.sSelectedItems.set(MyStickerList1Activity.this.multiSelectStart, Boolean.valueOf(false));
                    }
                    MyStickerList1Activity.this.multiSelectMode = Boolean.valueOf(false);
                }
                MyStickerList1Activity.this.mAdapter.notifyDataSetChanged();
                return true;
            }
        }

        RecyclerAdapter(ArrayList<DataPatternVo> arrayList, Context context) {
            this.sContext = context;
        }

        public int getItemCount() {
            MyStickerList1Activity.this.selectCnt = 0;
            if (MyStickerList1Activity.this.mDataPtrnInfoArr == null || MyStickerList1Activity.this.mDataPtrnInfoArr.size() <= 0) {
                MyStickerList1Activity.this.f2928p.setVisibility(View.INVISIBLE);
                MyStickerList1Activity.this.f2929q.setVisibility(View.INVISIBLE);
                MyStickerList1Activity.this.vList.setVisibility(View.INVISIBLE);
                MyStickerList1Activity.this.f2930r.setVisibility(View.VISIBLE);
            } else {
                MyStickerList1Activity.this.f2928p.setVisibility(View.VISIBLE);
                MyStickerList1Activity.this.f2929q.setVisibility(View.VISIBLE);
                MyStickerList1Activity.this.vList.setVisibility(View.VISIBLE);
                for (int i = 0; i < MyStickerList1Activity.this.mDataPtrnInfoArr.size(); i++) {
                    if (((Boolean) MyStickerList1Activity.this.sSelectedItems.get(i)).booleanValue()) {
                        MyStickerList1Activity.this.selectCnt = MyStickerList1Activity.this.selectCnt + 1;
                        MyStickerList1Activity.this.multiSelectStart = i;
                    }
                }
                if (MyStickerList1Activity.this.selectCnt > 0) {
                    MyStickerList1Activity.this.f2928p.setEnabled(true);
                    MyStickerList1Activity.this.f2928p.setBackgroundResource(R.drawable.new_blue);
                    MyStickerList1Activity.this.f2929q.setEnabled(false);
                    MyStickerList1Activity.this.f2929q.setBackgroundResource(R.drawable.new_grey);
                } else {
                    MyStickerList1Activity.this.f2928p.setEnabled(false);
                    MyStickerList1Activity.this.f2928p.setBackgroundResource(R.drawable.new_grey);
                    MyStickerList1Activity.this.f2929q.setEnabled(true);
                    MyStickerList1Activity.this.f2929q.setBackgroundResource(R.drawable.new_blue);
                    MyStickerList1Activity.this.f2926n.setBackground(MyStickerList1Activity.this.getResources().getDrawable(R.drawable.bg_title));
                    MyStickerList1Activity.this.SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
                    MyStickerList1Activity.this.multiSelectStart = -1;
                }
                MyStickerList1Activity.this.f2930r.setVisibility(View.INVISIBLE);
            }
            return MyStickerList1Activity.this.mDataPtrnInfoArr.size();
        }

        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            TextView textView = itemViewHolder.f2941n;
            StringBuilder sb = new StringBuilder();
            sb.append(((DataPatternVo) MyStickerList1Activity.this.mDataPtrnInfoArr.get(i)).getPage_address());
            sb.append(" ( ");
            sb.append(((DataPatternVo) MyStickerList1Activity.this.mDataPtrnInfoArr.get(i)).getUsable_area_cnt());
            sb.append(ConnectionFactory.DEFAULT_VHOST);
            sb.append(((DataPatternVo) MyStickerList1Activity.this.mDataPtrnInfoArr.get(i)).getArea_cnt());
            sb.append(" )");
            textView.setText(sb.toString());
            itemViewHolder.f2942o.setText(((DataPatternVo) MyStickerList1Activity.this.mDataPtrnInfoArr.get(i)).getReg_dt());
            itemViewHolder.f2940m.setSelected(((Boolean) MyStickerList1Activity.this.sSelectedItems.get(i)).booleanValue());
        }

        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_ptrn_item, viewGroup, false));
        }
    }

    private void finishApp() {
        this.f2925m.mo9933c();
    }

    /* access modifiers changed from: private */
    public void getMyPatternList() {
        if (!PermissionUtil.isNetworkConnect(this)) {
            Toast.makeText(this, R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(this);
        if (this.mApiClient != null) {
            this.mApiClient.getEusrPtrnPage(BasicUtil.getUserAccount(this));
        }
    }

    /* access modifiers changed from: private */
    public void goMainActivity(int i) {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("VIEW_MODE", i);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } catch (Exception unused) {
        }
    }

    private void initADNA() {
        String string = getResources().getString(R.string.server_address);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setServerURL(string);
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2931s);
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
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.MyStickerList1Activity.selectNavigation(int):void");
    }

    public void confirmReg() {
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
                MyStickerList1Activity.this.goRegLink();
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

    public void getEusrAccountInfo() {
        GlobalVar.USER_ACCOUNT = getSharedPreferences("eusr_info", 0).getString("user_email", "");
      //  BeaconReferenceApplication.USER_ACCOUNT = getSharedPreferences("eusr_info", 0).getString("user_email", "");
    }

    public void goMyStickerlist2(String str) {
        try {
            Intent intent = new Intent(this, MyStickerList2Activity.class);
            intent.putExtra("PAGE_ADDRESS", str);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } catch (Exception unused) {
        }
    }

    public void goRegLink() {
        boolean z;
        String string;
        String str = "";
        int i = 0;
        int i2 = 0;
        while (true) {
            try {
                if (i >= this.mDataPtrnInfoArr.size()) {
                    z = true;
                    break;
                }
                if (((Boolean) this.sSelectedItems.get(i)).booleanValue()) {
                    if (!((DataPatternVo) this.mDataPtrnInfoArr.get(i)).getUsable_area_cnt().equals(((DataPatternVo) this.mDataPtrnInfoArr.get(i)).getArea_cnt())) {
                        z = false;
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(((DataPatternVo) this.mDataPtrnInfoArr.get(i)).getPage_address());
                    sb.append(",");
                    str = sb.toString();
                    i2 += Integer.parseInt(((DataPatternVo) this.mDataPtrnInfoArr.get(i)).getUsable_area_cnt());
                }
                i++;
            } catch (Exception unused) {
                return;
            }
        }
        if (i2 == 0) {
            string = getResources().getString(R.string.txt_msg_no_reg_link);
        } else if (z) {
            Intent intent = new Intent(this, LinkRegActivity.class);
            intent.putExtra("PAGE_ADDRESS", str.substring(0, str.length() - 1));
            intent.putExtra("PTRN_COORDS_ID", "");
            intent.putExtra("QUANTITY", String.valueOf(i2));
            intent.putExtra("INDEX_LIST", "");
            intent.putExtra("PRE_PAGE", TAG);
            startActivity(intent);
            return;
        } else {
            string = getResources().getString(R.string.txt_msg_no_reg_link);
        }
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void init() {
        this.f2926n = (LinearLayout) findViewById(R.id.title);
        this.f2927o = (Button) findViewById(R.id.btn_add_sticker);
        this.f2928p = (Button) findViewById(R.id.btn_link_reg);
        this.f2929q = (Button) findViewById(R.id.btn_link_scan);
        this.f2930r = (TextView) findViewById(R.id.lv_empty);
        this.f2927o.setOnClickListener(this);
        this.f2928p.setOnClickListener(this);
        this.f2929q.setOnClickListener(this);
        this.f2928p.setVisibility(View.INVISIBLE);
        this.f2929q.setVisibility(View.INVISIBLE);
        this.mAdapter = new RecyclerAdapter(this.mDataPtrnInfoArr, this);
        this.vSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.lv_refresh);
        this.vSwipeRefresh.setEnabled(false);
        this.vList = (RecyclerView) findViewById(R.id.lv_stickers);
        this.vList.setAdapter(this.mAdapter);
        getMyPatternList();
    }

    public void onClick(View view) {
        Intent intent=null;
        String str="";
        String str2="";
        int id = view.getId();
        if (id == R.id.btn_add_sticker) {
            intent = new Intent(this, MyScanActivity.class);
            str2 = "SCAN_TYPE";
            str = MypagesFragment.SCAN_TYPE_ADD_FLAG;
        } else if (id == R.id.btn_link_reg) {
            if (this.SELECT_MODE.equals("1")) {
                confirmReg();
                return;
            }
        } else if (id == R.id.btn_link_scan) {
            intent = new Intent(this, MyScanActivity.class);
            str2 = "SCAN_TYPE";
            str = MypagesFragment.SCAN_TYPE_LINK_FLAG;
        } else {
            return;
        }
        intent.putExtra(str2, str);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_sticker_list1);
        init_bottomMenu();
        if (GlobalVar.USER_ACCOUNT.equals("")) {
          //  if (BeaconReferenceApplication.USER_ACCOUNT.equals("")) {
            getEusrAccountInfo();
        }
        initADNA();
        init();
        this.thiz = this;
        enableBottomMenu();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0 || i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.SELECT_MODE.equals("1")) {
            this.SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
            this.selectCnt = 0;
            this.f2926n.setBackground(getResources().getDrawable(R.drawable.bg_title));
            int itemCount = this.mAdapter.getItemCount();
            ArrayList<Boolean> arrayList = ((MyStickerList1Activity) this.mAdapter.sContext).sSelectedItems;
            for (int i2 = 0; i2 < itemCount; i2++) {
                if (((Boolean) this.sSelectedItems.get(i2)).booleanValue()) {
                    this.sSelectedItems.set(i2, Boolean.valueOf(false));
                }
            }
            this.mAdapter.notifyDataSetChanged();
            return false;
        }
        finishApp();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (GlobalVar.USER_ACCOUNT.equals("")) {
       // if (BeaconReferenceApplication.USER_ACCOUNT.equals("")) {
            getEusrAccountInfo();
        }
        initADNA();
        this.SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.selectCnt = 0;
        this.f2928p.setBackgroundResource(R.drawable.new_grey);
        this.f2928p.setEnabled(false);
        this.f2929q.setBackgroundResource(R.drawable.new_blue);
        this.f2929q.setEnabled(true);
        this.f2926n.setBackground(getResources().getDrawable(R.drawable.bg_title));
        getMyPatternList();
    }
}
