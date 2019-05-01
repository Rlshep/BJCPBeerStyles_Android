package io.github.rlshep.bjcp2015beerstyles.es;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.BJCPTest;
import io.github.rlshep.bjcp2015beerstyles.MainActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsEsTest extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void settings_english_titles_in_spanish() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settings_english_titles)).perform(click());

        // Assuming on default Spanish
        Espresso.pressBack();
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Belgian Ale", 25)));

        //set back
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.settings_english_titles)).perform(click());
    }
}
