package com.pqiorg.multitracker.feasybeacon.BlacklistedBeacon;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApi;
import com.feasycom.controler.FscBeaconApiImp;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.feasybeacon.Adapter.SearchDeviceListAdapter2;
import com.synapse.SharedPreferencesUtil;
import com.synapse.Utility;
import com.synapse.adapter.SpinnerBeaconListAdapter;
import com.synapse.listener.BeaconBlacklistedListener;
import com.synapse.listener.SpinnerBeaconSelectedListener;
import com.synapse.model.BlackListedBeacon;
import com.synapse.recycler_decorator.MyDividerItemDecoration;
import com.synapse.service.BeaconScannerService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class FragmentBeacon extends Fragment implements SpinnerBeaconSelectedListener {

    @BindView(R.id.imageViewScanning)
    ImageView imageViewScanning;
    @BindView(R.id.devicesList)
    ListView devicesList;



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


    @BindView(R.id.filter_beacons)
    TextView filter_beacons;


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


    ArrayList<BluetoothDeviceWrapper> mDevicesList = new ArrayList<>();
    ArrayList<String> spinnerDeviceList = new ArrayList<>();
    ArrayAdapter<String> aa;
    String allowedBeacon = "BlueCharm";
    //  String allowedBeacon="FSC_BP104";
    boolean manuallyChangedSelection = false;

    private FragmentActivity fragmentActivity;
    private BeaconBlacklistedListener beaconBlacklistedListener;
    BottomSheetDialog dialog ;
    public static Fragment newInstance() {
        FragmentBeacon frag = new FragmentBeacon();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    //    View view = inflater.inflate(R.layout.activity_main_feasybeacon, container, false);
        View view = inflater.inflate(R.layout.fragment_beacon, container, false);

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

        aa = new ArrayAdapter<String>(activity, R.layout.spinner_item, spinnerDeviceList);
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

        filter_beacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBeaconsDialog();
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
//        showAppCompatDialog(deviceWrapper);
        ShowDialog(deviceWrapper);
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

    private void ShowDialog(BluetoothDeviceWrapper deviceWrapper) {
        String name = deviceWrapper.getName() + " (" + deviceWrapper.getAddress() + ")";

        Dialog dialog =new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        dialog.setContentView(R.layout.blacklist_beacon);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView title = dialog.findViewById(R.id.device_name);
        TextView message = dialog.findViewById(R.id.message);

        message.setText("Do you want to add this beacon to Blacklist?");
        title.setText(name);
        Button yesBtn = dialog.findViewById(R.id.submit);
        Button noBtn = dialog.findViewById(R.id.cancel) ;

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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

                        dialog.dismiss();
                    }
                });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

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


            mDevicesList = (ArrayList<BluetoothDeviceWrapper>) intent.getSerializableExtra("deviceList");
            Log.e("Debugging--","mDevicesList "+mDevicesList.size());
            devicesAdapter.setDevices(mDevicesList);
            devicesAdapter.notifyDataSetChanged();
            if (getDevicesAdapter().getCount() == 0) imageViewScanning.setVisibility(View.VISIBLE);
            else imageViewScanning.setVisibility(View.GONE);
            getDevicesForSpinner();

        }
    };


    void getDevicesForSpinner() {
        if(spinnerDeviceList ==null) return;
        spinnerDeviceList.clear();
        spinnerDeviceList.add("All Beacons");
        for (BluetoothDeviceWrapper bluetoothDeviceWrapper : mDevicesList) {



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
                if (!spinnerDeviceList.contains(nameDevice)) {
                    spinnerDeviceList.add(nameDevice);
                }

            } catch (Exception e) {
                // Log.e("Error--",String.valueOf(e.getMessage()));
            }


        }

        try {
     //   showAllowedBeacons();

            showAllowedBeaconsNew();
        } catch (Exception e) {
            // Log.e("Error--",String.valueOf(e.getMessage()));
        }
    }

    void getFilterBeaconList(String beaconName) {
        ArrayList<BluetoothDeviceWrapper> mDevices_Filtered = new ArrayList<>();
        for (BluetoothDeviceWrapper bluetoothDeviceWrapper : mDevicesList) {
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

    void showAllowedBeacons() {
        aa.notifyDataSetChanged();
        if (spinnerDeviceList.size() > 0) {
            if (!manuallyChangedSelection) {
                if (spinnerDeviceList.contains(allowedBeacon)) {
                    int index = spinnerDeviceList.indexOf(allowedBeacon);
                    spinner_beacons.setSelection(index);
                }
            }
        }
        if (spinnerDeviceList.size() > 0) {
            spinner_beacons.setVisibility(View.VISIBLE);
        } else {
            spinner_beacons.setVisibility(View.INVISIBLE);
        }

    }

    void showAllowedBeaconsNew() {
        //aa.notifyDataSetChanged();
        if (spinnerDeviceList.size() > 0) {
            if (!manuallyChangedSelection) {
                if (spinnerDeviceList.contains(allowedBeacon)) {
                    int index = spinnerDeviceList.indexOf(allowedBeacon);
                    String allowedBeacon=spinnerDeviceList.get(index);
                   // onBeaconSelected(allowedBeacon);
                    filter_beacons.setText(allowedBeacon);
                    getFilterBeaconList(allowedBeacon);
                }
            }
        }


    }

    private void showBeaconsDialog() {
        View view = getLayoutInflater().inflate(R.layout.list_bottom_sheet, null);
        dialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        TextView title = view.findViewById(R.id.title);
        title.setText("Filter Beacon");

        RecyclerView filter_selector_recyclerview = view.findViewById(R.id.filter_selector_recyclerview);

        TextView textview_no_items = view.findViewById(R.id.txtvw_no_items);
        ImageView close = view.findViewById(R.id.close);
        ImageView refresh = view.findViewById(R.id.refresh);

        if(spinnerDeviceList.size()<2){
            textview_no_items.setVisibility(View.VISIBLE);
        }else {
            textview_no_items.setVisibility(View.GONE);
        }

        refresh.setVisibility(View.GONE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        filter_selector_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        filter_selector_recyclerview.setItemAnimator(new DefaultItemAnimator());
        filter_selector_recyclerview.addItemDecoration(new MyDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 0));
        filter_selector_recyclerview.setAdapter(new SpinnerBeaconListAdapter(getActivity(), spinnerDeviceList, this));


        dialog.setContentView(view);
        if(dialog!=null && !dialog.isShowing())
            dialog.show();
    }

    @Override
    public void onBeaconSelected(String beaconName) {
        manuallyChangedSelection=true;
        filter_beacons.setText(beaconName);
        getFilterBeaconList(beaconName);
        dialog.dismiss();
    }
}
