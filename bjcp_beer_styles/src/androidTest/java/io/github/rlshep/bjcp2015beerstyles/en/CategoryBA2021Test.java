package io.github.rlshep.bjcp2015beerstyles.en;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.github.rlshep.bjcp2015beerstyles.BJCPTest;
import io.github.rlshep.bjcp2015beerstyles.MainActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BA_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.Matchers.anything;

public class CategoryBA2021Test extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BA_2021));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testCategoryStrongAle_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("British Origin Ale Styles", 1)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(1).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Ale", 19)));
    }
}
