package io.github.rlshep.bjcp2015beerstyles;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.matchers.Matchers;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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

    @Test
    public void searchText_return_sweet_stout() {
        // Search for Keyword Espresso
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(STRING_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_sweet_stout_apostrophe() {
        // Search for Keyword
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(APOSTROPHE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(APOSTROPHE_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_fullers_exact() {
        // Search for Keyword
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(EXACT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(EXACT_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_new_england_ipa() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_hazy_ipa() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA_SYNONYM), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_kolsch() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(KOLSCH), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(KOLSCH_ACTUAL)));
    }

    @Test
    public void searchText_return_tag() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NORTH_AMERICA), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(1).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NORTH_AMERICA)));
    }

}
