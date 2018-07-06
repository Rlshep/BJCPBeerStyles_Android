package io.github.rlshep.bjcp2015beerstyles;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CategoryTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testCategoryCount() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasCountEqualTo(43)));
    }

    @Test
    public void testCategoryStrongAle() throws Exception {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong British Ale", 17)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(17).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("British Strong Ale: Burton Ale", 2)));
    }

    @Test
    public void testCategoryIPA() throws Exception {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("IPA", 21)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(21).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Specialty IPA: New England IPA", 9)));
    }

    @Test
    public void testCategoryLocal() throws Exception {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Local Styles", 41)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(41).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Catharina Sour", 4)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("New Zealand Pilsner", 5)));
    }

}
