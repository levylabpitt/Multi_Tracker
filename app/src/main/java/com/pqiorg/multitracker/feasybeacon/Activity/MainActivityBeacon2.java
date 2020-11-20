package com.pqiorg.multitracker.feasybeacon.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
/*import com.feasycom.fsybecon.Adapter.SearchDeviceListAdapter;
import com.feasycom.fsybecon.Controler.FscBeaconCallbacksImpMain;
import com.feasycom.fsybecon.R;
import com.feasycom.fsybecon.Widget.RefreshableView;*/
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.feasybeacon.Adapter.SearchDeviceListAdapter2;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
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

//import static com.feasycom.fsybecon.Activity.SetActivity.SCAN_FIXED_TIME;


/**
 * Copyright 2017 Shenzhen Feasycom Technology co.,Ltd
 */

public class MainActivityBeacon2 extends AppCompatActivity {
    /* @BindView(R.id.header_left)
     TextView headerLeft;*/
    // @BindView(R.id.header_title)
    // TextView headerTitle;
    /*   @BindView(R.id.header_right)
       TextView headerRight;
   */
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
    //private BeaconDeviceAdapter devicesAdapter;
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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivityBeacon2.class);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);// 淡出淡入动画效果

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feasybeacon);
        activity = this;
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
        /**/
        // setTitle("Beacon");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbar();
        /**/


        ButterKnife.bind(this);
        //  refreshHeader();
        initView();
        devicesAdapter = new SearchDeviceListAdapter2(activity, getLayoutInflater());
        //    devicesAdapter = new BeaconDeviceAdapter(activity);
        // LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        //  devicesList.setLayoutManager(layoutManager);
        devicesList.setAdapter(devicesAdapter);
        /**
         * remove the dividing line
         */
        devicesList.setDividerHeight(0);
        Glide.with(this)
                .load(R.drawable.radar)
                .into(imageViewScanning);
        setBeaconSpinner();


    }

    void setBeaconSpinner() {

        aa = new ArrayAdapter<String>(this, R.layout.spinner_item, devices);
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

    @Override
    protected void onResume() {
        super.onResume();
        initFeasyBeaconAPI();
        if (!Utility.isMyServiceRunning(BeaconScannerService.class, this)) {
            this.startForegroundService(new Intent(this, BeaconScannerService.class));
        }

        //  startBeacon();
    }

    void initFeasyBeaconAPI() {
        fscBeaconApi = FscBeaconApiImp.getInstance(activity);
        fscBeaconApi.initialize();
        fscBeaconApi.startScan(0);
    }

/*    protected void startBeacon() {
        fscBeaconApi = FscBeaconApiImp.getInstance(activity);
        fscBeaconApi.initialize();
        LogUtil.setDebug(true);
        if (fscBeaconApi.checkBleHardwareAvailable() == false) {
            bleMissing();
        }

        if (fscBeaconApi.isBtEnabled() == false) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
        }
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            fscBeaconApi.setCallbacks(new FscBeaconCallbacksImpMain2(new WeakReference<MainActivityBeacon2>((MainActivityBeacon2) activity)));
            if (SCAN_FIXED_TIME) {
                fscBeaconApi.startScan(60000);
            } else {
                fscBeaconApi.startScan(0);
                // fscBeaconApi.startScan(60000);
            }
        }
        timerUI = new Timer();
        timerTask = new UITimerTask(new WeakReference<MainActivityBeacon2>((MainActivityBeacon2) activity));
        timerUI.schedule(timerTask, 100, 100);
    }*/

    @Override
    protected void onPause() {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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


/*        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deviceQueue.clear();
                        devicesAdapter.clearList();
                        devicesAdapter.notifyDataSetChanged();
                        // fscBeaconApi.stopScan();
                        // fscBeaconApi.startScan(6000);
                        fscBeaconApi.startScan(0);

                    }
                });
            }
        }, 0);*/
    }

    @OnItemClick(R.id.devicesList)
    public void deviceItemClick(int position) {
        BluetoothDeviceWrapper deviceWrapper = (BluetoothDeviceWrapper) devicesAdapter.getItem(position);
        showAppCompatDialog(deviceWrapper);

      /*  BluetoothDeviceWrapper deviceWrapper = (BluetoothDeviceWrapper) devicesAdapter.getItem(position);
        EddystoneBeacon eddystoneBeacon = deviceWrapper.getgBeacon();
        try {
            Log.i("click", eddystoneBeacon.getUrl());
        } catch (Exception e) {
        }
        if ((null != eddystoneBeacon) && ("URL".equals(eddystoneBeacon.getFrameTypeString()))) {
            Uri uri = Uri.parse(eddystoneBeacon.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }*/
    }


    protected void showAppCompatDialog(BluetoothDeviceWrapper deviceWrapper) {
        String name = deviceWrapper.getName() + "(" + deviceWrapper.getAddress() + ")";


        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                .setMessage("Do you want to add this beacon to Blacklist?")
                .setTitle(name)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String uuid = "",type="", id="";
                        if (deviceWrapper.getiBeacon() != null) {
                            type="iBeacon";
                            uuid = deviceWrapper.getiBeacon().getUuid();
                            id=uuid;
                        } else if (deviceWrapper.getAltBeacon() != null) {
                            type="AltBeacon";
                            uuid = deviceWrapper.getAltBeacon().getId();
                            id=uuid;
                        } else if (deviceWrapper.getgBeacon() != null) {
                            type="gBeacon";
                            uuid = "";
                            id=Utility.getCurrentTimestamp();
                        }



                        String strBeacons = SharedPreferencesUtil.getBlacklistBeacon(MainActivityBeacon2.this);
                        List<BlackListedBeacon> blackListedBeacons = Utility.convertJSONStringBeaconsList(strBeacons);
                        blackListedBeacons.add(new BlackListedBeacon(deviceWrapper.getName(), deviceWrapper.getAddress(), uuid,type,id));
                        String json_BlacklistedBeacons = Utility.convertBeaconListToJSONString(blackListedBeacons);
                        SharedPreferencesUtil.setBlacklistBeacon(MainActivityBeacon2.this, json_BlacklistedBeacons);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }


    // @Override
    public void refreshHeader() {
        //   headerTitle.setText(getResources().getString(R.string.app_name));
        //   headerTitle.setText("Beacon");
        //headerLeft.setText("Sort");
        //headerRight.setText("Filter");
        //   headerLeft.setVisibility(View.GONE);
        // headerRight.setVisibility(View.GONE);
    }
/*
    @OnClick(R.id.header_left)
    public void deviceSort(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                devicesAdapter.sort();
                devicesAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.header_right)
    public void deviceFilterClick() {
        fscBeaconApi.stopScan();
        FilterDeviceActivity.actionStart(activity);
        finishActivity();
    }*/

    //@Override
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Beacon");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void refreshFooter() {
        /**
         * footer image src init
         */
        SetButton.setImageResource(R.drawable.setting_off);
        AboutButton.setImageResource(R.drawable.about_off);
        // SensorButton.setImageResource(R.drawable.sensor_off);
        SearchButton.setImageResource(R.drawable.search_on);
    }


    /**
     * search button binding event
     */
    @OnClick(R.id.Search_Button)
    public void searchClick() {

    }

 /*   @OnClick(R.id.Sensor_Button)
    public void sensorClick() {
        fscBeaconApi.stopScan();
        SensorActivity.actionStart(activity);
        finishActivity();
    }*/

    /**
     * about button binding events
     */
    @OnClick(R.id.About_Button)
    public void aboutClick() {
        fscBeaconApi.stopScan();
        //   AboutActivity.actionStart(activity);
        //  finishActivity();
    }

    private static final String TAG = "MainActivity";

    /**
     * set the button binding event
     */
    @OnClick(R.id.Set_Button)
    public void setClick() {
        fscBeaconApi.stopScan();
        Log.e(TAG, "setClick: ");
        SetActivity.actionStart(activity);
        // finishActivity();
    }

    /**
     * bluetooth is not turned on
     */
    private void btDisabled() {
        Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        // finishActivity();
    }

    /**
     * does not support BLE
     */
    private void bleMissing() {
        Toast.makeText(this, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        //  finishActivity();
    }

/*    class UITimerTask extends TimerTask {
        private WeakReference<MainActivityBeacon2> activityWeakReference;

        public UITimerTask(WeakReference<MainActivityBeacon2> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
        }

        @Override
        public void run() {
            activityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityWeakReference.get().getDevicesAdapter().addDevice(activityWeakReference.get().getDeviceQueue().poll());
                    activityWeakReference.get().getDevicesAdapter().notifyDataSetChanged();
                    if (getDevicesAdapter().getCount() == 0) {
                        imageViewScanning.setVisibility(View.VISIBLE);
                    } else imageViewScanning.setVisibility(View.GONE);
                }
            });
        }
    }*/

    public Queue<BluetoothDeviceWrapper> getDeviceQueue() {
        return deviceQueue;
    }

    public SearchDeviceListAdapter2 getDevicesAdapter() {
        return devicesAdapter;
    }


    /*   public BeaconDeviceAdapter getDevicesAdapter() {
           return devicesAdapter;
       }
   */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // android.os.Process.killProcess(android.os.Process.myPid()); // stopping timertask

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        devices.clear();
        devices.add("--All Beacons--");
        for (BluetoothDeviceWrapper bluetoothDeviceWrapper : mDevices) {
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

        }
        refreshSpinnerAdapter();


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
