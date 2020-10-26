package com.pqiorg.multitracker.feasybeacon.Controler;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconCallbacksImp;
import com.pqiorg.multitracker.feasybeacon.Activity.MainActivityBeacon2;
//import com.feasycom.fsybecon.Activity.MainActivity;

import java.lang.ref.WeakReference;

/**
 * Copyright 2017 Shenzhen Feasycom Technology co.,Ltd
 */

public class FscBeaconCallbacksImpMain2 extends FscBeaconCallbacksImp {
    private WeakReference<MainActivityBeacon2> weakReference;
    private static final String TAG = "FscBeaconCallbacksImpMa";

    public FscBeaconCallbacksImpMain2(WeakReference<MainActivityBeacon2> weakReference) {
        this.weakReference = weakReference;
    }

    @Override
    public void blePeripheralFound(BluetoothDeviceWrapper device, int rssi, byte[] record) {
        /**
         * BLE search speed is fast,please pay attention to the life cycle of the device object ,directly use the final type here
         */
        if ((null != device.getgBeacon()) || (null != device.getiBeacon()) || (null != device.getAltBeacon())) {
            if ((weakReference.get()!=null)&&(weakReference.get().getDeviceQueue().size() < 350)) {
                weakReference.get().getDeviceQueue().offer(device);
            }
        }
    }
}
