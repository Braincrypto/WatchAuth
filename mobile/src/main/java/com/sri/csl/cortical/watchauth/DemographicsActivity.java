package com.sri.csl.cortical.watchauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sri.csl.cortical.watchauth.logging.Logger;


public class DemographicsActivity extends Activity {

    RadioGroup age, gender, hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);
        age = (RadioGroup) findViewById(R.id.age);
        gender = (RadioGroup) findViewById(R.id.gender);
        hand = (RadioGroup) findViewById(R.id.hand);

        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonSelected();
            }
        };

        age.setOnCheckedChangeListener(listener);
        gender.setOnCheckedChangeListener(listener);
        hand.setOnCheckedChangeListener(listener);
    }

    public void radioButtonSelected() {
        Button nextButton = (Button) findViewById(R.id.next);
        nextButton.setEnabled(isFormFilledOut());
    }

    public void nextScreen(View view) {
        String age = getRadioLabel(this.age);
        String gender = getRadioLabel(this.gender);
        String hand = getRadioLabel(this.hand);

        Logger.newSession(this);
        Logger.logDemographics(age, gender, hand);

        Intent intent = new Intent(this, EnrollmentActivity.class);
        startActivity(intent);
    }

    private String getRadioLabel(RadioGroup group) {
        int checkedId = group.getCheckedRadioButtonId();
        RadioButton button = (RadioButton) findViewById(checkedId);
        return button.getText().toString();
    }

    private boolean isFormFilledOut() {
        return age.getCheckedRadioButtonId() != -1 && gender.getCheckedRadioButtonId() != -1 &&
                hand.getCheckedRadioButtonId() != -1;
    }
}
