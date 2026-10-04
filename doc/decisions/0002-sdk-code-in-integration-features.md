# 0002: SDK code lives in `features/integration/<sdk>/`

Status: Accepted, 2026-10-04

## Context

Firebase code was split between a top-level `integration/firebase/` package (Koin module, `FirebaseIntegration`, constants, and the messaging service) and `features/integration/firebase/` (the lab Activity and screen). Readers learning one SDK had to look in two places, and the docs described both from one location.

## Decision

All code for one SDK lives in `features/integration/<sdk>/`: the lab Activity and screen, the service boundary (`FirebaseIntegration`), constants, the Koin module (`FirebaseModule`), and Android components such as `GJPLabFirebaseMessagingService`. `shell/AppModule.kt` includes the SDK's Koin module, and `GJPLabApplication` calls its one-time initialization. The top-level `integration/` package is removed.

## Consequences

- One folder, and one pair of specs, covers everything about an SDK.
- Adding an SDK means a new `features/integration/<sdk>/` folder, its Koin module included from `AppModule`, and any manifest components.
- The app shell depends on this feature folder: startup reads Remote Config for maintenance mode, and the messaging service opens `MainActivity`. Unlike other features, the Firebase folder cannot be deleted on its own. This is acceptable because Firebase is itself a subject of the lab.

Related: the iOS lab made the same choice (iOS decision 0003).
