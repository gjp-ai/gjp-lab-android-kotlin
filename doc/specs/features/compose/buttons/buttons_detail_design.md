# Buttons & actions detailed design

Status: Implemented, with known gaps

Requirements: [Buttons & actions](buttons_requirement.md)

## Implementation goal

One screen-level `record(action)` function writes **Last action**, so every sample proves its action ran; gestures use `combinedClickable` and repeat their action as a `CustomAccessibilityAction`.

## Source map

| Source | Responsibility |
| --- | --- |
| [`ButtonsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/buttons/ButtonsScreen.kt) | Screen, `SortOrder`, and `SaveDurationMillis` |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.ButtonsActions` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `ButtonsScreen` for the route |

## Ownership and state

- `lastAction`, `sortOrder`, menu and dialog flags, `isFavorite`, and `likes` use `rememberSaveable`.
- `isSaving` uses `remember`: the save coroutine does not survive recreation, so its flag must not either.
- The save runs in `rememberCoroutineScope()`, so leaving the topic cancels it.
- Reached from `FeatureRoute.ButtonsActions`; pushes nothing.

## Gestures and accessibility

`combinedClickable(onDoubleClick = …)` likes the photo and `combinedClickable(onLongClick = …, onLongClickLabel = …)` opens the card menu. TalkBack cannot perform a double tap on content, so the photo also exposes `customActions = listOf(CustomAccessibilityAction("Like"))`; the long press is announced through its click label.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| The card menu opens at the card's start | It does not appear under the finger | Track the press position and offset the menu |
| Dismissing the dialog counts as cancel | Tapping outside records *Cancelled delete* | Accepted; it matches a Cancel tap |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeTopicsTest.everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `ButtonsScreen.kt`.
- Manual: BTN-AC-01 to BTN-AC-05 on a phone emulator in light and dark themes and at the largest font size.
