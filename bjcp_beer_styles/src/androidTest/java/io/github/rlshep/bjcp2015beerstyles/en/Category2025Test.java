package io.github.rlshep.bjcp2015beerstyles.en;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2025;
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

public class Category2025Test extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2025));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testCategoryIntro() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Cider 2025", 0)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Introduction to Cider Guidelines", 0)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Do not attempt to infer any deeper meaning")));
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Potential allergens must always be declared")));
    }

    @Test
    public void testCategoryCider_en() {
        // Fire Cider
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Cider 2025", 0)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Strong Cider", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(5).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("cidre de feu")));

        Espresso.pressBack();
        Espresso.pressBack();

        // Ice Perry
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Perry", 4)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(4).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Ice Perry", 3)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(3).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Pear juice is frozen before fermentation")));
    }

    @Test
    public void testCommonCider_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Cider 2025", 0)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Traditional Cider", 1)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(1).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Common Cider", 2)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(2).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Common Cider")));
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("culinary (table) apples")));
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Apple character noticeable, either as the flavor of the fruit")));
    }

    @Test
    public void testSpecialtyCider_en() {
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Cider 2025", 0)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Specialty Cider", 3)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(3).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo("Spiced Cider", 3)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(3).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("botanicals")));
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("A pleasant integration of cider and added spices.")));
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Reflecting base style. Cider may be tannic or astringent")));
    }

}
