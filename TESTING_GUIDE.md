# Testing Guide - LuckySpot Event App

## Quick Start

### Running Tests Locally

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.example.event_app.domain.ResultTest"

# Run tests with verbose output
./gradlew test --info

# Run tests and generate coverage report
./gradlew test jacocoTestReport
```

### Running Tests in CI

Tests automatically run on:
- **Push to `main` branch**
- **Pull requests to `main` branch**

## Test Structure

```
app/src/
├── test/                    # Unit tests (run on JVM)
│   └── java/com/example/event_app/
│       ├── domain/
│       │   ├── ResultTest.java
│       │   └── EntrantsFilterTest.java
│       ├── models/
│       │   └── EventTest.java
│       ├── utils/
│       │   └── UserRoleTest.java
│       └── ExampleUnitTest.java
│
└── androidTest/             # Instrumented tests (run on Android device/emulator)
    └── java/com/example/event_app/
        └── ExampleInstrumentedTest.java
```

## Test Coverage

### ✅ Current Coverage

| Component | Test File | Test Methods | Status |
|-----------|-----------|--------------|--------|
| Result | ResultTest.java | 5 | ✅ Complete |
| EntrantsFilter | EntrantsFilterTest.java | 12 | ✅ Complete |
| Event | EventTest.java | 13 | ✅ Complete |
| UserRole | UserRoleTest.java | 5 | ✅ Complete |
| Basic Tests | ExampleUnitTest.java | 2 | ✅ Complete |

**Total: 37 test methods across 5 test classes**

### Test Details

#### ResultTest.java
- Tests success cases (`Result.ok()`)
- Tests error cases with Throwable
- Tests error cases with String message
- Tests null data handling
- Tests different data types

#### EntrantsFilterTest.java
- Tests all enum values
- Tests case-insensitive matching
- Tests whitespace handling
- Tests null/invalid input handling

#### EventTest.java
- Tests cancellation rate calculations (0%, 50%, 100%, partial)
- Tests high cancellation rate detection (>30% threshold)
- Tests edge cases (zero selected, 100% cancellation)
- Tests constructors

#### UserRoleTest.java
- Tests all role constants
- Tests non-null and non-empty validation

## CI/CD Configuration

### Workflow: `.github/workflows/android.yml`

**Triggers:**
- Push to `main`
- Pull requests to `main`

**Steps:**
1. Checkout code
2. Set up JDK 17
3. Set up Android SDK (API 34)
4. Grant execute permission to gradlew
5. **Run unit tests** (`./gradlew test`)
6. Build project (`./gradlew build`)
7. Upload test results as artifacts

### Removed Conflicts

- ✅ **Removed `.github/workflows/ant.yml`** - Was trying to build with Ant (not applicable for Android/Gradle project)

## Ensuring CI Runs Without Conflicts

### ✅ Current Status

1. **Single CI Workflow**: Only one workflow file (android.yml) - no conflicts
2. **Proper Android Setup**: Android SDK configured correctly
3. **Test Execution**: Tests explicitly run before build
4. **Error Handling**: Proper error handling and artifact upload

### Best Practices Followed

1. ✅ **No conflicting workflows** - Removed Ant workflow
2. ✅ **Explicit test execution** - Tests run in separate step
3. ✅ **Test results uploaded** - Artifacts available for review
4. ✅ **Proper error handling** - `continue-on-error: false` ensures failures are caught
5. ✅ **Android SDK setup** - Required for Android builds

## Adding New Tests

### Unit Test Template

```java
package com.example.event_app.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class YourClassTest {
    @Test
    public void testMethodName_Scenario_ExpectedResult() {
        // Arrange
        YourClass instance = new YourClass();
        
        // Act
        Result result = instance.method();
        
        // Assert
        assertEquals("Expected value", expected, result);
    }
}
```

### Test Naming Convention

- Test method names: `testMethodName_Scenario_ExpectedResult`
- Example: `testGetCancellationRate_ZeroSelected_ReturnsZero`

## Troubleshooting

### Tests Not Running in CI

1. **Check Android SDK setup** - Ensure API level matches `compileSdk` in `build.gradle.kts`
2. **Check JDK version** - Must match `sourceCompatibility` (currently Java 17)
3. **Check workflow file** - Ensure `.github/workflows/android.yml` exists and is valid YAML

### Tests Failing Locally

1. **Android SDK not found**: Set `ANDROID_HOME` environment variable
2. **Gradle daemon issues**: Run with `--no-daemon` flag
3. **Dependencies**: Run `./gradlew --refresh-dependencies`

### Common Issues

- **"SDK location not found"**: Set up Android SDK or use CI
- **"Test class not found"**: Ensure test files are in `src/test/java/` directory
- **"Method not found"**: Check that test methods are annotated with `@Test`

## Next Steps

### Recommended Additions

1. **Mockito** for mocking dependencies
   ```kotlin
   testImplementation("org.mockito:mockito-core:5.1.1")
   ```

2. **Robolectric** for Android unit tests
   ```kotlin
   testImplementation("org.robolectric:robolectric:4.11.1")
   ```

3. **Code Coverage** with JaCoCo
   ```kotlin
   plugins {
       id("jacoco")
   }
   ```

4. **More Test Coverage** for:
   - QRService validation logic
   - Navigator intent creation
   - Repository query building
   - Activity form validation

## Summary

- ✅ **5 test files** with comprehensive coverage
- ✅ **37 test methods** covering critical business logic
- ✅ **CI configured** to run tests automatically
- ✅ **No conflicts** - Single workflow, no Ant conflicts
- ✅ **Test results** uploaded as artifacts
- ⚠️ **More coverage needed** for service and repository classes
