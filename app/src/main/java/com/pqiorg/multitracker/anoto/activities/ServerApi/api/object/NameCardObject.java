package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class NameCardObject extends AbstractObject {
    private NameCardData mNameCardInfo;

    public class NameCardData {
        public DataNameCardVo data;
        public Result result;

        public NameCardData() {
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

    public NameCardData getNameCardInfo() {
        return this.mNameCardInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mNameCardInfo = (NameCardData) new Gson().fromJson(str, NameCardData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
