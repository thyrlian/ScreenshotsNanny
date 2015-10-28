package com.basgeekball.screenshotsnanny.activityassistant;

import android.os.Handler;

import com.basgeekball.screenshotsnanny.core.ScreenshotsTask;
import com.basgeekball.screenshotsnanny.helper.Callback;
import com.basgeekball.screenshotsnanny.helper.KeyboardHelper;

public class ActivityLauncher {

    private ActivityLauncher() {
    }

    private static void startActivityAndTakeScreenshot(Class<?> T, final Callback callback, boolean hasMap, int mapFragmentId, long screenshotDelay) {
        if (!ActivityCounter.isCalledAlready(T) && !ActivityCounter.isAnyActivityRunning) {
            ActivityCounter.add(T);
            ActivityCounter.isAnyActivityRunning = true;
            Callback completeTaskCallback = new Callback() {
                @Override
                public void execute() {
                    callback.execute();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyboardHelper.hideKeyboard();
                        }
                    }, 1000);
                    ActivityCounter.isAnyActivityRunning = false;
                }
            };
            if (hasMap) {
                ScreenshotsTask.perform(T, completeTaskCallback, mapFragmentId, 0);
            } else {
                ScreenshotsTask.perform(completeTaskCallback, 0, screenshotDelay);
            }
        }
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, Callback callback, long screenshotDelay) {
        startActivityAndTakeScreenshot(T, callback, false, 0, screenshotDelay);
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, Callback callback) {
        startActivityAndTakeScreenshot(T, callback, 3000);
    }

    public static void startActivityContainsMapAndTakeScreenshot(Class<?> T, Callback callback, int mapFragmentId, long screenshotDelay) {
        startActivityAndTakeScreenshot(T, callback, true, mapFragmentId, screenshotDelay);
    }

    public static void startActivityContainsMapAndTakeScreenshot(Class<?> T, Callback callback, int mapFragmentId) {
        startActivityContainsMapAndTakeScreenshot(T, callback, mapFragmentId, 3000);
    }
}
