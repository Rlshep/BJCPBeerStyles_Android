package io.github.rlshep.bjcp2015beerstyles.en;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class Tag2021Test extends BJCPTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setLanguage("English");
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2021));
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void testTags_en() {
        ArrayList<List> tags = new ArrayList<>();
        tags.add(Arrays.asList(2, 2, "International Amber Lager"));
        tags.add(Arrays.asList(2, 3, "International Dark Lager"));
        tags.add(Arrays.asList(3, 3, "Czech Amber Lager"));
        tags.add(Arrays.asList(4, 1, "Munich Helles"));
        tags.add(Arrays.asList(4, 3, "Helles Bock"));
        tags.add(Arrays.asList(5, 4, "German Pils"));
        tags.add(Arrays.asList(6, 3, "Dunkles Bock"));
        tags.add(Arrays.asList(7, 2, "Altbier"));
        tags.add(Arrays.asList(8, 2, "Schwarzbier"));
        tags.add(Arrays.asList(11, 3, "Strong Bitter"));
        tags.add(Arrays.asList(13, 3, "English Porter"));
        tags.add(Arrays.asList(14, 3, "Scottish Export"));
        tags.add(Arrays.asList(15, 3, "Irish Extra Stout"));
        tags.add(Arrays.asList(16, 4, "Foreign Extra Stout"));
        tags.add(Arrays.asList(17, 5, "English Barley Wine"));
        tags.add(Arrays.asList(18, 2, "American Pale Ale"));
        tags.add(Arrays.asList(19, 3, "American Brown Ale"));
        tags.add(Arrays.asList(22, 4, "Wheatwine"));
        tags.add(Arrays.asList(25, 3, "Belgian Golden Strong Ale"));
        tags.add(Arrays.asList(27, 2, "Historical Beer: Kentucky Common"));
        tags.add(Arrays.asList(32, 2, "Specialty Smoked Beer"));
        tags.add(Arrays.asList(33, 2, "Specialty Wood-Aged Beer"));

        for (List tag : tags) {
            isTagPresent(tag);
        }
    }

    private void isTagPresent(List tag) {
        int categoryPosition = (Integer) tag.get(0);
        int subCategoryPosition = (Integer) tag.get(1);
        String title = (String) tag.get(2);

        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(categoryPosition).perform(click());
        onView(withId(R.id.categoryListView)).check(matches(Matchers.hasListViewEqualTo(title, subCategoryPosition)));
        onData(anything()).inAdapterView(withId(R.id.categoryListView)).atPosition(subCategoryPosition).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Tags")));
        Espresso.pressBack();
        Espresso.pressBack();
    }
}
