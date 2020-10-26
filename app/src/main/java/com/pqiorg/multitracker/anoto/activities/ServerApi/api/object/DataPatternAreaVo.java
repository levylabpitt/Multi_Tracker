package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;

public class DataPatternAreaVo implements Serializable {
    private String ctype;
    private String curl;
    private String index_in_page;
    private String link_dt;
    private String link_expire_dt;
    private String page_address;
    private String ptrn_coords_id;
    private String ptrn_coords_name;
    private String reg_dt;

    public String getCtype() {
        return this.ctype;
    }

    public String getCurl() {
        return this.curl;
    }

    public String getIndex_in_page() {
        return this.index_in_page;
    }

    public String getLink_dt() {
        return this.link_dt;
    }

    public String getLink_expire_dt() {
        return this.link_expire_dt;
    }

    public String getPage_address() {
        return this.page_address;
    }

    public String getPtrn_coords_id() {
        return this.ptrn_coords_id;
    }

    public String getPtrn_coords_name() {
        return this.ptrn_coords_name;
    }

    public String getReg_dt() {
        return this.reg_dt;
    }

    public void setCtype(String str) {
        this.ctype = str;
    }

    public void setCurl(String str) {
        this.curl = str;
    }

    public void setIndex_in_page(String str) {
        this.index_in_page = str;
    }

    public void setLink_dt(String str) {
        this.link_dt = str;
    }

    public void setLink_expire_dt(String str) {
        this.link_expire_dt = str;
    }

    public void setPage_address(String str) {
        this.page_address = str;
    }

    public void setPtrn_coords_id(String str) {
        this.ptrn_coords_id = str;
    }

    public void setPtrn_coords_name(String str) {
        this.ptrn_coords_name = str;
    }

    public void setReg_dt(String str) {
        this.reg_dt = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"page_address\":\"");
        sb.append(this.page_address);
        sb.append("\", \"ptrn_coords_id\":\"");
        sb.append(this.ptrn_coords_id);
        sb.append("\", \"ptrn_coords_name\":\"");
        sb.append(this.ptrn_coords_name);
        sb.append("\", \"index_in_page\":\"");
        sb.append(this.index_in_page);
        sb.append("\", \"reg_dt\":\"");
        sb.append(this.reg_dt);
        sb.append("\", \"curl\":\"");
        sb.append(this.curl);
        sb.append("\", \"ctype\":\"");
        sb.append(this.ctype);
        sb.append("\", \"link_dt\":\"");
        sb.append(this.link_dt);
        sb.append("\", \"link_expire_dt\":\"");
        sb.append(this.link_expire_dt);
        sb.append("\"}");
        return sb.toString();
    }
}
