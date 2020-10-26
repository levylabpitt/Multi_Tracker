package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;
import java.util.ArrayList;

public class DataNameCardVo implements Serializable {
    public ArrayList<Addr> addr;
    private String email;
    private String fname;
    private String name;

    /* renamed from: org reason: collision with root package name */
    private String f5623org;
    private String photo;
    public ArrayList<Tel> tel;
    private String title;

    public class Addr implements Serializable {
        public String addr_label;
        public String addr_text;

        public Addr() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Addr{addr_label='");
            sb.append(this.addr_label);
            sb.append('\'');
            sb.append(", addr_text='");
            sb.append(this.addr_text);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public class Tel implements Serializable {
        public String tel_no;
        public String tel_type;

        public Tel() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Tel{tel_type='");
            sb.append(this.tel_type);
            sb.append('\'');
            sb.append(", tel_no='");
            sb.append(this.tel_no);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public String getEmail() {
        return this.email;
    }

    public String getFname() {
        return this.fname;
    }

    public String getName() {
        return this.name;
    }

    public String getOrg() {
        return this.f5623org;
    }

    public String getPhoto() {
        return this.photo;
    }

    public String getTitle() {
        return this.title;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setFname(String str) {
        this.fname = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setOrg(String str) {
        this.f5623org = str;
    }

    public void setPhoto(String str) {
        this.photo = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"name\":\"");
        sb.append(this.name);
        sb.append("\", \"fname\":\"");
        sb.append(this.fname);
        sb.append("\", \"org\":\"");
        sb.append(this.f5623org);
        sb.append("\", \"title\":\"");
        sb.append(this.title);
        sb.append("\", \"photo\":\"");
        sb.append(this.photo);
        sb.append("\"}");
        return sb.toString();
    }
}
