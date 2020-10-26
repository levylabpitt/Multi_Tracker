
package com.synapse.model.search_task;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("projects")
    @Expose
    private List<Project> projects = null;
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
     * @param projects
     * @param name
     * @param resourceType
     */
    public Datum(String gid, String name, List<Project> projects, String resourceType) {
        super();
        this.gid = gid;
        this.name = name;
        this.projects = projects;
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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

}
