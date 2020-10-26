package com.synapse.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ScannedData implements Serializable {
    String qr_text;
    String dateTime;
    String Timestamp;
    String gdriveFileId;
    String gdriveImageURL;
    String gdriveFileParentId;
    Bitmap bitmap;
    String bitmapFilePath;
    public ScannedData(String qr_text, String dateTime, String timestamp, String gdriveFileId, String gdriveImageURL, String gdriveFileParentId, Bitmap bitmap,String bitmapFilePath) {
        this.qr_text = qr_text;
        this.dateTime = dateTime;
        this.Timestamp = timestamp;
        this.gdriveFileId = gdriveFileId;
        this.gdriveImageURL = gdriveImageURL;
        this.gdriveFileParentId = gdriveFileParentId;
        this.  bitmap= bitmap;
        this.  bitmapFilePath= bitmapFilePath;

    }

    public String getQr_text() {
        return qr_text;
    }

    public void setQr_text(String qr_text) {
        this.qr_text = qr_text;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getGdriveImageURL() {
        return gdriveImageURL;
    }

    public void setGdriveImageURL(String gdriveImageURL) {
        this.gdriveImageURL = gdriveImageURL;
    }

    public String getGdriveFileId() {
        return gdriveFileId;
    }

    public void setGdriveFileId(String gdriveFileId) {
        this.gdriveFileId = gdriveFileId;
    }

    public String getGdriveFileParentId() {
        return gdriveFileParentId;
    }

    public void setGdriveFileParentId(String gdriveFileParentId) {
        this.gdriveFileParentId = gdriveFileParentId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBitmapFilePath() {
        return bitmapFilePath;
    }

    public void setBitmapFilePath(String bitmapFilePath) {
        this.bitmapFilePath = bitmapFilePath;
    }
}
