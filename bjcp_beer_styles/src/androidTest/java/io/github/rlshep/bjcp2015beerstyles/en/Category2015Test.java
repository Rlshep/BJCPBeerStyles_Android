package io.github.rlshep.bjcp2015beerstyles.en;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2015;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.github.rlshep.bjcp2015beerstyles.BJCPTest;
import io.github.rlshep.bjcp2015beerstyles.MainActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

public class Category2015Test extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2015));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testCategoryCider_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Cider 2015", 38)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(38).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Introduction to Cider Guidelines", 0)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Standard Cider and Perry", 1)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Sweetness")));
        Espresso.pressBack();
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(1).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("New World Cider", 3)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(3).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Impression")));
    }
}
