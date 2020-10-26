
package com.synapse.model.search_project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Datum() {
    }

    /**
     * 
     * @param gid
     * @param name
     * @param resourceType
     */
    public Datum(String gid, String name, String resourceType) {
        super();
        this.gid = gid;
        this.name = name;
        this.resourceType = resourceType;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

}
