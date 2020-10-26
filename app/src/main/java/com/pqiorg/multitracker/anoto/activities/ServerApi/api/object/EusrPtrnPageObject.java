package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.Serializable;
import java.util.ArrayList;

public class EusrPtrnPageObject extends AbstractObject {
    private EusrPtrnPageData mEusrPtrnPageDataInfo;

    public class Data implements Serializable {
        public String area_cnt_sum;
        public ArrayList<DataPatternVo> page_address_list;
        public String total_cnt;
        public String usable_area_cnt_sum;

        public Data() {
        }
    }

    public class EusrPtrnPageData {
        public Data data;
        public Result result;

        public EusrPtrnPageData() {
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

    public EusrPtrnPageData getEusrPtrnPageDataInfo() {
        return this.mEusrPtrnPageDataInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mEusrPtrnPageDataInfo = (EusrPtrnPageData) new Gson().fromJson(str, EusrPtrnPageData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
