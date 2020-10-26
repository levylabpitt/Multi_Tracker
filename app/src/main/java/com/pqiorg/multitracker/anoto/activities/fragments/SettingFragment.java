package com.pqiorg.multitracker.anoto.activities.fragments;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*import com.anoto.adna.C0524R;
import com.anoto.adna.ServerApi.api.ADNAClient;
import com.anoto.adna.ServerApi.api.manager.SettingManager;
import com.anoto.adna.ServerApi.api.object.VersionObject.VersionData;
import com.anoto.adna.ServerApi.listener.ADNAListener;
import com.anoto.adna.activities.MainActivity;
import com.anoto.adna.global.GlobalVar;
import com.anoto.adna.sdk.util.DevLog;
import com.anoto.adna.util.BasicUtil;
import com.anoto.adna.util.PermissionUtil;*/
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.facebook.appevents.AppEventsConstants;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.ADNAClient;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.manager.SettingManager;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.VersionObject;
import com.pqiorg.multitracker.anoto.activities.ServerApi.listener.ADNAListener;
import com.pqiorg.multitracker.anoto.activities.MainActivity;
import com.pqiorg.multitracker.anoto.activities.global.GlobalVar;
import com.pqiorg.multitracker.anoto.activities.sdk.util.BasicUtil;
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;
import com.pqiorg.multitracker.anoto.activities.sdk.util.PermissionUtil;
//import com.pqiorg.multitracker.beacon.beaconreference.BeaconReferenceApplication;

public class SettingFragment extends Fragment {
    public String TAG = "SettingFragment";

