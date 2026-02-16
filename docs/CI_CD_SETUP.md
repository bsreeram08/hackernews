# CI/CD Setup for Android Releases

## Overview
Automated CI/CD pipelines build Android releases when version tags are pushed.

## Quick Start

### Creating a Release
```bash
git tag -a android-1.0.7 -m "Release 1.0.7"
git push origin android-1.0.7
```

## GitHub Actions

### File: `.github/workflows/android_tagged_release.yml`

### Triggers
- `android-*` tags
- `v*` tags

### Builds
1. Debug APK
2. Release AAB
3. Universal APK

### Required Secrets
- `ANDROID_RELEASE_KEYSTORE_BASE64`
- `ANDROID_RELEASE_KEY_ALIAS`
- `ANDROID_RELEASE_KEY_PASSWORD`  
- `ANDROID_RELEASE_STORE_PASSWORD`

## GitLab CI

### File: `.gitlab-ci.yml`

Runs the same builds in GitLab environment.

## Documentation
See full documentation in BUILD_ENVIRONMENT_STATUS.md
