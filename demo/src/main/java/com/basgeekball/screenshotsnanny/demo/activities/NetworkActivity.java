package com.basgeekball.screenshotsnanny.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.basgeekball.screenshotsnanny.demo.R;
import com.basgeekball.screenshotsnanny.demo.network.GithubService;
import com.basgeekball.screenshotsnanny.demo.network.models.User;
import com.bumptech.glide.Glide;

public class NetworkActivity extends AppCompatActivity {
    ImageView mImageViewAvatar;
    TextView mTextViewName;
    TextView mTextViewLogin;
    TextView mTextViewEmail;
    GithubService mGithubService = new GithubService();
    User mUser;
    final String mUsername = "octocat";

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, NetworkActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mImageViewAvatar = (ImageView) findViewById(R.id.image_view_avatar);
        mTextViewName = (TextView) findViewById(R.id.text_view_name);
        mTextViewLogin = (TextView) findViewById(R.id.text_view_login);
        mTextViewEmail = (TextView) findViewById(R.id.text_view_email);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mUser = mGithubService.getUser(mUsername);
                NetworkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(NetworkActivity.this).load(mUser.getAvatarUrl()).into(mImageViewAvatar);
                        mTextViewName.setText(mUser.getName());
                        mTextViewLogin.setText(mUser.getLogin());
                        mTextViewEmail.setText(mUser.getEmail());
                    }
                });
                return null;
            }
        }.execute();
    }
}
