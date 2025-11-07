# Bug Report - LuckySpot Event App

## Critical Bugs Fixed

### 1. **EventCreationActivity.java - Duplicate Date Parsing (FIXED)**
   - **Issue**: Lines 174-209 parsed dates and set event dates, but lines 214-225 parsed them again and overwrote the values, losing the combined date+time logic.
   - **Impact**: Event dates would lose time information and be overwritten with incorrect values.
   - **Fix**: Removed duplicate parsing code, preserved date+time combination logic.

### 2. **EventCreationActivity.java - Missing Null Checks (FIXED)**
   - **Issue**: Multiple calls to `EditText.getText().toString()` without null checks could cause NullPointerException.
   - **Impact**: App could crash when accessing EditText values.
   - **Fix**: Added null checks before calling `.toString()` on all EditText fields.

### 3. **EventCreationActivity.java - Missing Date Validation (FIXED)**
   - **Issue**: No validation that end date is after start date, or registration close is after registration open.
   - **Impact**: Users could create events with invalid date ranges.
   - **Fix**: Added date range validation in `validateForm()` method.

### 4. **EventCreationActivity.java - Missing Number Validation (FIXED)**
   - **Issue**: No validation that entrants count is a positive number.
   - **Impact**: Users could enter negative numbers or non-numeric values.
   - **Fix**: Added number format validation and positive number check.

### 5. **SplashActivity.java - Handler Memory Leak (FIXED)**
   - **Issue**: Using `new Handler()` without specifying Looper could cause memory leaks.
   - **Impact**: Potential memory leaks in the splash screen.
   - **Fix**: Changed to `new Handler(getMainLooper())` to use the main thread's Looper.

### 6. **MainActivity.java - Unsafe View Cast (FIXED)**
   - **Issue**: Direct cast from `View` to `Button` without `instanceof` check could cause ClassCastException.
   - **Impact**: App could crash if a non-Button View is passed to `toggle()` method.
   - **Fix**: Added `instanceof` check before casting.

### 7. **ProfileSetupActivity.java - Missing Device ID Validation (FIXED)**
   - **Issue**: No fallback if deviceId is null or empty from intent.
   - **Impact**: Profile creation could fail if deviceId is missing.
   - **Fix**: Added fallback to retrieve ANDROID_ID if not provided in intent.

### 8. **SettingsActivity.java - Missing Null Checks (FIXED)**
   - **Issue**: Calls to `EditText.getText().toString()` without null checks.
   - **Impact**: Potential NullPointerException when saving profile.
   - **Fix**: Added null checks before calling `.toString()`.

### 9. **EventDetailsActivity.java - Missing Null Check (FIXED)**
   - **Issue**: `displayEventData()` could be called with null `currentEvent`.
   - **Impact**: App could crash when displaying event details.
   - **Fix**: Added null check and null-safe string handling.

## Bugs Found But Not Fixed (Requires Context/Design Decision)

### 1. **FirestoreEntrantsRepository.java - Empty Implementation**
   - **Location**: `promoteNextFromWaitlist()` method (line 123-127)
   - **Issue**: Method immediately calls `onSuccess` without any actual implementation.
   - **Impact**: Waitlist promotion feature doesn't work.
   - **Note**: May be intentional stub - requires implementation based on business logic.

### 2. **OrganizerHomeActivity.java - Hardcoded Event ID**
   - **Location**: Line 26
   - **Issue**: Uses hardcoded `"event123"` instead of dynamic event ID.
   - **Impact**: Navigation to event management screens won't work with real events.
   - **Note**: Requires integration with actual event selection mechanism.

### 3. **EventPosterActivity.java - Hardcoded Event ID**
   - **Location**: Line 34
   - **Issue**: Default event ID is `"sampleEvent123"`.
   - **Impact**: May cause issues if intent doesn't provide eventId.
   - **Note**: Has fallback logic but default value should be null or handled differently.

### 4. **Event.java - Duplicate Date Fields**
   - **Location**: Fields `date` and `eventDate` both exist (lines 27, 32)
   - **Issue**: Two fields for event date could cause confusion and data inconsistency.
   - **Impact**: Unclear which field should be used, potential data sync issues.
   - **Note**: Requires design decision on which field to use or if both are needed.

### 5. **EventCreationActivity.java - Hardcoded Organizer ID**
   - **Location**: Line 175
   - **Issue**: Uses hardcoded `"organizer123"` instead of actual logged-in organizer.
   - **Impact**: Events won't be associated with the correct organizer.
   - **Note**: TODO comment indicates this needs to be replaced.

### 6. **EventCreationActivity.java - Hardcoded Poster URL**
   - **Location**: Line 176
   - **Issue**: Uses placeholder URL `"https://example.com/poster.png"`.
   - **Impact**: Events will have invalid poster URLs.
   - **Note**: TODO comment indicates this needs actual poster upload integration.

### 7. **SettingsActivity.java - Inefficient Query Pattern**
   - **Location**: `loadJoinedEvents()` method (lines 147-177)
   - **Issue**: Makes N+1 queries (one per event) to check waiting list membership.
   - **Impact**: Poor performance with many events, unnecessary Firestore reads.
   - **Note**: Should use array-contains query or restructure data model.

### 8. **EventDetailsActivity.java - Potential Race Condition**
   - **Location**: `joinEventWaitingList()` method
   - **Issue**: Button is disabled but no check if user is already in waiting list before update.
   - **Impact**: Could add duplicate entries if user clicks multiple times quickly.
   - **Note**: Firestore arrayUnion should prevent duplicates, but UI state could be inconsistent.

## Recommendations

1. **Implement proper waitlist promotion logic** in `FirestoreEntrantsRepository.promoteNextFromWaitlist()`
2. **Replace all hardcoded IDs** with dynamic values from Firebase Auth or user selection
3. **Resolve duplicate date fields** in Event model - decide on single source of truth
4. **Optimize SettingsActivity queries** to use more efficient Firestore queries
5. **Add proper error handling** for network failures and edge cases
6. **Consider adding unit tests** for critical business logic
7. **Review and implement proper lifecycle management** for all activities to prevent memory leaks

## Summary

- **Total Bugs Found**: 17
- **Critical Bugs Fixed**: 9
- **Bugs Requiring Design Decisions**: 8
- **No Linter Errors**: ✅ All fixes compile successfully
