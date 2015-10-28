package com.basgeekball.screenshotsnanny.activityassistant;

import java.util.ArrayList;

public class ActivityCounter {

    public static boolean isAnyActivityRunning = false;
    private static ArrayList<Class<?>> sActivities = new ArrayList<Class<?>>();

    private ActivityCounter() {
    }

    public static void add(Class<?> T) {
        sActivities.add(T);
    }

    public static boolean isCalledAlready(Class<?> T) {
        return sActivities.contains(T);
    }
}
