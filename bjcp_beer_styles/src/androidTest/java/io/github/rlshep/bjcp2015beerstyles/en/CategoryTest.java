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
import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2015;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.CoreMatchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CategoryTest extends BJCPTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2015));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testCategoryCount() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasCountEqualTo(39)));
    }

    @Test
    public void testCategoryStrongAle_en() {

        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong British Ale", 17)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(17).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("British Strong Ale: Burton Ale", 2)));
    }

    @Test
    public void testCategoryIPA_en() {

        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("IPA", 21)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(21).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Specialty IPA: New England IPA", 6)));
    }

    @Test
    public void testCategoryLocal() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Local Styles", 35)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(35).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Catharina Sour", 4)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("New Zealand Pilsner", 5)));
    }

    @Test
    public void testCategorySaison() throws InterruptedException {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Belgian Ale", 25)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Saison", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());

        onView(withId(R.id.srmText1)).check(matches(Matchers.hasValueEqualTo("dark SRM")));
        onView(withId(R.id.srmText2)).check(matches(Matchers.hasValueEqualTo("pale SRM")));
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
    public void testKellerBier() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Amber Bitter European Beer", 7)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(7).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Kellerbier", 3)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Kellerbier: Pale Kellerbier", 4)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Kellerbier: Amber Kellerbier", 5)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(3).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Sommerbier")));
    }
}
