package com.pqiorg.multitracker.help.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pqiorg.multitracker.R;
import com.synapse.Utility;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagAnchorsHelp extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help_tag_anchors, container, false);
        ButterKnife.bind(this, view);
        ImageView img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getActivity().onBackPressed();
                } catch (Exception e) {
                }
            }
        });

        return view;
    }

    @OnClick({
            R.id.screenshot_1,
            //, R.id.button_save, R.id.button_cancel
    })
    public void onClick(@NonNull View view) {
        if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            Drawable myDrawable = iv.getDrawable();
            Utility.show_preview(getActivity(),myDrawable);
        }}
}