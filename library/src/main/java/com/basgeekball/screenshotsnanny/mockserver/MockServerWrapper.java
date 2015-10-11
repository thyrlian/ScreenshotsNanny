package com.basgeekball.screenshotsnanny.mockserver;

import android.util.Log;

import com.basgeekball.screenshotsnanny.core.Callback;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.IOException;

public class MockServerWrapper {

    private MockWebServer mServer;
    private String mUrl;

    public void start(final MockResponse... mockResponses) {
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
                    mUrl = mServer.url("").url().toString();
                    Log.i("XXX", "MockServer URL: " + mUrl);
                }
            }
        });
    }

    public void start(final String... mockResponsesBodies) {
        int amount = mockResponsesBodies.length;
        MockResponse mockResponses[] = new MockResponse[amount];
        for (int i = 0; i < amount; i++) {
            mockResponses[i] = new MockResponse().setBody(mockResponsesBodies[i]);
        }
        start(mockResponses);
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
