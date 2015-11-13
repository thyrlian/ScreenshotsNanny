package com.basgeekball.screenshotsnanny.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.basgeekball.screenshotsnanny.demo.R;
import com.basgeekball.screenshotsnanny.demo.models.Account;
import com.basgeekball.screenshotsnanny.demo.models.AccountManager;

public class AccountActivity extends AppCompatActivity {

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, AccountActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getText(R.string.message_account_fab_about), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View viewNoUser = findViewById(R.id.info_no_user);
        View viewAccountDetails = findViewById(R.id.account_details);
        if (AccountManager.hasAny(this)) {
            viewNoUser.setVisibility(View.GONE);
            viewAccountDetails.setVisibility(View.VISIBLE);
            Account account = AccountManager.read(this);
            TextView textViewUser = (TextView) findViewById(R.id.user_value);
            TextView textViewPoints = (TextView) findViewById(R.id.points_value);
            textViewUser.setText(account.getUser());
            textViewPoints.setText(String.valueOf(account.getPoints()));
        } else {
            viewNoUser.setVisibility(View.VISIBLE);
            viewAccountDetails.setVisibility(View.GONE);
        }
    }

}
