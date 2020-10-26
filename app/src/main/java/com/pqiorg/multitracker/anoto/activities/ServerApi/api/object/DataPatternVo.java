package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;

public class DataPatternVo implements Serializable {
    private String area_cnt;
    private String page_address;
    private String reg_dt;
    private String usable_area_cnt;

    public String getArea_cnt() {
        return this.area_cnt;
    }

    public String getPage_address() {
        return this.page_address;
    }

    public String getReg_dt() {
        return this.reg_dt;
    }

    public String getUsable_area_cnt() {
        return this.usable_area_cnt;
    }

    public void setArea_cnt(String str) {
        this.area_cnt = str;
    }

    public void setPage_address(String str) {
        this.page_address = str;
    }

    public void setReg_dt(String str) {
        this.reg_dt = str;
    }

    public void setUsable_area_cnt(String str) {
        this.usable_area_cnt = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"page_address\":\"");
        sb.append(this.page_address);
        sb.append("\", \"area_cnt\":\"");
        sb.append(this.area_cnt);
        sb.append("\", \"usable_area_cnt\":\"");
        sb.append(this.usable_area_cnt);
        sb.append("\", \"reg_dt\":\"");
        sb.append(this.reg_dt);
        sb.append("\"}");
        return sb.toString();
    }
}
