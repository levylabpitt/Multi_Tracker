package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.Serializable;
import java.util.ArrayList;

public class ScanObject extends AbstractObject {
    private ScanData mScanInfo;

    public class Data implements Serializable {
        public String cid_count;
        public ArrayList<DataVo> cid_list;
        public String company_id;

        public Data() {
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

    public class ScanData {
        public Data data;
        public Result result;

        public ScanData() {
        }
    }

    public ScanData getmScanInfo() {
        return this.mScanInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mScanInfo = (ScanData) new Gson().fromJson(str, ScanData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
