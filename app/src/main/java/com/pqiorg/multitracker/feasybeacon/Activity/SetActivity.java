package com.pqiorg.multitracker.feasybeacon.Activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.bean.FeasyBeacon;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.feasybeacon.Adapter.SettingDeviceListAdapter;
import com.pqiorg.multitracker.feasybeacon.Bean.BaseEvent;
import com.pqiorg.multitracker.feasybeacon.Controler.FscBeaconCallbacksImpSet;
import com.pqiorg.multitracker.feasybeacon.Widget.PinDialog;
import com.pqiorg.multitracker.feasybeacon.Widget.RefreshableView;
/*import com.feasycom.fsybecon.Adapter.SettingDeviceListAdapter;
import com.feasycom.fsybecon.Bean.BaseEvent;
import com.feasycom.fsybecon.Controler.FscBeaconCallbacksImpSet;
import com.feasycom.fsybecon.R;
import com.feasycom.fsybecon.Widget.PinDialog;
import com.feasycom.fsybecon.Widget.RefreshableView;*/

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import pub.devrel.easypermissions.EasyPermissions;

import static com.pqiorg.multitracker.feasybeacon.Activity.ParameterSettingActivity.SUCESSFUL_COUNT;
import static com.pqiorg.multitracker.feasybeacon.Activity.ParameterSettingActivity.TOTAL_COUNT;

//import static com.feasycom.fsybecon.Activity.ParameterSettingActivity.SUCESSFUL_COUNT;
//import static com.feasycom.fsybecon.Activity.ParameterSettingActivity.TOTAL_COUNT;

/**
 * Copyright 2017 Shenzhen Feasycom Technology co.,Ltd
 */

