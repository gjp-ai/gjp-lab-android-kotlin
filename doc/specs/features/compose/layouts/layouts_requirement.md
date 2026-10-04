# Feature: Layouts

Status: Implemented

## Goal

Demonstrate Compose's measure-once layout model with Row, Column, and Box, weights and alignment, a layout that adapts to its width, and a custom `Layout`.

## Scope

### In scope

- The same three boxes arranged as a Row, Column, or Box, with a spacing slider and animated positions.
- `Modifier.weight` sharing leftover width and per-child cross-axis alignment.
- `BoxWithConstraints` switching between a row and a column as a box narrows.
- `FlowLayout`, a custom `Layout` that wraps tags onto new rows.

### Out of scope

- Lazy lists and scrolling performance (see Lists & grids).
- The adaptive navigation panes (see the sidebar detailed design).

## Behavior

- Changing the container or spacing animates the boxes to their new positions.
- Spacing is disabled for Box, which overlaps its children.
- Narrowing the BoxWithConstraints sample below 280 dp shows its three buttons in a column.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Layouts** catalogue item (`FeatureRoute.Layouts`).
- Four cards: **Row, Column, and Box**, **Weight and alignment**, **BoxWithConstraints**, **Custom Layout**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- `arrangeFlow(sizes, maxWidth, spacing)` is a pure function so unit tests can check it without rendering.
- An item wider than the available width is placed on its own row rather than dropped.

## Platform limitations

- None.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| LAY-AC-01 | Choose Column | Boxes animate into a vertical column. |
| LAY-AC-02 | Choose Box | Boxes overlap at the top start; the spacing slider is disabled. |
| LAY-AC-03 | Narrow the BoxWithConstraints sample | Buttons switch from a row to a column. |
| LAY-AC-04 | Unit tests | FlowLayout wraps full rows, places oversized items alone, and returns zero size for no items. |

## Technical implementation constraints

- Source lives in `features/compose/layouts/`.
- `FeatureRoute.Layouts` maps to `LayoutsScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Layouts`.
- No new dependencies.

## Related documents

- [Detailed design](layouts_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
