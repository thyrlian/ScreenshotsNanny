package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenshotsCapturer {

    private Bitmap captureScreenshot(Activity activity) {
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        return rootView.getDrawingCache();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void createDirIfNotExist(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void saveToFile(Bitmap bitmap, String filename, Activity activity) {
        String appName = activity.getString(activity.getApplicationInfo().labelRes);
        File screenshotDir = new File(Environment.getExternalStorageDirectory() + "/Screenshots/" + appName);
        createDirIfNotExist(screenshotDir);
        File screenshot = new File(screenshotDir.getPath() + File.separator + filename + ".png");
        try {
            screenshot.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(screenshot);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void performTaskWhenLayoutStateChanges(Activity activity, final Runnable task, final int delay) {
        final View contentView = activity.findViewById(android.R.id.content);
        ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // call getViewTreeObserver() again, because previous ViewTreeObserver is not alive
                ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }
                new Handler().postDelayed(task, delay);
            }
        });
    }

    public void execute(final Activity activity, int delay) {
        performTaskWhenLayoutStateChanges(activity, new Runnable() {
            @Override
            public void run() {
                execute(activity);
            }
        }, delay);
    }

    public void execute(Activity activity) {
        saveToFile(captureScreenshot(activity), activity.getClass().getSimpleName(), activity);
    }
}
