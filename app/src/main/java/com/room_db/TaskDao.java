package com.room_db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.synapse.model.TaskData;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM TaskData")
    List<TaskData> getAll();

    @Query("SELECT * FROM TaskData WHERE timestamp = :Timestamp")
    TaskData findByTimestamp(String Timestamp);

    @Query("SELECT timestampBeacon FROM TaskData WHERE timestamp = :Timestamp")
    String findBeaconTimestamp(String Timestamp);


    @Query("Delete FROM TaskData WHERE status = :status")
    int deleteAllCompletedRecords(String status);

    @Query("SELECT * FROM TaskData WHERE timestamp IN (:timestamps)")
    List<TaskData> findAllQRDataByTimestampsList(List<String> timestamps);

    @Insert
    void insert(TaskData task);

    @Delete
    void delete(TaskData task);

    @Update
    void update(TaskData task);

    @Query("UPDATE TaskData SET status=:status, taskId=:taskId, feasybeacon_UUID_gid=:feasybeacon_UUID_gid, feasybeacon_task_gid=:feasybeacon_task_gid, gdriveFileId=:gdriveFileId, gdriveFileParentId=:gdriveFileParentId,gdriveFileThumbnail=:gdriveFileThumbnail, beacon1_RSSI_gid=:beacon1_RSSI_gid, beacon1_URL=:beacon1_URL, beacon1_gid=:beacon1_gid   WHERE timestamp = :timestamp")
    void updateStatus(String timestamp, String taskId, String feasybeacon_UUID_gid, String feasybeacon_task_gid, String gdriveFileId, String gdriveFileParentId, String gdriveFileThumbnail, String beacon1_RSSI_gid, String beacon1_URL, String beacon1_gid, String status);





}
