package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class ContentLogObject extends AbstractObject {
    private ContentLogData mContentLogInfo;

    public class ContentLogData {
        public Result result;

        public ContentLogData() {
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

    public ContentLogData getContentLogInfo() {
        return this.mContentLogInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mContentLogInfo = (ContentLogData) new Gson().fromJson(str, ContentLogData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
