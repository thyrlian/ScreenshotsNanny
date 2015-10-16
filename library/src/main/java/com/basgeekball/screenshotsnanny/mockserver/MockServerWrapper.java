package com.basgeekball.screenshotsnanny.mockserver;

import android.util.Log;

import com.basgeekball.screenshotsnanny.helper.Callback;
import com.basgeekball.screenshotsnanny.helper.ParameterizedCallback;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.IOException;

import static com.basgeekball.screenshotsnanny.core.Constants.LOG_TAG;

public class MockServerWrapper {

    private MockWebServer mServer;
    private String mUrl;

    public void start(final ParameterizedCallback changeUrlCallback, final MockResponse... mockResponses) {
        new MockServerTask().run(new Callback() {
            @Override
            public void execute() {
                mServer = new MockWebServer();
                for (MockResponse mockResponse : mockResponses) {
                    mServer.enqueue(mockResponse);
                }
                try {
                    mServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mServer != null) {
                    mUrl = mServer.url("").url().toString().replaceAll("/$", "");
                    Log.i(LOG_TAG, "⚙ MockServer URL: " + mUrl);
                    changeUrlCallback.execute(mUrl);
                }
            }
        });
    }

    public void start(ParameterizedCallback changeUrlCallback, final String... mockResponsesBodies) {
        int amount = mockResponsesBodies.length;
        MockResponse mockResponses[] = new MockResponse[amount];
        for (int i = 0; i < amount; i++) {
            mockResponses[i] = new MockResponse().setBody(mockResponsesBodies[i]);
        }
        start(changeUrlCallback, mockResponses);
    }

    public void stop() {
        if (mServer != null) {
            new MockServerTask().run(new Callback() {
                @Override
                public void execute() {
                    try {
                        mServer.shutdown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
