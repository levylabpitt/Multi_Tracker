package com.pqiorg.multitracker.anoto.activities.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.util.List;
import java.util.Locale;

public class GetGpsToAddress extends Activity {
    public String getAddr(double d, double d2, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.JAPAN);
        if (geocoder != null) {
            try {
                List fromLocation = geocoder.getFromLocation(d, d2, 1);
                if (fromLocation != null && fromLocation.size() > 0) {
                    return ((Address) fromLocation.get(0)).getAddressLine(0);
                }
            } catch (Exception e) {
                Log.d("GetGpsToAddress", e.getMessage().toString());
            }
        }
        return null;
    }
}
