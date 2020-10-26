package com.pqiorg.multitracker.anoto.activities.util;

import java.util.Calendar;

public class DateCalendarHelper {
    private static DateCalendarHelper current;

    public static void freeInstance() {
        current = null;
    }

    public static DateCalendarHelper getInstance() {
        if (current == null) {
            current = new DateCalendarHelper();
        }
        return current;
    }

    public int[] get3MonthAgoDate() {
        Calendar instance = Calendar.getInstance();
        instance.add(2, -3);
        return new int[]{instance.get(1), instance.get(2) + 1, instance.get(5)};
    }

    public int[] getDate() {
        Calendar instance = Calendar.getInstance();
        return new int[]{instance.get(1), instance.get(2) + 1, instance.get(5)};
    }

    public int[] getMonthAgoDate() {
        Calendar instance = Calendar.getInstance();
        instance.add(2, -1);
        return new int[]{instance.get(1), instance.get(2) + 1, instance.get(5)};
    }

    public int[] getOndayAgoDate() {
        Calendar instance = Calendar.getInstance();
        instance.add(5, -1);
        return new int[]{instance.get(1), instance.get(2) + 1, instance.get(5)};
    }

    public int[] getWeekAgoDate() {
        Calendar instance = Calendar.getInstance();
        instance.add(5, -7);
        return new int[]{instance.get(1), instance.get(2) + 1, instance.get(5)};
    }
}
