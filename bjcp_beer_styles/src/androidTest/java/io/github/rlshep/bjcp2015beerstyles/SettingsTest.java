package io.github.rlshep.bjcp2015beerstyles;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsTest extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void settings_imperial_and_metric() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settingUnit)).check(matches(Matchers.hasValueEqualTo("Units")));

        // Switch to Metric
        onView(withId(R.id.metric)).perform(click());
        Espresso.pressBack();

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("11.9°P")));

        // Back to main
        Espresso.pressBack();
        Espresso.pressBack();
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settingUnit)).check(matches(Matchers.hasValueEqualTo("Units")));

        // Switch back to Imperial
        onView(withId(R.id.imperial)).perform(click());
        Espresso.pressBack();
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("1.048")));
    }
}
