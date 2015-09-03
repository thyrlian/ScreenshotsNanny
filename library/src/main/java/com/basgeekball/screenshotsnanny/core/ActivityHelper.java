package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ActivityHelper {
    private static final Class mActivityThreadClass;
    private static final Field mActivitiesField;
    private static final Field mPausedField;
    private static final Field mStoppedField;
    private static final Field mActivityField;

    // initialization
    static {
        Class tmpActivityThreadClass = null;
        Field tmpActivitiesField = null;
        Field tmpPausedField = null;
        Field tmpStoppedField = null;
        Field tmpActivityField = null;
        try {
            tmpActivityThreadClass = Class.forName("android.app.ActivityThread");
            tmpActivitiesField = tmpActivityThreadClass.getDeclaredField("mActivities");
            tmpActivitiesField.setAccessible(true);
            Class activityClientRecordClass = Class.forName("android.app.ActivityThread$ActivityClientRecord");
            tmpPausedField = activityClientRecordClass.getDeclaredField("paused");
            tmpStoppedField = activityClientRecordClass.getDeclaredField("stopped");
            tmpActivityField = activityClientRecordClass.getDeclaredField("activity");
            tmpPausedField.setAccessible(true);
            tmpStoppedField.setAccessible(true);
            tmpActivityField.setAccessible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mActivityThreadClass = tmpActivityThreadClass;
        mActivitiesField = tmpActivitiesField;
        mPausedField = tmpPausedField;
        mStoppedField = tmpStoppedField;
        mActivityField = tmpActivityField;
    }

    public static Activity getCurrentActivity() {
        Activity activity = null;
        try {
            Object currentActivityThread = mActivityThreadClass.getMethod("currentActivityThread").invoke(null);
            ArrayMap<Object, Object> activities = (ArrayMap<Object, Object>) mActivitiesField.get(currentActivityThread);
            for (Object activityClientRecord : activities.values()) {
                boolean paused = mPausedField.getBoolean(activityClientRecord);
                boolean stopped = mStoppedField.getBoolean(activityClientRecord);
                if (!paused && !stopped) {
                    activity = (Activity) mActivityField.get(activityClientRecord);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return activity;
    }
}
