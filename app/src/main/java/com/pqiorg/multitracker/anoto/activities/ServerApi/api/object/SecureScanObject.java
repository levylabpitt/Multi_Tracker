package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class SecureScanObject extends AbstractObject {
    private SecureScanData mSecureScanInfo;

    public class Data {
        public String attach_comments;
        public String attach_dt;
        public String attach_latitude;
        public String attach_longitude;
        public String attach_url;
        public String attach_user;
        public String attach_yn;
        public String page_address;
        public String tape_no;

        public Data() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Data{attach_user='");
            sb.append(this.attach_user);
            sb.append('\'');
            sb.append(", attach_url='");
            sb.append(this.attach_url);
            sb.append('\'');
            sb.append(", page_address='");
            sb.append(this.page_address);
            sb.append('\'');
            sb.append(", attach_longitude='");
            sb.append(this.attach_longitude);
            sb.append('\'');
            sb.append(", attach_dt='");
            sb.append(this.attach_dt);
            sb.append('\'');
            sb.append(", attach_comments='");
            sb.append(this.attach_comments);
            sb.append('\'');
            sb.append(", tape_no='");
            sb.append(this.tape_no);
            sb.append('\'');
            sb.append(", attach_yn='");
            sb.append(this.attach_yn);
            sb.append('\'');
            sb.append(", attach_latitude='");
            sb.append(this.attach_latitude);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
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

    public class SecureScanData {
        public Data data;
        public Result result;

        public SecureScanData() {
        }
    }

    public SecureScanData getSecureScanData() {
        return this.mSecureScanInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mSecureScanInfo = (SecureScanData) new Gson().fromJson(str, SecureScanData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
