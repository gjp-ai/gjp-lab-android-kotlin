# 0003: Navigation stays Activity-based with explicit Intents

Status: Accepted, 2026-10-04

## Context

The iOS lab uses one root view with a `NavigationSplitView` driven by selection state: categories, catalogue, and feature side by side on iPad, collapsed into one stack on iPhone. Android could match it with a single Activity and an adaptive list-detail pane, but that would need new dependencies (Material 3 adaptive or Navigation), a rewrite of every feature's state ownership, and a new way to host permissions and the call-blocking overlay.

## Decision

Android keeps its own navigation: `SplashActivity` → `MainActivity` (dashboard or maintenance) → `FeatureCatalogActivity` → one Activity per feature, opened with explicit `Intent`s. Activities own lifecycle, injected dependencies, runtime-permission launchers, and screen state; composables receive state and callbacks. The structure and names follow the iOS lab (decision 0001); the navigation mechanism does not.

## Consequences

- Each Activity hosts `CallBlockingHost` and starts and stops call monitoring in `onResume`/`onPause`, because there is no single root to host the overlay.
- Phones and tablets use the same Activity flow; tablets get wider layouts (adaptive dashboard grid, width-limited catalogue) but not side-by-side panes.
- Intent extras are the contract between screens: keep them small (an ID or a few values) and validate them on arrival.
- Revisit this decision if side-by-side tablet navigation, deep links, or shared cross-screen state become requirements. A migration would add a decision record and migration tests.
