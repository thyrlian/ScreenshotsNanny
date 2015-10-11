package com.basgeekball.screenshotsnanny.mockserver;

import android.os.AsyncTask;

import com.basgeekball.screenshotsnanny.helper.Callback;

public class MockServerTask extends AsyncTask<Void, Void, Void> {

    private Callback mCallback;

    @Override
    protected Void doInBackground(Void... voids) {
        mCallback.execute();
        return null;
    }

    public void run(Callback callback) {
        mCallback = callback;
        this.execute();
    }
}
