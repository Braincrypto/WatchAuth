package com.sri.csl.cortical.watchauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sri.csl.cortical.watchauth.logging.Logger;


public class DemographicsActivity extends Activity {

    EditText age;
    RadioGroup gender, hand, watch;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);
        age = (EditText) findViewById(R.id.age);
        gender = (RadioGroup) findViewById(R.id.gender);
        hand = (RadioGroup) findViewById(R.id.hand);
        watch = (RadioGroup) findViewById(R.id.watch);
        nextButton = (Button) findViewById(R.id.next);

        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonSelected();
            }
        };

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                radioButtonSelected();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        age.addTextChangedListener(watcher);
        gender.setOnCheckedChangeListener(listener);
        hand.setOnCheckedChangeListener(listener);
        watch.setOnCheckedChangeListener(listener);
    }

    public void radioButtonSelected() {
        nextButton.setEnabled(isFormFilledOut());
    }

    public void nextScreen(View view) {
        int age = Integer.parseInt(this.age.getText().toString());
        String gender = getRadioLabel(this.gender);
        String hand = getRadioLabel(this.hand);

        if(watch.getCheckedRadioButtonId() == R.id.watch_pebble)
        {
            TapActivity.loggingType = TapActivity.LOGGING_TYPE.PEBBLE;
        } else {
            TapActivity.loggingType = TapActivity.LOGGING_TYPE.WEAR;
        }

        FingerNames.hand = FingerNames.HANDS.valueOf(hand.toUpperCase());

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

    private boolean ageValid() {
        try {
            int age = Integer.parseInt(this.age.getText().toString());

            return (age > 0 || age < 1000);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFormFilledOut() {
        return ageValid() && gender.getCheckedRadioButtonId() != -1 &&
                hand.getCheckedRadioButtonId() != -1 && watch.getCheckedRadioButtonId() != -1;
    }
}
