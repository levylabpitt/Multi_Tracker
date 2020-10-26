
package com.synapse.model.tasks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TasksResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("next_page")
    @Expose
    private NextPage nextPage;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public NextPage getNextPage() {
        return nextPage;
    }

    public void setNextPage(NextPage nextPage) {
        this.nextPage = nextPage;
    }

}
