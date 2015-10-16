package com.basgeekball.screenshotsnanny.activityassistant;

import android.os.Handler;

import com.basgeekball.screenshotsnanny.core.ScreenshotsTask;
import com.basgeekball.screenshotsnanny.helper.Callback;
import com.basgeekball.screenshotsnanny.helper.KeyboardHelper;

public class ActivityLauncher {

    private ActivityLauncher() {
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, final Callback callback, long screenshotDelay) {
        if (!ActivityCounter.isCalledAlready(T) && !ActivityCounter.isAnyActivityRunning) {
            ActivityCounter.add(T);
            ActivityCounter.isAnyActivityRunning = true;
            ScreenshotsTask.perform(new Callback() {
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
            }, 0, screenshotDelay);
        }
    }

    public static void startActivityAndTakeScreenshot(Class<?> T, final Callback callback) {
        startActivityAndTakeScreenshot(T, callback, 3000);
    }
}
