# Selection detailed design

Status: Implemented, with known gaps

Requirements: [Selection](selection_requirement.md)

## Implementation goal

Hold every choice in one immutable `CoffeeOrder` that each control copies, so the summary is always consistent and testable.

## Source map

| Source | Responsibility |
| --- | --- |
| [`SelectionScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/selection/SelectionScreen.kt) | Screen, `FutureDates` (`SelectableDates`), and date formatting |
| [`CoffeeOrder.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/selection/CoffeeOrder.kt) | `CoffeeSize`, `Temperature`, `Extra`, and `CoffeeOrder` with `summary` |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Selection` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `SelectionScreen` for the route |

## Ownership and state

- `order` (`CoffeeOrder`) is held with `remember`; the menu and dialog flags and `pickupDate` use `rememberSaveable`.
- Reached from `FeatureRoute.Selection`; pushes nothing.

## Future dates

`rememberDatePickerState(selectableDates = FutureDates)` disables days whose UTC date is before today, and years before this one. The dialog's state is created only while it is open, so cancelling discards the selection.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| The order is not saved | Rotating the device resets the order | Make `CoffeeOrder` `Parcelable` or save its fields separately |
| Dropdown menu instead of an exposed dropdown field | The temperature control looks like a button | Use `ExposedDropdownMenuBox` once its anchor API is stable in the project's BOM |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeFeatureTest` checks `CoffeeOrder.summary`; `ComposeTopicsTest.everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `SelectionScreen.kt`.
- Manual: SEL-AC-01 to SEL-AC-04 on a phone emulator in light and dark themes and at the largest font size.
