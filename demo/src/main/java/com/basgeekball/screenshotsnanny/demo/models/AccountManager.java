package com.basgeekball.screenshotsnanny.demo.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AccountManager {

    private static final String PREFERENCE_FILE_NAME = "com.basgeekball.screenshotsnanny.demo.PREFERENCE";
    private static final String PREFERENCE_KEY_ACCOUNT_USER = "com.basgeekball.screenshotsnanny.demo.PREFERENCE_USER_KEY";
    private static final String PREFERENCE_KEY_ACCOUNT_POINTS = "com.basgeekball.screenshotsnanny.demo.PREFERENCE_POINTS_KEY";

    private AccountManager() {
    }

    private static boolean hasAny(SharedPreferences sharedPref) {
        if (read(sharedPref) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasAny(Activity activity) {
        return hasAny(getSharedPreferences(activity));
    }

    public static boolean hasAny(Context context) {
        return hasAny(getSharedPreferences(context));
    }

    private static Account create(SharedPreferences sharedPref, String user) {
        if (!hasAny(sharedPref)) {
            Account account = new Account(user);
            setAccount(sharedPref, account);
            return account;
        } else {
            return null;
        }
    }

    public static Account create(Activity activity, String user) {
        return create(getSharedPreferences(activity), user);
    }

    public static Account create(Context context, String user) {
        return create(getSharedPreferences(context), user);
    }

    private static Account read(SharedPreferences sharedPref) {
        String user = sharedPref.getString(PREFERENCE_KEY_ACCOUNT_USER, null);
        long points = sharedPref.getLong(PREFERENCE_KEY_ACCOUNT_POINTS, -1);
        if (user != null && points != -1) {
            return new Account(user, points);
        } else {
            return null;
        }
    }

    public static Account read(Activity activity) {
        return read(getSharedPreferences(activity));
    }

    public static Account read(Context context) {
        return read(getSharedPreferences(context));
    }

    private static void update(SharedPreferences sharedPref, long points) {
        Account account = read(sharedPref);
        if (account != null) {
            account.setPoints(points);
            setAccount(sharedPref, account);
        }
    }

    public static void update(Activity activity, long points) {
        update(getSharedPreferences(activity), points);
    }

    public static void update(Context context, long points) {
        update(getSharedPreferences(context), points);
    }

    private static void setAccount(SharedPreferences sharedPref, Account account) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREFERENCE_KEY_ACCOUNT_USER, account.getUser());
        editor.putLong(PREFERENCE_KEY_ACCOUNT_POINTS, account.getPoints());
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Activity activity) {
        return activity.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

}
