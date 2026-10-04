# Lists & grids detailed design

Status: Implemented, with known gaps

Requirements: [Lists & grids](lists_requirement.md)

## Implementation goal

Keep the items in a `mutableStateListOf` and derive the visible list from the query on every recomposition; both presentations read the same filtered list.

## Source map

| Source | Responsibility |
| --- | --- |
| [`ListsGridsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/lists/ListsGridsScreen.kt) | Screen, `ProduceLayout`, list rows with swipe, grid tiles, and the empty state |
| [`Produce.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/lists/Produce.kt) | `Produce` samples, `ProduceKind`, and the accent-insensitive `matching` filter |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.ListsGrids` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `ListsGridsScreen` for the route |

## Ownership and state

- `items` (`mutableStateListOf`) and `isRefreshing` use `remember`; `query`, `layout`, and `isEditing` use `rememberSaveable`.
- The screen is not a `LabDemoPage`: it fills the pane so the lazy list or grid can scroll on its own.
- Reached from `FeatureRoute.ListsGrids`; pushes nothing.

## Search folding

`foldForSearch` decomposes text with `Normalizer.Form.NFD`, removes combining marks, and lower-cases it, so *Jalapeño* becomes *jalapeno*. The query is folded the same way and matched with `contains`.

## Swipe to delete

Each row wraps its content in `SwipeToDismissBox` with start-to-end dismissal disabled; `onDismiss` removes the item. The red background shows a delete icon while swiping.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Deletions are not saved | Rotating the device restores deleted items | Save the remaining ids with `rememberSaveable` |
| No undo after a swipe | A mistaken swipe needs a pull to refresh | Show a snackbar with Undo |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeFeatureTest` checks the sample ids and `matching` (case, accents, blank and unknown queries); `ComposeTopicsTest.everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `ListsGridsScreen.kt`.
- Manual: LST-AC-01 to LST-AC-05 on a phone emulator in light and dark themes and at the largest font size.
