package com.basgeekball.screenshotsnanny.core;

public class ActivityLauncher {

    private ActivityLauncher() {
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, final Callback callback) {
        if (!ActivityCounter.isCalledAlready(T) && !ActivityCounter.isAnyActivityRunning) {
            ActivityCounter.add(T);
            ActivityCounter.isAnyActivityRunning = true;
            ScreenshotsTask.perform(new Callback() {
                @Override
                public void execute() {
                    callback.execute();
                    ActivityCounter.isAnyActivityRunning = false;
                }
            }, 0, 3000);
        }
    }
}
