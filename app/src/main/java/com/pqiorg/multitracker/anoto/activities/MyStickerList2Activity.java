package com.pqiorg.multitracker.anoto.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
/*import android.support.p000v4.content.ContextCompat;
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
import com.anoto.adna.ServerApi.api.object.DataPatternAreaVo;
import com.anoto.adna.ServerApi.api.object.EusrPtrnPageAreaObject.EusrPtrnPageAreaData;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.fragments.MypagesFragment;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.MyProgress;
import com.anoto.adna.util.PermissionUtil;*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.ShareConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataPatternAreaVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.EusrPtrnPageAreaObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.fragments.MypagesFragment;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;

import java.util.ArrayList;

public class MyStickerList2Activity extends AppCompatActivity implements OnClickListener {
    public static final int TAB_HISTORY = 1;
    public static final int TAB_MYPAGES = 3;
    public static final int TAB_SCAN = 0;
    public static final int TAB_SETTING = 2;
    public static final String TAG = "MyStickerList2Activity";
    private static ImageView ivHistory;
    private static ImageView ivMypages;
    private static ImageView ivScan;
    private static ImageView ivSetting;
    private static int mCurTabPos;
    /* access modifiers changed from: private */
    public String SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            MyStickerList2Activity.this.goMainActivity(view.getId());
        }
    };

    /* renamed from: m */
    LinearLayout f2944m;
    /* access modifiers changed from: private */
    public RecyclerAdapter mAdapter;
    private ADNAClient mApiClient;
    /* access modifiers changed from: private */
    public ArrayList<DataPatternAreaVo> mDataPtrnAreaInfoArr = new ArrayList<>();
    /* access modifiers changed from: private */
    public int multiSelectEnd = -1;
    /* access modifiers changed from: private */
    public Boolean multiSelectMode = Boolean.valueOf(false);
    /* access modifiers changed from: private */
    public int multiSelectStart = -1;

    /* renamed from: n */
    Button f2945n;

    /* renamed from: o */
    Button f2946o;

    /* renamed from: p */
    Button f2947p;

    /* renamed from: q */
    Button f2948q;

    /* renamed from: r */
    TextView f2949r;

    /* renamed from: s */
    String f2950s;
    /* access modifiers changed from: private */
    public ArrayList<Boolean> sSelectedItems = new ArrayList<>();
    /* access modifiers changed from: private */
    public int selectCnt = 0;


    /* renamed from: t */
    ADNAListener f2951t = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            Toast.makeText(MyStickerList2Activity.this, str,  Toast.LENGTH_SHORT).show();
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 9) {
                EusrPtrnPageAreaObject.EusrPtrnPageAreaData eusrPtrnPageAreaData = (EusrPtrnPageAreaObject.EusrPtrnPageAreaData) obj;
                MyStickerList2Activity.this.mDataPtrnAreaInfoArr.clear();
                MyProgress.hides();
                DevLog.defaultLogging("My Stickers List2 onSuccess.");
                MyStickerList2Activity.this.sSelectedItems.clear();
                try {
                    MyStickerList2Activity.this.mDataPtrnAreaInfoArr = eusrPtrnPageAreaData.data.ptrn_area_list;
                    for (int i2 = 0; i2 < MyStickerList2Activity.this.mDataPtrnAreaInfoArr.size(); i2++) {
                        MyStickerList2Activity.this.sSelectedItems.add(i2, Boolean.valueOf(false));
                    }
                } catch (Exception e) {
                    e.getMessage().toString();
                }
                MyStickerList2Activity.this.mAdapter.notifyDataSetChanged();
                MyStickerList2Activity.this.mAdapter.getItemCount();
            }
        }
    };
    private Context thiz = null;
    /* access modifiers changed from: private */
    public RecyclerView vList;

    public class RecyclerAdapter extends RecyclerView.Adapter<MyStickerList2Activity.RecyclerAdapter.ItemViewHolder> {
        private LayoutInflater inflater;
        /* access modifiers changed from: private */
        public Context sContext;

        class ItemViewHolder extends RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener {

            /* renamed from: m */
            LinearLayout f2959m;

            /* renamed from: n */
            TextView f2960n;

            /* renamed from: o */
            TextView f2961o;

            /* renamed from: p */
            TextView f2962p;

            public ItemViewHolder(View view) {
                super(view);
                this.f2959m = (LinearLayout) view.findViewById(R.id.v_item);
                this.f2960n = (TextView) view.findViewById(R.id.txt_ptrn_index);
                this.f2961o = (TextView) view.findViewById(R.id.txt_ptrn_coords_name);
                this.f2962p = (TextView) view.findViewById(R.id.txt_curl);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
            }

            public void onClick(View view) {
                if (!MyStickerList2Activity.this.SELECT_MODE.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    if (MyStickerList2Activity.this.SELECT_MODE.equals("1")) {
                        if (this.f2959m.isSelected()) {
                            MyStickerList2Activity.this.sSelectedItems.set(getAdapterPosition(), Boolean.valueOf(false));
                            this.f2959m.setSelected(false);
                        } else {
                            MyStickerList2Activity.this.sSelectedItems.set(getAdapterPosition(), Boolean.valueOf(true));
                            this.f2959m.setSelected(true);
                        }
                        MyStickerList2Activity.this.multiSelectMode = Boolean.valueOf(false);
                        MyStickerList2Activity.this.mAdapter.notifyDataSetChanged();
                    }
                } else if (((DataPatternAreaVo) MyStickerList2Activity.this.mDataPtrnAreaInfoArr.get(getAdapterPosition())).getLink_dt().equals("")) {
                    MyStickerList2Activity.this.selectCnt = MyStickerList2Activity.this.selectCnt + 1;
                    MyStickerList2Activity.this.goRegSingleLink((DataPatternAreaVo) MyStickerList2Activity.this.mDataPtrnAreaInfoArr.get(getAdapterPosition()));
                } else {
                    MyStickerList2Activity.this.goLinkDet((DataPatternAreaVo) MyStickerList2Activity.this.mDataPtrnAreaInfoArr.get(getAdapterPosition()));
                }
            }

            public boolean onLongClick(View view) {
                if (MyStickerList2Activity.this.SELECT_MODE.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    MyStickerList2Activity.this.SELECT_MODE = "1";
                    MyStickerList2Activity.this.sSelectedItems.set(getAdapterPosition(), Boolean.valueOf(true));
                    this.f2959m.setSelected(true);
                    MyStickerList2Activity.this.f2947p.setBackgroundResource(R.drawable.new_blue);
                    MyStickerList2Activity.this.f2947p.setEnabled(true);
                    MyStickerList2Activity.this.f2948q.setBackgroundResource(R.drawable.new_grey);
                    MyStickerList2Activity.this.f2948q.setEnabled(false);
                    MyStickerList2Activity.this.f2944m.setBackgroundColor(MyStickerList2Activity.this.getResources().getColor(R.color.bg_green));
                    MyStickerList2Activity.this.multiSelectMode = Boolean.valueOf(true);
                    MyStickerList2Activity.this.multiSelectStart = getAdapterPosition();
                    return true;
                }
                MyStickerList2Activity.this.multiSelectEnd = getAdapterPosition();
                if (MyStickerList2Activity.this.multiSelectStart >= 0) {
                    if (MyStickerList2Activity.this.multiSelectEnd > MyStickerList2Activity.this.multiSelectStart) {
                        for (int f = MyStickerList2Activity.this.multiSelectStart; f < MyStickerList2Activity.this.multiSelectEnd + 1; f++) {
                            MyStickerList2Activity.this.sSelectedItems.set(f, Boolean.valueOf(true));
                        }
                    } else if (MyStickerList2Activity.this.multiSelectStart <= MyStickerList2Activity.this.multiSelectEnd && MyStickerList2Activity.this.multiSelectStart == MyStickerList2Activity.this.multiSelectEnd) {
                        MyStickerList2Activity.this.sSelectedItems.set(MyStickerList2Activity.this.multiSelectStart, Boolean.valueOf(false));
                    }
                    MyStickerList2Activity.this.multiSelectMode = Boolean.valueOf(false);
                }
                MyStickerList2Activity.this.mAdapter.notifyDataSetChanged();
                return true;
            }
        }

        RecyclerAdapter(ArrayList<DataPatternAreaVo> arrayList, Context context) {
            this.sContext = context;
        }

        public int getItemCount() {
            MyStickerList2Activity.this.selectCnt = 0;
            if (MyStickerList2Activity.this.mDataPtrnAreaInfoArr == null || MyStickerList2Activity.this.mDataPtrnAreaInfoArr.size() <= 0) {
                MyStickerList2Activity.this.f2947p.setVisibility(View.INVISIBLE);
                MyStickerList2Activity.this.f2948q.setVisibility(View.INVISIBLE);
                MyStickerList2Activity.this.vList.setVisibility(View.INVISIBLE);
            } else {
                MyStickerList2Activity.this.f2947p.setVisibility(View.VISIBLE);
                MyStickerList2Activity.this.f2948q.setVisibility(View.VISIBLE);
                MyStickerList2Activity.this.vList.setVisibility(View.VISIBLE);
                for (int i = 0; i < MyStickerList2Activity.this.mDataPtrnAreaInfoArr.size(); i++) {
                    if (((Boolean) MyStickerList2Activity.this.sSelectedItems.get(i)).booleanValue()) {
                        MyStickerList2Activity.this.selectCnt = MyStickerList2Activity.this.selectCnt + 1;
                        MyStickerList2Activity.this.multiSelectStart = i;
                    }
                }
                if (MyStickerList2Activity.this.selectCnt > 0) {
                    MyStickerList2Activity.this.f2947p.setEnabled(true);
                    MyStickerList2Activity.this.f2947p.setBackgroundResource(R.drawable.new_blue);
                    MyStickerList2Activity.this.f2948q.setEnabled(false);
                    MyStickerList2Activity.this.f2948q.setBackgroundResource(R.drawable.new_grey);
                } else {
                    MyStickerList2Activity.this.f2947p.setEnabled(false);
                    MyStickerList2Activity.this.f2947p.setBackgroundResource(R.drawable.new_grey);
                    MyStickerList2Activity.this.f2948q.setEnabled(true);
                    MyStickerList2Activity.this.f2948q.setBackgroundResource(R.drawable.new_blue);
                    MyStickerList2Activity.this.f2944m.setBackground(MyStickerList2Activity.this.getResources().getDrawable(R.drawable.bg_title));
                    MyStickerList2Activity.this.SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
                    MyStickerList2Activity.this.multiSelectStart = -1;
                }
            }
            return MyStickerList2Activity.this.mDataPtrnAreaInfoArr.size();
        }

        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            TextView textView;
            String str;
            DataPatternAreaVo dataPatternAreaVo = (DataPatternAreaVo) MyStickerList2Activity.this.mDataPtrnAreaInfoArr.get(i);
            TextView textView2 = itemViewHolder.f2960n;
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(dataPatternAreaVo.getIndex_in_page());
            textView2.setText(sb.toString());
            if (!dataPatternAreaVo.getPtrn_coords_name().equals("")) {
                textView = itemViewHolder.f2961o;
                str = dataPatternAreaVo.getPtrn_coords_name();
            } else {
                textView = itemViewHolder.f2961o;
                str = "";
            }
            textView.setText(str);
            if (!dataPatternAreaVo.getCurl().equals("")) {
                itemViewHolder.f2962p.setText(dataPatternAreaVo.getCurl());
            } else {
                itemViewHolder.f2962p.setText(MyStickerList2Activity.this.getResources().getString(R.string.txt_empty_url));
            }
            itemViewHolder.f2959m.setSelected(((Boolean) MyStickerList2Activity.this.sSelectedItems.get(i)).booleanValue());
        }

        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_ptrn_area_item, viewGroup, false));
        }
    }

    private void getMyPatternAreaList() {
        if (!PermissionUtil.isNetworkConnect(this)) {
            Toast.makeText(this, R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(this);
        if (this.mApiClient != null) {
            this.mApiClient.getEusrPtrnPageArea(BasicUtil.getUserAccount(this), this.f2950s);
        }
    }

    /* access modifiers changed from: private */
    public void goMainActivity(int i) {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("VIEW_MODE", i);
            startActivity(intent);
        } catch (Exception unused) {
        }
    }

    private void initADNA() {
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this.f2951t);
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
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.MyStickerList2Activity.selectNavigation(int):void");
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
                MyStickerList2Activity.this.goRegLink();
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

    public void goLinkDet(DataPatternAreaVo dataPatternAreaVo) {
        try {
            Intent intent = new Intent(this, LinkDetActivity.class);
            intent.putExtra("PAGE_ADDRESS", dataPatternAreaVo.getPage_address());
            intent.putExtra("INDEX", dataPatternAreaVo.getIndex_in_page());
            intent.putExtra("REG_DATE", dataPatternAreaVo.getReg_dt());
            intent.putExtra(ShareConstants.TITLE, dataPatternAreaVo.getPtrn_coords_name());
            intent.putExtra("LINK_TYPE", dataPatternAreaVo.getCtype());
            intent.putExtra("LINK_URL", dataPatternAreaVo.getCurl());
            intent.putExtra("LINK_DATE", dataPatternAreaVo.getLink_dt());
            intent.putExtra("EXPIRE", dataPatternAreaVo.getLink_expire_dt());
            startActivity(intent);
        } catch (Exception unused) {
        }
    }

    public void goRegLink() {
        String string;
        String str = "";
        String str2 = "";
        String str3 = "";
        int i = 0;
        int i2 = 0;
        boolean z = true;
        while (i < this.mDataPtrnAreaInfoArr.size()) {
            try {
                if (((Boolean) this.sSelectedItems.get(i)).booleanValue()) {
                    if (!((DataPatternAreaVo) this.mDataPtrnAreaInfoArr.get(i)).getCurl().equals("")) {
                        z = false;
                    } else {
                        str = ((DataPatternAreaVo) this.mDataPtrnAreaInfoArr.get(i)).getPage_address();
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(((DataPatternAreaVo) this.mDataPtrnAreaInfoArr.get(i)).getPtrn_coords_id());
                        sb.append(",");
                        str2 = sb.toString();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str3);
                        sb2.append(((DataPatternAreaVo) this.mDataPtrnAreaInfoArr.get(i)).getIndex_in_page());
                        sb2.append(",");
                        str3 = sb2.toString();
                        i2++;
                    }
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
            intent.putExtra("PAGE_ADDRESS", str);
            intent.putExtra("PTRN_COORDS_ID", str2.substring(0, str2.length() - 1));
            intent.putExtra("QUANTITY", String.valueOf(i2));
            intent.putExtra("INDEX_LIST", str3.substring(0, str3.length() - 1));
            intent.putExtra("PRE_PAGE", TAG);
            startActivity(intent);
            return;
        } else {
            string = getResources().getString(R.string.txt_msg_no_reg_link);
        }
        Toast.makeText(this, string,  Toast.LENGTH_SHORT).show();
    }

    public void goRegSingleLink(DataPatternAreaVo dataPatternAreaVo) {
        try {
            Intent intent = new Intent(this, LinkRegActivity.class);
            intent.putExtra("PAGE_ADDRESS", dataPatternAreaVo.getPage_address());
            intent.putExtra("PTRN_COORDS_ID", dataPatternAreaVo.getPtrn_coords_id());
            intent.putExtra("QUANTITY", String.valueOf(this.selectCnt));
            intent.putExtra("INDEX_LIST", dataPatternAreaVo.getIndex_in_page());
            intent.putExtra("PRE_PAGE", TAG);
            startActivity(intent);
        } catch (Exception unused) {
        }
    }

    public void init() {
        this.f2944m = (LinearLayout) findViewById(R.id.title);
        this.f2945n = (Button) findViewById(R.id.btn_back);
        this.f2946o = (Button) findViewById(R.id.btn_add_sticker);
        this.f2947p = (Button) findViewById(R.id.btn_link_reg);
        this.f2948q = (Button) findViewById(R.id.btn_link_scan);
        this.f2949r = (TextView) findViewById(R.id.txt_page_address);
        this.f2945n.setOnClickListener(this);
        this.f2946o.setOnClickListener(this);
        this.f2947p.setOnClickListener(this);
        this.f2948q.setOnClickListener(this);
        this.f2947p.setVisibility(View.INVISIBLE);
        this.f2948q.setVisibility(View.INVISIBLE);
        this.mAdapter = new RecyclerAdapter(this.mDataPtrnAreaInfoArr, this);
        this.vList = (RecyclerView) findViewById(R.id.lv_stickers);
        this.vList.setAdapter(this.mAdapter);
        getMyPatternAreaList();
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
        } else if (id == R.id.btn_back) {
            finish();
            return;
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
        setContentView((int) R.layout.activity_my_sticker_list2);
        init_bottomMenu();
        this.f2950s = getIntent().getStringExtra("PAGE_ADDRESS");
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
            this.f2944m.setBackground(getResources().getDrawable(R.drawable.bg_title));
            int itemCount = this.mAdapter.getItemCount();
            ArrayList<Boolean> arrayList = ((MyStickerList2Activity) this.mAdapter.sContext).sSelectedItems;
            for (int i2 = 0; i2 < itemCount; i2++) {
                if (((Boolean) this.sSelectedItems.get(i2)).booleanValue()) {
                    this.sSelectedItems.set(i2, Boolean.valueOf(false));
                }
            }
            this.mAdapter.notifyDataSetChanged();
            return false;
        }
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        initADNA();
        this.SELECT_MODE = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        this.selectCnt = 0;
        this.f2947p.setBackgroundResource(R.drawable.new_grey);
        this.f2947p.setEnabled(false);
        this.f2948q.setBackgroundResource(R.drawable.new_blue);
        this.f2948q.setEnabled(true);
        this.f2944m.setBackground(getResources().getDrawable(R.drawable.bg_title));
        getMyPatternAreaList();
    }
}
