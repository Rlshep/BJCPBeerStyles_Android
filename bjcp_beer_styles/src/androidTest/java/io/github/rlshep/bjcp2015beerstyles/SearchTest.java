package io.github.rlshep.bjcp2015beerstyles;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.rlshep.bjcp2015beerstyles.MainActivity;
import io.github.rlshep.bjcp2015beerstyles.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchTest {
    public static final String STRING_TO_BE_TYPED = "Espresso";
    public static final String APOSTROPHE_TO_BE_TYPED = "Marston's";
    public static final String EXACT_TO_BE_TYPED = "Fuller's";

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
        onView(withId(R.id.sectionsText)).check(matches(hasValueEqualTo(STRING_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_sweet_stout_apostrophe() {
        // Search for Keyword
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(APOSTROPHE_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        // Click on first result Sweet Stout
        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(hasValueEqualTo(APOSTROPHE_TO_BE_TYPED)));
    }

    @Test
    public void searchText_return_fullers_exact() {
        // Search for Keyword
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.search_src_text)).perform(typeText(EXACT_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());

        onData(anything()).inAdapterView(withId(R.id.searchResults)).atPosition(0).perform(click());
        onView(withId(R.id.sectionsText)).check(matches(hasValueEqualTo(EXACT_TO_BE_TYPED)));
    }

    private Matcher<View> hasValueEqualTo(final String content) {

        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Has EditText/TextView the value:  " + content);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView) && !(view instanceof EditText)) {
                    return false;
                }
                if (view != null) {
                    String text;
                    if (view instanceof TextView) {
                        text = ((TextView) view).getText().toString();
                    } else {
                        text = ((EditText) view).getText().toString();
                    }

                    return StringUtils.containsIgnoreCase(text, content);
                }
                return false;
            }
        };
    }
}
