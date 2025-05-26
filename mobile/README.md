# QR Seed Mobile App

This Android app connects to a Node.js backend to generate, display, and validate QR code seeds in real time.

## Overview

- Requests a unique seed from the backend (`/seed`) and displays it as a QR code.
- Scans QR codes to extract and validate seeds using the `/validate` endpoint.
- Quick navigation via a floating action button.
- Modular architecture with dependency injection and clear separation of layers.
- Built with Jetpack Compose for modern, declarative UI.
- Supports dark mode for a better user experience.

## How It Works

- Tap the floating action button to generate or scan a QR code.
- When generating, the app fetches a new seed from the backend and displays it.
- When scanning, the app reads a QR code and checks its validity with the backend.
- The app provides feedback if the seed is valid, expired, or not found.

## Prerequisites

- Android Studio Meerkat or higher
- Android SDK 26 or higher
- JAVA 17
- Kotlin 2.0.21+

## Setup & Usage

1. Clone the repository
2. Open the project in Android Studio
3. Build and run on a device or emulator with camera support

## Running Unit Tests

You can run the unit tests from Android Studio:

- Right-click the `test` directory inside any module (e.g., `domain/src/test`, `data/src/test`, `presentation/src/test`) and select **Run Tests**.
- Or, from the terminal, run:

```sh
./gradlew test
```

## Technical Details

- **Architecture:** MVVM with Clean Architecture (modularized by layers for a balance between cost and benefit; feature-based modularization could be considered)
- **UI:** Jetpack Compose
- **Dependency Injection:** Dagger Hilt
- **Navigation:** Jetpack Navigation Component
- **Networking:** Retrofit
- **Serialization:** Kotlinx Serialization
- **QR Code:** ZXing
- **Testing:** JUnit, MockK
- **Dark Mode:** Supported

## Configuration

- API `BASE_URL` is set `data` module `build.gradle` file.

## TODO / Improvements

- General UX improvements for better error and loading state handling
- Add UI, E2E, or other types of tests
- Standardize component dimensions
- Add offline support with a `LocalDataSource`
- Set up CI/CD pipelines
- Add localization support
- Integrate a linter such as Ktlint or Detekt
- Measure code coverage and increase test coverage