public class SetActivity extends BaseActivity{
    @BindView(R.id.header_left)
    TextView headerLeft;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right)
    TextView headerRight;
    @BindView(R.id.devicesList)
    ListView devicesList;
    @BindView(R.id.refreshableView)
    RefreshableView refreshableView;
    @BindView(R.id.Search_Button)
    ImageView SearchButton;
    @BindView(R.id.Set_Button)
    ImageView SetButton;
    @BindView(R.id.About_Button)
    ImageView AboutButton;

    private SettingDeviceListAdapter devicesAdapter;
    private FscBeaconApi fscBeaconApi;
    private Activity activity;
    private static final int ENABLE_BT_REQUEST_ID = 1;
    private PinDialog pinDialog;
    private Handler handler = new Handler();

    private Timer timerUI;
    private TimerTask timerTask;
    public static final boolean OPEN_TEST_MODE = false;
    public static final boolean SCAN_FIXED_TIME = false;
    Queue<BluetoothDeviceWrapper> deviceQueue = new LinkedList<BluetoothDeviceWrapper>();
    /**
     * read and write permissions
     */
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    /**
     * location permissions
     */
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(fscBeaconApi!=null){
            Log.e("Set",fscBeaconApi.isConnected()+"");
        }
        setContentView(R.layout.activity_set);
        activity = this;
        ButterKnife.bind(this);
        initView();
        devicesAdapter = new SettingDeviceListAdapter(activity, getLayoutInflater());
        devicesList.setAdapter(devicesAdapter);
        /**
         * remove the dividing line
         */
        devicesList.setDividerHeight(0);
        pinDialog = new PinDialog(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * registered EventBus
         */
        EventBus.getDefault().register(this);
        fscBeaconApi = FscBeaconApiImp.getInstance(activity);
        fscBeaconApi.initialize();
        if (fscBeaconApi.checkBleHardwareAvailable() == false) {
            bleMissing();
        }
        /**
         * Check if we have write permission
         */
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        fscBeaconApi.setCallbacks(new FscBeaconCallbacksImpSet(new WeakReference<SetActivity>((SetActivity) activity)));
        /**
         *  on every Resume check if BT is enabled (user could turn it off while app was in background etc.)
         */
        if (fscBeaconApi.isBtEnabled() == false) {
            /**
             * BT is not turned on - ask user to make it enabled
             */
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
            /**
             * see onActivityResult to check what is the status of our request
             */
        }
        if (OPEN_TEST_MODE) {
            Log.e(TAG, "onResume: "+ "OPEN_TEST_MODE" );
            fscBeaconApi.startScan(25000);
        } else {
            if(SCAN_FIXED_TIME) {
                Log.e(TAG, "onResume: "+ "SCAN_FIXED_TIME" );
                fscBeaconApi.startScan(15000);
            }else {
                Log.e(TAG, "onResume: "+ "NO SCAN_FIXED_TIME" );
                fscBeaconApi.startScan(0);

            }
        }
        timerUI = new Timer();
        timerTask = new UITimerTask(new WeakReference<SetActivity>((SetActivity) activity));
        timerUI.schedule(timerTask, 100, 100);
    }

    private static final String TAG = "SetActivity";
   @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            MainActivityBeacon.actionStart(this);
            finishActivity();
        }
        return true;
    }

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
        EventBus.getDefault().unregister(this);
    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if (resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
                return;
            }
        }
    }

    /**
         * user didn't want to turn on BT
         *//*

        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if (resultCode == Activity.RESULT_CANCELED) {
                btDisabled();
                return;
            }
        }
    }*/



    @Override
    public void initView() {
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deviceQueue.clear();
                        devicesAdapter.clearList();
                        devicesAdapter.notifyDataSetChanged();
                      //  fscBeaconApi.startScan(15000);
                        fscBeaconApi.startScan(0);
                        refreshableView.finishRefreshing();
                    }
                });
            }
        }, 0);
    }


    @Subscribe
    public void onEventMainThread(BaseEvent event) {
        switch (event.getEventId()) {
            case BaseEvent.PIN_EVENT:
                int position = (int) event.getObject("position");
                String pin = (String) event.getObject("pin");
                fscBeaconApi.stopScan();
                ParameterSettingActivity.actionStart(activity, (BluetoothDeviceWrapper) devicesAdapter.getItem(position), pin);
                finishActivity();
                break;
        }
    }

    @Override
    public void refreshHeader() {
        if (OPEN_TEST_MODE) {
            headerTitle.setText(" total " + TOTAL_COUNT + " successful " + SUCESSFUL_COUNT);
        } else {
            //headerTitle.setText(getResources().getString(R.string.app_name));
            headerTitle.setText("Setting");
            //headerLeft.setText("   Sort");
            //headerRight.setText("Filter   ");
            headerLeft.setVisibility(View.GONE);
            headerRight.setVisibility(View.GONE);
        }
    }
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

    }
    @Override
    public void refreshFooter() {
        /**
         * footer image src init
         */
        SetButton.setImageResource(R.drawable.setting_on);
        AboutButton.setImageResource(R.drawable.about_off);
        SearchButton.setImageResource(R.drawable.search_off);
    }

    @OnItemClick(R.id.devicesList)
    public void deviceItemClick(int position) {
        BluetoothDeviceWrapper deviceDetail = (BluetoothDeviceWrapper) devicesAdapter.getItem(position);
        if (null != deviceDetail.getFeasyBeacon() && null != deviceDetail.getFeasyBeacon() && FeasyBeacon.BLE_KEY_WAY.equals(deviceDetail.getFeasyBeacon().getEncryptionWay().substring(1))) {
            Log.e(TAG, "deviceItemClick: 1" );
            pinDialog.show();
            pinDialog.setPosition(position);
        } else {
            Log.e(TAG, "deviceItemClick: 2" );
            fscBeaconApi.stopScan();
            ParameterSettingActivity.actionStart(activity, (BluetoothDeviceWrapper) devicesAdapter.getItem(position), null);
            finishActivity();
        }
    }

    /**
     * search button binding event
     */
    @OnClick(R.id.Search_Button)
    public void searchClick() {
        MainActivityBeacon.actionStart(activity);
        finishActivity();
    }

    /**
     * about button binding events
     */
    @OnClick(R.id.About_Button)
    public void aboutClick() {
        AboutActivityFeasyBeacon.actionStart(activity);
        finishActivity();
    }

    /**
     * set the button binding event
     */
    @OnClick(R.id.Set_Button)
    public void setClick() {
        fscBeaconApi.startScan(15000);
        //fscBeaconApi.startScan(0);
    }


    /**
     * bluetooth is not turned on
     */
    private void btDisabled() {
        Toast.makeText(this, "Sorry, BT has to be turned ON for us to work!", Toast.LENGTH_LONG).show();
        finishActivity();
    }

    /**
     * does not support BLE
     */
    private void bleMissing() {
        Toast.makeText(this, "BLE Hardware is required but not available!", Toast.LENGTH_LONG).show();
        finishActivity();
    }


    class UITimerTask extends TimerTask {
        private WeakReference<SetActivity> activityWeakReference;

        public UITimerTask(WeakReference<SetActivity> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
        }

        @Override
        public void run() {
            activityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityWeakReference.get().getDevicesAdapter().addDevice(activityWeakReference.get().getDeviceQueue().poll());
                    activityWeakReference.get().getDevicesAdapter().notifyDataSetChanged();
                }
            });
        }
    }

    public Queue<BluetoothDeviceWrapper> getDeviceQueue() {
        return deviceQueue;
    }

    public SettingDeviceListAdapter getDevicesAdapter() {
        return devicesAdapter;
    }

    public FscBeaconApi getFscBeaconApi() {
        return fscBeaconApi;
    }

    public Handler getHandler() {
        return handler;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];

        }
    }

}
