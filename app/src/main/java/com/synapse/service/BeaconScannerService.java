package com.synapse.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.pqiorg.multitracker.feasybeacon.Utils.SettingConfigUtil;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.model.BlackListedBeacon;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.GraphRequest.TAG;


/**
 * ChatHead Service
 */
public class BeaconScannerService extends Service {

    private ScreenStateReceiver mReceiver;
    private FscBeaconApi fscBeaconApi;
    private Handler handler = new Handler();
    Runnable myRunnable;
    private static Timer timerUI;
    private static TimerTask timerTask;
    Queue<BluetoothDeviceWrapper> deviceQueue = new LinkedList<BluetoothDeviceWrapper>();
    private static ArrayList<BluetoothDeviceWrapper> mDevices = new ArrayList<BluetoothDeviceWrapper>();
    private static ArrayList<BluetoothDeviceWrapper> nonBlacklistedDevices = new ArrayList<BluetoothDeviceWrapper>();

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (mFloatingViewManager != null) {
            return START_STICKY;
        }*/
        startBeacon();
        // startTimerThread();

        return START_STICKY;
        //  return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        stopTimerThread();
        unRegisterReceiver();
        destroy();
        super.onDestroy();
        Log.e("Service Stopped", "Beacon Scanner");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Viewを破棄します。
     */
    private void destroy() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timerUI != null) {
            timerUI.cancel();
            timerUI.purge();
            timerUI = null;
        }
    }


    public void unRegisterReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    public class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {

            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

            }
        }
    }


    void startBeacon() {
        fscBeaconApi = FscBeaconApiImp.getInstance();
        fscBeaconApi.initialize();

        fscBeaconApi.setCallbacks(new FscBeaconCallbacksImpScannerService(new WeakReference<BeaconScannerService>((BeaconScannerService) this)));
        fscBeaconApi.startScan(0);


        timerUI = new Timer();
        timerTask = new UITimerTask(new WeakReference<BeaconScannerService>((BeaconScannerService) this));
        timerUI.schedule(timerTask, 100, 100);
    }


    class UITimerTask extends TimerTask {
        private WeakReference<BeaconScannerService> activityWeakReference;

        public UITimerTask(WeakReference<BeaconScannerService> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
        }

        @Override
        public void run() {
            addDevice(activityWeakReference.get().getDeviceQueue().poll());


            /*activityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("UITimerTask", "Beacon Scanner Active");

                }
            });
*/

          /*  myRunnable = new Runnable() {
                public void run() {
                      addDevice(activityWeakReference.get().getDeviceQueue().poll());
                       handler.postDelayed(this, 10);//1000 milli second=1 second
                    Log.e("Service Active", "Beacon Scanner Active");
                }
            };
            handler.postDelayed(myRunnable, 0);*/


        }


    }

    public Queue<BluetoothDeviceWrapper> getDeviceQueue() {
        return deviceQueue;
    }

    public FscBeaconApi getFscBeaconApi() {
        return fscBeaconApi;
    }

    public Handler getHandler() {
        return handler;
    }



    synchronized public boolean addDevice(BluetoothDeviceWrapper deviceDetail) {
        sendBroadcast(deviceDetail);
        if (deviceDetail == null) {
            return false;
        }
        int i = 0;
        for (; i < mDevices.size(); i++) {
            if (deviceDetail.getAddress().equals(mDevices.get(i).getAddress())) {
                mDevices.get(i).setCompleteLocalName(deviceDetail.getCompleteLocalName());
                mDevices.get(i).setName(deviceDetail.getName());
                mDevices.get(i).setRssi(deviceDetail.getRssi());
                // mDevices.get(i).setFlag(Utility.getCurrentTimestamp());//
                if (null != deviceDetail.getiBeacon()) {
                    if (deviceDetail.getAdvData().equals(mDevices.get(i).getAdvData())) {
                        Log.i("iBeacon", Integer.toString(i));
                        return false;
                    }
                }
                if (null != deviceDetail.getgBeacon()) {
                    if (deviceDetail.getAdvData().equals(mDevices.get(i).getAdvData())) {
                        return false;
                    }
                }
                if (null != deviceDetail.getAltBeacon()) {
                    if (deviceDetail.getAdvData().equals(mDevices.get(i).getAdvData())) {
                        return false;
                    }
                }
            }
        }

        if (i >= mDevices.size()) {
            //if (!isBlackListedBeacon(deviceDetail)) {  // nks
            deviceDetail.setFlag(Utility.getCurrentTimestamp());//
            mDevices.add(deviceDetail);
            // }
        }

        //process the filter event.
        if ((boolean) SettingConfigUtil.getData(this, "filter_switch", false)) {
            if (mDevices.get(i).getRssi() < ((int) SettingConfigUtil.getData(this, "filter_value", -100) - 100)) {
                mDevices.remove(i);
            }
        }


        return false;
    }

    public static void clearBeaconsList() {
        mDevices.clear();
    }

    boolean isBlackListedBeacon(BluetoothDeviceWrapper deviceDetail) {
        String strBeacons = SharedPreferencesUtil.getBlacklistBeacon(this);
        List<BlackListedBeacon> blackListedBeacons = Utility.convertJSONStringBeaconsList(strBeacons);

        for (int i = 0; i < blackListedBeacons.size(); i++) {
            String id = blackListedBeacons.get(i).getId();
            String address = deviceDetail.getAddress();   // matching criteria
            if (address.equals(id)) {
                return true;
            }

         /*   if( null != deviceDetail.getiBeacon() ){
                if(beaconUUID.equals(deviceDetail.getiBeacon().getUuid())){ // matching criteria
                    return true;
                }
            }
            else  if (null != deviceDetail.getgBeacon()){
                if(beaconUUID.equals(deviceDetail.getgBeacon().getDeviceAddr())){
                    return true;
                }
            }
            else  if (null != deviceDetail.getAltBeacon()){
                if(beaconUUID.equals(deviceDetail.getAltBeacon().getId())){
                    return true;
                }
            }*/
        }
        return false;
    }

    void getNonBlacklistedDevices() {
        String strBeacons = SharedPreferencesUtil.getBlacklistBeacon(this);
        List<BlackListedBeacon> blackListedBeacons = Utility.convertJSONStringBeaconsList(strBeacons);
        nonBlacklistedDevices.clear();
        if (blackListedBeacons.isEmpty()) {
            nonBlacklistedDevices.addAll(mDevices);
            return;
        }
        for (int i = 0; i < mDevices.size(); i++) {
            BluetoothDeviceWrapper deviceDetail = mDevices.get(i);
            for (int j = 0; j < blackListedBeacons.size(); j++) {
                String id = blackListedBeacons.get(j).getId();
                if (!deviceDetail.getAddress().equals(id)) {
                    nonBlacklistedDevices.add(deviceDetail);
                }
            }
        }
    }

    private void sendBroadcast(BluetoothDeviceWrapper deviceDetail) {
        getNonBlacklistedDevices();
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", getBeaconList().size() + "");
        intent.putExtra("deviceDetail", deviceDetail);
        intent.putExtra("deviceList", nonBlacklistedDevices);  // mDevices
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static List<List<Object>> getBeaconList() {
        List<List<Object>> values = new ArrayList<>();
        for (int i = 0; i < nonBlacklistedDevices.size(); i++) { //mDevices
            List<Object> list1 = new ArrayList<>();
            list1.add(Utility.getCurrentDate()); //0
            // list1.add(Utility.getCurrentTimestamp());

            String deviceName;
            String completeName;
            String deviceAdd;
            String deviceModel;
            int deviceRssi;
            String timestamp;

            try {
                BluetoothDeviceWrapper deviceDetail = mDevices.get(i);
                deviceName = deviceDetail.getName();
                completeName = deviceDetail.getCompleteLocalName();
                deviceAdd = deviceDetail.getAddress();
                deviceModel = deviceDetail.getModel();
                deviceRssi = deviceDetail.getRssi();
                timestamp = deviceDetail.getFlag();


                if ((completeName != null) && completeName.length() > 0) {

                    if (completeName.length() > 10) {
                        completeName = completeName.substring(0, 10);
                    }
                    list1.add(completeName);                                       //1
                } else if (deviceName != null && deviceName.length() > 0) {

                    if (deviceName.length() > 10) {
                        deviceName = deviceName.substring(0, 10);
                    }
                    list1.add(deviceName);
                } else {
                    list1.add("unknown");
                }

                list1.add(timestamp); // 2
                list1.add(deviceRssi);// 3


                if (deviceAdd != null && deviceAdd.length() > 0) {
                    list1.add(deviceAdd);   // 4
                } else {
                    list1.add("");
                }

                //iBeacon
                if (null != deviceDetail.getiBeacon()) {
                    list1.add(deviceDetail.getiBeacon().getMajor());  // 5
                    list1.add(deviceDetail.getiBeacon().getMinor());  // 6
                }

                //gBeacon
                else if (null != deviceDetail.getgBeacon()) {
                    list1.add("");
                    list1.add("");
                }


                //AltBeacon
                else if (null != deviceDetail.getAltBeacon()) {
                    list1.add("");
                    list1.add("");
                }


                if (deviceDetail.getFeasyBeacon() != null && deviceDetail.getFeasyBeacon().getBattery() != null) {
                    list1.add(deviceDetail.getFeasyBeacon().getBattery()); // 7
                } else {
                    list1.add("");
                }

            } catch (Exception e) {
                e.getCause();
            }


            values.add(list1);
        }
        return values;
    }

    public void stopTimerThread() {
        try {
            handler.removeCallbacks(myRunnable);
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "Error in Thread Stopping: ");
        }
    }
}