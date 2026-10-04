# Feature: Category catalogue

Status: Implemented

## Goal

Show every topic in a category, make clear which ones can be opened today, and open an implemented topic in one tap.

## Scope

### In scope

- The catalogue screen for one category.
- A list of the category's topics with their availability.
- Opening an available topic's feature screen.

### Out of scope

- The category dashboard (see the [dashboard requirement](sidebar_requirement.md)) and the feature screens.
- Search, filtering, or sorting topics.
- Hiding planned topics or showing release dates for them.

## Behavior

- The catalogue opens when the user taps a category on the dashboard.
- Topics appear in a fixed order defined per category.
- Tapping an available topic opens its feature screen; Back returns to the catalogue.
- A planned topic is shown for orientation but does nothing when tapped.
- An unknown category closes the catalogue instead of showing an empty screen.

## UI & navigation

- A back action to the dashboard, the category title, and the category description above the topic list.
- A table-style card with "Component" and "Status" headings and one row per topic showing its title and description.
- Available rows end with a forward arrow announced as "Open <title>"; planned rows end with a clock announced as "Planned".
- Content width is limited to 720dp on large screens; light and dark themes and enlarged text are supported; descriptions wrap.

## Rules & constraints

- Category titles, descriptions, topics, and routes come from one source (`NavigationMenu`); the screen does not hard-code them.
- A topic is available only when it has a route; there is no separate "enabled" flag.
- Topic titles are unique within a category, and routes are unique across the catalogue.

## Platform limitations

None.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| CAT-AC-01 | Open the HTTP Client category | HttpsURLConnection shows an arrow; Retrofit shows a clock. |
| CAT-AC-02 | Tap an available topic | Its feature opens; Back returns to the catalogue. |
| CAT-AC-03 | Tap a planned topic | Nothing happens. |
| CAT-AC-04 | TalkBack on a row | It reads the title, description, and availability. |
| CAT-AC-05 | Dark theme and a large font size | Rows remain readable and descriptions wrap. |

## Technical implementation constraints

- Source lives in `shell/navigation/`.
- `FeatureCatalogActivity` receives the category `id` as an Intent extra and maps each `FeatureRoute` to a feature Activity.
- Each topic's optional `route` is a `FeatureRoute` case; `NavigationMenuTest` checks every route appears exactly once.

## Related documents

- [Detailed design](catalog_detail_design.md)
- [Dashboard requirement](sidebar_requirement.md)
- [Slate design system](../../common/theme/theme_detail_design.md)
