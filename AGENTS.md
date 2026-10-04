# gjp-lab-android-kotlin agent guide

This repository is a small Android learning lab and the practice host for a portable Android skill library. User instructions define the task; this file defines local project constraints; each skill supplies a reusable workflow for one engineering concern.

## Skill routing

Activate the smallest set of skills that fully covers the request:

| Concern | Skill |
| --- | --- |
| Compose, Material 3, accessibility, adaptive UI, previews, or UI tests | [`android-compose-material3`](.agents/skills/android-compose-material3/SKILL.md) |
| Feature boundaries, UI state, state holders, lifecycle, navigation, or dependency injection | [`android-feature-architecture`](.agents/skills/android-feature-architecture/SKILL.md) |
| Repositories, networking, persistence, coroutines, Flow, caching, or synchronization | [`android-data-concurrency`](.agents/skills/android-data-concurrency/SKILL.md) |
| Permissions, manifests, intents, components, background execution, privacy, or platform security | [`android-platform-privacy`](.agents/skills/android-platform-privacy/SKILL.md) |
| Diagnosis, tests, Gradle, dependencies, CI, performance, or release verification | [`android-quality-build`](.agents/skills/android-quality-build/SKILL.md) |

- Follow each selected skill's mode routing and read only references relevant to the task.
- Project instructions in this file take precedence over portable defaults when they differ.
- For cross-cutting work, coordinate the selected skills around one user outcome; do not duplicate layers, tests, or verification.
- When practising, use a real, contained GJPLab change. Modify a portable skill only when observed evidence reveals a lesson that applies across Android projects.

## Project profile

- Package `com.ganjianping.lab.ak`; `minSdk 30`; `compileSdk 37`; Java 11 source compatibility; JDK 21 Gradle toolchain.
- Compose Material 3 UI. `GJPLabTheme` in `common/theme/` provides dark theme and Android 12+ dynamic color.
- Activity navigation: `SplashActivity` → `MainActivity` → feature activities. Use explicit `Intent`s and declare activities in `AndroidManifest.xml`.
- App shell (application, root Activity, startup, navigation) under `shell/`; feature code in one flat folder per feature under `features/<category>/<feature>/`; reusable app code under `common/`; Firebase integration under `features/integration/firebase/`.

## GJPLab adapter

- Use Koin for application-wide dependencies. Register repositories in `shell/AppModule.kt` and Firebase services in `features/integration/firebase/FirebaseModule.kt`; inject them into activities rather than constructing them in composables.
- Preserve the current activity/screen state and callback pattern unless the task explicitly introduces another state holder. Do not add a ViewModel or navigation framework as incidental UI refactoring.
- Keep platform and network work out of composables. `HttpURLConnectionRepository.execute` runs on `Dispatchers.IO`, has 15-second timeouts, preserves error bodies, formats JSON, and always disconnects.
- Route Firebase SDK calls through `FirebaseIntegration`; put stable event names, keys, trace names, and Remote Config keys in `FirebaseConstants`.
- Keep runtime permission handling in the activity. Update the manifest only when a feature needs a permission, service, or activity declaration.
- Do not commit or expose server credentials, OAuth secrets, or App Check debug tokens. Treat existing FCM-token logging as lab/demo behavior, not a production pattern to copy.

## Verification

- Compile with `./gradlew assembleDebug`; run local tests with `./gradlew test`.
- Run `./gradlew connectedDebugAndroidTest` when a connected emulator/device is available and the change needs instrumented coverage.
- Keep automated tests deterministic; do not depend on live HTTP endpoints or Firebase responses.
- Report checks run and prerequisites that prevented any relevant verification.
