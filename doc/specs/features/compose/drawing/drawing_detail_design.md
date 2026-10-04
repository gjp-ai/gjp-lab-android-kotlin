# Drawing & graphics detailed design

Status: Implemented, with known gaps

Requirements: [Drawing & graphics](drawing_requirement.md)

## Implementation goal

Express the star as a `Shape`, so any modifier that takes a shape (`background`, `clip`, `border`) can use it, and keep its geometry in a pure companion function.

## Source map

| Source | Responsibility |
| --- | --- |
| [`DrawingScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/drawing/DrawingScreen.kt) | Screen, wave Canvas, and touch drawing |
| [`StarShape.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/drawing/StarShape.kt) | `StarShape` and the pure `vertices` function |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Drawing` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `DrawingScreen` for the route |

## Ownership and state

- `points`, `innerRatio`, and `iconSize` use `rememberSaveable`; `strokes` (`mutableStateListOf`) and `currentStroke` use `remember`.
- Reached from `FeatureRoute.Drawing`; pushes nothing.

## StarShape geometry

`vertices` returns `2 × points` corners, alternating the outer radius and `outer × innerRatio`, starting at −90° (the top centre) and stepping π / points clockwise. `createOutline` joins them into a closed `Path` and returns `Outline.Generic`. Fewer than two points returns no vertices.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Strokes are not saved | Rotating the device clears the drawing | Save strokes as lists of floats with `rememberSaveable` |
| Each move copies the current stroke | Very long strokes allocate more than needed | Append to a `Path` or a mutable list instead |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeFeatureTest` checks `StarShape.vertices`; `ComposeTopicsTest.everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `DrawingScreen.kt`.
- Manual: DRW-AC-01 to DRW-AC-04 on a phone emulator in light and dark themes and at the largest font size.
