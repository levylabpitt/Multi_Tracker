
package com.synapse.model.create_project_by_workspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Owner() {
    }

    /**
     * 
     * @param gid
     * @param name
     * @param resourceType
     */
    public Owner(String gid, String resourceType, String name) {
        super();
        this.gid = gid;
        this.resourceType = resourceType;
        this.name = name;
    }

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
