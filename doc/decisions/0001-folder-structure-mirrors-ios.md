# 0001: Folder structure and names mirror the iOS lab; the app shell lives in `shell/`

Status: Accepted, 2026-10-04

## Context

The Android and iOS labs teach the same features, but the Android code used a different layout: startup and dashboard files sat in the root package, the catalogue had `catalog/` and `model/` subfolders, features had `data/` and `model/` subfolders, Koin lived in `di/`, and docs were split into `requirements/`, `detail-design/`, and `integrations/`. Readers moving between the two projects had to relearn where everything was, and several types had different names for the same role.

## Decision

- The package root has three areas, matching iOS: `shell/` (iOS `app/`), `common/`, and `features/<category>/<feature>/`.
- `shell/` is used instead of `app/` because the Gradle module is already called `app`, and `app/src/main/java/.../app/` reads badly. `shell/` holds `GJPLabApplication`, `MainActivity`, and the Koin `AppModule`; `shell/startup/` holds splash and maintenance; `shell/navigation/` holds the dashboard, catalogue, routes, and menu.
- Each feature and each shell area is one flat folder holding its Activities, screens, repositories, and models. No subfolders by file type. If a feature grows large, split it by sub-feature, not by file type.
- Types that have an iOS counterpart use the iOS name (`CategorySidebar`, `NavigationCategory`, `NavigationTopic`, `BlockAppDuringCallsController`). Android-only types keep Android names (Activities, `AppModule`, `FirebaseModule`, `CallBlockingHost`).
- `doc/specs/` mirrors the package root: docs for `<package root>/<path>/` live in `doc/specs/<path>/`.
- Implementation stays Android-native; only structure and names are shared.

## Consequences

- One folder shows everything a feature needs, and the same path finds the matching code and docs in both labs.
- `shell/` is the one intentional path difference from iOS; mention it when comparing the projects.
- Moving a class moves its package, so the manifest's `android:name` entries change with it.
- Renaming the launcher Activity's package changes its component name; home-screen shortcuts pinned to the old name stop working until the app is reinstalled.
