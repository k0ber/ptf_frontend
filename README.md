# Patifiner Frontend 📱

[![Build Status](https://github.com/k0ber/ptf_frontend/actions/workflows/mobile_test.yaml/badge.svg)](https://github.com/k0ber/ptf_frontend/actions/workflows/mobile_test.yaml)
[![Mobile Report](https://img.shields.io/badge/Allure%20Report-mobile-blue)](https://k0ber.github.io/ptf_frontend/mobile/)

Front-end part of social platform "Patifiner" - application for discover real connections based on your interests

Back-end part available here: [ptf_backend](https://github.com/k0ber/ptf_backend)

---

It is kotlin multiplatform compose application build with Gradle 9+

## Stack

### Core & Architecture
- [**Kotlin Multiplatform**](https://kotlinlang.org/docs/multiplatform.html)
- [**Compose Multiplatform**](https://www.jetbrains.com/lp/compose-multiplatform/)
- [**Navigation 3**](https://kotlinlang.org/docs/multiplatform/compose-navigation-3.html)
- [**MVIKotlin**](https://github.com/arkivanov/MVIKotlin)
- [**Koin (DI)**](https://insert-koin.io/)
- [**Ktor**](https://ktor.io/)
- [**KotlinX Serialization**](https://github.com/Kotlin/kotlinx.serialization)
- [**KotlinX Datetime**](https://github.com/Kotlin/kotlinx-datetime)
- [**Kotlin Logging**](https://github.com/oshai/kotlin-logging)
- [**Multiplatform Settings**](https://github.com/russhwolf/multiplatform-settings)

### Web
- [**Wasm**](https://kotlinlang.org/docs/wasm-overview.html)
- [**Docker**](https://www.docker.com/)
- [**Traefik**](https://traefik.io/traefik)

### iOS
- [**XCFramework**](https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks)
- [**UIKit Integration**](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-ios-uikit-interop.html)

### Desktop
- **Swing**

### Testing & Quality
- [**Macrobenchmark**](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview)
- [**Baseline Profiles**](https://developer.android.com/topic/performance/baselineprofiles/overview)
- [**Allure Report**](https://allurereport.org/)
- **UiAutomator**
- **Junit 4**
- **Kotlin test**
- **Mockk**

---

## Project structure

├── `:androidApp` — entry point for android build
├── `:iosApp` — entry point for iOS Xproject build
├── `:benchmark` — baseline generation and performance tests for android
└── `:composeApp` — main module for compose multiplatform
    ├── `androidMain` — android specific staff, contains manifest with required permissions
    ├── `commonMain` — shared code across all targets, project base
    ├── `iosMain` — entry point for iOS app (MainViewController.kt) and platform specific staff
    ├── `jvmMain` — entry point for Desktop, platform specific staff
    ├── `jvmTest` — unit tests module
    └── `wasmJsMain` — entry point for Web app, contains `index.html` and `styles.css`

---

## Gradle tasks

### Web
`./gradlew :composeApp:wasmJsBrowserDevelopmentRun --continuous`
- run web interface locally for debug, supports hot reload

`./gradlew :composeApp:wasmJsBrowserProductionRun`
- run web release build locally

### Android
`./gradlew :androidApp:generateReleaseBaselineProfile`
- requires real android device to be connected
- generate Baseline Profiles `baseline-prof.txt` and `startup-prof.txt` which are used by R8 for release build

`./gradlew :androidApp:assemble`
- also creates compose compiler report in composeApp/build/compose_compiler
- assemble debug apk, also creates compose reports in `composeApp/build/compose_compiler`
- could be useful if you want to check compose stability

`./gradlew :benchmark:connectedBenchmarkBenchmarkAndroidTest`
- android performance tests for tracking frames rate, you should have android device connected and seen via `adb devices`
- test results and traces location is TODO 

### iOS
`./gradlew :composeApp:iosSimulatorArm64Run -Pcompose.ios.simulator.device="iPhone 16e"`
- launch iOS app, you need running xcode and iOS simulator

### Desktop
`./gradlew :composeApp:run`
- launch desktop app

### Unit tests
`./gradlew :composeApp:jvmTest`
- run junit tests
