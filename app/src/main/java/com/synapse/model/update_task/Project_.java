
package com.synapse.model.update_task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project_ {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("name")
    @Expose
    private String name;

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

}
