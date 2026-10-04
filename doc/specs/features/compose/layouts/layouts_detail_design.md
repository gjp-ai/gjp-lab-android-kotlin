# Layouts detailed design

Status: Implemented, with known gaps

Requirements: [Layouts](layouts_requirement.md)

## Implementation goal

Animate each box's offset rather than swapping Row for Column, so the same children move between arrangements; implement wrapping with a custom `Layout` whose arithmetic is a pure function.

## Source map

| Source | Responsibility |
| --- | --- |
| [`LayoutsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/layouts/LayoutsScreen.kt) | Screen, `StackKind`, and private `StackDemo` and `LabelBox` |
| [`FlowLayout.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/layouts/FlowLayout.kt) | `FlowLayout` composable, `FlowResult`, and the pure `arrangeFlow` |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Layouts` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `LayoutsScreen` for the route |

## Ownership and state

- `LayoutsScreen` owns `stack` (`StackKind`), `spacing` (0–40 dp, default 12), and `fitWidth` (140–320 dp, default 320) with `rememberSaveable`.
- Reached from `FeatureRoute.Layouts`; pushes nothing.

## Animated containers

Swapping a `Row` for a `Column` would remove and recreate the boxes. `StackDemo` instead keeps three boxes in one `Box` and computes each one's x and y for the chosen container; `animateDpAsState` animates those offsets, so the boxes glide between arrangements. This is the Compose counterpart of SwiftUI's `AnyLayout`.

## FlowLayout algorithm

Each child is measured with the row's maximum width. Items are placed left to right; when the next item would pass `maxWidth` and the row is not empty, the row ends and y moves down by the row's tallest item plus `spacing`. The returned size is the widest row and the total height. `placeRelative` mirrors positions in right-to-left languages.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| `StackDemo` reserves a fixed height | The Row and Box arrangements leave empty space below | Animate the container height too |
| FlowLayout has no line alignment options | Rows are always start-aligned | Add a horizontal arrangement parameter |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeFeatureTest` checks `arrangeFlow` (row wrap, oversized item, empty input); `ComposeTopicsTest.everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `LayoutsScreen.kt`.
- Manual: LAY-AC-01 to LAY-AC-04 on a phone emulator in light and dark themes and at the largest font size.
