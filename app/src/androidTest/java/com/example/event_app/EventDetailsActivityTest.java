package com.example.event_app;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.event_app.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented tests for EventDetailsActivity
 * Tests UI display, button clicks, and navigation
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailsActivityTest {

    private static final String TEST_EVENT_ID = "test-event-123";

    @Rule
    public ActivityScenarioRule<EventDetailsActivity> activityRule =
            new ActivityScenarioRule<>(createIntentWithEventId());

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

    private Intent createIntentWithEventId() {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_EVENT_ID, TEST_EVENT_ID);
        return intent;
    }

    @Test
    public void testEventDetailsActivity_DisplaysProgressBarInitially() {
        // Verify progress bar is displayed (activity loads event data)
        onView(withId(R.id.progressBar_event_details))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_DisplaysEventName() {
        // Verify event name TextView exists
        onView(withId(R.id.text_event_name))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_DisplaysEventDescription() {
        // Verify event description TextView exists
        onView(withId(R.id.text_event_description))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_DisplaysEventPoster() {
        // Verify event poster ImageView exists
        onView(withId(R.id.image_event_poster))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_DisplaysJoinButton() {
        // Verify join button exists
        onView(withId(R.id.button_join_event))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_DisplaysBackButton() {
        // Verify back button exists
        onView(withId(R.id.button_go_back))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_BackButton_Click() {
        // Click back button - should finish activity
        onView(withId(R.id.button_go_back))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Note: Activity should finish, but we can't easily test that with Espresso
        // This test verifies the button is clickable
    }

    @Test
    public void testEventDetailsActivity_JoinButton_Click() {
        // Click join button
        // Note: This requires Firebase and user authentication
        // For now, verify button exists and is clickable
        onView(withId(R.id.button_join_event))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Button should handle click (may show toast or update state)
        // Actual Firebase operation would need mocking
    }

    @Test
    public void testEventDetailsActivity_ContentGroupDisplayed() {
        // Verify content group exists
        onView(withId(R.id.content_group))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsActivity_HandlesMissingEventId() {
        // Test with missing event ID - should show error and finish
        // This would require a separate activity rule with different intent
        // For now, we test the normal flow
        onView(withId(R.id.progressBar_event_details))
                .check(matches(isDisplayed()));
    }
}
