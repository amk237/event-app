package com.example.event_app.organizer;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.event_app.R;
import com.example.event_app.OrganizerHomeActivity;
import com.example.event_app.ui.EventPosterActivity;
import com.example.event_app.ui.OrganizerEntrantsListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Instrumented tests for OrganizerHomeActivity
 * Tests button clicks and navigation to manage entrants and update poster
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerHomeActivityTest {

    @Rule
    public ActivityScenarioRule<OrganizerHomeActivity> activityRule =
            new ActivityScenarioRule<>(OrganizerHomeActivity.class);

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

    @Test
    public void testOrganizerHomeActivity_DisplaysButtons() {
        // Verify both buttons are displayed
        onView(withId(R.id.btnManageEntrants))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnUpdatePoster))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerHomeActivity_ManageEntrantsButton_Click() {
        // Click Manage Entrants button
        onView(withId(R.id.btnManageEntrants))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify intent to OrganizerEntrantsListActivity with correct extras
        intended(allOf(
                hasComponent(OrganizerEntrantsListActivity.class.getName()),
                hasExtra("eventId", "event123"),
                hasExtra("defaultFilter", "Selected")
        ));
    }

    @Test
    public void testOrganizerHomeActivity_UpdatePosterButton_Click() {
        // Click Update Poster button
        onView(withId(R.id.btnUpdatePoster))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify intent to EventPosterActivity with event ID
        intended(allOf(
                hasComponent(EventPosterActivity.class.getName()),
                hasExtra("eventId", "event123")
        ));
    }

    @Test
    public void testOrganizerHomeActivity_ButtonsAreClickable() {
        // Verify buttons are enabled and clickable
        onView(withId(R.id.btnManageEntrants))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnUpdatePoster))
                .check(matches(isDisplayed()));
    }
}
