package io.github.rlshep.bjcp2015beerstyles.en;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.BJCPTest;
import io.github.rlshep.bjcp2015beerstyles.MainActivity;
import io.github.rlshep.bjcp2015beerstyles.R;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2015;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BookmarksTest extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2015));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testSetupBookmarksAndPersist() {
        onData(anything()).inAdapterView(ViewMatchers.withId(R.id.categoryListView)).atPosition(0).perform(longClick());
        onView(withId(R.id.pager)).perform(swipeLeft());    // Bookmarks

        onData(anything()).inAdapterView(withId(R.id.bookmarkedListView)).atPosition(1).check(matches(anything()));

        onView(withId(R.id.pager)).perform(swipeLeft());    // Color Chart
        onView(withId(R.id.pager)).perform(swipeRight());    // Back to Bookmarks

        onData(anything()).inAdapterView(withId(R.id.bookmarkedListView)).atPosition(1).check(matches(anything()));
    }
}
