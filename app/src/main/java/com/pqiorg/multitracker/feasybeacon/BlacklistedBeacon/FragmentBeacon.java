package com.pqiorg.multitracker.feasybeacon.BlacklistedBeacon;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.feasybeacon.Activity.MainActivityBeacon2;
import com.pqiorg.multitracker.feasybeacon.Activity.SetActivity;
import com.pqiorg.multitracker.feasybeacon.Adapter.SearchDeviceListAdapter2;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.listener.BeaconBlacklistedListener;
import com.synapse.model.BlackListedBeacon;
import com.synapse.service.BeaconScannerService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class FragmentBeacon extends Fragment {

    @BindView(R.id.imageViewScanning)
    ImageView imageViewScanning;
    @BindView(R.id.devicesList)
    ListView devicesList;

   /* @BindView(R.id.refreshableView)
    RefreshableView refreshableView;*/


    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @BindView(R.id.Search_Button)
    ImageView SearchButton;
    @BindView(R.id.Set_Button)
    ImageView SetButton;
    @BindView(R.id.About_Button)
    ImageView AboutButton;
    @BindView(R.id.spinner_beacons)
    Spinner spinner_beacons;


    // @BindView(R.id.Sensor_Button)
    // ImageView SensorButton;

    private SearchDeviceListAdapter2 devicesAdapter;

    private FscBeaconApi fscBeaconApi;
    private Activity activity;
    private static final int ENABLE_BT_REQUEST_ID = 1;
    Queue<BluetoothDeviceWrapper> deviceQueue = new LinkedList<BluetoothDeviceWrapper>();
    private Timer timerUI;
    private TimerTask timerTask;
    private Handler handler = new Handler();


    ArrayList<BluetoothDeviceWrapper> mDevices = new ArrayList<>();
    ArrayList<String> devices = new ArrayList<>();
    ArrayAdapter<String> aa;
    String allowedBeacon = "BlueCharm";
    //  String allowedBeacon="FSC_BP104";
    boolean manuallyChangedSelection = false;

    private FragmentActivity fragmentActivity;
    private BeaconBlacklistedListener beaconBlacklistedListener;

    public static Fragment newInstance() {
        FragmentBeacon frag = new FragmentBeacon();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_feasybeacon, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        initView();
        devicesAdapter = new SearchDeviceListAdapter2(activity, getLayoutInflater());
        devicesList.setAdapter(devicesAdapter);
        devicesList.setDividerHeight(0);
        Glide.with(this)
                .load(R.drawable.radar)
                .into(imageViewScanning);
        setBeaconSpinner();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (fragmentActivity == null) {
            fragmentActivity = getActivity();
            beaconBlacklistedListener = (BeaconBlacklistedListener) fragmentActivity;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initFeasyBeaconAPI();
        if (!Utility.isMyServiceRunning(BeaconScannerService.class, activity)) {
            activity.startForegroundService(new Intent(activity, BeaconScannerService.class));
        }

        //  startBeacon();
    }

    void setBeaconSpinner() {

        aa = new ArrayAdapter<String>(activity, R.layout.spinner_item, devices);
        aa.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_beacons.setAdapter(aa);

        spinner_beacons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String beaconName = parent.getItemAtPosition(position).toString();
                getFilterBeaconList(beaconName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_beacons.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                manuallyChangedSelection = true;
                return false;
            }
        });
    }


    void initFeasyBeaconAPI() {
        fscBeaconApi = FscBeaconApiImp.getInstance(activity);
        fscBeaconApi.initialize();
        fscBeaconApi.startScan(0);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timerUI != null) {
            timerUI.cancel();
            timerUI = null;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if (resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
                return;
            }
        }
    }


    // @Override
    public void initView() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                deviceQueue.clear();
                devicesAdapter.clearList();
                devicesAdapter.notifyDataSetChanged();
                // fscBeaconApi.stopScan();
                // fscBeaconApi.startScan(6000);
                fscBeaconApi.startScan(0);
                if (pullToRefresh != null && pullToRefresh.isRefreshing())
                    pullToRefresh.setRefreshing(false);
            }
        });


    }

    @OnItemClick(R.id.devicesList)
    public void deviceItemClick(int position) {
        BluetoothDeviceWrapper deviceWrapper = (BluetoothDeviceWrapper) devicesAdapter.getItem(position);
        showAppCompatDialog(deviceWrapper);
    }


    protected void showAppCompatDialog(BluetoothDeviceWrapper deviceWrapper) {
        String name = deviceWrapper.getName() + " (" + deviceWrapper.getAddress() + ")";


        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity)
                .setMessage("Do you want to add this beacon to Blacklist?")
                .setTitle(name)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String uuid = "", type = "", id = "";
                        if (deviceWrapper.getiBeacon() != null) {
                            type = "iBeacon";
                            uuid = deviceWrapper.getiBeacon().getUuid();
                            id = deviceWrapper.getAddress();    // matching criteria
                        } else if (deviceWrapper.getAltBeacon() != null) {
                            type = "AltBeacon";
                            uuid = deviceWrapper.getAltBeacon().getId();
                            id = deviceWrapper.getAddress();  // matching criteria
                        } else if (deviceWrapper.getgBeacon() != null) {
                            type = "gBeacon";
                            uuid = deviceWrapper.getgBeacon().getUrl();
                            id = deviceWrapper.getAddress();  // matching criteria
                        }


                        String strBeacons = SharedPreferencesUtil.getBlacklistBeacon(activity);
                        List<BlackListedBeacon> blackListedBeacons = Utility.convertJSONStringBeaconsList(strBeacons);
                        blackListedBeacons.add(new BlackListedBeacon(deviceWrapper.getName(), deviceWrapper.getAddress(), uuid, type, id));
                        String json_BlacklistedBeacons = Utility.convertBeaconListToJSONString(blackListedBeacons);
                        SharedPreferencesUtil.setBlacklistBeacon(activity, json_BlacklistedBeacons);
                        beaconBlacklistedListener.onItemBlacklisted();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }


    /**
     * bluetooth is not turned on
     */
    private void btDisabled() {
        Toast.makeText(activity, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        // finishActivity();
    }

    /**
     * does not support BLE
     */
    private void bleMissing() {
        Toast.makeText(activity, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        //  finishActivity();
    }


    public Queue<BluetoothDeviceWrapper> getDeviceQueue() {
        return deviceQueue;
    }

    public SearchDeviceListAdapter2 getDevicesAdapter() {
        return devicesAdapter;
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            mDevices = (ArrayList<BluetoothDeviceWrapper>) intent.getSerializableExtra("deviceList");
            devicesAdapter.setDevices(mDevices);
            devicesAdapter.notifyDataSetChanged();
            if (getDevicesAdapter().getCount() == 0) imageViewScanning.setVisibility(View.VISIBLE);
            else imageViewScanning.setVisibility(View.GONE);
            getDevicesForSpinner();

        }
    };


    void getDevicesForSpinner() {
        if(devices==null) return;
        devices.clear();
        devices.add("--All Beacons--");
        for (BluetoothDeviceWrapper bluetoothDeviceWrapper : mDevices) {

            try {
                if (bluetoothDeviceWrapper == null) continue;
                String deviceName = bluetoothDeviceWrapper.getName();
                String completeName = bluetoothDeviceWrapper.getCompleteLocalName();
                String nameDevice = "";
                if ((completeName != null) && completeName.length() > 0) {
                    nameDevice = completeName;
                    if (completeName.length() > 10) {
                        nameDevice = completeName.substring(0, 10);
                    }
                } else if (deviceName != null && deviceName.length() > 0) {
                    nameDevice = deviceName;
                    if (deviceName.length() > 10) {
                        nameDevice = deviceName.substring(0, 10);
                    }
                }
                if (!devices.contains(nameDevice)) {
                    devices.add(nameDevice);
                }

            } catch (Exception e) {
                // Log.e("Error--",String.valueOf(e.getMessage()));
            }


        }

        try {
        refreshSpinnerAdapter();
        } catch (Exception e) {
            // Log.e("Error--",String.valueOf(e.getMessage()));
        }
    }

    void getFilterBeaconList(String beaconName) {
        ArrayList<BluetoothDeviceWrapper> mDevices_Filtered = new ArrayList<>();
        for (BluetoothDeviceWrapper bluetoothDeviceWrapper : mDevices) {
            String deviceName = bluetoothDeviceWrapper.getName();
            String completeName = bluetoothDeviceWrapper.getCompleteLocalName();
            if (completeName != null && completeName.equals(beaconName)) {
                mDevices_Filtered.add(bluetoothDeviceWrapper);
            } else if (deviceName != null && deviceName.equals(beaconName)) {
                mDevices_Filtered.add(bluetoothDeviceWrapper);
            }
        }

        devicesAdapter.setDevices(mDevices_Filtered);
        devicesAdapter.notifyDataSetChanged();

    }

    void refreshSpinnerAdapter() {
        aa.notifyDataSetChanged();
        if (devices.size() > 0) {
            if (!manuallyChangedSelection) {
                if (devices.contains(allowedBeacon)) {
                    int index = devices.indexOf(allowedBeacon);
                    spinner_beacons.setSelection(index);
                }
            }
        }
        if (devices.size() > 0) {
            spinner_beacons.setVisibility(View.VISIBLE);
        } else {
            spinner_beacons.setVisibility(View.INVISIBLE);
        }

    }
}
