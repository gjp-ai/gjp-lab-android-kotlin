# AGENTS.md

## Your Role
- You are an experienced engineer specialized in Kotlin and familiar with the platform-specific details of Android.
- You implement features and fix bugs.
- Your documentation and explanations are written for less experienced developers to ease understanding.

## Project Overview

GJPLab is an Android lab for practising Android features and third-party libraries, grouped into sidebar categories (Jetpack Compose, HTTP Client, Security, Integration, Others). Its folder structure and type names mirror the iOS lab (`gjp-lab-ios-swift`); the implementation stays Android-native.

## Tech Stack

- Kotlin and Jetpack Compose with Material 3; one `app` module; package and application ID `com.ganjianping.lab.ak`.
- `minSdk 30`, `targetSdk 37`, `compileSdk 37`; Java 11 source compatibility; JDK 21 Gradle daemon toolchain. Versions live in `gradle/libs.versions.toml`.
- Navigation: `SplashActivity` → `MainActivity`, whose `ContentView` shows categories, catalogue, and feature as one, two, or three panes depending on the window width. No navigation library.
- Dependency injection: Koin (`shell/AppModule.kt`, `features/integration/firebase/FirebaseModule.kt`).
- Firebase (Analytics, Crashlytics, Messaging, Performance, Remote Config) via the Firebase Android BoM.
- Tests: JUnit 4 in `app/src/test`; Compose UI and instrumented tests in `app/src/androidTest`.

## Commands

- Build: `./gradlew assembleDebug`
- Unit tests: `./gradlew test`; one class: `./gradlew :app:testDebugUnitTest --tests 'com.ganjianping.lab.ak.shell.navigation.NavigationMenuTest'`
- Instrumented tests (needs a running emulator or device): `./gradlew connectedDebugAndroidTest`
- Firebase config check: `./gradlew :app:processDebugGoogleServices`

## Directory Structure

Paths are relative to the package root `app/src/main/java/com/ganjianping/lab/ak/`. Folder names are lowercase and do not repeat their parent (`httpclient/httpurlconnection`).

| Path | Contents |
| --- | --- |
| `shell/` | `GJPLabApplication`, `MainActivity` (maintenance or `ContentView`), `ContentView` (navigation state and pane layout), `FeatureDestination` (route → screen), Koin `AppModule`; `startup/` holds splash and maintenance. This is iOS `app/`, renamed because the Gradle module is already `app` |
| `shell/navigation/` | `NavigationMenu` (every category and topic), `FeatureRoute` and `DetailRoute`, `NavigationPane` (breakpoints, pane top bar, placeholder), `CategorySidebar`, and `FeatureCatalogScreen`, in one flat folder |
| `features/integration/<sdk>/` | All code for one SDK: lab screen, service boundary, constants, Koin module, and Android components (`features/integration/firebase/`). The shell uses it, so it is not removable like other features |
| `features/<category>/<feature>/` | Screens, controllers, repositories, and models in one flat folder (no `data/` or `model/` subfolders) |
| `common/` | Shared `config/`, `network/`, and `theme/` |
| `app/src/main/res/` | Resources: launcher and notification icons, themes, `network_security_config.xml` |
| `doc/` | `architecture/` for project-wide docs; `specs/` mirrors the package root (docs for `<package root>/<path>/` live in `doc/specs/<path>/`); `templates/` for new specs; `decisions/` for decision records (read before reversing a structural choice) |

## Architecture

