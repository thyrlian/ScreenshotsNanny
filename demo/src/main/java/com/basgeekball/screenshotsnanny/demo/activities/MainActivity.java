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
        Button buttonSecond = (Button) findViewById(R.id.button_second);
        Button buttonNetwork = (Button) findViewById(R.id.button_network);
        Button buttonMaps = (Button) findViewById(R.id.button_maps);
        buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editTextContent.getText().toString();
                startActivity(SecondActivity.createIntent(MainActivity.this, content));
            }
        });
        buttonNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(NetworkActivity.createIntent(MainActivity.this));
            }
        });
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MapsActivity.createIntent(MainActivity.this));
            }
        });
    }
}
