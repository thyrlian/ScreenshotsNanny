package com.basgeekball.screenshotsnanny.core;

import android.os.Handler;

import com.basgeekball.screenshotsnanny.activityassistant.ActivityHelper;
import com.basgeekball.screenshotsnanny.helper.Callback;
import com.basgeekball.screenshotsnanny.helper.KeyboardHelper;

public class ScreenshotsTask {
    public static void perform(final Class<?> T, final Callback callback, final Callback completionCallback, final long screenshotDelay) {
        new Runnable() {
            @Override
            public void run() {
                callback.execute();
                ActivityHelper.performTaskWhenActivityIsReady(T, new Callback() {
                    @Override
                    public void execute() {
                        KeyboardHelper.hideKeyboard();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ScreenshotsCapturer.execute(ActivityHelper.getCurrentActivity(), completionCallback);
                            }
                        }, screenshotDelay);
                    }
                });
            }
        }.run();
    }

    public static void perform(final Class<?> T, final Callback callback, final Callback completionCallback, final int mapFragmentId) {
        new Runnable() {
            @Override
            public void run() {
                callback.execute();
                ActivityHelper.performTaskWhenActivityIsReady(T, new Callback() {
                    @Override
                    public void execute() {
                        KeyboardHelper.hideKeyboard();
                        ScreenshotsCapturer.executeWithMap(ActivityHelper.getCurrentActivity(), mapFragmentId, completionCallback);
                    }
                });
            }
        }.run();
    }
}
