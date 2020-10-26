package com.pqiorg.multitracker.anoto.activities.fragments;

import android.content.res.Resources;
import android.os.Bundle;
//import android.support.p000v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pqiorg.multitracker.R;
//import com.anoto.adna.C0524R;

public class TutorialFragment extends Fragment {
    private View vImg;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view;
        Resources resources;
        int i;
        int i2 = getArguments() != null ? getArguments().getInt("data") : -1;
        View inflate = layoutInflater.inflate(R.layout.fragment_tutorial, viewGroup, false);
        this.vImg = inflate.findViewById(R.id.v_img);
        if (i2 == 0) {
            view = this.vImg;
            resources = getResources();
            i = R.drawable.tutorial1;
        } else if (i2 == 1) {
            view = this.vImg;
            resources = getResources();
            i =R.drawable.tutorial2;
        } else if (i2 != 2) {
            return inflate;
        } else {
            view = this.vImg;
            resources = getResources();
            i = R.drawable.tutorial3;
        }
        view.setBackground(resources.getDrawable(i));
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.vImg.setBackground(null);
        System.gc();
    }
}
