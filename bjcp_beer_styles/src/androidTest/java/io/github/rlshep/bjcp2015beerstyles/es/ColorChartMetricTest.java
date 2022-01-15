package io.github.rlshep.bjcp2015beerstyles.es;

import androidx.test.espresso.matcher.ViewMatchers;
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
import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2015;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest

// Run with metric Locale country
public class ColorChartMetricTest extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void reset_settings() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2015));
        setLanguage("Español");
        setSrmSgAbv(false, false, true);
    }

    @Test
    public void testColorChartUS() {
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.categoryListView)).atPosition(0).perform(longClick());
        onView(withId(R.id.pager)).perform(swipeLeft());    // Bookmarks
        onView(withId(R.id.pager)).perform(swipeLeft());    // Color Chart

        onView(withId(R.id.color_amt_1)).check(matches(Matchers.hasValueEqualTo("EBC")));
    }
}
