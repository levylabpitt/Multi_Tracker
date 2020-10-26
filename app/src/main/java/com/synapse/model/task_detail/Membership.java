
package com.synapse.model.task_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Membership {

    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("section")
    @Expose
    private Section section;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

}
