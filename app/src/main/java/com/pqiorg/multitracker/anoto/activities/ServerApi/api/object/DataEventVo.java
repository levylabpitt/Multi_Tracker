package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import java.io.Serializable;

public class DataEventVo implements Serializable {
    public String description;
    public String end_dt;
    public String event_loc;
    public String start_dt;
    public String title;

    public String getDescription() {
        return this.description;
    }

    public String getEnd_dt() {
        return this.end_dt;
    }

    public String getEvent_loc() {
        return this.event_loc;
    }

    public String getStart_dt() {
        return this.start_dt;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setEnd_dt(String str) {
        this.end_dt = str;
    }

    public void setEvent_loc(String str) {
        this.event_loc = str;
    }

    public void setStart_dt(String str) {
        this.start_dt = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"start_dt\":\"");
        sb.append(this.start_dt);
        sb.append("\", \"end_dt\":\"");
        sb.append(this.end_dt);
        sb.append("\", \"event_loc\":\"");
        sb.append(this.event_loc);
        sb.append("\", \"title\":\"");
        sb.append(this.title);
        sb.append("\", \"description\":\"");
        sb.append(this.description);
        sb.append("\"}");
        return sb.toString();
    }
}
