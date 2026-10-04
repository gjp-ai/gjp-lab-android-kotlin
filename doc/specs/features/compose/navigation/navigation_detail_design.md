# Navigation detailed design

Status: Implemented, with known gaps

Requirements: [Navigation](navigation_requirement.md)

## Implementation goal

Reuse the feature pane's `detailPath`: the topic pushes `DetailRoute.NavigationLevel`, and `ContentView` draws the top level over the topic with `NavigationLevelScreen`, exactly like the HTTP response.

## Source map

| Source | Responsibility |
| --- | --- |
| [`NavigationPatternsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/navigation/NavigationPatternsScreen.kt) | Screen, bottom sheet, and full-screen dialog |
| [`NavigationLevelScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/navigation/NavigationLevelScreen.kt) | One pushed level with push, back, and pop-to-root actions |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`ContentView.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/ContentView.kt) | Owns `detailPath`, draws `NavigationLevelScreen`, and provides `goBack` and `popToRoot` |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.NavigationPatterns` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `NavigationPatternsScreen` for the route |

## Ownership and state

- `isSheetOpen` and `isFullScreenOpen` use `rememberSaveable`.
- Pushed levels live in `ContentView`'s `detailPath`, which `onPush` adds to only while this topic is selected.
- Reached from `FeatureRoute.NavigationPatterns`; pushes `DetailRoute.NavigationLevel(n)`.

## Closing the sheet

The **Close** button calls `sheetState.hide()` in a coroutine and removes the sheet when the hide animation finishes, so it slides away instead of vanishing.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Pushed levels are not saved | Rotating the device returns to the topic screen | Save `detailPath` (see the sidebar detailed design) |
| No transition between levels | Levels appear instantly | Animate the top `DetailRoute` with `AnimatedContent` |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeTopicsTest.navigationTopicPushesAndPopsLevels` pushes two levels and pops to root; `everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `NavigationPatternsScreen.kt`.
- Manual: NAV-AC-01 to NAV-AC-04 on a phone emulator in light and dark themes and at the largest font size.
