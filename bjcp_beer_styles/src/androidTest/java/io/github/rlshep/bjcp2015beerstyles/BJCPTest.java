package io.github.rlshep.bjcp2015beerstyles;

import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.Locale;

import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;

public abstract class BJCPTest {
    protected void setLocale(String language, String country) {
        Locale locale = new Locale(language, country);
        // here we update locale for date formatters
        Locale.setDefault(locale);
        // here we update locale for app resources
        Resources res = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static void setGuideline(String guideline) {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settingGuidelineName)).check(matches(Matchers.hasValueEqualTo("Guideline:")));
        onView(withId(R.id.settings_guideline)).perform(click());
        onData(hasToString(guideline)).perform(click());
        onView(withId(R.id.settings_guideline)).check(matches(withSpinnerText(containsString(guideline))));
        Espresso.pressBack();
    }
}
