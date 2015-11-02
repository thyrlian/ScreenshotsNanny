package com.basgeekball.screenshotsnanny.core;

import android.os.Handler;

import com.basgeekball.screenshotsnanny.activityassistant.ActivityHelper;
import com.basgeekball.screenshotsnanny.helper.Callback;
import com.basgeekball.screenshotsnanny.helper.KeyboardHelper;

public class ScreenshotsTask {
    private static void perform(final Class<?> T, final Callback callback, final Callback completionCallback, final boolean hasMap, final long screenshotDelay, final int mapFragmentId) {
        new Runnable() {
            @Override
            public void run() {
                callback.execute();
                ActivityHelper.performTaskWhenActivityIsReady(T, new Callback() {
                    @Override
                    public void execute() {
                        KeyboardHelper.hideKeyboard();
                        if (hasMap) {
                            ScreenshotsCapturer.executeWithMap(ActivityHelper.getCurrentActivity(), mapFragmentId, completionCallback);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ScreenshotsCapturer.execute(ActivityHelper.getCurrentActivity(), completionCallback);
                                }
                            }, screenshotDelay);
                        }
                    }
                });
            }
        }.run();
    }

    public static void perform(Class<?> T, Callback callback, Callback completionCallback, long screenshotDelay) {
        perform(T, callback, completionCallback, false, screenshotDelay, 0);
    }

    public static void perform(Class<?> T, Callback callback, Callback completionCallback, int mapFragmentId) {
        perform(T, callback, completionCallback, true, 0, mapFragmentId);
    }
}
