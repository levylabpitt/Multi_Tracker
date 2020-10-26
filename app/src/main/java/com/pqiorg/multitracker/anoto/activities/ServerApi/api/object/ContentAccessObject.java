package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.Serializable;
import java.util.ArrayList;

public class ContentAccessObject extends AbstractObject {
    private ContentAccessData mContentAccessInfo;

    public class ContentAccessData {
        public Data data;
        public Result result;

        public ContentAccessData() {
        }
    }

    public class Data implements Serializable {
        public ArrayList<DataVo> contents_access_list;
        public String total_cnt;

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

    public ContentAccessData getContentAccessInfo() {
        return this.mContentAccessInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mContentAccessInfo = (ContentAccessData) new Gson().fromJson(str, ContentAccessData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}