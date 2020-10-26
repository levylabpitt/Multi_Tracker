package com.synapse.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;


@Entity//(primaryKeys = {"timestamp"})
public class TaskData implements Serializable {
// this class will be used for passing object to another activity via intent as well as an entity class for Room DB
    @PrimaryKey(autoGenerate = true)
    private int id;

    //local fields
    @ColumnInfo(name = "qrText")
    String qrText;
    @ColumnInfo(name = "dateTime")
    String dateTime;
    @ColumnInfo(name = "timestamp")
    String timestamp;
    @ColumnInfo(name = "bitmapFilePath")
    String bitmapFilePath;

    // gdrive fields
    @ColumnInfo(name = "gdriveFileThumbnail")
    String gdriveFileThumbnail;
    @ColumnInfo(name = "gdriveFileId")
    String gdriveFileId;
    @ColumnInfo(name = "gdriveFileParentId")
    String gdriveFileParentId;

    // asana task fields
    @ColumnInfo(name = "taskId")
    String taskId;
    @ColumnInfo(name = "beacon1_gid")
    String beacon1_gid;
    @ColumnInfo(name = "beacon1_RSSI_gid")
    String beacon1_RSSI_gid;
    @ColumnInfo(name = "beacon1_URL")
    String beacon1_URL;
    @ColumnInfo(name = "feasybeacon_task_gid")
    String feasybeacon_task_gid;
    @ColumnInfo(name = "feasybeacon_UUID_gid")
    String feasybeacon_UUID_gid;
    @ColumnInfo(name = "isAnchor")
    boolean isAnchor;
    @ColumnInfo(name = "barcode")
    String barcode;
    @ColumnInfo(name = "nearAnchor_gid")
    String nearAnchor_gid;


    // status of uploading and updating data
    @ColumnInfo(name = "status")
    String status;

    // Beacon
    @ColumnInfo(name = "timestampBeacon")
    String timestampBeacon;



    public TaskData(String qrText, String dateTime, String timestamp, String bitmapFilePath, String gdriveFileThumbnail, String gdriveFileId, String gdriveFileParentId, String taskId, String beacon1_gid, String beacon1_RSSI_gid, String beacon1_URL, String feasybeacon_task_gid, String feasybeacon_UUID_gid, String status,String timestampBeacon, boolean isAnchor, String barcode, String nearAnchor_gid) {
        this.qrText = qrText;
        this.dateTime = dateTime;
        this.timestamp = timestamp;
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
        this.isAnchor=isAnchor;
        this.barcode=barcode;
        this.nearAnchor_gid=nearAnchor_gid;
        this.status=status;
        this.timestampBeacon=timestampBeacon;



    }

    public String getQrText() {
        return qrText;
    }

    public void setQrText(String qrText) {
        this.qrText = qrText;
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

    public String getBitmapFilePath() {
        return bitmapFilePath;
    }

    public void setBitmapFilePath(String bitmapFilePath) {
        this.bitmapFilePath = bitmapFilePath;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestampBeacon() {
        return timestampBeacon;
    }

    public void setTimestampBeacon(String timestampBeacon) {
        this.timestampBeacon = timestampBeacon;
    }

    public boolean isAnchor() {
        return isAnchor;
    }

    public void setAnchor(boolean anchor) {
        isAnchor = anchor;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNearAnchor_gid() {
        return nearAnchor_gid;
    }

    public void setNearAnchor_gid(String nearAnchor_gid) {
        this.nearAnchor_gid = nearAnchor_gid;
    }
}
