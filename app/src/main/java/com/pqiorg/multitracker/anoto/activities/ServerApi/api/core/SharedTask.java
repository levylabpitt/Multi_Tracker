package com.pqiorg.multitracker.anoto.activities.ServerApi.api.core;

public class SharedTask extends AbstractTaskThread {

    private static final class GenericTask {
        private Object mParam = null;
        private ITaskHandler mTaskHandler = null;

        public GenericTask(ITaskHandler iTaskHandler, Object obj) {
            this.mTaskHandler = iTaskHandler;
            this.mParam = obj;
        }

        public ITaskHandler getHandler() {
            return this.mTaskHandler;
        }

        public Object getParam() {
            return this.mParam;
        }
    }

    public SharedTask(String str) {
        super(str);
    }

    public void addTask(ITaskHandler iTaskHandler, ITaskType iTaskType, Object obj) {
        super.mo9496a(iTaskType, new GenericTask(iTaskHandler, obj));
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo9497b(ITaskType iTaskType, Object obj) {
        GenericTask genericTask = (GenericTask) obj;
        genericTask.getHandler().handleTask(iTaskType, genericTask.getParam());
    }

    public void stopThread() {
        super.mo9495a();
    }
}
