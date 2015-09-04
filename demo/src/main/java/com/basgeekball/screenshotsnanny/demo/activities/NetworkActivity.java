package com.basgeekball.screenshotsnanny.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.basgeekball.screenshotsnanny.demo.R;

public class NetworkActivity extends AppCompatActivity {

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, NetworkActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
    }
}
