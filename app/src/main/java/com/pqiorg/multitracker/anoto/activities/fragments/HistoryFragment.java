package com.pqiorg.multitracker.anoto.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.Contacts;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.DataNameCardVo.Addr;
import com.anoto.adna.ServerApi.api.object.DataNameCardVo.Tel;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.ServerApi.api.object.DeleteContentAccessObject.ContentAccessData;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.ImageActivity;
import com.anoto.adna.activities.MP3Activity;
import com.anoto.adna.activities.MainActivity;
import com.anoto.adna.activities.TextActivity;
import com.anoto.adna.activities.VideoActivity;
import com.anoto.adna.activities.WebActivity;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.MyProgress;
import com.anoto.adna.util.PermissionUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;*/
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataNameCardVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DeleteContentAccessObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.ImageActivity;
import com.pqiorg.multitracker.anoto.activities.MP3Activity;
import com.pqiorg.multitracker.anoto.activities.MainActivity;
import com.pqiorg.multitracker.anoto.activities.TextActivity;
import com.pqiorg.multitracker.anoto.activities.VideoActivity;
import com.pqiorg.multitracker.anoto.activities.WebActivity;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.MyProgress;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    public static final String TAG = "HistoryFragment";

    /* renamed from: a */
    ADNAListener f2993a = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            MyProgress.hides();
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            Toast.makeText(HistoryFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
        }

        public void onReceiveADNA(int i, Object obj) {
            switch (i) {
                case 6:
                    HistoryFragment.this.mDataInfoArr.clear();
                    MyProgress.hides();
                    DevLog.defaultLogging("ContentAccess onSuccess.");
                    HistoryFragment.this.mDataInfoArr = (ArrayList) obj;
                    HistoryFragment.this.mAdapter.notifyDataSetChanged();
                    HistoryFragment.this.mAdapter.getItemCount();
                    return;
                case 7:
                    MyProgress.hides();
                    DeleteContentAccessObject.ContentAccessData contentAccessData = (DeleteContentAccessObject.ContentAccessData) obj;
                    DevLog.defaultLogging("ContentAccess onSuccess.");
                    DevLog.defaultLogging(contentAccessData.result.toString());
                    HistoryFragment.this.getHistoryList();
                    return;
                default:
                    return;
            }
        }
    };
    private OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (view.getId() == R.id.bt_delete) {
                HistoryFragment.this.showDeleteDialog(-1);
            }
        }
    };
    /* access modifiers changed from: private */
    public View btDelete;
    /* access modifiers changed from: private */
    public OnClickListener itemClickListener = new OnClickListener() {
        public void onClick(View view) {
            Intent intent;
            int intValue = ((Integer) view.getTag()).intValue();
            String str = HistoryFragment.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("itemClickListener 0000 : ");
            sb.append(intValue);
            Log.e(str, sb.toString());
            final DataVo dataVo = (DataVo) HistoryFragment.this.mDataInfoArr.get(intValue);
            String ctype = dataVo.getCtype();
            DevLog.defaultLogging(ctype);
            if (ctype.equals("01")) {
                intent = new Intent(HistoryFragment.this.getActivity(), WebActivity.class);
            } else if (ctype.equals("02")) {
                intent = new Intent(HistoryFragment.this.getActivity(), TextActivity.class);
            } else if (ctype.equals("03")) {
                intent = new Intent(HistoryFragment.this.getActivity(), ImageActivity.class);
            } else if (ctype.equals("04")) {
                intent = new Intent(HistoryFragment.this.getActivity(), MP3Activity.class);
            } else if (ctype.equals("05")) {
                intent = new Intent(HistoryFragment.this.getActivity(), VideoActivity.class);
            } else if (ctype.equals("06")) {
                DevLog.defaultLogging("TYPE_NAMECARD 11111");
                Picasso.get().load(dataVo.getNameCard().getPhoto()).into((Target) new Target() {
                    public void onBitmapFailed(Exception exc, Drawable drawable) {
                        DevLog.defaultLogging("onBitmapFailed...");
                        HistoryFragment.this.addContact(dataVo, null);
                    }

                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        DevLog.defaultLogging("loaded bitmap is here (bitmap");
                        HistoryFragment.this.addContact(dataVo, bitmap);
                    }

                    public void onPrepareLoad(Drawable drawable) {
                    }
                });
                return;
            } else if (ctype.equals("07")) {
                DevLog.defaultLogging("TYPE_SCHEDULE 11111");
                HistoryFragment.this.addEvent(dataVo.getEvent().title, dataVo.getEvent().event_loc, dataVo.getEvent().description, HistoryFragment.this.getDate(dataVo.getEvent().start_dt), HistoryFragment.this.getDate(dataVo.getEvent().end_dt));
                return;
            } else {
                intent = new Intent(HistoryFragment.this.getActivity(), WebActivity.class);
            }
            intent.putExtra("data_info", dataVo);
            HistoryFragment.this.startActivity(intent);
        }
    };
    /* access modifiers changed from: private */
    public OnLongClickListener itemLongClickListener = new OnLongClickListener() {
        public boolean onLongClick(View view) {
            int intValue = ((Integer) view.getTag()).intValue();
            String str = HistoryFragment.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("itemLongClickListener: ");
            sb.append(intValue);
            Log.e(str, sb.toString());
            HistoryFragment.this.showCustomDeleteDialog(intValue, ((DataVo) HistoryFragment.this.mDataInfoArr.get(intValue)).getCurl());
            return false;
        }
    };
    private ADNAListener listener;
    /* access modifiers changed from: private */
    public RecyclerAdapter mAdapter;
    private ADNAClient mApiClient;
    /* access modifiers changed from: private */
    public ArrayList<DataVo> mDataInfoArr = new ArrayList<>();
    private SettingManager mSettingManager;
    private float orgBrightness = -1.0f;
    private LayoutParams params;
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            HistoryFragment.this.getHistoryList();
            HistoryFragment.this.vSwipeRefresh.setRefreshing(false);
        }
    };
    /* access modifiers changed from: private */
    public View vEmpty;
    /* access modifiers changed from: private */
    public RecyclerView vList;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout vSwipeRefresh;

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public ImageView ivIcon;
        /* access modifiers changed from: private */
        public TextView tvText1;
        /* access modifiers changed from: private */
        public TextView tvText2;
        /* access modifiers changed from: private */
        public View vItem;

        public ItemViewHolder(View view) {
            super(view);
            this.vItem = view.findViewById(R.id.v_item);
            this.vItem.setOnClickListener(HistoryFragment.this.itemClickListener);
            this.vItem.setOnLongClickListener(HistoryFragment.this.itemLongClickListener);
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            this.tvText1 = (TextView) view.findViewById(R.id.tv_text1);
            this.tvText2 = (TextView) view.findViewById(R.id.tv_text2);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter {
        private LayoutInflater inflater;

        public RecyclerAdapter() {
            this.inflater = LayoutInflater.from(HistoryFragment.this.getContext());
        }

        public int getItemCount() {
            if (HistoryFragment.this.mDataInfoArr == null || HistoryFragment.this.mDataInfoArr.size() <= 0) {
                HistoryFragment.this.btDelete.setVisibility(View.GONE);
                HistoryFragment.this.vList.setVisibility(View.GONE);
                HistoryFragment.this.vEmpty.setVisibility(View.VISIBLE);
            } else {
                HistoryFragment.this.btDelete.setVisibility(View.VISIBLE);
                HistoryFragment.this.vList.setVisibility(View.VISIBLE);
                HistoryFragment.this.vEmpty.setVisibility(View.GONE);
            }
            return HistoryFragment.this.mDataInfoArr.size();
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TextView textView=null;
            String str="";
            DataVo dataVo = (DataVo) HistoryFragment.this.mDataInfoArr.get(i);
            DevLog.defaultLogging(dataVo.getCname());
            DevLog.defaultLogging(dataVo.getCurl());
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            itemViewHolder.vItem.setTag(Integer.valueOf(i));
            itemViewHolder.tvText1.setText(dataVo.getCname());
            itemViewHolder.tvText2.setText(dataVo.getCurl());
            String ctype = dataVo.getCtype();
            boolean equals = ctype.equals("01");
            int i2 = R.drawable.icon_web_48;
            if (!equals) {
                if (ctype.equals("02")) {
                    i2 = R.drawable.icon_txt_48;
                } else if (ctype.equals("03")) {
                    i2 = R.drawable.icon_img_48;
                } else if (ctype.equals("04")) {
                    i2 = R.drawable.icon_mp3_48;
                } else if (ctype.equals("05")) {
                    i2 = R.drawable.icon_video_48;
                } else {
                    if (ctype.equals("06")) {
                        i2 = R.drawable.icon_namecard_48;
                        TextView b = itemViewHolder.tvText1;
                        StringBuilder sb = new StringBuilder();
                        sb.append(dataVo.getNameCard().getFname());
                        sb.append(" ");
                        sb.append(dataVo.getNameCard().getFname());
                        b.setText(sb.toString());
                        if (dataVo.getNameCard().tel.size() > 0) {
                            textView = itemViewHolder.tvText2;
                            str = ((DataNameCardVo.Tel) dataVo.getNameCard().tel.get(0)).tel_no;
                        } else if (dataVo.getNameCard().getEmail() != null) {
                            textView = itemViewHolder.tvText2;
                            str = dataVo.getNameCard().getEmail();
                        }
                    } else if (ctype.equals("07")) {
                        i2 = R.drawable.icon_schedule_48;
                        itemViewHolder.tvText1.setText(dataVo.getEvent().title);
                        textView = itemViewHolder.tvText2;
                        str = dataVo.getEvent().description;
                    }
                    textView.setText(str);
                }
            }
            itemViewHolder.ivIcon.setImageResource(i2);
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ItemViewHolder(this.inflater.inflate(R.layout.item_recycler_item, viewGroup, false));
        }
    }

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
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void addEvent(String str, String str2, String str3, long j, long j2) {
        Intent putExtra = new Intent("android.intent.action.INSERT").setData(Events.CONTENT_URI).putExtra("title", str).putExtra("eventLocation", str2).putExtra("beginTime", j).putExtra("endTime", j2).putExtra("description", str3);
        if (putExtra.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(putExtra);
        }
    }

    /* access modifiers changed from: private */
    public void deleteHistory(int i) {
        StringBuilder sb;
        String str;
        if (!PermissionUtil.isNetworkConnect(getContext())) {
            Toast.makeText(getContext(), R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        new ArrayList();
        String deviceId = BasicUtil.getDeviceId(getContext());
        String str2 = "";
        MyProgress.shows(getContext());
        if (i != -1) {
            str2 = ((DataVo) this.mDataInfoArr.get(i)).getAccess_no();
            sb = new StringBuilder();
            str = "Delete item ... ";
        } else {
            sb = new StringBuilder();
            str = "Delete all item ... ";
        }
        sb.append(str);
        sb.append(deviceId);
        sb.append(" ");
        sb.append(str2);
        DevLog.defaultLogging(sb.toString());
        if (this.mApiClient != null) {
            this.mApiClient.deleteContentAccess(deviceId, str2);
        }
    }

    /* access modifiers changed from: private */
    public long getDate(String str) {
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

    /* access modifiers changed from: private */
    public void getHistoryList() {
        if (!PermissionUtil.isNetworkConnect(getContext())) {
            Toast.makeText(getContext(), R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        MyProgress.shows(getContext());
        if (this.mApiClient != null) {
            this.mApiClient.getContentAccess(BasicUtil.getDeviceId(getContext()));
        }
    }

    private void initADNA() {
        String string = getResources().getString(R.string.server_address);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setServerURL(string);
        this.mApiClient = ADNAClient.getInstance(getActivity());
        this.mApiClient.setADNAListener(this.f2993a);
    }

    private void setOrgBrightness() {
        this.params.screenBrightness = this.orgBrightness;
        getActivity().getWindow().setAttributes(this.params);
    }

    /* access modifiers changed from: private */
    public void showDeleteDialog(final int i) {
        (VERSION.SDK_INT >= 21 ? new AlertDialog.Builder(getActivity(), 16974374) : new AlertDialog.Builder(getActivity())).setMessage(i != -1 ? R.string.txt_history_delete_item : R.string.txt_history_delete_all).setPositiveButton((int) R.string.txt_delete, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                HistoryFragment.this.deleteHistory(i);
            }
        }).setNegativeButton((int) R.string.txt_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                HistoryFragment.this.getActivity().onBackPressed();
            }
        }).setIcon(17301543).show();
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        initADNA();
        try {
            this.params = getActivity().getWindow().getAttributes();
        } catch (Exception unused) {
        }
        setOrgBrightness();
        getActivity().setTitle(R.string.txt_history);
        MainActivity.selectNavigation(1);
        View inflate = layoutInflater.inflate(R.layout.fragment_history, viewGroup, false);
        this.btDelete = inflate.findViewById(R.id.bt_delete);
        this.btDelete.setOnClickListener(this.btClickListener);
        this.mAdapter = new RecyclerAdapter();
        this.vEmpty = inflate.findViewById(R.id.lv_empty);
        this.vSwipeRefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.lv_refresh);
        this.vSwipeRefresh.setOnRefreshListener(this.refreshListener);
        this.vList = (RecyclerView) inflate.findViewById(R.id.lv_recycler);
        this.vList.setAdapter(this.mAdapter);
        getHistoryList();
        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    public void showCustomDeleteDialog(final int i, final String str) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = getLayoutInflater().inflate(R.layout.custom_history_longclick_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_cancel);
        Button button2 = (Button) inflate.findViewById(R.id.btn_delete);
        Button button3 = (Button) inflate.findViewById(R.id.btn_link_copy);
        textView.setText(str);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                HistoryFragment.this.deleteHistory(i);
            }
        });
        button3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                try {
                    ((ClipboardManager) HistoryFragment.this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", str));
                    Toast.makeText(HistoryFragment.this.getActivity(), HistoryFragment.this.getResources().getString(R.string.txt_link_copy_successful), 0).show();
                } catch (Exception unused) {
                    Toast.makeText(HistoryFragment.this.getActivity(), HistoryFragment.this.getResources().getString(R.string.txt_link_copy_fail), 0).show();
                }
            }
        });
        dialog.show();
    }
}
