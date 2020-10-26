package com.pqiorg.multitracker.anoto.activities.ServerApi.api.core;

public interface ITaskHandler {
    void handleTask(ITaskType iTaskType, Object obj) throws IllegalArgumentException;
}
