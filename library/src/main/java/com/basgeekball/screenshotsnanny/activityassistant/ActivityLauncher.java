package com.basgeekball.screenshotsnanny.activityassistant;

import com.basgeekball.screenshotsnanny.core.ScreenshotsTask;
import com.basgeekball.screenshotsnanny.helper.Callback;

public class ActivityLauncher {

    private static long sDefaultScreenshotDelay = 3000;

    private ActivityLauncher() {
    }

    private static void startActivityAndTakeScreenshot(Class<?> T, Callback callback, boolean hasMap, int mapFragmentId, long screenshotDelay) {
        if (!ActivityCounter.isCalledAlready(T) && !ActivityCounter.isAnyActivityRunning) {
            ActivityCounter.add(T);
            ActivityCounter.isAnyActivityRunning = true;
            Callback completionCallback = new Callback() {
                @Override
                public void execute() {
                    ActivityCounter.isAnyActivityRunning = false;
                    ActivityHelper.getCurrentActivity().finish();
                }
            };
            if (hasMap) {
                ScreenshotsTask.perform(T, callback, completionCallback, mapFragmentId);
            } else {
                ScreenshotsTask.perform(T, callback, completionCallback, screenshotDelay);
            }
        }
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, Callback callback, long screenshotDelay) {
        startActivityAndTakeScreenshot(T, callback, false, 0, screenshotDelay);
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, Callback callback) {
        startActivityAndTakeScreenshot(T, callback, sDefaultScreenshotDelay);
    }

    public static void startActivityContainsMapAndTakeScreenshot(Class<?> T, Callback callback, int mapFragmentId, long screenshotDelay) {
        startActivityAndTakeScreenshot(T, callback, true, mapFragmentId, screenshotDelay);
    }

    public static void startActivityContainsMapAndTakeScreenshot(Class<?> T, Callback callback, int mapFragmentId) {
        startActivityContainsMapAndTakeScreenshot(T, callback, mapFragmentId, sDefaultScreenshotDelay);
    }
}
