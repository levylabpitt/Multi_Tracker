package com.pqiorg.multitracker.anoto.activities.ServerApi.api.core;

/*import com.anoto.adna.ServerApi.api.connector.ADNAConnector;
import com.anoto.adna.ServerApi.api.connector.IConnector;*/

import com.pqiorg.multitracker.anoto.activities.ServerApi.api.connector.ADNAConnector;
import com.pqiorg.multitracker.anoto.activities.ServerApi.api.connector.IConnector;

public class CoreAccessHelper {
    private volatile IConnector mConnector = null;
    private volatile SharedTask mSharedTask = null;

    private SharedTask getSharedTask() {
        if (this.mSharedTask == null) {
            synchronized (this) {
                if (this.mSharedTask == null) {
                    this.mSharedTask = new SharedTask("Common_Thread");
                    this.mSharedTask.start();
                }
            }
        }
        return this.mSharedTask;
    }

    public IConnector getConnector() {
        if (this.mConnector == null) {
            synchronized (this) {
                if (this.mConnector == null) {
                    this.mConnector = new ADNAConnector(getSharedTask());
                }
            }
        }
        return this.mConnector;
    }
}
