
package com.synapse.model.create_project_by_workspace;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateProjectByWorkspace {

    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CreateProjectByWorkspace() {
    }

    /**
     * 
     * @param data
     */
    public CreateProjectByWorkspace(Data data) {
        super();
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
