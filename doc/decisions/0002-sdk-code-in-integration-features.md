# 0002: SDK code lives in `features/integration/<sdk>/`

Status: Accepted, 2026-10-04

## Context

Firebase code was split between a top-level `integration/firebase/` package (SDK construction, constants, operations, messaging service) and `features/integration/firebase/` (the lab Activity and screen). Readers learning one SDK had to look in two places. The iOS lab had the same split and merged it (iOS decision 0003).

## Decision

All code for one SDK lives in `features/integration/<sdk>/`: the lab Activity and screen, the service boundary (`FirebaseIntegration`), constants, the Koin module (`FirebaseModule`), and Android components such as `GJPLabFirebaseMessagingService`. `shell/AppModule.kt` includes the SDK's Koin module, and `GJPLabApplication` calls its one-time initialization.

## Consequences

- One folder, and one pair of specs, covers everything about an SDK.
- Adding an SDK means a new `features/integration/<sdk>/` folder, its Koin module included from `AppModule`, and any manifest components.
- The app shell depends on this feature folder: startup reads Remote Config for maintenance mode, and the messaging service opens `MainActivity`. Unlike other features, the Firebase folder cannot be deleted on its own. This is acceptable because Firebase is itself a subject of the lab.
