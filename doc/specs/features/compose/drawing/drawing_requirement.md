# Feature: Drawing & graphics

Status: Implemented

## Goal

Show how Compose draws: built-in shapes and brushes, a custom `Shape`, Canvas animation, touch drawing, and vector icons.

## Scope

### In scope

- Circle, rounded, and cut-corner shapes with linear, radial, and sweep gradients, and a dashed stroke.
- `StarShape`: 3–12 points with an animated inner radius.
- A sine wave drawn by `Canvas` and moved by an infinite transition.
- Finger drawing with `detectDragGestures` and a **Clear** button.
- Material vector icons tinted with theme roles at a chosen size.

### Out of scope

- Bitmaps, image loading, and RenderEffect.
- Saving drawings.

## Behavior

- Dragging in the drawing area draws a line under the finger; lifting ends the stroke.
- **Clear** removes all strokes and is disabled when the area is empty.
- The wave pauses, and the star changes without animation, when Remove animations is on.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Drawing & graphics** catalogue item (`FeatureRoute.Drawing`).
- Five cards: **Shapes and brushes**, **Custom Shape**, **Canvas animation**, **Touch drawing**, **Vector icons**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- `StarShape.vertices` is a pure function so unit tests can check the geometry.

## Platform limitations

- None.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| DRW-AC-01 | Change the star to 8 points | An 8-pointed star is drawn. |
| DRW-AC-02 | Move the inner radius slider | The star morphs smoothly. |
| DRW-AC-03 | Draw, then tap **Clear** | Strokes appear, then the area is empty and Clear is disabled. |
| DRW-AC-04 | Unit tests | A 5-point star has 10 vertices starting at the top centre. |

## Technical implementation constraints

- Source lives in `features/compose/drawing/`.
- `FeatureRoute.Drawing` maps to `DrawingScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Drawing`.
- No new dependencies.

## Related documents

- [Detailed design](drawing_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
