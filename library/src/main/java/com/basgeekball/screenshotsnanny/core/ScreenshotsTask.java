package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;
import android.os.Handler;

public class ScreenshotsTask {
    public static void perform(final Callback callback, long activityDelay, final long screenshotDelay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = ActivityHelper.getCurrentActivity();
                        ScreenshotsCapturer.execute(activity);
                        activity.finish();
                    }
                }, screenshotDelay);
            }
        }, activityDelay);
    }
}
