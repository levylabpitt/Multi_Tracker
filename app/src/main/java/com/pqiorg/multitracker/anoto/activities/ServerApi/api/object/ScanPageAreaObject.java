package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.Serializable;
import java.util.ArrayList;

public class ScanPageAreaObject extends AbstractObject {
    private ScanPageAreaData mScanPageAreaInfo;

    public class Data implements Serializable {
        public String cid_count;
        public ArrayList<ScanPageAreaDataVo> cid_list;
        public String company_id;
        public String ctnt_editable_yn;
        public String index_in_page;
        public String owner_eusr_email;
        public String page_address;
        public String ptrn_coords_count;
        public String ptrn_coords_id;
        public String ptrn_coords_name;
        public String reg_dt;

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

    public class ScanPageAreaData {
        public Data data;
        public Result result;

        public ScanPageAreaData() {
        }
    }

    public ScanPageAreaData getmScanPageAreaInfo() {
        return this.mScanPageAreaInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mScanPageAreaInfo = (ScanPageAreaData) new Gson().fromJson(str, ScanPageAreaData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
