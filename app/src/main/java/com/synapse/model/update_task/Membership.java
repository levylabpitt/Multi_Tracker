
package com.synapse.model.update_task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Membership {

    @SerializedName("project")
    @Expose
    private Project_ project;
    @SerializedName("section")
    @Expose
    private Section section;

    public Project_ getProject() {
        return project;
    }

    public void setProject(Project_ project) {
        this.project = project;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

}
