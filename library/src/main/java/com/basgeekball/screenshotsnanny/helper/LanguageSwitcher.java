package com.basgeekball.screenshotsnanny.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

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
        }
    }

}
