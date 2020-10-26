package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class UpdEusrPtrnPageAreaObject extends AbstractObject {
    private UpdEusrPtrnPageAreaData mUpdEusrPtrnPageAreaInfo;

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

    public class UpdEusrPtrnPageAreaData {
        public Result result;

        public UpdEusrPtrnPageAreaData() {
        }
    }

    public UpdEusrPtrnPageAreaData getUpdEusrPtrnPageAreaInfo() {
        return this.mUpdEusrPtrnPageAreaInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mUpdEusrPtrnPageAreaInfo = (UpdEusrPtrnPageAreaData) new Gson().fromJson(str, UpdEusrPtrnPageAreaData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
