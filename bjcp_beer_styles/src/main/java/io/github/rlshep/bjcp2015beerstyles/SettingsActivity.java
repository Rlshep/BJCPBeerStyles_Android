package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;

public class SettingsActivity extends BjcpActivity {
    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_settings);

        preferencesHelper = new PreferencesHelper(this);
        setupToolbar(R.id.scbToolbar, getString(R.string.title_activity_settings), true, true);

        initializeRadioButtons();
        initializeSpinners();
        addListenerOnButton();
        addListenerOnSpinners();
    }

    private void initializeRadioButtons() {
        RadioButton specificGravity = findViewById(R.id.settings_specific_gravity);
        RadioButton plato = findViewById(R.id.settings_plato);
        RadioButton srm = findViewById(R.id.settings_srm);
        RadioButton ebc = findViewById(R.id.settings_ebc);

        specificGravity.setChecked(!preferencesHelper.isPlato());
        plato.setChecked(preferencesHelper.isPlato());
        srm.setChecked(!preferencesHelper.isEBC());
        ebc.setChecked(preferencesHelper.isEBC());
    }

    private void initializeSpinners() {
        Spinner guideline = (Spinner) findViewById(R.id.settings_guideline);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.settings_guidelines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guideline.setAdapter(adapter);
    }

    private void addListenerOnButton() {
        RadioButton specificGravity = findViewById(R.id.settings_specific_gravity);
        RadioButton plato = findViewById(R.id.settings_plato);
        RadioButton srm = findViewById(R.id.settings_srm);
        RadioButton ebc = findViewById(R.id.settings_ebc);

        specificGravity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_GRAVITY, PreferencesHelper.GRAVITY_SPECIFIC);
            }
        });

        plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_GRAVITY, PreferencesHelper.GRAVITY_PLATO);
            }
        });

        srm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_COLOR, PreferencesHelper.COLOR_SRM);
            }
        });

        ebc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_COLOR, PreferencesHelper.COLOR_EBC);
            }
        });
    }

    private void addListenerOnSpinners() {
        Spinner guideline = (Spinner) findViewById(R.id.settings_guideline);

        guideline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String styleTypeName = (String) parent.getItemAtPosition(pos);

                preferencesHelper.setPreferences(PreferencesHelper.UNIT_STYLE_TYPE, GUIDELINE_MAP.get(styleTypeName));
                getToolbar().setTitle(styleTypeName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
