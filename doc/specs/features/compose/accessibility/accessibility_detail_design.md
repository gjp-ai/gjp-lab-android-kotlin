# Accessibility & testing detailed design

Status: Implemented, with known gaps

Requirements: [Accessibility & testing](accessibility_requirement.md)

## Implementation goal

Receive `AccessibilityStatus` as a parameter (from `AccessibilitySettings` through `FeatureDestination`), read font scale from `LocalDensity`, and describe custom controls with `clearAndSetSemantics`.

## Source map

| Source | Responsibility |
| --- | --- |
| [`AccessibilityScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/accessibility/AccessibilityScreen.kt) | Screen, `AccessibilityTestTags`, `LargeFontScale`, `tapCountLabel`, and `clampRating` |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`AccessibilitySettings.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/accessibility/AccessibilitySettings.kt) | Reads TalkBack and Remove animations and reports changes while collected |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Accessibility` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `AccessibilityScreen` for the route |

## Ownership and state

- `rating` and `taps` use `rememberSaveable`.
- Font scale comes from `LocalDensity.current.fontScale`, which updates on configuration change.
- Reached from `FeatureRoute.Accessibility`; pushes nothing.

## Adjustable rating

The star row uses `clearAndSetSemantics` to replace the five icons with one element: `contentDescription = "Rating"`, `stateDescription = "N of 5 stars"`, a `ProgressBarRangeInfo` from 1 to 5, `setProgress` for TalkBack's adjust gestures, and Increase and Decrease custom actions. `clampRating` keeps every path between 1 and 5.

## Live settings

`AccessibilitySettings.status` is a `callbackFlow` that registers a touch-exploration listener and a `ContentObserver` for the animator duration scale, and unregisters them when collection stops.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| UI tests assume a phone-width window | They fail on a tablet where panes are side by side | Make the test helpers handle two- and three-pane layouts |
| TalkBack announces the rating as a seek bar | Some versions add a percentage to the stars | Accept, or replace `setProgress` with custom actions only |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeTopicsTest.accessibilityTapCountChanges` taps the button by test tag and checks the count; `ComposeFeatureTest` checks `tapCountLabel` and `clampRating`.
- Previews: light and dark pairs in `AccessibilityScreen.kt`.
- Manual: A11Y-AC-01 to A11Y-AC-04 on a phone emulator in light and dark themes and at the largest font size.
