# Feature: Material 3

Status: Implemented

## Goal

Show how Material 3 styles composables from the theme, and how modifiers wrap a composable, so a reader understands colour roles, the type scale, theme shapes, and why modifier order matters.

## Scope

### In scope

- Colour roles shown as container and on-colour pairs (`primary`, `primaryContainer`, `surfaceContainer`, `errorContainer`).
- The type scale from `MaterialTheme.typography`.
- `Card`, `ElevatedCard`, and `OutlinedCard`, and the theme's small, medium, and large shapes.
- Modifier order: the same `background` and `padding` in two orders, with a padding slider.

### Out of scope

- Changing the theme at run time or dynamic (wallpaper) colour.
- Custom layouts (see Layouts).

## Behavior

- Moving the padding slider updates both modifier-order samples immediately.
- All colours and text styles follow the light or dark theme.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Material 3** catalogue item (`FeatureRoute.Material3`).
- A one-line introduction and four cards: **Colour roles**, **Type scale**, **Cards and shapes**, **Modifier order**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- The padding slider ranges 0–32 dp in 1 dp steps; the default is 12 dp.

## Platform limitations

- None; all behavior is local to the screen.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| M3-AC-01 | Move the padding slider | The fill grows with the padding in *Background first*; in *Padding first* the fill stays around the text and the padding is outside it. |
| M3-AC-02 | Open in dark theme | Every swatch keeps readable text in its on-colour. |
| M3-AC-03 | Large font size | Type samples grow and wrap; cards stay readable. |

## Technical implementation constraints

- Source lives in `features/compose/material3/`.
- `FeatureRoute.Material3` maps to `Material3Screen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Material3`.
- No new dependencies.

## Related documents

- [Detailed design](material3_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
