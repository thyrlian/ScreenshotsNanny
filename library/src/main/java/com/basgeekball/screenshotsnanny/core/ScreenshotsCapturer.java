package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.basgeekball.screenshotsnanny.activityassistant.ActivityHelper;
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

    private static int[] getViewLocationOnlyIfRendered(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    private static int[] getViewLocationOnlyIfRendered(int viewId) {
        Activity activity = ActivityHelper.getCurrentActivity();
        View view;
        if (activity != null && (view = activity.findViewById(viewId)) != null) {
            return getViewLocationOnlyIfRendered(view);
        } else {
            return null;
        }
    }

    public static void execute(Activity activity, Callback callback) {
        saveToFile(captureScreenshot(activity), activity.getClass().getSimpleName(), activity);
        Log.i(Constants.LOG_TAG, "♬ Screenshot is taken");
        callback.execute();
    }

    public static void executeWithMap(final Activity activity, final int mapFragmentId, final long screenshotDelay, final Callback callback) {
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
                            public void onSnapshotReady(final Bitmap map) {
                                final Callback drawScreenshot = new Callback() {
                                    @Override
                                    public void execute() {
                                        View rootView = activity.findViewById(android.R.id.content).getRootView();
                                        rootView.setDrawingCacheEnabled(true);
                                        Bitmap background = rootView.getDrawingCache();
                                        Bitmap bitmap = Bitmap.createBitmap(background.getWidth(), background.getHeight(), background.getConfig());
                                        Canvas canvas = new Canvas(bitmap);
                                        canvas.drawBitmap(background, 0, 0, null);
                                        int[] mapLocation = getViewLocationOnlyIfRendered(mapFragmentId);
                                        canvas.drawBitmap(map, mapLocation[0], mapLocation[1], null);
                                        saveToFile(bitmap, activity.getClass().getSimpleName(), activity);
                                        Log.i(Constants.LOG_TAG, "♬ Screenshot is taken (including Map)");
                                        callback.execute();
                                    }
                                };
                                if (screenshotDelay > 0) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            drawScreenshot.execute();
                                        }
                                    }, screenshotDelay);
                                } else {
                                    drawScreenshot.execute();
                                }
                            }
                        });
                    }
                });
            }
        }
    }
}
