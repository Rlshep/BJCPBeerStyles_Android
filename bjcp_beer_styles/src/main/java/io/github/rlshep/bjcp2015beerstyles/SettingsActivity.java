package io.github.rlshep.bjcp2015beerstyles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants;
import io.github.rlshep.bjcp2015beerstyles.exceptions.ExceptionHandler;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BA_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.ENGLISH;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.LANGUAGE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper.UNIT_LANGUAGE;

public class SettingsActivity extends BjcpActivity {
    private PreferencesHelper preferencesHelper;
    private boolean onLoad = true;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_settings);

        preferencesHelper = new PreferencesHelper(this);
        currentLanguage = preferencesHelper.getLanguage();
        setupToolbar(R.id.scbToolbar, getString(R.string.title_activity_settings), true, true);
        initializeRadioButtons();
        initializeSpinners();
        addListenerOnButton();
        addListenerOnSpinners();
        setAppLanguage(currentLanguage);
    }

    private void initializeRadioButtons() {
        RadioButton specificGravity = findViewById(R.id.settings_specific_gravity);
        RadioButton plato = findViewById(R.id.settings_plato);
        RadioButton srm = findViewById(R.id.settings_srm);
        RadioButton ebc = findViewById(R.id.settings_ebc);
        RadioButton abv = findViewById(R.id.settings_abv);
        RadioButton abw = findViewById(R.id.settings_abw);

        specificGravity.setChecked(!preferencesHelper.isPlato());
        plato.setChecked(preferencesHelper.isPlato());
        srm.setChecked(!preferencesHelper.isEBC());
        ebc.setChecked(preferencesHelper.isEBC());
        abv.setChecked(preferencesHelper.isABV());
        abw.setChecked(!preferencesHelper.isABV());
    }

    private void initializeSpinners() {
        initializeSpinner(R.id.settings_guideline, R.array.settings_guidelines, preferencesHelper.getStyleTypeName());
        initializeSpinner(R.id.settings_language, R.array.settings_languages, BjcpConstants.getKeyValue(LANGUAGE_MAP, preferencesHelper.getLanguage()));
    }

    private void initializeSpinner(int spinnerId, int valuesId, String defaultValue) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, valuesId, android.R.layout.simple_spinner_item);
        String[] values = this.getResources().getStringArray(valuesId);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(preferencesHelper.getArrayPosition(values, defaultValue));
    }

    private void addListenerOnButton() {
        RadioButton specificGravity = findViewById(R.id.settings_specific_gravity);
        RadioButton plato = findViewById(R.id.settings_plato);
        RadioButton srm = findViewById(R.id.settings_srm);
        RadioButton ebc = findViewById(R.id.settings_ebc);
        RadioButton abv = findViewById(R.id.settings_abv);
        RadioButton abw = findViewById(R.id.settings_abw);

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

        abv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_ALCOHOL, PreferencesHelper.ALCOHOL_ABV);
            }
        });

        abw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_ALCOHOL, PreferencesHelper.ALCOHOL_ABW);
            }
        });
    }

    private void addListenerOnSpinners() {
        Spinner guideline = (Spinner) findViewById(R.id.settings_guideline);
        Spinner language = (Spinner) findViewById(R.id.settings_language);

        guideline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String styleTypeName = (String) parent.getItemAtPosition(pos);
                preferencesHelper.setPreferences(PreferencesHelper.UNIT_STYLE_TYPE, GUIDELINE_MAP.get(styleTypeName));
                getToolbar().setTitle(styleTypeName);
                showAvailabilityMessage();
                onLoad = false; // hack to skip first time in.
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String languageName = (String) parent.getItemAtPosition(pos);
                String lang = LANGUAGE_MAP.get(languageName);
                setAppLanguage(lang);
                preferencesHelper.setPreferences(UNIT_LANGUAGE, lang);

                if (!lang.equals(currentLanguage)) {
                    recreate();
                }
                showAvailabilityMessage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void showAvailabilityMessage() {
        if (isShowAvailabilityMessage()) {
            Spinner guideline = (Spinner) findViewById(R.id.settings_guideline);
            Spinner language = (Spinner) findViewById(R.id.settings_language);

            StringBuilder message = new StringBuilder();
            message.append(guideline.getSelectedItem());
            message.append(" ");
            message.append(getResources().getString(R.string.message_available_1));
            message.append(" ");
            message.append(language.getSelectedItem());
            message.append(" ");
            message.append(getResources().getString(R.string.message_available_2));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(message.toString());
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.button_never_show),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferencesHelper.setPreferences(PreferencesHelper.MESSAGE_NEVER_SHOW_AVAILABILITY, "true");
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private boolean isShowAvailabilityMessage() {
        final String[] UNAVAILABLE_GUIDELINES = {BJCP_2021, BA_2021};
        List availableGuidelines = Arrays.asList(UNAVAILABLE_GUIDELINES);

        return !onLoad &&
                preferencesHelper.isShowLanguageAvailabilityMessage() &&
                !preferencesHelper.getLanguage().equals(ENGLISH) &&
                availableGuidelines.contains(preferencesHelper.getStyleType());
    }
}
