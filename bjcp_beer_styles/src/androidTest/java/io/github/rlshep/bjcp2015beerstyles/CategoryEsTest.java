package io.github.rlshep.bjcp2015beerstyles;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
public class CategoryEsTest extends BJCPTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testCategoryCount() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasCountEqualTo(43)));
    }

    @Test
    public void testCategoryStrongAle_es() {
        setLocale("es", "ES");

        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Ale Británica Fuerte", 17)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(17).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("British Strong Ale: Burton Ale", 2)));
    }


    @Test
    public void testCategoryIPA_es() {
        setLocale("es", "ES");

        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("IPA", 21)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(21).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("IPA Especialidad: IPA Nueva Inglaterra", 8)));
    }

    @Test
    public void testCategoryLocal_es() {
        setLocale("es", "ES");
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Estilos Locales", 35)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(35).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Catharina Agrio", 4)));
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("New Zealand Pilsner", 5)));
    }

    @Test
    public void testCategorySaison_es() {
        setLocale("es", "ES");

        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Ale Fuerte Belga", 25)));

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(25).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Saison", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onView(withId(R.id.srmText1)).check(matches(Matchers.hasValueEqualTo("Pálida SRM")));
        onView(withId(R.id.srmText2)).check(matches(Matchers.hasValueEqualTo("Oscura SRM")));
    }

    @Test
    public void testCategoryMead_es() {
        setLocale("es", "ES");
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Hidromiel Con Especias", 40)));
    }

}
