package com.pqiorg.multitracker.feasybeacon.Controler;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.util.Log;

import com.feasycom.bean.BeaconBean;
import com.feasycom.bean.CommandBean;
import com.feasycom.bean.FeasyBeacon;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconCallbacksImp;
import com.feasycom.controler.FscBleCentralApi;
import com.feasycom.controler.FscBleCentralApiImp;
/*import com.feasycom.fsybecon.Activity.MainActivity;
import com.feasycom.fsybecon.Activity.ParameterSettingActivity;
import com.feasycom.fsybecon.Activity.SetActivity;
import com.feasycom.fsybecon.Widget.InfoDialog;*/
import com.feasycom.util.ToastUtil;
import com.pqiorg.multitracker.feasybeacon.Activity.MainActivityBeacon;
import com.pqiorg.multitracker.feasybeacon.Activity.ParameterSettingActivity;
import com.pqiorg.multitracker.feasybeacon.Activity.SetActivity;
import com.pqiorg.multitracker.feasybeacon.Widget.InfoDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.pqiorg.multitracker.feasybeacon.Activity.ParameterSettingActivity.isModule_BP671;
import static com.pqiorg.multitracker.feasybeacon.Activity.SetActivity.OPEN_TEST_MODE;

import static com.pqiorg.multitracker.feasybeacon.Activity.ParameterSettingActivity.SUCESSFUL_COUNT;
/*
import static com.feasycom.fsybecon.Activity.ParameterSettingActivity.SUCESSFUL_COUNT;
import static com.feasycom.fsybecon.Activity.ParameterSettingActivity.isModule_BP671;
import static com.feasycom.fsybecon.Activity.SetActivity.OPEN_TEST_MODE;
*/


/**
 * Copyright 2017 Shenzhen Feasycom Technology co.,Ltd
 */

public class FscBeaconCallbacksImpParameter extends FscBeaconCallbacksImp {
    private WeakReference<ParameterSettingActivity> parameterSettingActivityWeakReference;
    private FscBeaconApi fscBeaconApi;
    private String moduleString;
    private FeasyBeacon fb;
    private FscBleCentralApi fscBleCentralApi = FscBleCentralApiImp.getInstance();
    private CommandBean commandBean = new CommandBean();

    public static boolean state = false;

    public FscBeaconCallbacksImpParameter(WeakReference<ParameterSettingActivity> parameterSettingActivityWeakReference, FscBeaconApi fscBeaconApi, String moduleString, FeasyBeacon fb) {
        this.parameterSettingActivityWeakReference = parameterSettingActivityWeakReference;
        this.fscBeaconApi = fscBeaconApi;
        this.moduleString = moduleString;
        this.fb = fb;
    }

    @Override
    public void blePeripheralConnected(BluetoothGatt gatt, BluetoothDevice device) {
        if (parameterSettingActivityWeakReference.get().getPin2Connect() == null) {
            fscBeaconApi.startGetDeviceInfo(moduleString, fb);

        }
        SUCESSFUL_COUNT++;
    }

    @Override
    public void connectProgressUpdate(BluetoothDevice device, int status) {
        if (parameterSettingActivityWeakReference.get() == null) {
            return;
        }
        if (status == CommandBean.PASSWORD_CHECK) {
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("check password...");
        } else if (status == CommandBean.PASSWORD_SUCCESSFULE) {
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("password sucessful...");
            fscBeaconApi.startGetDeviceInfo(moduleString, fb);
        } else if (status == CommandBean.PASSWORD_FAILED) {
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("password failed...");
            Log.e(TAG, "connectProgressUpdate: " + "1");
            parameterSettingActivityWeakReference.get().connectFailedHandler();
        } else if (status == CommandBean.PASSWORD_TIME_OUT) {
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("timeout");
            Log.e(TAG, "connectProgressUpdate: " + "2");
            parameterSettingActivityWeakReference.get().connectFailedHandler();
        }
    }


