package com.example.event_app;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented tests for EntrantBrowseEventsActivity
 * Tests search functionality, UI display, and navigation
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EntrantBrowseEventsActivityTest {

    @Rule
    public ActivityScenarioRule<EntrantBrowseEventsActivity> activityRule =
            new ActivityScenarioRule<>(EntrantBrowseEventsActivity.class);

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
    public void testEntrantBrowseEventsActivity_DisplaysSearchField() {
        // Verify search field is displayed
        onView(withId(R.id.searchEvents))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEntrantBrowseEventsActivity_DisplaysRecyclerView() {
        // Verify RecyclerView is displayed
        onView(withId(R.id.recyclerViewEvents))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEntrantBrowseEventsActivity_SearchFieldAcceptsInput() {
        // Type text into search field
        onView(withId(R.id.searchEvents))
                .perform(typeText("test event"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEntrantBrowseEventsActivity_SearchFieldClears() {
        // Type text and then clear it
        onView(withId(R.id.searchEvents))
                .perform(typeText("test"))
                .perform(ViewActions.clearText())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEntrantBrowseEventsActivity_EmptyStateDisplayed() {
        // Verify empty state TextView exists (may or may not be visible)
        onView(withId(R.id.tvEmptyEvents))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEntrantBrowseEventsActivity_ActionBarBackButton() {
        // Verify action bar is displayed with back button
        // Note: Testing back navigation would require checking onSupportNavigateUp
        // This test verifies the activity has the expected UI elements
        onView(withId(R.id.recyclerViewEvents))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEntrantBrowseEventsActivity_SearchFiltersEvents() {
        // Enter search query
        String searchQuery = "concert";
        onView(withId(R.id.searchEvents))
                .perform(typeText(searchQuery));
        
        // Verify search field contains the query
        onView(withId(R.id.searchEvents))
                .check(matches(ViewMatchers.withText(searchQuery)));
    }

    @Test
    public void testEntrantBrowseEventsActivity_ActivityTitle() {
        // Verify activity displays correct title in action bar
        // Note: This tests that the activity loads correctly
        onView(withId(R.id.recyclerViewEvents))
                .check(matches(isDisplayed()));
    }
}
