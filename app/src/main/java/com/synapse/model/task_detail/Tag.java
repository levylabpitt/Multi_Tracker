package com.synapse.model.task_detail;

public class Tag {

   private String gid;
    private String name;
    private String resource_type;

    public Tag(String gid, String name, String resource_type) {
        this.gid = gid;
        this.name = name;
        this.resource_type = resource_type;
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

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }
}
