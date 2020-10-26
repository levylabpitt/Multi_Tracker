package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class VersionObject extends AbstractObject {
    private VersionData mVersionInfo;

    public class Data {
        public String app_id;
        public String app_version;
        public String build_number;
        public String company_id;
        public String company_name;
        public String force_update_yn;

        public Data() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Data{app_id='");
            sb.append(this.app_id);
            sb.append('\'');
            sb.append(", app_version='");
            sb.append(this.app_version);
            sb.append('\'');
            sb.append(", build_number='");
            sb.append(this.build_number);
            sb.append('\'');
            sb.append(", force_update_yn='");
            sb.append(this.force_update_yn);
            sb.append('\'');
            sb.append(", company_id='");
            sb.append(this.company_id);
            sb.append('\'');
            sb.append(", company_name='");
            sb.append(this.company_name);
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

    public class VersionData {
        public Data data;
        public Result result;

        public VersionData() {
        }
    }

    public VersionData getVersionData() {
        return this.mVersionInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mVersionInfo = (VersionData) new Gson().fromJson(str, VersionData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
