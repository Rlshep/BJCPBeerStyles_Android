package io.github.rlshep.bjcp2015beerstyles;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterTest extends BJCPTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testScreenRenders() {
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(longClick());
        onView(withId(R.id.pager)).perform(swipeLeft());    // Bookmarks
        onView(withId(R.id.pager)).perform(swipeLeft());    // Color Chart
        onView(withId(R.id.pager)).perform(swipeLeft());    // Filter

        onView(withId(R.id.ibu_text)).check(matches(Matchers.hasValueEqualTo("IBU")));
        onView(withId(R.id.abv_text)).check(matches(Matchers.hasValueEqualTo("ABV")));
        onView(withId(R.id.color_text)).check(matches(Matchers.hasValueEqualTo("SRM")));
    }
}
