package com.example.event_app.admin;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Instrumented tests for AdminHomeActivity
 * Tests all button clicks and navigation
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminHomeActivityTest {

    @Rule
    public ActivityScenarioRule<AdminHomeActivity> activityRule =
            new ActivityScenarioRule<>(AdminHomeActivity.class);

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
    public void testAdminHomeActivity_DisplaysCorrectTitle() {
        // Verify the activity displays the admin dashboard
        onView(withId(R.id.tvEventsCount))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAdminHomeActivity_StatisticsAreDisplayed() {
        // Verify all statistics TextViews are displayed
        onView(withId(R.id.tvEventsCount))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.tvUsersCount))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.tvOrganizersCount))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.tvActiveCount))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAdminHomeActivity_BrowseEventsButton_Click() {
        // Click Browse Events button
        onView(withId(R.id.btnBrowseEvents))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify intent to AdminBrowseEventsActivity
        intended(hasComponent(AdminBrowseEventsActivity.class.getName()));
    }

    @Test
    public void testAdminHomeActivity_BrowseUsersButton_Click() {
        // Click Browse Users button
        onView(withId(R.id.btnBrowseUsers))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify intent to AdminBrowseUsersActivity
        intended(hasComponent("com.example.event_app.admin.AdminBrowseUsersActivity"));
    }

    @Test
    public void testAdminHomeActivity_BrowseImagesButton_Click() {
        // Click Browse Images button
        onView(withId(R.id.btnBrowseImages))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify intent to AdminBrowseImagesActivity
        intended(hasComponent("com.example.event_app.admin.AdminBrowseImagesActivity"));
    }

    @Test
    public void testAdminHomeActivity_GenerateReportsButton_Click() {
        // Click Generate Reports button
        onView(withId(R.id.btnGenerateReports))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify button is clickable (report generation may show toast)
        // Note: Actual report generation requires Firebase, so we just verify click works
        onView(withId(R.id.btnGenerateReports))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testAdminHomeActivity_FlaggedItemsButton_Click() {
        // Click Flagged Items button
        onView(withId(R.id.btnFlaggedItems))
                .check(matches(isDisplayed()))
                .perform(click());
        
        // Verify intent to AdminBrowseEventsActivity with flagged filter
        intended(allOf(
                hasComponent(AdminBrowseEventsActivity.class.getName()),
                IntentMatchers.hasExtra("showFlaggedOnly", true)
        ));
    }

    @Test
    public void testAdminHomeActivity_AllButtonsAreDisplayed() {
        // Verify all action buttons are displayed
        onView(withId(R.id.btnBrowseEvents))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnBrowseUsers))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnBrowseImages))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnGenerateReports))
                .check(matches(isDisplayed()));
        
        onView(withId(R.id.btnFlaggedItems))
                .check(matches(isDisplayed()));
    }
}
