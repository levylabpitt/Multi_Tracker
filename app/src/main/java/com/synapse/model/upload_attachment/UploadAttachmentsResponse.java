
package com.synapse.model.upload_attachment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadAttachmentsResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
