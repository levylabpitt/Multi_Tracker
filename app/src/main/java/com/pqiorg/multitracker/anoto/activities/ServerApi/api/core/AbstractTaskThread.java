package com.pqiorg.multitracker.anoto.activities.ServerApi.api.core;

/*import com.anoto.adna.sdk.util.DevLog;*/
import com.pqiorg.multitracker.anoto.activities.sdk.util.DevLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractTaskThread extends Thread {
    private List<Task> mTaskList = Collections.synchronizedList(new ArrayList());
    private boolean mTerminated = false;

    private static final class Task {
        private Object mParam = null;
        private ITaskType mTaskType = null;

        public Task(ITaskType iTaskType, Object obj) {
            this.mTaskType = iTaskType;
            this.mParam = obj;
        }

        public Object getParam() {
            return this.mParam;
        }

        public ITaskType getTaskType() {
            return this.mTaskType;
        }
    }

    protected AbstractTaskThread(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo9495a() {
        synchronized (this.mTaskList) {
            this.mTerminated = true;
            this.mTaskList.notify();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo9496a(ITaskType iTaskType, Object obj) {
        if (this.mTerminated) {
            DevLog.LoggingError("Exception: AbstractTaskThread addTask", "Cannot add task because thread is terminated");
            return;
        }
        synchronized (this.mTaskList) {
            this.mTaskList.add(new Task(iTaskType, obj));
            this.mTaskList.notify();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public abstract void mo9497b(ITaskType iTaskType, Object obj);

    public void run() {
        while (!this.mTerminated) {
            while (!this.mTaskList.isEmpty() && !this.mTerminated) {
                Task task = (Task) this.mTaskList.remove(0);
                try {
                    mo9497b(task.getTaskType(), task.getParam());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                synchronized (this.mTaskList) {
                    if (this.mTaskList.isEmpty() && !this.mTerminated) {
                        this.mTaskList.wait();
                    }
                }
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }
}
