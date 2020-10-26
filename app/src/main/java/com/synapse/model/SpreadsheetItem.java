package com.synapse.model;

public class SpreadsheetItem {

    String Name;
    String id;
    String url;
    String dateCreated;


    public SpreadsheetItem() {
    }

    public SpreadsheetItem(String name, String id, String url, String dateCreated) {
        Name = name;
        this.id = id;
        this.url = url;
        this.dateCreated = dateCreated;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


}