    @Override
    public void blePeripheralDisonnected(BluetoothGatt gatt, BluetoothDevice device) {
        if (parameterSettingActivityWeakReference.get() == null) {
            return;
        }
        ToastUtil.show(parameterSettingActivityWeakReference.get(), "disconnect!");
        state = false;
        parameterSettingActivityWeakReference.get().connectFailedHandler();
    }

    @Override
    public void atCommandCallBack(String command, String param, String status) {
        if (parameterSettingActivityWeakReference.get() == null) {
            return;
        }
        // Log.e("atCommandCallBack",command+"");
        /**
         * get module information and save module information are through the AT command to configure
         * after saving the module information you can do something
         */
        if (CommandBean.COMMAND_FINISH.equals(status) && "save...".equals(parameterSettingActivityWeakReference.get().getConnectDialog().getInfo())) {
            /**
             *  if you want to switch activity please wait a moment for release service connection
             */
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("finish...");
            Log.e(TAG, "atCommandCallBack: " + "9");
            fscBeaconApi.disconnect();
            parameterSettingActivityWeakReference.get().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (parameterSettingActivityWeakReference.get().getConnectDialog().isShowing()) {
                        parameterSettingActivityWeakReference.get().getConnectDialog().dismiss();
                    }
                  /* if (OPEN_TEST_MODE) {
                        SetActivity.actionStart(parameterSettingActivityWeakReference.get().getActivity());
                    } else {
                        MainActivity.actionStart(parameterSettingActivityWeakReference.get().getActivity());
                    }*/
                    parameterSettingActivityWeakReference.get().finishActivity();
                    Log.i("finish", "1");
                }
            }, InfoDialog.INFO_DIAOLOG_SHOW_TIME);
        }

        if (CommandBean.COMMAND_TIME_OUT.equals(status) && "save...".equals(parameterSettingActivityWeakReference.get().getConnectDialog().getInfo())) {
            /**
             *  if you want to switch activity please wait a moment for release service connection
             */
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("timeout...");
            Log.e(TAG, "atCommandCallBack: " + "10");
            fscBeaconApi.disconnect();
            parameterSettingActivityWeakReference.get().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    parameterSettingActivityWeakReference.get().getConnectDialog().dismiss();
                    if (OPEN_TEST_MODE) {
                        SetActivity.actionStart(parameterSettingActivityWeakReference.get().getActivity());
                    } else {
                        MainActivityBeacon.actionStart(parameterSettingActivityWeakReference.get().getActivity());
                    }
                    parameterSettingActivityWeakReference.get().finishActivity();
                    Log.i("timeout", "1");
                }
            }, InfoDialog.INFO_DIAOLOG_SHOW_TIME);
        }

        if (CommandBean.COMMAND_FINISH.equals(status) && "connected".equals(parameterSettingActivityWeakReference.get().getConnectDialog().getInfo())) {
            // fscBeaconApi.setBuzzer(true);
            parameterSettingActivityWeakReference.get().getConnectDialog().dismiss();
            parameterSettingActivityWeakReference.get().getHandler().removeCallbacks(parameterSettingActivityWeakReference.get().getCheckConnect());
            if (OPEN_TEST_MODE) {
                parameterSettingActivityWeakReference.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("save...");
                        parameterSettingActivityWeakReference.get().getConnectDialog().show();
                        parameterSettingActivityWeakReference.get().getFscBeaconApi().saveBeaconInfo();
                    }
                }, 2000);
            }

        } else {
            /**
             * parameter modification success or failure here to deal with
             * eg command  AT+NAME=123  status COMMAND_SUCCESSFUL
             */
            if (CommandBean.COMMAND_TIME_OUT.equals(status)) {
//                Log.i("timeout", command);
            } else if (CommandBean.COMMAND_FAILED.equals(status)) {
//                Log.i("failed", command);
            } else if (CommandBean.COMMAND_SUCCESSFUL.equals(status)) {
//                Log.i("success", command);
            } else if (CommandBean.COMMAND_NO_NEED.equals(status)) {
//                Log.i("no_need", command);
            }
        }
    }

    private static final String TAG = "FscBeaconCallbacksImpPa";

    @Override
    public void deviceInfo(final String parameterName, final Object parameter) {
        Log.e(TAG, "deviceInfo: " + parameterName);
        if (parameterSettingActivityWeakReference.get() == null) {
            return;
        }
        if (CommandBean.COMMAND_BEGIN.equals(parameterName)) {
            parameterSettingActivityWeakReference.get().getConnectDialog().setInfo("connected");
            // fscBeaconApi.setBuzzer(true);`
        } else if (CommandBean.COMMAND_MODEL.equals(parameterName)) {
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parameterSettingActivityWeakReference.get().getModule().setText((String) parameter, false);
                }
            });
        } else if (CommandBean.COMMAND_VERSION.equals(parameterName)) {
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parameterSettingActivityWeakReference.get().getVersion().setText((String) parameter, false);
                }
            });
        } else if (CommandBean.COMMAND_NAME.equals(parameterName)) {
            Log.e(TAG, "deviceInfo: " + (String) parameter);
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parameterSettingActivityWeakReference.get().getName().setText((String) parameter, true);
                }
            });
        } else if (CommandBean.COMMAND_ADVIN.equals(parameterName)) {
            Integer data = Integer.valueOf((String) parameter);
            Log.e(TAG, "deviceInfo: " + data);
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (fb.getGsensor() || fb.getKeycfg()) {
                        if (Integer.parseInt((String) parameter) == 0) {                                                                    //100ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(0);
                        } else if (Integer.parseInt((String) parameter) == 100) {                                                            //100ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(1);
                        } else if (Integer.parseInt((String) parameter) >= 100 && Integer.parseInt((String) parameter) <= 199) {             //152ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(2);
                        } else if (Integer.parseInt((String) parameter) >= 200 && Integer.parseInt((String) parameter) <= 299) {             //211ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(3);
                        } else if (Integer.parseInt((String) parameter) >= 300 && Integer.parseInt((String) parameter) <= 399) {             //318ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(4);
                        } else if (Integer.parseInt((String) parameter) >= 400 && Integer.parseInt((String) parameter) <= 499) {             //417ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(5);
                        } else if (Integer.parseInt((String) parameter) >= 500 && Integer.parseInt((String) parameter) <= 599) {             //546ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(6);
                        } else if (Integer.parseInt((String) parameter) >= 600 && Integer.parseInt((String) parameter) <= 799) {             //760ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(7);
                        } else if (Integer.parseInt((String) parameter) >= 800 && Integer.parseInt((String) parameter) <= 899) {             //852ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(8);
                        } else if (Integer.parseInt((String) parameter) >= 900 && Integer.parseInt((String) parameter) <= 1099) {            //1022ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(9);
                        } else if (Integer.parseInt((String) parameter) >= 1100 && Integer.parseInt((String) parameter) <= 1499) {           //1280ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(10);
                        } else if (Integer.parseInt((String) parameter) >= 1499 && Integer.parseInt((String) parameter) <= 2000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(11);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 2000 && Integer.parseInt((String) parameter) <= 3000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(12);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 3000 && Integer.parseInt((String) parameter) <= 4000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(13);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 4000 && Integer.parseInt((String) parameter) <= 5000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(14);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 5000 && Integer.parseInt((String) parameter) <= 6000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(15);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 6000 && Integer.parseInt((String) parameter) <= 7000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(16);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 7000 && Integer.parseInt((String) parameter) <= 8000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(17);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 8000 && Integer.parseInt((String) parameter) <= 9000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(18);        //2000ms
                        } else if (Integer.parseInt((String) parameter) > 9000) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(19);        //2000ms
                        }
                    } else {
                        if (Integer.parseInt((String) parameter) == 100) {             //152ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(0);
                        } else if (Integer.parseInt((String) parameter) >= 100 && Integer.parseInt((String) parameter) <= 199) {             //152ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(1);
                        } else if (Integer.parseInt((String) parameter) >= 200 && Integer.parseInt((String) parameter) <= 299) {             //211ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(2);
                        } else if (Integer.parseInt((String) parameter) >= 300 && Integer.parseInt((String) parameter) <= 399) {             //318ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(3);
                        } else if (Integer.parseInt((String) parameter) >= 400 && Integer.parseInt((String) parameter) <= 499) {             //417ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(4);
                        } else if (Integer.parseInt((String) parameter) >= 500 && Integer.parseInt((String) parameter) <= 599) {             //546ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(5);
                        } else if (Integer.parseInt((String) parameter) >= 600 && Integer.parseInt((String) parameter) <= 799) {             //760ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(6);
                        } else if (Integer.parseInt((String) parameter) >= 800 && Integer.parseInt((String) parameter) <= 899) {             //852ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(7);
                        } else if (Integer.parseInt((String) parameter) >= 900 && Integer.parseInt((String) parameter) <= 1099) {            //1022ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(8);
                        } else if (Integer.parseInt((String) parameter) >= 1100 && Integer.parseInt((String) parameter) <= 1499) {           //1280ms
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(9);
                        } else if (Integer.parseInt((String) parameter) >= 1499) {
                            parameterSettingActivityWeakReference.get().getAdvInterval().setSelect(10);        //2000ms
                        }
                    }

                }
            });

            /*
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parameterSettingActivityWeakReference.get().getInterval().setText((String) parameter, true);
                }
            });
            */
        } else if (CommandBean.COMMAND_KEYCFG.equals(parameterName)) {
            String[] s = (String[]) parameter;
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (Integer.parseInt(s[0]) == 0) {                                                      //0ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(0);
                    } else if (Integer.parseInt(s[0]) == 100) {                                              //100ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(1);
                    } else if (Integer.parseInt(s[0]) > 100 && Integer.parseInt(s[0]) <= 160) {             //152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(2);
                    } else if (Integer.parseInt(s[0]) > 160 && Integer.parseInt(s[0]) <= 220) {             //211ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(3);
                    } else if (Integer.parseInt(s[0]) > 220 && Integer.parseInt(s[0]) <= 350) {             //318ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(4);
                    } else if (Integer.parseInt(s[0]) > 350 && Integer.parseInt(s[0]) <= 450) {             //417ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(5);
                    } else if (Integer.parseInt(s[0]) > 450 && Integer.parseInt(s[0]) <= 600) {             //546ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(6);
                    } else if (Integer.parseInt(s[0]) > 600 && Integer.parseInt(s[0]) <= 800) {             //760ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(7);
                    } else if (Integer.parseInt(s[0]) > 800 && Integer.parseInt(s[0]) <= 900) {             //852ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(8);
                    } else if (Integer.parseInt(s[0]) > 900 && Integer.parseInt(s[0]) <= 1100) {            //1022ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(9);
                    } else if (Integer.parseInt(s[0]) > 1100 && Integer.parseInt(s[0]) <= 1300) {           //1280ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(10);
                    } else if (Integer.parseInt(s[0]) > 1300 && Integer.parseInt(s[0]) <= 2000) {
                        parameterSettingActivityWeakReference.get().getKeycfg().setAdvinSelect(11);        //2000ms
                    }

                    if (Integer.parseInt(s[1]) >= 1000 && Integer.parseInt(s[1]) <= 4999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(1);//150ms
                    } else if (Integer.parseInt(s[1]) >= 4999 && Integer.parseInt(s[1]) <= 9999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(2);//150ms
                    } else if (Integer.parseInt(s[1]) >= 10000 && Integer.parseInt(s[1]) <= 14999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(3);//150ms
                    } else if (Integer.parseInt(s[1]) >= 15000 && Integer.parseInt(s[1]) <= 19999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(4);//150ms
                    } else if (Integer.parseInt(s[1]) >= 20000 && Integer.parseInt(s[1]) <= 24999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(5);//150ms
                    } else if (Integer.parseInt(s[1]) >= 25000 && Integer.parseInt(s[1]) <= 29999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(6);//150ms
                    } else if (Integer.parseInt(s[1]) >= 30000 && Integer.parseInt(s[1]) <= 34999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(7);//150ms
                    } else if (Integer.parseInt(s[1]) >= 35000 && Integer.parseInt(s[1]) <= 39999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(8);//150ms
                    } else if (Integer.parseInt(s[1]) >= 40000 && Integer.parseInt(s[1]) <= 44999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(9);//150ms
                    } else if (Integer.parseInt(s[1]) >= 45000 && Integer.parseInt(s[1]) <= 49999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(10);//150ms
                    } else if (Integer.parseInt(s[1]) >= 50000 && Integer.parseInt(s[1]) <= 54999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(11);//150ms
                    } else if (Integer.parseInt(s[1]) >= 55000 && Integer.parseInt(s[1]) <= 59999) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(12);//150ms
                    } else if (Integer.parseInt(s[1]) >= 60000) {//152ms
                        parameterSettingActivityWeakReference.get().getKeycfg().setDurationSelect(13);//150ms
                    }
                }
            });
        } else if (CommandBean.COMMAND_GSCFG.equals(parameterName)) {
            String[] s = (String[]) parameter;
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (Integer.parseInt(s[0]) == 0) {                                                      //0ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(0);
                    } else if (Integer.parseInt(s[0]) > 0 && Integer.parseInt(s[0]) <= 100) {                //100ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(1);
                    } else if (Integer.parseInt(s[0]) > 100 && Integer.parseInt(s[0]) <= 199) {             //152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(2);
                    } else if (Integer.parseInt(s[0]) >= 200 && Integer.parseInt(s[0]) <= 299) {             //211ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(3);
                    } else if (Integer.parseInt(s[0]) >= 300 && Integer.parseInt(s[0]) <= 399) {             //318ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(4);
                    } else if (Integer.parseInt(s[0]) >= 400 && Integer.parseInt(s[0]) <= 499) {             //417ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(5);
                    } else if (Integer.parseInt(s[0]) >= 500 && Integer.parseInt(s[0]) <= 599) {             //546ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(6);
                    } else if (Integer.parseInt(s[0]) >= 600 && Integer.parseInt(s[0]) <= 799) {             //760ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(7);
                    } else if (Integer.parseInt(s[0]) >= 800 && Integer.parseInt(s[0]) <= 899) {             //852ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(8);
                    } else if (Integer.parseInt(s[0]) >= 900 && Integer.parseInt(s[0]) <= 1099) {            //1022ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(9);
                    } else if (Integer.parseInt(s[0]) >= 1100 && Integer.parseInt(s[0]) <= 1599) {           //1280ms
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(10);
                    } else if (Integer.parseInt(s[0]) >= 1599) {
                        parameterSettingActivityWeakReference.get().getGsensor().setAdvinSelect(11);        //2000ms
                    }

                    if (Integer.parseInt(s[1]) >= 1000 && Integer.parseInt(s[1]) <= 4999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(1);//150ms
                    } else if (Integer.parseInt(s[1]) >= 4999 && Integer.parseInt(s[1]) <= 9999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(2);//150ms
                    } else if (Integer.parseInt(s[1]) >= 10000 && Integer.parseInt(s[1]) <= 14999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(3);//150ms
                    } else if (Integer.parseInt(s[1]) >= 15000 && Integer.parseInt(s[1]) <= 19999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(4);//150ms
                    } else if (Integer.parseInt(s[1]) >= 20000 && Integer.parseInt(s[1]) <= 24999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(5);//150ms
                    } else if (Integer.parseInt(s[1]) >= 25000 && Integer.parseInt(s[1]) <= 29999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(6);//150ms
                    } else if (Integer.parseInt(s[1]) >= 30000 && Integer.parseInt(s[1]) <= 34999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(7);//150ms
                    } else if (Integer.parseInt(s[1]) >= 35000 && Integer.parseInt(s[1]) <= 39999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(8);//150ms
                    } else if (Integer.parseInt(s[1]) >= 40000 && Integer.parseInt(s[1]) <= 44999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(9);//150ms
                    } else if (Integer.parseInt(s[1]) >= 45000 && Integer.parseInt(s[1]) <= 49999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(10);//150ms
                    } else if (Integer.parseInt(s[1]) >= 50000 && Integer.parseInt(s[1]) <= 54999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(11);//150ms
                    } else if (Integer.parseInt(s[1]) >= 55000 && Integer.parseInt(s[1]) <= 59999) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(12);//150ms
                    } else if (Integer.parseInt(s[1]) >= 60000) {//152ms
                        parameterSettingActivityWeakReference.get().getGsensor().setDurationSelect(13);//150ms
                    }
                }
            });
        } else if (CommandBean.COMMAND_BUZ.equals(parameterName)) {

        } else if (CommandBean.COMMAND_LED.equals(parameterName)) {

        } else if (CommandBean.COMMAND_BWMODE.equals(parameterName)) {
            /**
             * if use the password ,open the connectable directly
             */
            if (FeasyBeacon.BLE_KEY_WAY.equals(parameterSettingActivityWeakReference.get().getEncryptWay())) {
                parameterSettingActivityWeakReference.get().getConnectable().setCheck(true);
            } else if (CommandBean.COMMAND_BWMODE_OPEN.equals((String) parameter)) {
                parameterSettingActivityWeakReference.get().getConnectable().setCheck(true);
            } else {
                parameterSettingActivityWeakReference.get().getConnectable().setCheck(false);
            }
        } else if (CommandBean.COMMAND_PIN.equals(parameterName)) {
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String temp = (String) parameter;
                    if (temp.length() == 1) {
                        parameterSettingActivityWeakReference.get().getPIN().setText("00000" + (String) parameter, true);
                    } else if (temp.length() == 2) {
                        parameterSettingActivityWeakReference.get().getPIN().setText("0000" + (String) parameter, true);
                    } else if (temp.length() == 3) {
                        parameterSettingActivityWeakReference.get().getPIN().setText("000" + (String) parameter, true);
                    } else if (temp.length() == 4) {
                        parameterSettingActivityWeakReference.get().getPIN().setText("00" + (String) parameter, true);
                    } else if (temp.length() == 5) {
                        parameterSettingActivityWeakReference.get().getPIN().setText("0" + (String) parameter, true);
                    } else {
                        parameterSettingActivityWeakReference.get().getPIN().setText((String) parameter, true);
                    }
                }
            });
        } else if (CommandBean.COMMAND_TX_POWER.equals(parameterName)) {
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (isModule_BP671 == true) {
                        String BP671[] = {"-200", "-107", "-95", "-82", "-70", "-65", "-5", "30", "45", "53", "60", "71", "80", "92", "102", "110", "120", "130", "141", "150", "160", "170", "181", "190", "200"};
                        for (int i = 0; i < BP671.length; i++) {
                            if (BP671[i].equals((String) parameter)) {
                                Log.e(TAG, "run: " + i);
                                parameterSettingActivityWeakReference.get().getTxPower().setSelect(i + 1);
                                break;
                            }
                        }
                    } else {
                        parameterSettingActivityWeakReference.get().getTxPower().setSelect(Integer.valueOf((String) parameter).intValue() + 1);
                    }
                }
            });
        } else if (CommandBean.COMMAND_BADVDATA.equals(parameterName)) {
            /**
             *  after each connection is successful SDK internal cache  broadcast information
             *  please ensure that your operation on the broadcast information is cached data for the SDK
             */
            parameterSettingActivityWeakReference.get().getAdapter().updateAllBeacon((ArrayList<BeaconBean>) parameter);
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (fscBeaconApi.isBeaconInfoFull()) {
                        parameterSettingActivityWeakReference.get().addBeaconEnable(false);
                    }
                    parameterSettingActivityWeakReference.get().getAdapter().notifyDataSetChanged();
                }
            });
        } else if (CommandBean.COMMAND_EXTEND.equals(parameterName)) {
            parameterSettingActivityWeakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parameterSettingActivityWeakReference.get().getExtEnd().setText((String) parameter);
                }
            });
        }
        state = true;

    }

}
