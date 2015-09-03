package com.basgeekball.screenshotsnanny.demo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.screenshotsnanny.demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editTextContent = (EditText) findViewById(R.id.edit_text_content);
        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editTextContent.getText().toString();
                startActivity(SecondActivity.createIntent(MainActivity.this, content));
            }
        });
    }
}
