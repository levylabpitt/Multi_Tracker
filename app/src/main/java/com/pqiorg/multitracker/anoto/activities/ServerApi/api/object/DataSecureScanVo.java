package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;

public class DataSecureScanVo implements Serializable {
    public String TAPE_EXIST = "";
    public String attach_comments;
    public String attach_dt;
    public String attach_latitude;
    public String attach_longitude;
    public String attach_url;
    public String attach_user;
    public String attach_yn;
    public String code;
    public String message;
    public String page_address;
    public String resultMsg;
    public String tape_no;
    public String warn_yn;

    public String getAttach_comments() {
        return this.attach_comments;
    }

    public String getAttach_dt() {
        return this.attach_dt;
    }

    public String getAttach_latitude() {
        return this.attach_latitude;
    }

    public String getAttach_longitude() {
        return this.attach_longitude;
    }

    public String getAttach_url() {
        return this.attach_url;
    }

    public String getAttach_user() {
        return this.attach_user;
    }

    public String getAttach_yn() {
        return this.attach_yn;
    }

    public String getPage_addresse() {
        return this.page_address;
    }

    public String getTape_no() {
        return this.tape_no;
    }

    public void setAttach_comments(String str) {
        this.attach_comments = str;
    }

    public void setAttach_dt(String str) {
        this.attach_dt = str;
    }

    public void setAttach_latitude(String str) {
        this.attach_latitude = str;
    }

    public void setAttach_longitude(String str) {
        this.attach_longitude = str;
    }

    public void setAttach_url(String str) {
        this.attach_url = str;
    }

    public void setAttach_user(String str) {
        this.attach_user = str;
    }

    public void setAttach_yn(String str) {
        this.attach_yn = str;
    }

    public void setPage_address(String str) {
        this.page_address = str;
    }

    public void setTape_no(String str) {
        this.tape_no = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data{code='");
        sb.append(this.code);
        sb.append('\'');
        sb.append("message='");
        sb.append(this.message);
        sb.append('\'');
        sb.append("warn_yn='");
        sb.append(this.warn_yn);
        sb.append('\'');
        sb.append("resultMsg='");
        sb.append(this.resultMsg);
        sb.append('\'');
        sb.append("TAPE_EXIST='");
        sb.append(this.TAPE_EXIST);
        sb.append('\'');
        sb.append("attach_user='");
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
