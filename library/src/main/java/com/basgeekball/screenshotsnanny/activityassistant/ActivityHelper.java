package com.basgeekball.screenshotsnanny.activityassistant;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;

import com.basgeekball.screenshotsnanny.core.Constants;
import com.basgeekball.screenshotsnanny.helper.Callback;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ActivityHelper {
    private static final Class mActivityThreadClass;
    private static final Field mActivitiesField;
    private static final Field mPausedField;
    private static final Field mStoppedField;
    private static final Field mActivityField;

    private static final long mWait = 100;
    private static final int mRetriesMax = 20;
    private static int mRetriesCounter = 0;

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
            Map<Object, Object> activities;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activities = (ArrayMap<Object, Object>) mActivitiesField.get(currentActivityThread);
            } else {
                activities = (HashMap<Object, Object>) mActivitiesField.get(currentActivityThread);
            }
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

    public static void doSomethingWhenActivityIsReady(final Class<?> T, final Callback callback) {
        if (mRetriesCounter < mRetriesMax) {
            if (T.isInstance(getCurrentActivity())) {
                Log.i(Constants.LOG_TAG, "⚒ Activity is ready, just do it");
                mRetriesCounter = 0;
                callback.execute();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doSomethingWhenActivityIsReady(T, callback);
                    }
                }, mWait);
                mRetriesCounter += 1;
            }
        } else {
            Log.i(Constants.LOG_TAG, String.format("☠ Timeout: can not find %s", T.getSimpleName()));
            mRetriesCounter = 0;
        }
    }
}
