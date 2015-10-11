package com.basgeekball.screenshotsnanny.activityassistant;

import java.util.ArrayList;

public class ActivityCounter {

    public static boolean isAnyActivityRunning = false;
    public static ArrayList<Class<?>> activities = new ArrayList<Class<?>>();

    private ActivityCounter() {
    }

    public static void add(Class<?> T) {
        activities.add(T);
    }

    public static boolean isCalledAlready(Class<?> T) {
        return activities.contains(T);
    }
}
