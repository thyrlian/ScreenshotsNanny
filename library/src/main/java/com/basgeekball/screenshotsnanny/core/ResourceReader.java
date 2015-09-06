package com.basgeekball.screenshotsnanny.core;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

public class ResourceReader {

    private ResourceReader() {
    }

    public static String readFromRawResource(Activity activity, int resourceId) {
        String content = "";
        InputStream inputStream = activity.getResources().openRawResource(resourceId);
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            content = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
