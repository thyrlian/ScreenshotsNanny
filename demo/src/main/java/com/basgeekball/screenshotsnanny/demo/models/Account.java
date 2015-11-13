package com.basgeekball.screenshotsnanny.demo.models;

public class Account {

    private String mUser;
    private long mPoints;

    public Account(String user) {
        mUser = user;
        mPoints = 0;
    }

    public Account(String user, long points) {
        mUser = user;
        mPoints = points;
    }

    public String getUser() {
        return mUser;
    }

    public long getPoints() {
        return mPoints;
    }

    void setUser(String user) {
        this.mUser = user;
    }

    void setPoints(long points) {
        this.mPoints = points;
    }

}
