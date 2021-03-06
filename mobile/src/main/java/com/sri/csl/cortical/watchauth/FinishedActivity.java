package com.sri.csl.cortical.watchauth;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sri.csl.cortical.watchauth.logging.Logger;


public class FinishedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        TextView textView = (TextView) findViewById(R.id.sessionID);
        textView.setText("Your session ID was " + Logger.currentSession());
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, DemographicsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
