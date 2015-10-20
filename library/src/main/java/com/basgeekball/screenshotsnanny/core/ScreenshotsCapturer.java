package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.basgeekball.screenshotsnanny.helper.Callback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenshotsCapturer {
    private ScreenshotsCapturer() {
        throw new AssertionError();
    }

    private static Bitmap captureScreenshot(Activity activity) {
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rootView.draw(canvas);
        return bitmap;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static void createDirIfNotExist(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static void saveToFile(Bitmap bitmap, String filename, Activity activity) {
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

    private static void performTaskWhenLayoutStateChanges(Activity activity, final Runnable task, final long delay) {
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

    public static void execute(final Activity activity, long delay) {
        performTaskWhenLayoutStateChanges(activity, new Runnable() {
            @Override
            public void run() {
                execute(activity);
            }
        }, delay);
    }

    public static void execute(Activity activity) {
        saveToFile(captureScreenshot(activity), activity.getClass().getSimpleName(), activity);
        Log.i(Constants.LOG_TAG, "♬ Screenshot is taken");
    }

    private static void executeWithMap(final Activity activity, int mapFragmentId, final Callback callback) {
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(mapFragmentId);
            if (fragment instanceof SupportMapFragment) {
                final GoogleMap map = ((SupportMapFragment) fragment).getMap();
                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                            @Override
                            public void onSnapshotReady(Bitmap bitmap) {
                                saveToFile(bitmap, activity.getClass().getSimpleName(), activity);
                                Log.i(Constants.LOG_TAG, "♬ Screenshot is taken (including Map)");
                                callback.execute();
                            }
                        });
                    }
                });
            }
        }
    }

    public static void executeWithMap(final Activity activity, int mapFragmentId) {
        executeWithMap(activity, mapFragmentId, new Callback() {
            @Override
            public void execute() {
            }
        });
    }

    public static void executeWithMap(final Activity activity, int mapFragmentId, boolean finishActivityAfterExecution) {
        if (finishActivityAfterExecution) {
            executeWithMap(activity, mapFragmentId, new Callback() {
                @Override
                public void execute() {
                    activity.finish();
                }
            });
        } else {
            executeWithMap(activity, mapFragmentId);
        }
    }
}
