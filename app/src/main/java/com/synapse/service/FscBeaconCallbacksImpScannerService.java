package com.synapse.service;

import android.util.Log;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconCallbacksImp;

import java.lang.ref.WeakReference;
/*
import com.feasycom.fsybecon.Activity.ParameterSettingActivity;
import com.feasycom.fsybecon.Activity.SetActivity;

import java.lang.ref.WeakReference;

import static com.feasycom.fsybecon.Activity.SetActivity.OPEN_TEST_MODE;
*/

/**
 * Copyright 2017 Shenzhen Feasycom Technology co.,Ltd
 */

public class FscBeaconCallbacksImpScannerService extends FscBeaconCallbacksImp {

    private WeakReference<BeaconScannerService> weakReference;
    private boolean testDeviceFound = false;

    public FscBeaconCallbacksImpScannerService(WeakReference<BeaconScannerService> weakReference) {
        this.weakReference = weakReference;
    }

    @Override
    public void blePeripheralFound(BluetoothDeviceWrapper device, int rssi, byte[] record) {

      //  Log.e("Beacon-------> ","Scanning..........");
          if ((null != device.getgBeacon()) || (null != device.getiBeacon()) || (null != device.getAltBeacon())) {
            if ((weakReference.get() != null) && (weakReference.get().getDeviceQueue().size() < 350)) {
                weakReference.get().getDeviceQueue().offer(device);
                Log.e("Beacon-------> ","blePeripheralFound");
            }
          }
       // }

    }
}
