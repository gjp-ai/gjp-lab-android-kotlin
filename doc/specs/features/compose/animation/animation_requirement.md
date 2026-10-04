# Feature: Animation

Status: Implemented

## Goal

Show state-driven animation in Compose, from single values to enter and exit transitions and infinite loops, and how to respect the system Remove animations setting.

## Scope

### In scope

- `animateDpAsState` and `animateColorAsState` on a growing circle.
- `Animatable` with linear, ease, spring, and bouncy curves.
- `AnimatedVisibility` with fade, slide, scale, and expand transitions.
- A shared tab indicator that slides by animating its offset.
- An infinite pulse with `rememberInfiniteTransition`.

### Out of scope

- Shared element transitions between screens.
- Lottie or other animation libraries.

## Behavior

- When Remove animations is on, a notice is shown, changes apply instantly (`snap()` and no transitions), and the heart does not pulse.
- The setting is read live: turning it on or off updates the screen without reopening it.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Animation** catalogue item (`FeatureRoute.Animation`).
- An optional Remove animations notice, then five cards: **animate*AsState**, **Animatable and curves**, **AnimatedVisibility**, **Shared indicator**, **Infinite transition**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- Decorative loops stop when Remove animations is on.

## Platform limitations

- Remove animations is read from the system animator duration scale; other motion preferences are not exposed to apps.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| ANI-AC-01 | Tap **Grow** | The circle grows with a bounce and fills with `primary`. |
| ANI-AC-02 | Pick *Linear* then tap **Move** | The dot moves at constant speed. |
| ANI-AC-03 | Pick *Slide* and tap **Remove** | The card slides down and fades out. |
| ANI-AC-04 | Turn on Remove animations | The notice appears and changes happen instantly. |

## Technical implementation constraints

- Source lives in `features/compose/animation/`.
- `FeatureRoute.Animation` maps to `AnimationScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Animation`.
- No new dependencies.

## Related documents

- [Detailed design](animation_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
