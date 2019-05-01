package io.github.rlshep.bjcp2015beerstyles;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.DEFAULT_LANGUAGE;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.SPANISH;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.SPANISH_HYBRID;


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
        addListenerOnButton();
    }

    private void initializeRadioButtons() {
        RadioButton specificGravity = findViewById(R.id.settings_specific_gravity);
        RadioButton plato = findViewById(R.id.settings_plato);
        RadioButton srm = findViewById(R.id.settings_srm);
        RadioButton ebc = findViewById(R.id.settings_ebc);
        Switch englishTitles = findViewById((R.id.settings_english_titles));

        specificGravity.setChecked(!preferencesHelper.isPlato());
        plato.setChecked(preferencesHelper.isPlato());
        srm.setChecked(!preferencesHelper.isEBC());
        ebc.setChecked(preferencesHelper.isEBC());
        englishTitles.setChecked(preferencesHelper.isPreferenceEqual(PreferencesHelper.LANGUAGE, SPANISH_HYBRID));
        
        if (DEFAULT_LANGUAGE.equals(preferencesHelper.getLanguage())) {
            TextView title = findViewById(R.id.settingTitle);

            title.setVisibility(View.INVISIBLE);
            englishTitles.setEnabled(false);
            englishTitles.setVisibility(View.INVISIBLE);
        }
    }

    public void addListenerOnButton() {
        RadioButton specificGravity = findViewById(R.id.settings_specific_gravity);
        RadioButton plato = findViewById(R.id.settings_plato);
        RadioButton srm = findViewById(R.id.settings_srm);
        RadioButton ebc = findViewById(R.id.settings_ebc);
        Switch englishTitles = findViewById((R.id.settings_english_titles));

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

        englishTitles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (englishTitles.isChecked()) {
                    preferencesHelper.setPreferences(PreferencesHelper.LANGUAGE, SPANISH_HYBRID);
                } else {
                    preferencesHelper.setPreferences(PreferencesHelper.LANGUAGE, SPANISH);
                }
            }
        });
    }
}
