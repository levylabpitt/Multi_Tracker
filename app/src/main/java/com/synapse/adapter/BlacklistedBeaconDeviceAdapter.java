package com.synapse.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.pqiorg.multitracker.R;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.listener.BeaconItemClickListener;
import com.synapse.model.BlackListedBeacon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BlacklistedBeaconDeviceAdapter extends RecyclerView.Adapter<BlacklistedBeaconDeviceAdapter.MyViewHolder> {
    Context context;
    private Context mContext;

    private List<BlackListedBeacon> mDevices = new ArrayList<BlackListedBeacon>();
BeaconItemClickListener beaconItemClickListener;
    public BlacklistedBeaconDeviceAdapter(Context context, List<BlackListedBeacon> mDevices,BeaconItemClickListener beaconItemClickListener) {
        this.context = context;
        this.mDevices = mDevices;
        this.beaconItemClickListener = beaconItemClickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blacklisted_beacon_info, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        BlackListedBeacon deviceDetail = mDevices.get(position);
        String BeaconName = deviceDetail.getBeaconName();
        String BeaconAddress = deviceDetail.getBeaconAddress();
        String BeaconType = deviceDetail.getBeaconType();
        String BeaconUUID = deviceDetail.getBeaconUUID();

        viewHolder.tvName.setText(BeaconName);
        viewHolder.tvAddr.setText(" ("+BeaconAddress+")");



        if (BeaconType.equalsIgnoreCase("iBeacon")) {
            viewHolder.tv_type.setText(BeaconType);
            viewHolder.tv_uuid.setText("UUID: " + BeaconUUID);
        } else if (BeaconType.equalsIgnoreCase("AltBeacon")) {
            viewHolder.tv_type.setText(BeaconType);
            viewHolder.tv_uuid.setText("ID: " + BeaconUUID);
        } else if (BeaconType.equalsIgnoreCase("gBeacon")) {
            viewHolder.tv_type.setText("Eddystone");
            viewHolder.tv_uuid.setText("URL: " + BeaconUUID);
        }
    }


    @Override
    public int getItemCount() {
        return mDevices.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beacon_pic)
        ImageView beaconPic;

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_addr)
        TextView tvAddr;
        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.tv_uuid)
        TextView tv_uuid;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = mDevices.get(getAdapterPosition()).getBeaconName();
                    String address = mDevices.get(getAdapterPosition()).getBeaconAddress();
                    String id = mDevices.get(getAdapterPosition()).getId();
                    beaconItemClickListener.onItemClick(name,address,id);


                }
            });
        }


    }




}
