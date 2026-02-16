# CI/CD Setup for Android

## Overview

This repository includes automated CI/CD pipelines for Android builds, testing, and releases.

## Workflows

### 1. 📸 Screenshot Testing (PR Workflow)

**File**: `.github/workflows/android_screenshot_testing.yml`

**Triggers**: 
- Pull requests to `main` branch
- Changes in `android/**` paths

**Purpose**: Automated UI screenshot generation for visual review

**What it does**:
1. Runs Roborazzi screenshot tests
2. Uploads screenshots as GitHub artifacts (14-day retention)
3. Posts PR comment with screenshot info and download instructions

**Benefits**:
- ✅ Reviewers see visual changes before merging
- ✅ Automated visual regression testing
- ✅ No manual testing required

**See**: [SCREENSHOT_TESTING.md](SCREENSHOT_TESTING.md) for complete guide

---

### 2. 🚀 Tagged Release Build

**File**: `.github/workflows/android_tagged_release.yml`

**Triggers**: Tags matching `android-*` or `v*`

**Builds**: Debug APK, Release AAB, Universal APK

**Usage**:
```bash
git tag -a android-1.0.7 -m "Release 1.0.7"
git push origin android-1.0.7
```

---

## Removed Workflows

The following Emerge Tools workflows have been **removed**:
- ❌ `android_emerge_snapshots.yml` - Replaced by screenshot testing
- ❌ `android_emerge_upload.yml` - Emerge plugin removed

**Reason**: Emerge removed for security (see REPOSITORY_SECURITY_AUDIT.md)
