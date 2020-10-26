package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;

public class DataVo implements Serializable {
    private String access_no;
    private String cid;
    private String cname;
    private String company_id;
    private String company_name;
    private String ctype;
    private String curl;
    private String device_id;
    private String device_type;
    private DataEventVo event;
    private String last_access_dt;
    private String last_city;
    private String last_country_cd;
    private DataNameCardVo nameCard;

    public String getAccess_no() {
        return this.access_no;
    }

    public String getCid() {
        return this.cid;
    }

    public String getCname() {
        return this.cname;
    }

    public String getCompany_id() {
        return this.company_id;
    }

    public String getCompany_name() {
        return this.company_name;
    }

    public String getCtype() {
        return this.ctype;
    }

    public String getCurl() {
        return this.curl;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public String getDevice_type() {
        return this.device_type;
    }

    public DataEventVo getEvent() {
        return this.event;
    }

    public String getLast_access_dt() {
        return this.last_access_dt;
    }

    public String getLast_city() {
        return this.last_city;
    }

    public String getLast_country_cd() {
        return this.last_country_cd;
    }

    public DataNameCardVo getNameCard() {
        return this.nameCard;
    }

    public void setAccess_no(String str) {
        this.access_no = str;
    }

    public void setCid(String str) {
        this.cid = str;
    }

    public void setCname(String str) {
        this.cname = str;
    }

    public void setCompany_id(String str) {
        this.company_id = str;
    }

    public void setCompany_name(String str) {
        this.company_name = str;
    }

    public void setCtype(String str) {
        this.ctype = str;
    }

    public void setCurl(String str) {
        this.curl = str;
    }

    public void setDevice_id(String str) {
        this.device_id = str;
    }

    public void setDevice_type(String str) {
        this.device_type = str;
    }

    public void setEvent(DataEventVo dataEventVo) {
        this.event = dataEventVo;
    }

    public void setLast_access_dt(String str) {
        this.last_access_dt = str;
    }

    public void setLast_city(String str) {
        this.last_city = str;
    }

    public void setLast_country_cd(String str) {
        this.last_country_cd = str;
    }

    public void setNameCard(DataNameCardVo dataNameCardVo) {
        this.nameCard = dataNameCardVo;
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
        sb.append("\", \"company_id\":\"");
        sb.append(this.company_id);
        sb.append("\", \"company_name\":\"");
        sb.append(this.company_name);
        sb.append("\", \"last_access_dt\":\"");
        sb.append(this.last_access_dt);
        sb.append("\", \"last_country_cd\":\"");
        sb.append(this.last_country_cd);
        sb.append("\", \"last_city\":\"");
        sb.append(this.last_city);
        sb.append("\", \"device_id\":\"");
        sb.append(this.device_id);
        sb.append("\", \"device_type\":\"");
        sb.append(this.device_type);
        sb.append("\", \"access_no\":\"");
        sb.append(this.access_no);
        sb.append("\"}");
        return sb.toString();
    }
}
