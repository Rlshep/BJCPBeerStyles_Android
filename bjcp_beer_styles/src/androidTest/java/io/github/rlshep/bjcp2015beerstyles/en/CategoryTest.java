package io.github.rlshep.bjcp2015beerstyles.en;

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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategoryTest extends BJCPTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testCategoryCount() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasCountEqualTo(43)));
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
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Local Styles", 41)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(41).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Catharina Sour", 4)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("New Zealand Pilsner", 5)));
    }

    @Test
    public void testCategorySaison() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Belgian Ale", 25)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Saison", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onView(withId(R.id.srmText1)).check(matches(Matchers.hasValueEqualTo("pale SRM")));
        onView(withId(R.id.srmText2)).check(matches(Matchers.hasValueEqualTo("dark SRM")));
    }

    @Test
    public void testCategoryMead_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Spiced Mead", 39)));
    }
}
