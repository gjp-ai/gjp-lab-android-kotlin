# 0003: Activity-based navigation with explicit Intents

Status: Accepted, 2026-10-04

## Context

The lab opens one Activity per screen: `SplashActivity` → `MainActivity` (dashboard or maintenance) → `FeatureCatalogActivity` → a feature Activity. The alternative is a single Activity with Compose navigation or an adaptive list-detail layout, which would show the catalogue and a feature side by side on tablets. That would need new dependencies (Navigation Compose or Material 3 adaptive), would move state out of Activities, and would need a new home for runtime-permission launchers, Koin injection, and the call-blocking overlay, which today all live in Activities.

## Decision

Keep Activity navigation. Each screen is a non-exported Activity declared in `AndroidManifest.xml` and opened with an explicit `Intent`. Activities own lifecycle, injected dependencies, runtime-permission launchers, and screen state; composables receive state and callbacks. `FeatureCatalogActivity.openFeature` is the one place that maps a `FeatureRoute` to an Activity.

## Consequences

- Each Activity hosts `CallBlockingHost` and starts and stops call monitoring in `onResume`/`onPause`, because there is no single root to host the overlay.
- Phones and tablets use the same flow; tablets get wider layouts (adaptive dashboard grid, width-limited catalogue) but not side-by-side panes.
- Intent extras are the contract between screens: keep them small (an ID or a few values) and validate them on arrival.
- Do not add a navigation framework or single-Activity migration as incidental refactoring. Revisit this decision if side-by-side tablet navigation, deep links, or shared cross-screen state become requirements; a migration needs a new decision record and migration tests.

Related: the iOS lab uses a single `NavigationSplitView` instead; the two labs share folder structure, not navigation mechanism.
