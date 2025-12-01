# Testing report - LuckySpot Event App

## Commands
```bash
# Full unit suite (requires Android SDK path configured)
./gradlew test

# Example targeted run
./gradlew test --tests "com.example.event_app.activities.ActivityLifecycleTest"

# Debug logging
./gradlew test --info
```

## Current Status
- **Execution**: `./gradlew test` currently fails in this environment because the Android SDK location is not configured; configure `ANDROID_HOME`/`sdk.dir` locally or in CI before running.
- **Scope**: All tests rely on dummy data, mocked Firebase/Auth/Firestore, or Robolectric shadows—no live services or UI automation.

## Coverage Summary (unit tests)
- **ActivityLifecycleTest** – 21 lifecycle/intent wiring checks across every activity (entrant, organizer, admin) using mocked Firebase and dummy intent extras.
- **ActivityIntentFlowTest** – 6 intent payload validations for navigation targets.
- **NavigatorTest** – Verifies event navigation intent is launched with the correct extra.
- **QRServiceTest** – 3 QR-code validation paths (valid, invalid, blank) exercising navigation decisions.
- **UserServiceTest** – 6 service behaviors including validation failures, persistence interactions, and favorite/notification toggles with dummy users.
- **UserValidatorTest** – Valid and invalid user data permutations plus required-field aggregation.
- **EventModelTest** – 6 event-derived calculations (spots remaining, cancellation rate, capacity full, replacement pool, past-event logic).

**Total: 45 test cases across 7 test classes.**

## Notes
- Tests emphasize data flow and backend/model validation rather than UI rendering.
- Each activity class has a dedicated lifecycle validation to confirm required extras and bindings are initialized from dummy inputs.
- Extend coverage with Jacoco once the Android SDK is available to generate reliable reports.
