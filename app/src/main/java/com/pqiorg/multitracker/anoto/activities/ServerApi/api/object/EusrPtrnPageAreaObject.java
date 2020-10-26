package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.Serializable;
import java.util.ArrayList;

public class EusrPtrnPageAreaObject extends AbstractObject {
    private EusrPtrnPageAreaData mEusrPtrnPageAreaDataInfo;

    public class Data implements Serializable {
        public ArrayList<DataPatternAreaVo> ptrn_area_list;
        public String total_cnt;

        public Data() {
        }
    }

    public class EusrPtrnPageAreaData {
        public Data data;
        public Result result;

        public EusrPtrnPageAreaData() {
        }
    }

    public class Result {
        public String code;
        public String message;

        public Result() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Result{code='");
            sb.append(this.code);
            sb.append('\'');
            sb.append(", message='");
            sb.append(this.message);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public EusrPtrnPageAreaData getEusrPtrnPageAreaDataInfo() {
        return this.mEusrPtrnPageAreaDataInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mEusrPtrnPageAreaDataInfo = (EusrPtrnPageAreaData) new Gson().fromJson(str, EusrPtrnPageAreaData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
