package io.github.rlshep.bjcp2015beerstyles.en;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.BJCP_2015;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.GUIDELINE_MAP;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.getKeyValue;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest extends BJCPTest {
    public static final String STRING_TO_BE_TYPED = "Espresso";
    public static final String APOSTROPHE_TO_BE_TYPED = "Marston's";
    public static final String EXACT_TO_BE_TYPED = "Fuller's";
    public static final String NEW_ENGLAND_IPA = "New England IPA";
    public static final String NEW_ENGLAND_IPA_SYNONYM = "Hazy IPA";
    public static final String KOLSCH = "Kolsch";
    public static final String KOLSCH_ACTUAL = "Kölsch";
    public static final String NORTH_AMERICA = "north-america";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setup() {
        setGuideline(getKeyValue(GUIDELINE_MAP, BJCP_2015));
        setLanguage("English");
        setSrmSgAbv(true, true, true);
    }

    @Test
    public void searchText_return_sweet_stout() throws InterruptedException {
        // Search for Keyword Espresso
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(STRING_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_sweet_stout_apostrophe() throws InterruptedException {
        // Search for Keyword
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(APOSTROPHE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(APOSTROPHE_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_fullers_exact() throws InterruptedException {
        // Search for Keyword
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(EXACT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(EXACT_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_new_england_ipa() throws InterruptedException {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_hazy_ipa() throws InterruptedException {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA_SYNONYM), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_kolsch() throws InterruptedException {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(KOLSCH), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(KOLSCH_ACTUAL)));
    }

    @Test
    public void searchText_return_tag() throws InterruptedException {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NORTH_AMERICA), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(1).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NORTH_AMERICA)));
    }

}
