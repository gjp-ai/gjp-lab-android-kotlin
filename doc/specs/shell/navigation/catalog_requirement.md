# Feature: Category catalogue

Status: Implemented

## Goal

Show every topic in a category, make clear which ones can be opened today, and open an implemented topic in one tap.

## Scope

### In scope

- The catalogue pane for the selected category.
- A list of the category's topics with their availability.
- Selecting an available topic to show its feature.

### Out of scope

- The category sidebar (see the [sidebar requirement](sidebar_requirement.md)) and the feature screens.
- Search, filtering, or sorting topics.
- Hiding planned topics or showing release dates for them.

## Behavior

- The catalogue appears when a category is selected: beside the sidebar on large windows, in place of it on smaller ones.
- Topics appear in a fixed order defined per category.
- Selecting an available topic shows its feature: in the next pane on wide windows, in place of the catalogue on phones.
- A planned topic is shown for orientation but cannot be selected.

## UI & navigation

- Pane title with the category name (and a back arrow when the sidebar is not visible), then the category description above the topic list.
- One card per topic showing its title and description.
- Available rows end with a chevron announced as "Open"; planned rows end with a clock announced as "Planned".
- The selected topic's card has a thicker `primary` border when the feature is shown beside the catalogue.
- Light and dark themes and enlarged text are supported; descriptions wrap instead of truncating.

## Rules & constraints

- Category names, descriptions, topics, and routes come from one source (`NavigationMenu`); the screen does not hard-code them.
- A topic is available only when it has a route; there is no separate "enabled" flag.
- Topic titles are unique within a category, and routes are unique across the catalogue.

## Platform limitations

None.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| CAT-AC-01 | Select the HTTP Client category | HttpURLConnection shows a chevron; Retrofit shows a clock. |
| CAT-AC-02 | Select an available topic | Its feature is shown; on a phone, Back returns to the catalogue. |
| CAT-AC-03 | Tap a planned topic | Nothing happens. |
| CAT-AC-04 | TalkBack on a row | It reads the title, description, and availability as one element. |
| CAT-AC-05 | Dark theme and a large font size | Rows remain readable and descriptions wrap. |

## Technical implementation constraints

- Source lives in `shell/navigation/`.
- The list reports selection to `ContentView`'s selected topic (`FeatureRoute?`); only available rows are clickable.
- Each topic's optional `route` is a `FeatureRoute` case; `NavigationMenuTest` checks every route appears exactly once.

## Related documents

- [Detailed design](catalog_detail_design.md)
- [Sidebar requirement](sidebar_requirement.md)
- [Slate design system](../../common/theme/theme_detail_design.md)
