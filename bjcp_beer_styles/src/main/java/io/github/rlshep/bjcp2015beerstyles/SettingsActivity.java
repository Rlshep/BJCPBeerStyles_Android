package io.github.rlshep.bjcp2015beerstyles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;

public class SettingsActivity extends BjcpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_settings);

        setupToolbar(R.id.scbToolbar, getString(R.string.title_activity_settings), true, true);

        initializeRadioButtons();
        addListenerOnButton();
    }

    private void initializeRadioButtons() {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String unitsPref = sharedPref.getString(BjcpConstants.UNIT, null); // getting String
        RadioButton metric = findViewById(R.id.metric);
        RadioButton imperial = findViewById(R.id.imperial);

        if (BjcpConstants.METRIC.equals(unitsPref)) {
            metric.setChecked(true);
            imperial.setChecked(false);
        } else {
            metric.setChecked(false);
            imperial.setChecked(true);
        }
    }

    public void addListenerOnButton() {
        RadioButton metric = findViewById(R.id.metric);
        RadioButton imperial = findViewById(R.id.imperial);

        metric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnitPreferences(BjcpConstants.METRIC);
            }
        });

        imperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnitPreferences(BjcpConstants.IMPERIAL);
            }
        });

    }
}
