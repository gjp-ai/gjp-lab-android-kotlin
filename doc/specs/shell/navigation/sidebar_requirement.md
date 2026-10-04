# Feature: Category sidebar

Status: Implemented

## Goal

Give users one starting point that lists every lab category with a short summary, and leads to its topics with the layout that suits the window: side by side on large screens, one step at a time on phones.

## Scope

### In scope

- The first pane of the app's navigation: category list and selection.
- How the three levels (categories, catalogue, feature) are arranged at each window width.

### Out of scope

- The catalogue pane's contents (see the [catalogue requirement](catalog_requirement.md)) and the feature screens.
- Search, favorites, recently used items, deep links, and restoring the last selection after the app is closed.

## Behavior

- After startup (unless maintenance is on) the app shows the category sidebar.
- Selecting a category shows its catalogue; selecting an available topic shows the feature.
- **Large windows (1200dp and wider):** categories, catalogue, and feature appear side by side. Before a selection, the catalogue and feature panes show a short prompt ("Choose a category", "Choose a topic").
- **Expanded windows (840dp to under 1200dp):** two panes. The left pane shows the categories, or the selected category's catalogue with a back arrow; the right pane shows the feature or a prompt.
- **Narrow windows (under 840dp):** one stack: categories → catalogue → feature, with a back arrow and system Back between levels.
- Changing the category clears the selected topic; changing the topic closes any screen pushed inside the feature.
- System Back first closes a pushed screen, then clears the topic, then clears the category; with nothing selected it leaves the app.
- Rotating or resizing the window keeps the selected category and topic.
- Categories appear in this order: Jetpack Compose, HTTP Client, Security, Integration, Others.

## UI & navigation

- Pane title "GJP Lab".
- Each row is its own card showing the category icon in a tinted tile, the title, and a short summary.
- The selected category's card has a thicker `primary` border; this is visible whenever the sidebar shares the screen with another pane.
- Each row is one accessible element whose click label names the catalogue it opens.
- Supports light and dark themes and enlarged text; rows grow with text size.

## Rules & constraints

- Category titles, summaries, and icons come from one source (`NavigationMenu`); the sidebar does not hard-code them.
- Topic availability is shown in the catalogue, not in the sidebar.
- Layout depends on window width only, never on the device model.

## Platform limitations

- Which layout appears on a tablet or foldable depends on its current window width, which changes with orientation, folding, and multi-window.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| SDB-AC-01 | Startup completes with maintenance off | The sidebar lists all five categories in order with icon, title, and summary. |
| SDB-AC-02 | Phone: tap a category, then an available topic | The catalogue, then the feature, replace the screen; Back returns step by step. |
| SDB-AC-03 | Large window: select a category and a topic | Categories, catalogue, and feature are visible side by side; both selections are outlined. |
| SDB-AC-04 | Large window: select a different category while a feature is shown | The catalogue changes and the feature pane returns to "Choose a topic". |
| SDB-AC-05 | Rotate a tablet with a topic open | The same category and topic remain selected in the new layout. |
| SDB-AC-06 | TalkBack | Each row reads its title and summary as one element with its click label. |

## Technical implementation constraints

- Source lives in `shell/navigation/` and `shell/ContentView.kt`.
- `ContentView` owns the selected category and topic and the feature pane's pushed screens; `MainActivity` is the only navigation Activity.
- Adding a category means adding an entry to `NavigationMenu`; the sidebar updates automatically.

## Related documents

- [Detailed design](sidebar_detail_design.md)
- [Catalogue requirement](catalog_requirement.md)
- [Decision 0005: adaptive pane navigation](../../../decisions/0005-adaptive-pane-navigation.md)
