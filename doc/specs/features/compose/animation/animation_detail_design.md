# Animation detailed design

Status: Implemented, with known gaps

Requirements: [Animation](animation_requirement.md)

## Implementation goal

Receive `reduceMotion` as a parameter (from `AccessibilitySettings` through `FeatureDestination`) and choose `snap()` or no transition when it is true, so the screen stays previewable and testable.

## Source map

| Source | Responsibility |
| --- | --- |
| [`AnimationScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/animation/AnimationScreen.kt) | Screen, `Curve`, `CardTransition`, and their animation specs |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`AccessibilitySettings.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/accessibility/AccessibilitySettings.kt) | Reads Remove animations and reports changes while collected |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Animation` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `AnimationScreen` for the route |

## Ownership and state

- `isGrown`, `curve`, `isMoved`, `transition`, `isCardVisible`, and `selectedTab` use `rememberSaveable`.
- The `Animatable` for the dot is created with `remember` and driven by `LaunchedEffect(isMoved)`.
- Reached from `FeatureRoute.Animation`; pushes nothing.

## Respecting Remove animations

`AccessibilitySettings` reads `Settings.Global.ANIMATOR_DURATION_SCALE` and observes it with a `ContentObserver`; `FeatureDestination` collects the flow and passes `reduceMotion`. The screen then uses `snap()` specs, `EnterTransition.None` and `ExitTransition.None`, and replaces the infinite transition with a still icon.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| The visibility sample reserves a fixed height | Scale and slide transitions are clipped at the box edge | Let the box size follow the card |
| No shared element transition | Screen-to-screen continuity is not shown | Add `SharedTransitionLayout` once it is stable in the project's BOM |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeTopicsTest.everyComposeTopicOpens` opens the screen. Previews cover normal and Remove animations states.
- Previews: light and dark pairs in `AnimationScreen.kt`.
- Manual: ANI-AC-01 to ANI-AC-04 on a phone emulator in light and dark themes and at the largest font size.
