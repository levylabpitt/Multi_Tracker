package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;

public class ScanPageAreaDataVo implements Serializable {
    private String cid;
    private String cname;
    private String ctype;
    private String curl;
    private String link_dt;
    private String link_expire_dt;

    public String getCid() {
        return this.cid;
    }

    public String getCname() {
        return this.cname;
    }

    public String getCtype() {
        return this.ctype;
    }

    public String getCurl() {
        return this.curl;
    }

    public String getLink_dt() {
        return this.link_dt;
    }

    public String getLink_expire_dt() {
        return this.link_expire_dt;
    }

    public void setCid(String str) {
        this.cid = str;
    }

    public void setCname(String str) {
        this.cname = str;
    }

    public void setCtype(String str) {
        this.ctype = str;
    }

    public void setCurl(String str) {
        this.curl = str;
    }

    public void setLink_dt(String str) {
        this.link_dt = str;
    }

    public void setLink_expire_dt(String str) {
        this.link_expire_dt = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"cid\":\"");
        sb.append(this.cid);
        sb.append("\", \"cname\":\"");
        sb.append(this.cname);
        sb.append("\", \"ctype\":\"");
        sb.append(this.ctype);
        sb.append("\", \"curl\":\"");
        sb.append(this.curl);
        sb.append("\", \"link_dt\":\"");
        sb.append(this.link_dt);
        sb.append("\", \"link_expire_dt_\":\"");
        sb.append(this.link_expire_dt);
        sb.append("\"}");
        return sb.toString();
    }
}
