# 0004: Dashboard and catalogue content in one `NavigationMenu.kt`

Status: Accepted, 2026-10-04

## Context

Category text lived in two places: a private list in the dashboard screen and three `when` blocks (title, description, topics) in `FeatureCatalogScreen`. The copies had already drifted: the dashboard said "Android UI" where the catalogue said "Jetpack Compose". Adding a category meant editing both files and three `when` branches.

## Decision

- All categories and topics, in display order, live in `shell/navigation/NavigationMenu.kt` as Kotlin data: `NavigationCategory` (id, title, summary, description, icon, topics) and `NavigationTopic` (title, description, optional `FeatureRoute`).
- A topic without a route is shown as planned.
- The mapping from route to Activity stays in `FeatureCatalogActivity.openFeature`. The menu never names an Activity.
- The menu is Kotlin rather than a bundled JSON file: it needs no asset loading, no serialization dependency, and no decode-failure path, and Material icons stay type-checked.

## Consequences

- Menu text, icons, order, and planned topics change in one file.
- A feature is still a code change: a `FeatureRoute` case, its Activity in `FeatureCatalogActivity`, the manifest entry, and the topic in `NavigationMenu`.
- `NavigationMenuTest` guards the link: category IDs and topic titles are unique, and every `FeatureRoute` appears exactly once. A forgotten or duplicated route fails the unit tests.
- The catalogue receives the category `id` as an Intent extra and looks it up with `NavigationMenu.category(id)`; an unknown id finishes the Activity.
- Serving the menu remotely would need a new decision, because a remote menu must have a fallback.

Related: the iOS lab keeps the same content in a bundled `navigation.json` (iOS decision 0004).
