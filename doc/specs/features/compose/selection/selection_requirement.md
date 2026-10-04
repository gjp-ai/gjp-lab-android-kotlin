# Feature: Selection

Status: Implemented

## Goal

Show the Compose controls for single choice, on/off, numbers, multiple choice, and dates, combined into one coffee order.

## Scope

### In scope

- Single choice with segmented buttons and a dropdown menu.
- `Switch`, a stepper built from two icon buttons, and a `Slider`.
- Multiple choice of extras stored in a `Set`, using `FilterChip`s.
- `DatePickerDialog` limited to today and later.
- An order summary that combines every selection.

### Out of scope

- Time pickers and colour pickers.
- Placing the order.

## Behavior

- Changing any control updates the **Your order** summary immediately.
- Espresso shots range 1–4; sweetness 0–100 % in steps of 10; past dates cannot be picked.
- The summary lists extras in a fixed order, or says *no extras*.
- **Reset order** restores every default.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Selection** catalogue item (`FeatureRoute.Selection`).
- Four cards (**Single choice**, **On/off and numbers**, **Multiple choice**, **Date**), then the **Your order** summary and **Reset order**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- The summary text is built by `CoffeeOrder.summary`, a pure property that unit tests check.

## Platform limitations

- The date picker works in UTC days; the chosen date is shown in the device's date format.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| SEL-AC-01 | Select Large and Iced | The summary reads *Large iced coffee…* and the icon changes to a cold drink. |
| SEL-AC-02 | Turn Vanilla on and Cinnamon off | The summary lists only Vanilla. |
| SEL-AC-03 | Open the date picker | Days before today cannot be selected. |
| SEL-AC-04 | Unit tests | The default, combined, and extras-order summaries match their expected text. |

## Technical implementation constraints

- Source lives in `features/compose/selection/`.
- `FeatureRoute.Selection` maps to `SelectionScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Selection`.
- No new dependencies.

## Related documents

- [Detailed design](selection_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
