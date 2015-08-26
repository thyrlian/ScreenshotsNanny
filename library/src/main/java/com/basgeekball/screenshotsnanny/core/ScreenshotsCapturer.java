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

    public void execute(final Activity activity, final int delay) {
        final View contentView = activity.findViewById(android.R.id.content);
        final ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // call getViewTreeObserver() again, because previous ViewTreeObserver is not alive
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveToFile(captureScreenshot(activity), activity.getClass().getSimpleName(), activity);
                    }
                }, delay);
            }
        });
    }
}
