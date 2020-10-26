package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

/*
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
*/

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class AddEusrPtrnPageObject extends AbstractObject {
    private AddEusrPtrnPageData mAddEusrPtrnPageInfo;

    public class AddEusrPtrnPageData {
        public Result result;

        public AddEusrPtrnPageData() {
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

    public AddEusrPtrnPageData getAddEusrPtrnPageInfo() {
        return this.mAddEusrPtrnPageInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mAddEusrPtrnPageInfo = (AddEusrPtrnPageData) new Gson().fromJson(str, AddEusrPtrnPageData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