    /* renamed from: a */
    GlobalVar f3026a;
    //BeaconReferenceApplication f3026a;
    /* renamed from: b */
    LinearLayout f3027b;
    private OnClickListener btClickListener = new OnClickListener() {
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0090, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x005c, code lost:
            r3.f3029a.startActivity(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0061, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onClick(View r4) {
            /*
                r3 = this;
                int r4 = r4.getId()
                switch(r4) {
                    case 2131296310: goto L_0x007e;
                    case 2131296312: goto L_0x0068;
                    case 2131296314: goto L_0x0090;
                    case 2131296318: goto L_0x0062;
                    case 2131296325: goto L_0x0038;
                    case 2131296331: goto L_0x000e;
                    case 2131296332: goto L_0x0008;
                    default: goto L_0x0007;
                }
            L_0x0007:
                return
            L_0x0008:
                com.anoto.adna.activities.fragments.SettingFragment r4 = com.anoto.adna.activities.fragments.SettingFragment.this
                r4.getVersion()
                return
            L_0x000e:
                android.content.Intent r4 = new android.content.Intent
                java.lang.String r0 = "android.intent.action.VIEW"
                r4.<init>(r0)
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "market://details?id="
                r0.append(r1)
                com.anoto.adna.activities.fragments.SettingFragment r1 = com.anoto.adna.activities.fragments.SettingFragment.this
                android.support.v4.app.FragmentActivity r1 = r1.getActivity()
                java.lang.String r1 = r1.getPackageName()
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                android.net.Uri r0 = android.net.Uri.parse(r0)
                r4.setData(r0)
                goto L_0x005c
            L_0x0038:
                android.content.Intent r4 = new android.content.Intent
                com.anoto.adna.activities.fragments.SettingFragment r0 = com.anoto.adna.activities.fragments.SettingFragment.this
                android.support.v4.app.FragmentActivity r0 = r0.getActivity()
                java.lang.Class<com.anoto.adna.activities.WebActivity> r1 = com.anoto.adna.activities.WebActivity.class
                r4.<init>(r0, r1)
                java.lang.String r0 = "url"
                com.anoto.adna.activities.fragments.SettingFragment r1 = com.anoto.adna.activities.fragments.SettingFragment.this
                android.widget.TextView r1 = r1.tvSample
                java.lang.CharSequence r1 = r1.getText()
                java.lang.String r1 = r1.toString()
                java.lang.String r1 = r1.trim()
                r4.putExtra(r0, r1)
            L_0x005c:
                com.anoto.adna.activities.fragments.SettingFragment r0 = com.anoto.adna.activities.fragments.SettingFragment.this
                r0.startActivity(r4)
                return
            L_0x0062:
                com.anoto.adna.activities.fragments.SettingFragment r4 = com.anoto.adna.activities.fragments.SettingFragment.this
                r4.confirmLogout()
                return
            L_0x0068:
                com.anoto.adna.activities.fragments.SettingFragment r4 = com.anoto.adna.activities.fragments.SettingFragment.this
                com.anoto.adna.activities.fragments.SettingFragment r0 = com.anoto.adna.activities.fragments.SettingFragment.this
                android.widget.TextView r0 = r0.tvDecode
                java.lang.Object r0 = r0.getTag()
                java.lang.Integer r0 = (java.lang.Integer) r0
                int r0 = r0.intValue()
                r4.showDialogEventDecodeMode(r0)
                return
            L_0x007e:
                com.anoto.adna.activities.fragments.SettingFragment r4 = com.anoto.adna.activities.fragments.SettingFragment.this
                android.content.Intent r0 = new android.content.Intent
                com.anoto.adna.activities.fragments.SettingFragment r1 = com.anoto.adna.activities.fragments.SettingFragment.this
                android.support.v4.app.FragmentActivity r1 = r1.getActivity()
                java.lang.Class<com.anoto.adna.activities.AboutActivity> r2 = com.anoto.adna.activities.AboutActivity.class
                r0.<init>(r1, r2)
                r4.startActivity(r0)
            L_0x0090:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.fragments.SettingFragment.C06161.onClick(android.view.View):void");
        }
    };
    /* access modifiers changed from: private */
    public View btUpdate;

    /* renamed from: c */
    ADNAListener f3028c = new ADNAListener() {
        public void onFailedToReceiveADNA(int i, String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("onFailedToReceiveADNA. ");
            sb.append(i);
            sb.append(" ");
            sb.append(str);
            DevLog.defaultLogging(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error Type: ");
            sb2.append(i);
            sb2.append(" Error Message:");
            sb2.append(str);
            DevLog.defaultLogging(sb2.toString());
            Toast.makeText(SettingFragment.this.getContext(), str, Toast.LENGTH_SHORT).show();
            SettingFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    SettingFragment.this.progressVersion.setVisibility(View.GONE);
                }
            });
        }

        public void onReceiveADNA(int i, Object obj) {
            if (i == 1) {
                SettingFragment.this.procVersion(obj);
            }
        }
    };
    private ADNAListener listener;
    private ADNAClient mApiClient;
    /* access modifiers changed from: private */
    public String[] mDecodeModeArr;
    private SettingManager mSettingManager;
    private float orgBrightness = -1.0f;
    private LayoutParams params;
    /* access modifiers changed from: private */
    public View progressVersion;
    private SwitchCompat switchBeep;
    private OnCheckedChangeListener switchChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            String str = "";
            switch (compoundButton.getId()) {
                case R.id.switch_beep /*2131296664*/:
                    str = "pref_beep";
                    break;
                case R.id.switch_history /*2131296665*/:
                    str = "pref_history_save";
                    break;
                case R.id.switch_vibration /*2131296666*/:
                    str = "pref_vibration";
                    break;
            }
            Editor edit = SettingFragment.this.getActivity().getSharedPreferences("pref_adna", 0).edit();
            edit.putBoolean(str, z);
            edit.apply();
        }
    };
    private SwitchCompat switchHistory;
    private SwitchCompat switchVibration;
    private TextView tvAccount;
    /* access modifiers changed from: private */
    public TextView tvDecode;
    private TextView tvGrayScale;
    /* access modifiers changed from: private */
    public TextView tvSample;
    private TextView tvVersion;

    /* access modifiers changed from: private */
    public void getVersion() {
        if (!PermissionUtil.isNetworkConnect(getContext())) {
            Toast.makeText(getContext(), R.string.txt_network_disconnected, Toast.LENGTH_SHORT).show();
            return;
        }
        this.progressVersion.setVisibility(View.VISIBLE);
        this.mApiClient.getVersion();
    }

    private void initADNA() {
        String string = getResources().getString(R.string.server_address);
        this.mSettingManager = SettingManager.getInstance();
        this.mSettingManager.setServerURL(string);
        this.mApiClient = ADNAClient.getInstance(getActivity());
        this.mApiClient.setADNAListener(this.f3028c);
    }

    private void setOrgBrightness() {
        this.params.screenBrightness = this.orgBrightness;
        getActivity().getWindow().setAttributes(this.params);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00ae  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void settingInfo() {
        /*
            r6 = this;
            android.support.v4.app.FragmentActivity r0 = r6.getActivity()
            java.lang.String r1 = "pref_adna"
            r2 = 0
            android.content.SharedPreferences r0 = r0.getSharedPreferences(r1, r2)
            android.support.v7.widget.SwitchCompat r1 = r6.switchBeep
            java.lang.String r3 = "pref_beep"
            r4 = 1
            boolean r3 = r0.getBoolean(r3, r4)
            r1.setChecked(r3)
            android.support.v7.widget.SwitchCompat r1 = r6.switchVibration
            java.lang.String r3 = "pref_vibration"
            boolean r3 = r0.getBoolean(r3, r4)
            r1.setChecked(r3)
            android.support.v7.widget.SwitchCompat r1 = r6.switchHistory
            java.lang.String r3 = "pref_history_save"
            boolean r3 = r0.getBoolean(r3, r4)
            r1.setChecked(r3)
            java.lang.String r1 = "pref_decode_mode"
            int r0 = r0.getInt(r1, r2)
            android.widget.TextView r1 = r6.tvDecode
            java.lang.String[] r3 = r6.mDecodeModeArr
            r3 = r3[r0]
            r1.setText(r3)
            android.widget.TextView r1 = r6.tvDecode
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r1.setTag(r0)
            java.lang.String r0 = "unknown"
            java.lang.String r1 = ""
            android.support.v4.app.FragmentActivity r3 = r6.getActivity()     // Catch:{ Exception -> 0x0066 }
            android.content.pm.PackageManager r3 = r3.getPackageManager()     // Catch:{ Exception -> 0x0066 }
            android.support.v4.app.FragmentActivity r4 = r6.getActivity()     // Catch:{ Exception -> 0x0066 }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ Exception -> 0x0066 }
            android.content.pm.PackageInfo r3 = r3.getPackageInfo(r4, r2)     // Catch:{ Exception -> 0x0066 }
            java.lang.String r4 = r3.versionName     // Catch:{ Exception -> 0x0066 }
            int r0 = r3.versionCode     // Catch:{ Exception -> 0x0067 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0067 }
            goto L_0x0068
        L_0x0066:
            r4 = r0
        L_0x0067:
            r0 = r1
        L_0x0068:
            if (r0 == 0) goto L_0x0071
            int r1 = r0.length()
            if (r1 <= 0) goto L_0x0071
            goto L_0x0073
        L_0x0071:
            java.lang.String r0 = "-"
        L_0x0073:
            android.widget.TextView r1 = r6.tvVersion
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "version "
            r3.append(r5)
            r3.append(r4)
            java.lang.String r4 = " / build "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1.setText(r0)
            android.widget.TextView r0 = r6.tvAccount
            com.anoto.adna.global.GlobalVar r1 = r6.f3026a
            java.lang.String r1 = com.anoto.adna.global.GlobalVar.USER_ACCOUNT
            r0.setText(r1)
            com.anoto.adna.global.GlobalVar r0 = r6.f3026a
            java.lang.String r0 = com.anoto.adna.global.GlobalVar.USER_ACCOUNT
            java.lang.String r1 = ""
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x00ae
            android.widget.LinearLayout r0 = r6.f3027b
            r1 = 8
            r0.setVisibility(r1)
            return
        L_0x00ae:
            android.widget.LinearLayout r0 = r6.f3027b
            r0.setVisibility(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.anoto.adna.activities.fragments.SettingFragment.settingInfo():void");
    }

    /* access modifiers changed from: private */
    public void showDialogEventDecodeMode(int i) {
        Builder builder = new Builder(getActivity());
        builder.setSingleChoiceItems(this.mDecodeModeArr, i, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i != 2) {
                    Editor edit = SettingFragment.this.getActivity().getSharedPreferences("pref_adna", 0).edit();
                    edit.putInt("pref_decode_mode", i);
                    edit.apply();
                    SettingFragment.this.tvDecode.setText(SettingFragment.this.mDecodeModeArr[i]);
                    SettingFragment.this.tvDecode.setTag(Integer.valueOf(i));
                } else {
                    SettingFragment.this.showDialogToEnterWebSite();
                }
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public void showDialogToEnterWebSite() {
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref_adna", 0);
        String string = sharedPreferences.getString("pref_manual_url", "http://");
        final EditText editText = new EditText(getActivity());
        editText.setText(string);
        editText.setSelection(editText.getText().length());
        Builder builder = new Builder(getActivity());
        builder.setTitle("Enter web site");
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                if (!URLUtil.isValidUrl(obj)) {
                    Toast.makeText(SettingFragment.this.getActivity(), "Website is not valid.", Toast.LENGTH_SHORT).show();
                }
                Editor edit = sharedPreferences.edit();
                edit.putString("pref_manual_url", obj);
                edit.putInt("pref_decode_mode", 2);
                edit.apply();
                SettingFragment.this.tvDecode.setText(SettingFragment.this.mDecodeModeArr[2]);
                SettingFragment.this.tvDecode.setTag(Integer.valueOf(2));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void confirmLogout() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = getLayoutInflater().inflate(R.layout.custom_confirm_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_ok);
        Button button2 = (Button) inflate.findViewById(R.id.btn_cancel);
        button.setText(R.string.txt_ok);
        button.setBackgroundColor(Color.parseColor("#bdbdbd"));
        button2.setText(R.string.txt_no);
        button.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));
        textView.setText(R.string.txt_msg_logout);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
                SettingFragment.this.deleteUserAccount();
                SettingFragment.this.customAlert();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void customAlert() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
        dialog.setCanceledOnTouchOutside(false);
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.custom_alret_dialog, null);
        dialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_dialog_msg);
        Button button = (Button) inflate.findViewById(R.id.btn_ok);
        textView.setText(getResources().getString(R.string.txt_msg_logout_ok));
        textView.setGravity(17);
        button.setText(R.string.txt_ok);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void deleteUserAccount() {
        try {
            Editor edit = getActivity().getSharedPreferences("eusr_info", 0).edit();
            edit.clear();
            edit.commit();
            this.f3027b.setVisibility(View.GONE);
            GlobalVar globalVar = this.f3026a;
           // BeaconReferenceApplication globalVar = this.f3026a;
            GlobalVar.USER_ACCOUNT = "";
            //BeaconReferenceApplication.USER_ACCOUNT = "";
            TextView textView = this.tvAccount;
            GlobalVar globalVar2 = this.f3026a;
           // BeaconReferenceApplication globalVar2 = this.f3026a;
         //   textView.setText(GlobalVar.USER_ACCOUNT);
            textView.setText(GlobalVar.USER_ACCOUNT);
        } catch (Exception unused) {
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        initADNA();
        MainActivity.selectNavigation(2);
        View inflate = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);
      //  this.f3026a = (GlobalVar) getActivity().getApplicationContext();
        this.f3026a = (GlobalVar) getActivity().getApplicationContext();
        try {
            this.params = getActivity().getWindow().getAttributes();
        } catch (Exception unused) {
        }
        setOrgBrightness();
        this.mDecodeModeArr = getResources().getStringArray(R.array.event_decode_mode_value);
        inflate.findViewById(R.id.bt_grayscale_method).setOnClickListener(this.btClickListener);
        inflate.findViewById(R.id.bt_decode_mode).setOnClickListener(this.btClickListener);
        inflate.findViewById(R.id.bt_sample).setOnClickListener(this.btClickListener);
        inflate.findViewById(R.id.bt_about).setOnClickListener(this.btClickListener);
        inflate.findViewById(R.id.bt_version).setOnClickListener(this.btClickListener);
        this.tvGrayScale = (TextView) inflate.findViewById(R.id.tv_grayscale_method);
        this.tvDecode = (TextView) inflate.findViewById(R.id.tv_decode_mode);
        this.tvSample = (TextView) inflate.findViewById(R.id.tv_sample);
        this.tvVersion = (TextView) inflate.findViewById(R.id.tv_version);
        this.tvAccount = (TextView) inflate.findViewById(R.id.tv_account);
        this.switchBeep = (SwitchCompat) inflate.findViewById(R.id.switch_beep);
        this.switchVibration = (SwitchCompat) inflate.findViewById(R.id.switch_vibration);
        this.switchHistory = (SwitchCompat) inflate.findViewById(R.id.switch_history);
        this.switchBeep.setOnCheckedChangeListener(this.switchChangeListener);
        this.switchVibration.setOnCheckedChangeListener(this.switchChangeListener);
        this.switchHistory.setOnCheckedChangeListener(this.switchChangeListener);
        this.f3027b = (LinearLayout) inflate.findViewById(R.id.bt_logout);
        this.f3027b.setOnClickListener(this.btClickListener);
        settingInfo();
        this.progressVersion = inflate.findViewById(R.id.pg_version);
        this.btUpdate = inflate.findViewById(R.id.bt_update);
        this.btUpdate.setOnClickListener(this.btClickListener);
        return inflate;
    }

    public void procVersion(final Object obj) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                String versionName = BasicUtil.getVersionName(SettingFragment.this.getActivity().getApplicationContext());
                VersionObject.VersionData versionData = (VersionObject.VersionData) obj;
                DevLog.defaultLogging("Version onSuccess.");
                DevLog.defaultLogging(versionData.result.toString());
                DevLog.defaultLogging(versionData.data.toString());
                SettingFragment.this.progressVersion.setVisibility(View.GONE);
                if (versionData.result.code.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("APP Version Code: ");
                    sb.append(versionName);
                    sb.append(":");
                    sb.append(versionData.data.app_version);
                    DevLog.defaultLogging(sb.toString());
                    if (!versionName.equals(versionData.data.app_version)) {
                        SettingFragment.this.btUpdate.setVisibility(View.VISIBLE);
                        return;
                    }
                    SettingFragment.this.btUpdate.setVisibility(View.GONE);
                    Toast.makeText(SettingFragment.this.getContext(), R.string.txt_version_latest, Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Code");
                sb2.append(versionData.result.code);
                sb2.append("\n");
                sb2.append(versionData.result.message);
                DevLog.defaultLogging(sb2.toString());
                SettingFragment.this.btUpdate.setVisibility(View.GONE);
            }
        });
    }
}
