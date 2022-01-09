package io.github.rlshep.bjcp2015beerstyles.es;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchEsTest extends BJCPTest {
    public static final String ESPRESSO = "expreso";
    public static final String APOSTROPHE_TO_BE_TYPED = "Marston's";
    public static final String EXACT_TO_BE_TYPED = "Fuller's";
    public static final String NEW_ENGLAND_IPA = "New England IPA";
    public static final String NEW_ENGLAND_IPA_SYNONYM = "Hazy IPA";
    public static final String KOLSCH = "Kolsch";
    public static final String KOLSCH_ACTUAL = "Kölsch";
    public static final String PREPRO = "Pre-Prohibition";
    public static final String TRADITIONAL = "estilo-tradicional";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void searchText_return_sweet_stout() throws InterruptedException {
        setLocale("es", "ES");

        // Search for Keyword Espresso
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(ESPRESSO), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(ESPRESSO)));
    }

    @Test
    public void searchText_return_sweet_stout_apostrophe() throws InterruptedException {
        setLocale("es", "ES");

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
        setLocale("es", "ES");

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
        setLocale("es", "ES");

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_new_england_ipa_en() throws InterruptedException {
        setLocale("es", "ES");

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_hazy_ipa() throws InterruptedException {
        setLocale("es", "ES");

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(NEW_ENGLAND_IPA_SYNONYM), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(NEW_ENGLAND_IPA)));
    }

    @Test
    public void searchText_return_kolsch() throws InterruptedException {
        setLocale("es", "ES");

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(KOLSCH), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(KOLSCH_ACTUAL)));
    }

    @Test
    public void searchText_return_prepro() throws InterruptedException {
        setLocale("es", "ES");

        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(PREPRO), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo("Pilsner Americana")));
    }


    @Test
    public void searchText_return_English_search_returns_spanish() throws InterruptedException {
        setLocale("es", "ES");

        // Search for Keyword Espresso
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(ESPRESSO), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(ESPRESSO)));
    }

    @Test
    public void searchText_return_tag() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(TRADITIONAL), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(1).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(Matchers.hasValueEqualTo(TRADITIONAL)));
    }
}
