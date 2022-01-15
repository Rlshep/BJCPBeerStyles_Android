package io.github.rlshep.bjcp2015beerstyles.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang.StringUtils;

import io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.LANGUAGE;

public class PreferencesHelper {
    public static final String PREFERENCE_FILE_KEY = "bjcp_preferences";
    public static final String UNIT_GRAVITY = "gravity";
    public static final String UNIT_COLOR = "color";
    public static final String UNIT_STYLE_TYPE = "style_type";
    public static final String UNIT_ALCOHOL = "alcohol";
    public static final String GRAVITY_PLATO = "plato";
    public static final String GRAVITY_SPECIFIC = "specific";
    public static final String COLOR_SRM = "srm";
    public static final String COLOR_EBC = "ebc";
    public static final String ALCOHOL_ABV = "abv";
    public static final String ALCOHOL_ABW = "abw";

    private final SharedPreferences sharedPref;
    private final Activity activity;
    private final LocaleHelper lh;

    public PreferencesHelper(Activity a) {
        this.activity = a;
        this.sharedPref = a.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        this.lh = new LocaleHelper(this.activity);
    }

    public void setupPreferences() {
        String gravityPref = sharedPref.getString(UNIT_GRAVITY, null);
        String stylePref = sharedPref.getString(UNIT_STYLE_TYPE, null);
        String alcoholPref = sharedPref.getString(UNIT_ALCOHOL, null);
        String languagePref = sharedPref.getString(LANGUAGE, null);

        if (StringUtils.isEmpty(gravityPref)) {       //First time in
            if (MetricConverter.isCountryMetric(lh.getCountry())) {
                setPreferences(UNIT_GRAVITY, GRAVITY_PLATO);
                setPreferences(UNIT_COLOR, COLOR_EBC);
            } else {
                setPreferences(UNIT_GRAVITY, GRAVITY_SPECIFIC);
                setPreferences(UNIT_COLOR, COLOR_SRM);
            }
        }

        if (StringUtils.isEmpty(alcoholPref)) {
            setPreferences(UNIT_ALCOHOL, ALCOHOL_ABV);
        }

        if (StringUtils.isEmpty(stylePref)) {
            setPreferences(UNIT_STYLE_TYPE, BJCP_2021);
        }

        if (StringUtils.isEmpty(languagePref)) {
            setPreferences(LANGUAGE, lh.getLocaleLanguage());
        }
    }

    public void setPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public boolean isEBC() {
        return isPreferenceEqual(UNIT_COLOR, COLOR_EBC);
    }

    public boolean isSRM() {
        return isPreferenceEqual(UNIT_COLOR, COLOR_SRM);
    }

    public boolean isPlato() {
        return isPreferenceEqual(UNIT_GRAVITY, GRAVITY_PLATO);
    }

    public boolean isABV() { return isPreferenceEqual(UNIT_ALCOHOL, ALCOHOL_ABV); }

    public boolean isPreferenceEqual(String key, String value) {
        boolean equal = false;
        String pref = sharedPref.getString(key, null);

        if (!StringUtils.isEmpty(pref) && pref.equals(value)) {
            equal = true;
        }

        return equal;
    }

    public String getStyleTypeName() {
        String styleTypeValue = sharedPref.getString(UNIT_STYLE_TYPE, null);

        return BjcpConstants.getKeyValue(GUIDELINE_MAP, styleTypeValue);
    }

    public String getStyleType() {
        return  sharedPref.getString(UNIT_STYLE_TYPE, null);
    }

    public int getArrayPosition(String[] array, String item) {
        int i;

        for (i=0; i<array.length; i++) {
            if (item.equals(array[i])) {
                return i;
            }
        }

        return i;
    }

    public String getLanguage() {
        return sharedPref.getString(LANGUAGE, null);
    }
}
