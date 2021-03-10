package com.synapse.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class QRCode implements Serializable {

    //local
    String qrText;
    String expendedURL;
    String taskId;

    public QRCode(String qrText, String expendedURL, String taskId) {
        this.qrText = qrText;
        this.expendedURL = expendedURL;
        this.taskId = taskId;
    }

    public String getQrText() {
        return qrText;
    }

    public void setQrText(String qrText) {
        this.qrText = qrText;
    }

    public String getExpendedURL() {
        return expendedURL;
    }

    public void setExpendedURL(String expendedURL) {
        this.expendedURL = expendedURL;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
