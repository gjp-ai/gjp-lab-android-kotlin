# Material 3 detailed design

Status: Implemented, with known gaps

Requirements: [Material 3](material3_requirement.md)

## Implementation goal

One `LabDemoPage` with four `LabDemoSection` cards. Swatches and samples read the theme directly, so they always show the current light or dark values.

## Source map

| Source | Responsibility |
| --- | --- |
| [`Material3Screen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/material3/Material3Screen.kt) | Screen and private `RoleSwatch`, `TypeSample`, `ShapeSample`, and `OrderSample` helpers |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Material3` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `Material3Screen` for the route |

## Ownership and state

- `Material3Screen` owns `padding` (`Float`, default 12) with `rememberSaveable`, so it survives rotation.
- Reached from `FeatureRoute.Material3`; pushes nothing.

## Modifier order

Compose applies modifiers from the outside in. `Modifier.background().padding()` draws the background at the full size, so the fill includes the padding; `Modifier.padding().background()` insets first, so the padding is transparent space outside the fill. This is the reverse of SwiftUI, where `.padding().background()` includes the padding. A thin `outlineVariant` border marks each sample's frame so the difference is visible.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Only four colour roles are shown | Secondary, tertiary, and the surface container steps are not demonstrated | Add a scrolling grid of every role pair |
| Dynamic colour is not shown | Readers cannot compare the Slate palette with wallpaper colours | Add a preview with `GJPLabTheme(dynamicColor = true)` on Android 12+ |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeTopicsTest.everyComposeTopicOpens` opens the screen from the catalogue and checks its pane title.
- Previews: light and dark pairs in `Material3Screen.kt`.
- Manual: M3-AC-01 to M3-AC-03 on a phone emulator in light and dark themes and at the largest font size.
