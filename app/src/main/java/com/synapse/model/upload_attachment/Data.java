
package com.synapse.model.upload_attachment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("resource_subtype")
    @Expose
    private Object resourceSubtype;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;
    @SerializedName("host")
    @Expose
    private String host;
    @SerializedName("parent")
    @Expose
    private Parent parent;
    @SerializedName("view_url")
    @Expose
    private String viewUrl;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getResourceSubtype() {
        return resourceSubtype;
    }

    public void setResourceSubtype(Object resourceSubtype) {
        this.resourceSubtype = resourceSubtype;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

}
