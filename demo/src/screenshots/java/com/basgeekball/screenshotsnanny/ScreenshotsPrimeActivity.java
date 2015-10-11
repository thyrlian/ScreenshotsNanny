package com.basgeekball.screenshotsnanny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.basgeekball.screenshotsnanny.activityassistant.ActivityCounter;
import com.basgeekball.screenshotsnanny.helper.Callback;
import com.basgeekball.screenshotsnanny.helper.ResourceReader;
import com.basgeekball.screenshotsnanny.demo.R;
import com.basgeekball.screenshotsnanny.demo.activities.MainActivity;
import com.basgeekball.screenshotsnanny.demo.activities.NetworkActivity;
import com.basgeekball.screenshotsnanny.demo.activities.SecondActivity;
import com.basgeekball.screenshotsnanny.mockserver.MockServerWrapper;

import static com.basgeekball.screenshotsnanny.activityassistant.ActivityLauncher.startActivityAndTakeScreenshot;

public class ScreenshotsPrimeActivity extends AppCompatActivity {

    private MockServerWrapper mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshots_prime);

        mServer = new MockServerWrapper();
        String response = ResourceReader.readFromRawResource(ScreenshotsPrimeActivity.this, R.raw.github_user);
        mServer.start(response);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startActivityAndTakeScreenshot(MainActivity.class, new Callback() {
            @Override
            public void execute() {
                startActivity(new Intent(ScreenshotsPrimeActivity.this, MainActivity.class));
            }
        });

        startActivityAndTakeScreenshot(SecondActivity.class, new Callback() {
            @Override
            public void execute() {
                startActivity(SecondActivity.createIntent(ScreenshotsPrimeActivity.this, "London bridge is falling down"));
            }
        });

        startActivityAndTakeScreenshot(NetworkActivity.class, new Callback() {
            @Override
            public void execute() {
                startActivity(new Intent(ScreenshotsPrimeActivity.this, NetworkActivity.class));
            }
        });

        if (!ActivityCounter.isAnyActivityRunning) {
            mServer.stop();
            finish();
        }
    }
}
