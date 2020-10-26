
package com.synapse.model.tasks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextPage {

    @SerializedName("offset")
    @Expose
    private String offset;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
