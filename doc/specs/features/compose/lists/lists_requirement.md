# Feature: Lists & grids

Status: Implemented

## Goal

Show lazy lists and grids that compose only visible items, with sections, search, swipe to delete, edit mode, and pull to refresh.

## Scope

### In scope

- A `LazyColumn` grouped into Fruit and Vegetables sections with counts.
- Swipe to delete (`SwipeToDismissBox`), an **Edit** mode with delete buttons, and pull to refresh (`PullToRefreshBox`).
- A `LazyVerticalGrid` with adaptive columns where tapping a tile toggles favourite.
- Search filtering both presentations, with an empty state.
- Segmented buttons switching between List and Grid.

### Out of scope

- Paging from a data source.
- Reordering by drag.

## Behavior

- Search matches names ignoring case and accents (*jalapeno* finds *Jalapeño*).
- Pull to refresh waits one second and restores all 20 sample items, including deleted ones.
- **Edit** is shown only in List mode.
- When nothing matches, a *No results* message names the search.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Lists & grids** catalogue item (`FeatureRoute.ListsGrids`).
- An introduction, a search field, the List/Grid switch with **Edit**, then the list or grid filling the rest of the pane.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- Items are keyed by id, so deleting or favouriting one does not recompose the rest.
- Search lives in the pure `matching` function so tests can check it.

## Platform limitations

- The number of grid columns depends on the pane width.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| LST-AC-01 | Swipe a row from end to start | The row is removed and the section count drops. |
| LST-AC-02 | Pull down | After about one second all items are back. |
| LST-AC-03 | Search *an* | Only matching items are shown in both List and Grid. |
| LST-AC-04 | Search *zzz* | The empty search message is shown. |
| LST-AC-05 | Switch to Grid on a tablet | More columns appear than on a phone. |

## Technical implementation constraints

- Source lives in `features/compose/lists/`.
- `FeatureRoute.ListsGrids` maps to `ListsGridsScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.ListsGrids`.
- No new dependencies.

## Related documents

- [Detailed design](lists_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
