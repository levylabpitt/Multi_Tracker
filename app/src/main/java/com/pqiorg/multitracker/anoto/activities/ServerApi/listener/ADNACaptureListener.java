package com.pqiorg.multitracker.anoto.activities.ServerApi.listener;

/*import com.anoto.adna.ServerApi.api.object.DataVo;
import java.util.ArrayList;*/

import com.pqiorg.multitracker.anoto.activities.ServerApi.api.object.DataVo;

import java.util.ArrayList;

public interface ADNACaptureListener extends ProtocolBase {
    void onFailedToReceiveADNA(int i, String str);

    void onReadyToCaptureView(int i);

    void onScanMultiData(ArrayList<DataVo> arrayList);

    void onScanResultImage(DataVo dataVo);

    void onScanResultMP3(DataVo dataVo);

    void onScanResultNameCard(DataVo dataVo);

    void onScanResultSchedule(DataVo dataVo);

    void onScanResultText(DataVo dataVo);

    void onScanResultVideo(DataVo dataVo);

    void onScanResultWeb(DataVo dataVo);
}
