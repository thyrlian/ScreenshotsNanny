package com.basgeekball.screenshotsnanny.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.basgeekball.screenshotsnanny.activityassistant.ActivityHelper;
import com.basgeekball.screenshotsnanny.core.Constants;

public class KeyboardHelper {

    private KeyboardHelper() {
    }

    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Log.i(Constants.LOG_TAG, "♬ Hide keyboard");
        }
    }

    public static void hideKeyboard() {
        Activity activity = ActivityHelper.getCurrentActivity();
        if (activity != null) {
            hideKeyboard(activity);
        }
    }
}
