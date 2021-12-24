package io.github.rlshep.bjcp2015beerstyles.es;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest

// Run with language Espanol, Country Argentina
public class CategoryEsTest extends BJCPTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testCategoryCount() {
        onView(ViewMatchers.withId(R.id.categoryListView)).check(matches(Matchers.hasCountEqualTo(43)));
    }

    @Test
    public void testCategoryStrongAle_es() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong British Ale", 17)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(17).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("British Strong Ale: Burton Ale", 2)));
    }


    @Test
    public void testCategoryIPA_es() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("IPA", 21)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(21).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Specialty IPA: New England IPA", 8)));
    }

    @Test
    public void testCategoryLocal_es() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Local Styles", 35)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(35).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Catharina Sour", 4)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("New Zealand Pilsner", 5)));
    }

    @Test
    public void testCategorySaison_es() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Belgian Ale", 25)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Saison", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onView(withId(R.id.srmText1)).check(matches(Matchers.hasValueEqualTo("Pálida EBC")));
        onView(withId(R.id.srmText2)).check(matches(Matchers.hasValueEqualTo("Oscura EBC")));
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("11,9°P")));
    }

    @Test
    public void testCategoryMead_es() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Spiced Mead", 40)));
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
