[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![JCenter](https://api.bintray.com/packages/thyrlian/android-libraries/com.basgeekball.screenshotsnanny/images/download.svg) ](https://bintray.com/thyrlian/android-libraries/com.basgeekball.screenshotsnanny/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ScreenshotsNanny-green.svg?style=true)](https://android-arsenal.com/details/1/2825)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23181-brightgreen.svg)](http://androidweekly.net/issues/issue-181)

# ScreenshotsNanny

## Introduction

Until the time of writing, the Android toolchain doesn't have anything to help take screenshots automatically for publishing on Google Play Store.

The other fact is that most of the modern apps consume internet resources, or have UGC (user-generated content).  And for the screenshots showing on Google Play, no one would like to see any arbitrary content which may be ugly or even inappropriate.

Be professional!  Be beautiful!  You can achieve it easily by using ScreenshotsNanny.

## Comparison

Below are two different screenshots for the same activity.  The left one is using real arbitrary content, while the right one is using prepared mock response.

![Comparison](https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_comparison.png)

## Setup & Sample code

There are two approaches to utilizing this library.

* Setup an **automated UI test** (e.g. [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html)).
* Create another **product flavor** in your project to do the screenshot job.

I'll explain the product flavor approach in detail.  You can also check out the demo module along with this project.

**1** - Add a product flavor (let's name it "**screenshots**") to your target module's **build.gradle**:
```groovy
productFlavors {
    prod {
        applicationId "PRODUCT_DEFAULT_APP_ID"
    }
    screenshots {
        applicationId "PACKAGE_NAME.screenshots"
    }
}
```
**2** - Create a blank dummy activity in the screenshots flavor: *MODULE/src/screenshots/java/PACKAGE_NAME/ScreenshotsPrimeActivity.java*

You can leave the layout as it is (an empty view group), because we don't really need it.

**3** - Set the created activity as the launcher activity in that product flavor:

*MODULE/src/screenshots/AndroidManifest.xml*
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application>
        <activity android:name="PACKAGE_NAME.ScreenshotsPrimeActivity"
            android:label="@string/title_activity_screenshots_prime">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**4** - Add the core screenshot code to the new launcher activity **ScreenshotsPrimeActivity.java**.

There are two major methods: ```startActivityAndTakeScreenshot``` & ```startActivityContainsMapAndTakeScreenshot```.  Without passing **screenshotDelay**, the former one uses default value 3 seconds, and the latter one takes screenshot immediately when map view is ready.  You could give your own screenshotDelay to both of the methods.
```java
public class ScreenshotsPrimeActivity extends AppCompatActivity {

    private MockServerWrapper mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshots_prime);

        // Set up the screenshot fixture

        // Set language (resources configuration) other than the default one if it's necessary
        // LanguageSwitcher.change(this, "de");

        // Set up the mock server
        mServer = new MockServerWrapper();
        // Read mock response(s) from resource directory
        String response = ResourceReader.readFromRawResource(ScreenshotsPrimeActivity.this, R.raw.github_user);
        ParameterizedCallback changeUrlCallback = new ParameterizedCallback() {
            @Override
            public void execute(String value) {
                // The MockServer runs on arbitrary port each time
                // We have to change production's base URL to the MockServer URL via reflection
                PowerChanger.changeFinalString(GithubService.class, "API_URL", value);
            }
        };
        // Start mock server with canned response(s), it accepts response(s) as varargs
        mServer.start(changeUrlCallback, response);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start desired activities one by one, and take screenshot accordingly

        startActivityAndTakeScreenshot(MainActivity.class, new Callback() {
            @Override
            public void execute() {
                // Start a normal activity
                startActivity(new Intent(ScreenshotsPrimeActivity.this, MainActivity.class));
            }
        });

        startActivityAndTakeScreenshot(SecondActivity.class, new Callback() {
            @Override
            public void execute() {
                // Start an activity by an intent which contains something
                startActivity(SecondActivity.createIntent(ScreenshotsPrimeActivity.this, "London bridge is falling down"));
            }
        });

        startActivityAndTakeScreenshot(NetworkActivity.class, new Callback() {
            @Override
            public void execute() {
                // This activity will consume mock response and present it
                startActivity(new Intent(ScreenshotsPrimeActivity.this, NetworkActivity.class));
            }
        });

        startActivityAndTakeScreenshot(AccountActivity.class, new Callback() {
            @Override
            public void execute() {
                // Prepare persistent data before starting the activity
                AccountManager.create(getApplicationContext(), "Bruce Lee");
                AccountManager.update(getApplicationContext(), 1048576);
                startActivity(new Intent(ScreenshotsPrimeActivity.this, AccountActivity.class));
            }
        });

        startActivityContainsMapAndTakeScreenshot(MapsActivity.class, new Callback() {
            @Override
            public void execute() {
                // Take screenshot for an activity which contains Map, need to pass map view id
                startActivity(new Intent(ScreenshotsPrimeActivity.this, MapsActivity.class));
            }
        }, R.id.map);

        if (!ActivityCounter.isAnyActivityRunning) {
            Log.i(Constants.LOG_TAG, "⚙ Done.");
            // Stop mock server when all screenshot jobs are done
            mServer.stop();
            finish();
        }
    }
}
```

**5** - Select **screenshotsDebug** as build variant, run it.  Then all screenshots will be placed under *DEVICE_STORAGE/Screenshots/APP_NAME/*.  Each screenshot file is named as corresponding activity name (format is PNG).

## Screenshots

* The Android Status Bar won't be captured as part of the screenshot, so you don't have to worry about the messy icons there.
* Forget about the annoying on-screen keyboard, this library will hide it for you.
* MapView (no matter if it fulfills the window or not) will also be taken into the screenshot.
* For any activity consumes network resources, you can replace the content by canned mock responses.  (This library is using [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) from [Square, Inc.](https://github.com/square))
* Some activities may read values from persistent data (e.g. SharedPreferences), you can also prepare the values before activity starts.

<a href="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_main_activity.png" target="_blank"><img src="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_main_activity.png" height="300"></a>
<a href="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_second_activity.png" target="_blank"><img src="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_second_activity.png" height="300"></a>
<a href="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_network_activity.png" target="_blank"><img src="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_network_activity.png" height="300"></a>
<a href="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_account_activity.png" target="_blank"><img src="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_account_activity.png" height="300"></a>
<a href="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_maps_activity.png" target="_blank"><img src="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/screenshot_maps_activity.png" height="300"></a>

(sorry, these demo activities layouts were made with poor design, look ugly)

## Logs

Filter: tag = SSN

![Logs](https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/logs.png)

## Import as dependency

Gradle: (available in Bintray's [JCenter](https://bintray.com/thyrlian/android-libraries/com.basgeekball.screenshotsnanny/view))
```java
dependencies {
    compile 'com.basgeekball:screenshots-nanny:1.2'
}
```

## Publish

```bash
gradle generateRelease
```

## License

Copyright (c) 2015 Jing Li. See the LICENSE file for license rights and limitations (MIT).

## Last but not least

This is made in Berlin with love and passion  ʕ´•ᴥ•`ʔ

<a href="../../" target="_blank"><img src="https://github.com/thyrlian/ScreenshotsNanny/blob/master/resource/images/made_in_berlin_badge.png" height="200"></a>
