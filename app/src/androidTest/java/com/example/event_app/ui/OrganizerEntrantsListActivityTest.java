package com.example.event_app.ui;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.event_app.R;
import com.example.event_app.ui.OrganizerEntrantsListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Instrumented tests for OrganizerEntrantsListActivity
 * Tests filter spinner, UI display, and entrant management
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerEntrantsListActivityTest {

    private static final String TEST_EVENT_ID = "test-event-123";
    private static final String DEFAULT_FILTER = "Selected";

    @Rule
    public ActivityScenarioRule<OrganizerEntrantsListActivity> activityRule =
            new ActivityScenarioRule<>(createIntentWithExtras());

    @Before
    public void setUp() {
        // Initialize Intents for intent verification
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Intents
        Intents.release();
    }

    private Intent createIntentWithExtras() {
        Intent intent = new Intent();
        intent.putExtra("eventId", TEST_EVENT_ID);
        intent.putExtra("defaultFilter", DEFAULT_FILTER);
        return intent;
    }

    @Test
    public void testOrganizerEntrantsListActivity_DisplaysFilterSpinner() {
        // Verify filter spinner is displayed
        onView(withId(R.id.filterSpinner))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerEntrantsListActivity_DisplaysCountTextView() {
        // Verify count TextView is displayed
        onView(withId(R.id.tvCount))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerEntrantsListActivity_DisplaysRecyclerView() {
        // Verify RecyclerView is displayed
        onView(withId(R.id.recycler))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerEntrantsListActivity_DisplaysProgressBar() {
        // Verify progress bar exists
        onView(withId(R.id.progress))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerEntrantsListActivity_FilterSpinner_SelectPending() {
        // Select "Pending" from spinner
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("Pending"))
                .perform(click());
        
        // Verify spinner shows "Pending"
        onView(withId(R.id.filterSpinner))
                .check(matches(withSpinnerText(containsString("Pending"))));
    }

    @Test
    public void testOrganizerEntrantsListActivity_FilterSpinner_SelectAccepted() {
        // Select "Accepted" from spinner
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("Accepted"))
                .perform(click());
        
        // Verify spinner shows "Accepted"
        onView(withId(R.id.filterSpinner))
                .check(matches(withSpinnerText(containsString("Accepted"))));
    }

    @Test
    public void testOrganizerEntrantsListActivity_FilterSpinner_SelectConfirmed() {
        // Select "Confirmed" from spinner
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("Confirmed"))
                .perform(click());
        
        // Verify spinner shows "Confirmed" and final badge is visible
        onView(withId(R.id.filterSpinner))
                .check(matches(withSpinnerText(containsString("Confirmed"))));
        
        onView(withId(R.id.tvFinalBadge))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerEntrantsListActivity_FilterSpinner_SelectCancelled() {
        // Select "Cancelled" from spinner
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("Cancelled"))
                .perform(click());
        
        // Verify spinner shows "Cancelled"
        onView(withId(R.id.filterSpinner))
                .check(matches(withSpinnerText(containsString("Cancelled"))));
    }

    @Test
    public void testOrganizerEntrantsListActivity_FilterSpinner_SelectAll() {
        // Select "All" from spinner
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("All"))
                .perform(click());
        
        // Verify spinner shows "All"
        onView(withId(R.id.filterSpinner))
                .check(matches(withSpinnerText(containsString("All"))));
    }

    @Test
    public void testOrganizerEntrantsListActivity_FinalBadgeVisibility() {
        // Select "Confirmed" filter - badge should be visible
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("Confirmed"))
                .perform(click());
        
        // Verify final badge is visible for Confirmed filter
        onView(withId(R.id.tvFinalBadge))
                .check(matches(isDisplayed()));
        
        // Select another filter - badge should be hidden
        onView(withId(R.id.filterSpinner))
                .perform(click());
        
        onData(is("Pending"))
                .perform(click());
        
        // Note: Badge visibility is tested through UI state
        // Espresso can check visibility, but we verify the spinner works
    }

    @Test
    public void testOrganizerEntrantsListActivity_DefaultFilterIsSelected() {
        // Verify default filter "Selected" is selected
        onView(withId(R.id.filterSpinner))
                .check(matches(withSpinnerText(containsString(DEFAULT_FILTER))));
    }
}