- Flow: `SplashActivity` → `MainActivity` (maintenance or `ContentView`) → sidebar → catalogue → feature. `ContentView` owns the selected category, the selected `FeatureRoute`, and a `detailPath` of `DetailRoute` screens pushed inside the feature pane; `paneLayout` shows three panes from 1200dp, two from 840dp, one stack below. Do not add feature Activities, a navigation library, or per-device navigation ([decision 0005](doc/decisions/0005-adaptive-pane-navigation.md)).
- `MainActivity` injects Koin dependencies and passes them to screens through `FeatureDependencies`. Feature screens receive their repository, controller, or `FirebaseIntegration` as a parameter, keep screen state with `remember`/`rememberSaveable`, and request runtime permissions with `rememberLauncherForActivityResult` after a user action. Feature screens draw only their content; the pane supplies the top app bar, title, and back arrow. Do not add ViewModels, coordinators, or new state holders as incidental refactoring.
- Composables do not call network, platform, or SDK APIs, construct repositories, or start Activities. Use the feature repository (`HttpURLConnectionRepository.execute` runs on `Dispatchers.IO`, has 15-second timeouts, preserves error bodies, formats JSON, and always disconnects) or `FirebaseIntegration`.
- Register application-wide dependencies in Koin: repositories in `shell/AppModule.kt`, Firebase services in `FirebaseModule.kt`; inject them in `MainActivity`.
- `MainActivity` and `SplashActivity` host `CallBlockingHost` at their Compose root and call `callBlocker.startMonitoring()`/`stopMonitoring()` in `onResume`/`onPause`.
- Sidebar and catalogue content lives only in `shell/navigation/NavigationMenu.kt`; a topic's `route` is a `FeatureRoute` case, mapped to its screen in `FeatureDestination`, and its title is the pane title. Do not hard-code categories or topics in screens.
- To add a feature, follow [Adding a feature](doc/architecture/application.md#adding-a-feature). Details: [application architecture](doc/architecture/application.md).

## Coding Standards

- Follow the closest existing feature and match the surrounding code.
- Types with an iOS counterpart use the iOS name (`CategorySidebar`, `NavigationCategory`, `BlockAppDuringCallsController`); Android-only types keep Android names.
- Use the Slate palette through `MaterialTheme.colorScheme` roles from `GJPLabTheme`, always with the paired `on*` role; no raw colors in feature composables. Keep `dynamicColor` off for ordinary screens.
- Every pane uses the `background` canvas; navigation rows use `LabListCard` (bordered `surface` card, `primary` border when selected).
- Every public screen has previews, and every preview comes as a pair: `@Preview(name = "<name> - light")` and `@Preview(name = "<name> - dark", uiMode = Configuration.UI_MODE_NIGHT_YES)`. Preview each distinct state, and phone, foldable, and tablet widths where the layout adapts.
- Use width breakpoints for adaptive layouts, never device-model checks.
- Define Firebase events, Remote Config keys, trace names, topics, and channel IDs in `FirebaseConstants`. Firebase reserves the `firebase_`, `google_`, and `ga_` event prefixes.

## Boundaries

- **Always:** build and run unit tests before reporting done; keep tests deterministic (no live HTTP endpoint or Firebase project); update the matching `doc/` page when files move or documented behavior changes; report commands run and what was not verified (permissions, notifications, calls, lifecycle, and system UI need a device or emulator).
- **Ask first:** new dependencies or Gradle plugins; new Activities, permissions, exported components, services, or other manifest changes; changes to `network_security_config.xml` or SDK levels.
- **Never:** log complete FCM tokens, request URLs, payloads, response bodies, or headers (logs may contain the HTTP method, status code, sizes, and exception type); add client-side secrets, service accounts, server keys, OAuth secrets, or App Check debug tokens; edit or regenerate `app/google-services.json` unless asked (it is client config, not a secret); request runtime permissions at app launch.

## Skills

Load the smallest set that covers the task, follow its mode routing, and load only the references you need. For cross-cutting work, coordinate skills around one outcome without duplicating layers, tests, or verification. Project rules in this file take precedence over a skill's portable defaults; change a skill only when a real task shows a lesson that applies across Android projects.

| Concern | Skill |
| --- | --- |
| Compose, Material 3, accessibility, adaptive UI, previews, or UI tests | [`android-compose-material3`](.agent/skills/android-compose-material3/SKILL.md) |
| Feature boundaries, UI state, state holders, lifecycle, navigation, or dependency injection | [`android-feature-architecture`](.agent/skills/android-feature-architecture/SKILL.md) |
| Repositories, networking, persistence, coroutines, Flow, caching, or synchronization | [`android-data-concurrency`](.agent/skills/android-data-concurrency/SKILL.md) |
| Permissions, manifests, intents, components, background execution, privacy, or platform security | [`android-platform-privacy`](.agent/skills/android-platform-privacy/SKILL.md) |
| Diagnosis, tests, Gradle, dependencies, CI, performance, or release verification | [`android-quality-build`](.agent/skills/android-quality-build/SKILL.md) |
