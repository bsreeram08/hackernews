# Screenshot Testing Guide

## Overview

This project uses **Roborazzi** for automated screenshot testing in CI. Screenshots are automatically generated on every PR and made available for reviewers to download.

## How It Works

### Automatic Screenshot Generation

When you create or update a PR with Android changes:

1. **GitHub Actions triggers** the screenshot workflow
2. **Roborazzi tests run** - generates screenshots of UI components
3. **Screenshots are uploaded** as GitHub artifacts
4. **Bot comments on PR** with download instructions

### Viewing Screenshots

1. Go to your PR on GitHub
2. Look for the bot comment "🤖 Screenshot Test Results"
3. Click the workflow run link
4. Scroll to "Artifacts" section
5. Download `roborazzi-screenshots-{PR-number}`
6. Extract the ZIP and view PNG files

## Writing Screenshot Tests

### Example Test

```kotlin
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
class MyComponentTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun screenshotTest() {
    composeRule.setContent {
      HackerNewsTheme {
        MyComponent(
          // Provide test data
          title = "Test Title",
          onClick = {}
        )
      }
    }

    composeRule
      .onRoot()
      .captureRoboImage()
  }
}
```

### Best Practices

1. **Test Different States**
   - Loading state
   - Success state with data
   - Error state
   - Empty state

2. **Use Descriptive Test Names**
   ```kotlin
   @Test
   fun loginScreen_emptyState() { ... }
   
   @Test
   fun loginScreen_errorState() { ... }
   ```

3. **Provide Realistic Data**
   - Use actual-looking content
   - Test with various text lengths
   - Include edge cases

4. **Test Both Light and Dark Themes**
   ```kotlin
   @Test
   fun myComponent_lightTheme() {
     composeRule.setContent {
       HackerNewsTheme(darkTheme = false) {
         MyComponent()
       }
     }
     composeRule.onRoot().captureRoboImage()
   }
   
   @Test
   fun myComponent_darkTheme() {
     composeRule.setContent {
       HackerNewsTheme(darkTheme = true) {
         MyComponent()
       }
     }
     composeRule.onRoot().captureRoboImage()
   }
   ```

## Running Tests Locally

### Generate Screenshots

```bash
cd android
./gradlew :app:recordRoborazziDebug
```

Screenshots will be saved to:
```
android/app/build/outputs/roborazzi/
```

### Verify Screenshots

```bash
./gradlew :app:verifyRoborazziDebug
```

This compares current screenshots with previously recorded ones.

### Clear Screenshots

```bash
./gradlew :app:clearRoborazziDebug
```

## CI Workflow

### Trigger Conditions

The screenshot workflow runs when:
- A PR is created targeting `main`
- A PR is updated (new commits)
- Changes are made in `android/**`

### Workflow Steps

1. **Checkout code** - PR head
2. **Setup JDK 17** - with Gradle caching
3. **Run tests** - `recordRoborazziDebug`
4. **Upload artifacts** - all generated PNGs
5. **Comment on PR** - with download info

### Permissions

The workflow requires:
- `contents: read` - to checkout code
- `pull-requests: write` - to comment on PRs

## Troubleshooting

### No Screenshots Generated

**Possible causes:**
- No screenshot tests in the project
- Tests failed to compile
- Tests crashed during execution

**Solution:**
Check the workflow logs in GitHub Actions for error messages.

### Screenshots Look Wrong

**Possible causes:**
- Test data issues
- Theme not applied correctly
- Component state not set up properly

**Solution:**
Run tests locally with `recordRoborazziDebug` to debug.

### Bot Doesn't Comment

**Possible causes:**
- No screenshots were generated
- Permissions issue

**Solution:**
Check workflow logs and ensure permissions are set correctly.

## Adding More Screenshot Tests

### 1. Create Test File

Create a new file in `app/src/test/kotlin/`:

```kotlin
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.emergetools.hackernews.ui.theme.HackerNewsTheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
class NewFeatureScreenshotTest {
  
  @get:Rule
  val composeRule = createComposeRule()
  
  @Test
  fun newFeature_defaultState() {
    composeRule.setContent {
      HackerNewsTheme {
        NewFeatureScreen()
      }
    }
    composeRule.onRoot().captureRoboImage()
  }
}
```

### 2. Run Locally

```bash
./gradlew :app:recordRoborazziDebug
```

### 3. Commit

```bash
git add app/src/test/kotlin/NewFeatureScreenshotTest.kt
git commit -m "Add screenshot test for new feature"
```

### 4. Create PR

The screenshot workflow will run automatically!

## Existing Screenshot Tests

Current screenshot tests in the project:

1. **StoryRowComposeTest**
   - Tests story row component
   - Shows loading state
   - File: `StoryRowComposeTest.kt`

2. **BookmarksScreenComposeTest**
   - Tests bookmarks screen
   - Shows multiple bookmarked items
   - File: `BookmarksScreenComposeTest.kt`

## Benefits

✅ **Visual Regression Testing** - Catch unintended UI changes  
✅ **Reviewer Confidence** - See exactly what changed  
✅ **Documentation** - Screenshots serve as visual docs  
✅ **Automated** - No manual testing needed  
✅ **Fast Feedback** - Results in minutes  

## Resources

- [Roborazzi Documentation](https://github.com/takahirom/roborazzi)
- [Compose Testing Guide](https://developer.android.com/jetpack/compose/testing)
- [Robolectric Documentation](http://robolectric.org/)

---

**Happy Testing! 📸**
