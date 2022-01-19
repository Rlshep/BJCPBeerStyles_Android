package io.github.rlshep.bjcp2015beerstyles.en;

import androidx.test.espresso.Espresso;
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
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2021;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.CoreMatchers.anything;

public class Category2021Test extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2021));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testCategoryStrongAle_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong British Ale", 17)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(17).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("British Strong Ale: Burton Ale", 2)));
    }

    @Test
    public void testCategoryMead_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Mead 2015", 37)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(37).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Introduction to Mead Guidelines", 0)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Traditional Mead", 1)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Sweetness")));
        Espresso.pressBack();
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(1).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Dry Mead", 1)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(1).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Impression")));
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

    @Test
    public void testCategorySaison() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Belgian Ale", 25)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Saison", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());

        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("(standard) ABV: 5.0% - 7.0%")));
        onView(withId(R.id.srmText1)).check(matches(Matchers.hasValueEqualTo("(dark) SRM")));
        onView(withId(R.id.srmText2)).check(matches(Matchers.hasValueEqualTo("(pale) SRM")));
    }

    @Test
    public void testCategoryWoodAged() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Wood Beer", 33)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(33).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Wood-Aged Beer", 1)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(1).perform(click());

        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("ABV: varies with base style, typically above-average")));
    }
}
