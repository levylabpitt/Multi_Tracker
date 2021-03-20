package com.pqiorg.multitracker.feasybeacon.BlacklistedBeacon;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterBeacon extends FragmentPagerAdapter {


    private static final String[] TAB_TITLES ={"Beacons", "BlackListed"};
    private final Context mContext;

    public SectionsPagerAdapterBeacon(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {


        if (position == 0) {
            return  FragmentBeacon.newInstance();
           // return  new BeaconScannerHelp();
        } else if (position == 1) {
            return  BlacklistedBeaconFragment.newInstance();
        }

        return null;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}