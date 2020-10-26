package com.pqiorg.multitracker.anoto.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.Contacts;
/*import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.ContentLogObject.ContentLogData;
import com.anoto.adna.ServerApi.api.object.DataNameCardVo.Addr;
import com.anoto.adna.ServerApi.api.object.DataNameCardVo.Tel;
import com.anoto.adna.ServerApi.api.object.DataVo;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.PermissionUtil;
import com.anoto.adna.util.StringUtil;*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.ContentLogObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataNameCardVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.StringUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DataListActivity extends AppCompatActivity implements ADNAListener {
    private String TAG = "DataListActivity";
    /* access modifiers changed from: private */
    public OnClickListener btClickListener = new OnClickListener() {
        public void onClick(View view) {
            Intent intent;
            DataListActivity dataListActivity;
            int id = view.getId();
            if (id == R.id.bt_top_back) {
                DataListActivity.this.onBackPressed();
            } else if (id == R.id.v_item) {
                int intValue = ((Integer) view.getTag()).intValue();
                DataListActivity.this.saveLog(intValue);
                try {
                    final DataVo dataVo = (DataVo) DataListActivity.this.mDataInfoArr.get(intValue);
                    String ctype = dataVo.getCtype();
                    StringBuilder sb = new StringBuilder();
                    sb.append(">>>>>>>>>>>>>>>>>> ");
                    sb.append(ctype);
                    DevLog.defaultLogging(sb.toString());
                    if (ctype.equals("02")) {
                        intent = new Intent(DataListActivity.this.getApplicationContext(), TextActivity.class);
                        intent.putExtra("data_info", dataVo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        dataListActivity = DataListActivity.this;
                    } else if (ctype.equals("03")) {
                        intent = new Intent(DataListActivity.this.getApplicationContext(), ImageActivity.class);
                        intent.putExtra("data_info", dataVo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        dataListActivity = DataListActivity.this;
                    } else if (ctype.equals("04")) {
                        intent = new Intent(DataListActivity.this.getApplicationContext(), MP3Activity.class);
                        intent.putExtra("data_info", dataVo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        dataListActivity = DataListActivity.this;
                    } else if (ctype.equals("05")) {
                        intent = new Intent(DataListActivity.this.getApplicationContext(), VideoActivity.class);
                        intent.putExtra("data_info", dataVo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        dataListActivity = DataListActivity.this;
                    } else if (ctype.equals("06")) {
                        DataNameCardVo.Tel tel = (DataNameCardVo.Tel) dataVo.getNameCard().tel.get(0);
                        Picasso.get().load(dataVo.getNameCard().getPhoto()).into((Target) new Target() {
                            public void onBitmapFailed(Exception exc, Drawable drawable) {
                                DevLog.defaultLogging("onBitmapFailed...");
                                DataListActivity.this.addContact(dataVo, null);
                            }

                            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
                                DevLog.defaultLogging("loaded bitmap is here (bitmap");
                                DataListActivity.this.addContact(dataVo, bitmap);
                            }

                            public void onPrepareLoad(Drawable drawable) {
                            }
                        });

                       /* Picasso.with(mContext).load(fileImage)
                                .placeholder(R.drawable.draw_detailed_view_display)
                                .error(R.drawable.draw_detailed_view_display)
                                .resize(200, 200)
                                .into(holder.mImageEvidence, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        holder.mMediaEvidencePb.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        holder.mErrorImage.setVisibility(View.VISIBLE);
                                    }
                                });
*/


                        return;
                    } else if (ctype.equals("07")) {
                        DataListActivity.this.addEvent(dataVo.getEvent().title, dataVo.getEvent().event_loc, dataVo.getEvent().description, DataListActivity.this.getDate(dataVo.getEvent().start_dt), DataListActivity.this.getDate(dataVo.getEvent().end_dt));
                        return;
                    } else {
                        intent = new Intent(DataListActivity.this.getApplicationContext(), WebActivity.class);
                        intent.putExtra("data_info", dataVo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        dataListActivity = DataListActivity.this;
                    }
                    dataListActivity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ADNAListener listener;
    private RecyclerAdapter mAdapter;
    private ADNAClient mApiClient;
    private String mCompanyCode;
    /* access modifiers changed from: private */
    public ArrayList<DataVo> mDataInfoArr = new ArrayList<>();
    private SettingManager mSettingManager;
    private RecyclerView vList;

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
            this.vItem.setOnClickListener(DataListActivity.this.btClickListener);
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            this.tvText1 = (TextView) view.findViewById(R.id.tv_text1);
            this.tvText2 = (TextView) view.findViewById(R.id.tv_text2);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter {
        private LayoutInflater inflater;

        public RecyclerAdapter() {
            this.inflater = LayoutInflater.from(DataListActivity.this.getApplicationContext());
        }

        public int getItemCount() {
            return DataListActivity.this.mDataInfoArr.size();
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            try {
                DataVo dataVo = (DataVo) DataListActivity.this.mDataInfoArr.get(i);
                ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                itemViewHolder.vItem.setTag(Integer.valueOf(i));
                itemViewHolder.tvText1.setText(StringUtil.nvl(dataVo.getCname(), ""));
                itemViewHolder.tvText2.setText(dataVo.getCurl());
                String ctype = dataVo.getCtype();
                int i2 = ctype.equals("02") ? R.drawable.icon_txt_48 : ctype.equals("03") ? R.drawable.icon_img_48 : ctype.equals("04") ? R.drawable.icon_mp3_48 : ctype.equals("05") ? R.drawable.icon_video_48 : ctype.equals("07") ? R.drawable.icon_schedule_48 : ctype.equals("06") ? R.drawable.icon_namecard_48 : R.drawable.icon_web_48;
                itemViewHolder.ivIcon.setImageResource(i2);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void addEvent(String str, String str2, String str3, long j, long j2) {
        Intent putExtra = new Intent("android.intent.action.INSERT").setData(Events.CONTENT_URI).putExtra("title", str).putExtra("eventLocation", str2).putExtra("beginTime", j).putExtra("endTime", j2).putExtra("description", str3);
        if (putExtra.resolveActivity(getPackageManager()) != null) {
            putExtra.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(putExtra);
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
            e2 = e2;
            j = 0;
            e2.printStackTrace();
            return j;
        }
    }

    private void initADNA() {
        String string = getResources().getString(R.string.server_address);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setServerURL(string);
        String string2 = getResources().getString(R.string.adna_app_id);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setApiKey(string2);
        this.mApiClient = ADNAClient.getInstance(this);
        this.mApiClient.setADNAListener(this);
    }

    /* access modifiers changed from: private */
    public void saveLog(int i) {
        DataVo dataVo = (DataVo) this.mDataInfoArr.get(i);
        if (!PermissionUtil.isNetworkConnect(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.mApiClient != null) {
            this.mApiClient.getContentLog(this.mCompanyCode, dataVo.getCid(), dataVo.getCtype(), dataVo.getCurl());
        }
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_data_list);
        initADNA();
        this.mCompanyCode = "company_id";
        this.mDataInfoArr = (ArrayList) getIntent().getSerializableExtra("data_info_arr");
        StringBuilder sb = new StringBuilder();
        sb.append("mCompanyCode: ");
        sb.append(this.mCompanyCode);
        DevLog.defaultLogging(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Data Info Array Size: ");
        sb2.append(this.mDataInfoArr.size());
        DevLog.defaultLogging(sb2.toString());
        findViewById(R.id.bt_top_back).setOnClickListener(this.btClickListener);
        this.mAdapter = new RecyclerAdapter();
        this.vList = (RecyclerView) findViewById(R.id.lv_recycler);
        this.vList.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

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
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void onReceiveADNA(int i, Object obj) {
        if (i == 5) {
            ContentLogObject.ContentLogData contentLogData = (ContentLogObject.ContentLogData) obj;
            DevLog.defaultLogging("ContentLog onSuccess.");
            DevLog.defaultLogging(contentLogData.result.toString());
        }


    }
}
