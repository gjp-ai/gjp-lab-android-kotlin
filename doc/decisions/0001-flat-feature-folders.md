# 0001: One flat folder per feature; the app shell lives in `shell/`

Status: Accepted, 2026-10-04

## Context

Startup, dashboard, and maintenance files sat in the root package next to `GJPLabApplication`. The catalogue used `navigation/catalog/` and `navigation/catalog/model/`, features used `data/` and `model/` subfolders, and the Koin module lived alone in `di/`. Most of those folders held a single file, so following one small feature meant opening several packages, and the root package mixed app-wide and screen-specific code.

## Decision

- The package root has three areas: `shell/` for the app-wide flow, `common/` for shared helpers, and `features/<category>/<feature>/` for features.
- `shell/` holds `GJPLabApplication`, `MainActivity`, and the Koin `AppModule`; `shell/startup/` holds splash and maintenance; `shell/navigation/` holds the dashboard, catalogue, routes, and menu.
- The shell package is named `shell/`, not `app/`, because the Gradle module is already called `app` and `app/src/main/java/.../app/` is easy to misread.
- Each feature and each shell area is one flat folder holding its Activities, screens, repositories, and models. Category folders hold only feature folders. Do not create subfolders by file type.
- `doc/specs/` mirrors the package root: docs for `<package root>/<path>/` live in `doc/specs/<path>/`, told apart by file-name prefix when several screens share a folder.

## Consequences

- One folder shows everything a feature needs, which suits a learning lab, and the same path finds its docs.
- If a feature grows large, split it by sub-feature (for example `httpurlconnection/history/`), not by file type.
- Moving a class moves its package, so `android:name` entries in `AndroidManifest.xml` change with it.
- Moving the launcher Activity changes its component name; home-screen shortcuts pinned to the old name stop working until the app is reinstalled.

Related: the iOS lab uses the same layout, with `app/` in place of `shell/` (iOS decision 0002). Types that exist in both labs share a name (for example `CategorySidebar`, `BlockAppDuringCallsController`).
