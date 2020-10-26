package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class SignupEusrEmailObject extends AbstractObject {
    private SignupEusrEmailData mSignupEusrEmailInfo;

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

    public class SignupEusrEmailData {
        public Result result;

        public SignupEusrEmailData() {
        }
    }

    public SignupEusrEmailData getSignupEusrEmailInfo() {
        return this.mSignupEusrEmailInfo;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mSignupEusrEmailInfo = (SignupEusrEmailData) new Gson().fromJson(str, SignupEusrEmailData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
