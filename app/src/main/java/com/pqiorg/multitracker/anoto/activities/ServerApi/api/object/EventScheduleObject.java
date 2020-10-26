package com.pqiorg.multitracker.anoto.activities.ServerApi.api.object;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.util.ArrayList;

public class EventScheduleObject extends AbstractObject {
    private EventData mEventData;

    public class Data {
        public String event_count;
        public ArrayList<DataEventVo> event_list;

        public Data() {
        }
    }

    public class EventData {
        public Data data;
        public Result result;

        public EventData() {
        }
    }

    public class Result {
        public String code;
        public String message;

        public Result() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Result{code='");
            sb.append(this.code);
            sb.append('\'');
            sb.append(", message='");
            sb.append(this.message);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public EventData getEventInfo() {
        return this.mEventData;
    }

    public boolean onResponseListener(String str) {
        try {
            this.mEventData = (EventData) new Gson().fromJson(str, EventData.class);
            return true;
        } catch (JsonParseException unused) {
            return false;
        }
    }
}
