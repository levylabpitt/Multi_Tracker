package com.synapse.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.feasybeacon.TabbedActivityBeacon;
import com.pqiorg.multitracker.feasybeacon.Utils.SettingConfigUtil;
import com.synapse.HomeActivity;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.model.BlackListedBeacon;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
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
    private static List<BluetoothDeviceWrapper> mDevices = Collections.synchronizedList(new ArrayList<BluetoothDeviceWrapper>());

    private static List<BluetoothDeviceWrapper> nonBlacklistedDevices = Collections.synchronizedList(new ArrayList<BluetoothDeviceWrapper>());

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);

        // Utility.startNotification(this);
        startNotification();
    }


    void startNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);


        //  Intent notificationIntent = new Intent(this, HomeActivity.class);
        Intent notificationIntent = new Intent(this, TabbedActivityBeacon.class);


        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Beacon scanner running...")
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        notification.contentIntent = intent;


        startForeground(101, notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (mFloatingViewManager != null) {
            return START_STICKY;
        }*/
        try {
            startBeacon();
        } catch (Exception e) {
            e.getMessage();
        }
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
        try {
            fscBeaconApi = FscBeaconApiImp.getInstance();
            if (fscBeaconApi == null) return;
            fscBeaconApi.initialize();
        } catch (Exception e) {
            e.getMessage();
        }
        fscBeaconApi.setCallbacks(new FscBeaconCallbacksImpScannerService(new WeakReference<BeaconScannerService>((BeaconScannerService) this)));
        fscBeaconApi.startScan(0);


        timerUI = new Timer();
        timerTask = new UITimerTask(new WeakReference<BeaconScannerService>((BeaconScannerService) this));
        timerUI.schedule(timerTask, 0, 1500);

        /* @param task   task to be scheduled.
           @param delay  delay in milliseconds before task is to be executed.
           @param period time in milliseconds between successive task executions.
        */

    }


    class UITimerTask extends TimerTask {
        private WeakReference<BeaconScannerService> activityWeakReference;

        public UITimerTask(WeakReference<BeaconScannerService> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
        }

        @Override
        public void run() {
            addDevice(activityWeakReference.get().getDeviceQueue().poll());
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

    /*   public static void clearBeaconsList() {
           mDevices.clear();
       }
   */
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
        // nonBlacklistedDevices.clear();
        try {
            nonBlacklistedDevices = Collections.synchronizedList(new ArrayList<BluetoothDeviceWrapper>());
            if (blackListedBeacons == null || blackListedBeacons.isEmpty()) {
                nonBlacklistedDevices.addAll(mDevices);
                return;
            }
        } catch (Exception e) {
        }

        for (int i = 0; i < mDevices.size(); i++) {
            try {
                BluetoothDeviceWrapper deviceDetail = mDevices.get(i);
                for (int j = 0; j < blackListedBeacons.size(); j++) {
                    String id = blackListedBeacons.get(j).getId();
                    if (!deviceDetail.getAddress().equals(id)) {
                        nonBlacklistedDevices.add(deviceDetail);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void sendBroadcast(BluetoothDeviceWrapper deviceDetail) {
        getNonBlacklistedDevices();
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", String.valueOf(getBeaconList().size()));
        intent.putExtra("deviceDetail", deviceDetail);
        intent.putExtra("deviceList", new ArrayList<BluetoothDeviceWrapper>(nonBlacklistedDevices));  // mDevices
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

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }
}
