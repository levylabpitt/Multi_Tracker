package com.pqiorg.multitracker.feasybeacon.BlacklistedBeacon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pqiorg.multitracker.R;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.adapter.BlacklistedBeaconDeviceAdapter;
import com.synapse.listener.BeaconBlacklistedListener;
import com.synapse.listener.BeaconItemClickListener;
import com.synapse.model.BlackListedBeacon;
import com.synapse.recycler_decorator.ListSpacingDecoration;
import com.synapse.recycler_decorator.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlacklistedBeaconFragment extends Fragment implements BeaconItemClickListener {


    @BindView(R.id.devicesList)
    RecyclerView devicesList;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    public static Fragment newInstance() {
        BlacklistedBeaconFragment frag = new BlacklistedBeaconFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blacklisted_beacon, container, false);
        ButterKnife.bind(this, view);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                if (pullToRefresh != null && pullToRefresh.isRefreshing())
                    pullToRefresh.setRefreshing(false);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


    public void onItemBlacklisted() {
        onResume();
    }

    void initView() {

        String strBeacons = SharedPreferencesUtil.getBlacklistBeacon(getActivity());
        List<BlackListedBeacon> blackListedBeacons = Utility.convertJSONStringBeaconsList(strBeacons);

        if (blackListedBeacons.size() > 0) tv_msg.setVisibility(View.GONE);
        else tv_msg.setVisibility(View.VISIBLE);

        //    devicesList.addItemDecoration( new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 10));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(devicesList.getContext(), layoutManager.getOrientation());
        devicesList.addItemDecoration(dividerItemDecoration);

        BlacklistedBeaconDeviceAdapter devicesAdapter = new BlacklistedBeaconDeviceAdapter(getActivity(), blackListedBeacons,this);

        devicesList.setLayoutManager(layoutManager);
        devicesList.setAdapter(devicesAdapter);
    }

    @Override
    public void onItemClick(String name, String address, String id) {
        showAppCompatDialog(name, address, id);
    }
    protected void showAppCompatDialog(String name, String address, String id) {


        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(getActivity())
                .setMessage("Do you want to remove this beacon from Blacklist?")
                .setTitle(name + " (" + address + ")")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        String strBeacons = SharedPreferencesUtil.getBlacklistBeacon(getActivity());
                        List<BlackListedBeacon> blackListedBeacons = Utility.convertJSONStringBeaconsList(strBeacons);
                        List<BlackListedBeacon> blackListedBeaconsNew = new ArrayList<>();
                        for (BlackListedBeacon blackListedBeacon : blackListedBeacons) {
                            if (!blackListedBeacon.getId().equals(id))
                                blackListedBeaconsNew.add(blackListedBeacon);
                        }
                        String json_BlacklistedBeacons = Utility.convertBeaconListToJSONString(blackListedBeaconsNew);
                        SharedPreferencesUtil.setBlacklistBeacon(getActivity(), json_BlacklistedBeacons);
                        onResume();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}