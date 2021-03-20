package com.pqiorg.multitracker.feasybeacon;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pqiorg.multitracker.R;
import com.pqiorg.multitracker.feasybeacon.BlacklistedBeacon.BlacklistedBeaconFragment;
import com.pqiorg.multitracker.feasybeacon.BlacklistedBeacon.SectionsPagerAdapterBeacon;
import com.synapse.Utility;
import com.synapse.listener.BeaconBlacklistedListener;

public class TabbedActivityBeacon extends AppCompatActivity implements BeaconBlacklistedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_beacon);
        Utility.transparentStatusBar(this);
        SectionsPagerAdapterBeacon sectionsPagerAdapter = new SectionsPagerAdapterBeacon(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        // if (fragment != null) {
        //    fragment.onActivityResult(requestCode, resultCode, data);
        // }
    }

    @Override
    public void onItemBlacklisted() {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 1;
        BlacklistedBeaconFragment f = (BlacklistedBeaconFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.onItemBlacklisted();
    }


}