package com.pqiorg.multitracker.anoto.activities.ServerApi.listener;

public interface ADNAListener {
    void onFailedToReceiveADNA(int i, String str);

    void onReceiveADNA(int i, Object obj);
}
