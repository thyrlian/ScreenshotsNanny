package com.basgeekball.screenshotsnanny.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import static com.basgeekball.screenshotsnanny.core.Constants.LOG_TAG;

public class LanguageSwitcher {

    private LanguageSwitcher() {
    }

    public static void change(Context context, String language) {
        if (context != null) {
            Resources resources = context.getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = new Locale(language.toLowerCase());
            resources.updateConfiguration(configuration, displayMetrics);
            Log.i(LOG_TAG, "⚙ Language is set to: " + language.toUpperCase());
        }
    }

}
