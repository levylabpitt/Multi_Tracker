package com.synapse.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Task_data implements Serializable {

    //local
    String qrText;
    String dateTime;
    String timestamp;
    Bitmap bitmap;
    String bitmapFilePath;
    // gdrive
    String gdriveFileThumbnail;
    String gdriveFileId;
    String gdriveFileParentId;
    // asana
    String taskId;
    String beacon1_gid;
    String beacon1_RSSI_gid;
    String beacon1_URL;
    String feasybeacon_task_gid;
    String feasybeacon_UUID_gid;


    public Task_data(String qrText, String dateTime, String timestamp, Bitmap bitmap, String bitmapFilePath, String gdriveFileThumbnail, String gdriveFileId, String gdriveFileParentId, String taskId, String beacon1_gid, String beacon1_RSSI_gid, String beacon1_URL, String feasybeacon_task_gid, String feasybeacon_UUID_gid) {
        this.qrText = qrText;
        this.dateTime = dateTime;
        this.timestamp = timestamp;
        this.bitmap = bitmap;
        this.bitmapFilePath = bitmapFilePath;
        this.gdriveFileThumbnail = gdriveFileThumbnail;
        this.gdriveFileId = gdriveFileId;
        this.gdriveFileParentId = gdriveFileParentId;
        this.taskId = taskId;
        this.beacon1_gid = beacon1_gid;
        this.beacon1_RSSI_gid = beacon1_RSSI_gid;
        this.beacon1_URL = beacon1_URL;
        this.feasybeacon_task_gid = feasybeacon_task_gid;
        this.feasybeacon_UUID_gid = feasybeacon_UUID_gid;
    }

    public String getBitmapFilePath() {
        return bitmapFilePath;
    }

    public void setBitmapFilePath(String bitmapFilePath) {
        this.bitmapFilePath = bitmapFilePath;
    }

    public String getQrText() {
        return qrText;
    }

    public void setQrText(String qrText) {
        this.qrText = qrText;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBeacon1_gid() {
        return beacon1_gid;
    }

    public void setBeacon1_gid(String beacon1_gid) {
        this.beacon1_gid = beacon1_gid;
    }

    public String getBeacon1_RSSI_gid() {
        return beacon1_RSSI_gid;
    }

    public void setBeacon1_RSSI_gid(String beacon1_RSSI_gid) {
        this.beacon1_RSSI_gid = beacon1_RSSI_gid;
    }

    public String getBeacon1_URL() {
        return beacon1_URL;
    }

    public void setBeacon1_URL(String beacon1_URL) {
        this.beacon1_URL = beacon1_URL;
    }

    public String getFeasybeacon_task_gid() {
        return feasybeacon_task_gid;
    }

    public void setFeasybeacon_task_gid(String feasybeacon_task_gid) {
        this.feasybeacon_task_gid = feasybeacon_task_gid;
    }

    public String getFeasybeacon_UUID_gid() {
        return feasybeacon_UUID_gid;
    }

    public void setFeasybeacon_UUID_gid(String feasybeacon_UUID_gid) {
        this.feasybeacon_UUID_gid = feasybeacon_UUID_gid;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getGdriveFileThumbnail() {
        return gdriveFileThumbnail;
    }

    public void setGdriveFileThumbnail(String gdriveFileThumbnail) {
        this.gdriveFileThumbnail = gdriveFileThumbnail;
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
}
