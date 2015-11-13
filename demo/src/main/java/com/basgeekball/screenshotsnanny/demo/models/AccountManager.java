package com.basgeekball.screenshotsnanny.demo.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AccountManager {

    private static final String PREFERENCE_KEY_ACCOUNT_USER = "com.basgeekball.screenshotsnanny.demo.PREFERENCE_USER_KEY";
    private static final String PREFERENCE_KEY_ACCOUNT_POINTS = "com.basgeekball.screenshotsnanny.demo.PREFERENCE_POINTS_KEY";

    private AccountManager() {
    }

    public static boolean hasAny(Activity activity) {
        if (read(activity) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static Account create(Activity activity, String user) {
        if (!hasAny(activity)) {
            Account account = new Account(user);
            setAccount(activity, account);
            return account;
        } else {
            return null;
        }
    }

    public static Account read(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(PREFERENCE_KEY_ACCOUNT_USER, null);
        long points = sharedPref.getLong(PREFERENCE_KEY_ACCOUNT_POINTS, -1);
        if (user != null && points != -1) {
            return new Account(user, points);
        } else {
            return null;
        }
    }

    public static void update(Activity activity, long points) {
        Account account = read(activity);
        if (account != null) {
            account.setPoints(points);
            setAccount(activity, account);
        }
    }

    private static void setAccount(Activity activity, Account account) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREFERENCE_KEY_ACCOUNT_USER, account.getUser());
        editor.putLong(PREFERENCE_KEY_ACCOUNT_POINTS, account.getPoints());
        editor.apply();
    }

}
