package io.github.rlshep.bjcp2015beerstyles.en;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.BJCPTest;
import io.github.rlshep.bjcp2015beerstyles.MainActivity;
import io.github.rlshep.bjcp2015beerstyles.R;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2015;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GuidelineLanguageTest extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void reset_settings() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2015));
        setMeadGuideline("2015 BJCP");
        setCiderGuideline("2015 BJCP");
        setLanguage("English");
    }

    @Test
    public void testMeadCiderGuidelineSwitching() {
        // Verify defaults (after reset)
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settings_mead_guideline)).check(matches(withSpinnerText(containsString("2015 BJCP"))));
        onView(withId(R.id.settings_cider_guideline)).check(matches(withSpinnerText(containsString("2015 BJCP"))));
        Espresso.pressBack();

        // Switch to newer Mead/Cider
        setMeadGuideline("2026 BJCP");
        setCiderGuideline("2025 BJCP");

        // Verify persist
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settings_mead_guideline)).check(matches(withSpinnerText(containsString("2026 BJCP"))));
        onView(withId(R.id.settings_cider_guideline)).check(matches(withSpinnerText(containsString("2025 BJCP"))));
        Espresso.pressBack();
    }

    @Test
    public void testMeadCiderGuidelineSpanish() {
        setLanguage("Español");
        onView(withId(R.id.action_settings)).perform(click());
        
        // Check labels in Spanish
        onView(withId(R.id.settingMeadGuidelineName)).check(matches(withText(containsString("Guía de Hidromiel"))));
        onView(withId(R.id.settingCiderGuidelineName)).check(matches(withText(containsString("Guía de Sidra"))));
        
        // Change while in Spanish
        onView(withId(R.id.settings_mead_guideline)).perform(click());
        onData(containsString("2026 BJCP")).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void testMeadCiderGuidelineUkrainian() {
        setLanguage("Українська");
        onView(withId(R.id.action_settings)).perform(click());
        
        // Check labels in Ukrainian
        onView(withId(R.id.settingMeadGuidelineName)).check(matches(withText(containsString("Настанови для медовухи"))));
        onView(withId(R.id.settingCiderGuidelineName)).check(matches(withText(containsString("Настанови для сидру"))));
        
        onView(withId(R.id.settings_mead_guideline)).perform(click());
        onData(containsString("2026 BJCP")).perform(click());
        Espresso.pressBack();
    }
}
