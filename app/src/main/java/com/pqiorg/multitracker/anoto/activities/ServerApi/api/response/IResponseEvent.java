package com.pqiorg.multitracker.anoto.activities.ServerApi.api.response;

public interface IResponseEvent<T> {
    void onResponse(T t);
}
