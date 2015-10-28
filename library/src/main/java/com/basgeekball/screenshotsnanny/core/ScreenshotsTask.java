package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;
import android.os.Handler;

import com.basgeekball.screenshotsnanny.activityassistant.ActivityHelper;
import com.basgeekball.screenshotsnanny.helper.Callback;

public class ScreenshotsTask {
    public static void perform(final Callback callback, long activityDelay, final long screenshotDelay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Activity activity = ActivityHelper.getCurrentActivity();
                        ScreenshotsCapturer.execute(activity, new Callback() {
                            @Override
                            public void execute() {
                                activity.finish();
                            }
                        });
                    }
                }, screenshotDelay);
            }
        }, activityDelay);
    }

    public static void perform(Callback callback, long screenshotDelay) {
        perform(callback, 0, screenshotDelay);
    }

    public static void perform(final Class<?> T, final Callback callback, final int mapFragmentId, long activityDelay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.execute();
                ActivityHelper.performTaskWhenActivityIsReady(T, new Callback() {
                    @Override
                    public void execute() {
                        final Activity activity = ActivityHelper.getCurrentActivity();
                        ScreenshotsCapturer.executeWithMap(activity, mapFragmentId, new Callback() {
                            @Override
                            public void execute() {
                                activity.finish();
                            }
                        });
                    }
                });
            }
        }, activityDelay);
    }

    public static void perform(Class<?> T, Callback callback, int mapFragmentId) {
        perform(T, callback, mapFragmentId, 0);
    }
}
