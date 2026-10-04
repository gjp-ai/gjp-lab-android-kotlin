# 0005: One navigation Activity with adaptive panes

Status: Accepted, 2026-10-04. Supersedes [0003](0003-activity-navigation.md).

## Context

With one Activity per screen, a tablet showed one screen at a time: the dashboard, then a full-screen catalogue, then a full-screen feature. Most of a wide window was empty, and moving between topics meant going back and forth through Activities. Every Activity also repeated the same setup: theme, `CallBlockingHost`, and call monitoring in `onResume`/`onPause`. The response screen received the HTTP response through Intent extras, which flattened headers to a string and could exceed the Binder transaction limit.

## Decision

- `MainActivity` is the only navigation Activity. It shows maintenance, or `ContentView`, which owns the navigation state: the selected category id, the selected `FeatureRoute`, and a `detailPath` of `DetailRoute` screens pushed on top of a feature.
- `ContentView` picks a layout from the window width (`paneLayout`), using the Material breakpoints: below 840dp one stack (categories → catalogue → feature, with Back between levels); from 840dp two panes (categories or catalogue, then feature); from 1200dp three panes (categories, catalogue, feature).
- Selection drives every layout, so a rotation or window resize keeps the same category and topic and only changes how many panes are visible. Changing the category clears the topic; changing the topic clears `detailPath`.
- Feature screens are composables, mapped from `FeatureRoute` in `FeatureDestination`. `MainActivity` injects the Koin dependencies and passes them down in `FeatureDependencies`. Screens that need runtime permissions use `rememberLauncherForActivityResult`.
- The panes are built from plain Compose layouts and `BackHandler`, with no navigation or adaptive-layout library, so the behavior is easy to read and needs no new dependency.
- `SplashActivity` stays the launcher; startup is separate from navigation.

## Consequences

- Tablets and foldables use their width: on a large window, categories, catalogue, and feature are visible together, and the selected row in each pane has a `primary` border.
- `CallBlockingHost` and call monitoring live in `MainActivity` (and `SplashActivity`) only.
- New features add a `FeatureRoute` case, a branch in `FeatureDestination`, and the topic in `NavigationMenu`; they do not add an Activity or manifest entry.
- Category and topic survive configuration changes (`rememberSaveable`); `detailPath` does not, so a pushed response closes on rotation or process death.
- A pushed screen is drawn over its feature rather than replacing it, so the feature keeps its state (for example the HTTP form) while the response is open.
- There are no pane animations or predictive-back previews; add them, or adopt a library such as Material 3 adaptive, only with a new decision.
- Decision [0004](0004-navigation-menu-in-kotlin.md) still applies to the menu itself; the route-to-screen mapping moves from `FeatureCatalogActivity.openFeature` to `FeatureDestination`, and categories are selected by id in `ContentView` state instead of an Intent extra.

Related: this matches the iOS lab's `NavigationSplitView` in `ContentView` (selection-driven, three columns on wide windows, one stack on narrow ones).
