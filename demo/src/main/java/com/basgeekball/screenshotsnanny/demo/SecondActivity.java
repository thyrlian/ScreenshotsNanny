package com.basgeekball.screenshotsnanny.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String EXTRA_TEXT = "com.basgeekball.screenshotsnanny.demo.TEXT";

    public static Intent createIntent(Activity activity, String text) {
        Intent intent = new Intent(activity, SecondActivity.class);
        intent.putExtra(EXTRA_TEXT, text);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView textViewContent = (TextView) findViewById(R.id.text_view_content);
        textViewContent.setText("Once upon a time, " + getIntent().getStringExtra(EXTRA_TEXT));
    }
}
