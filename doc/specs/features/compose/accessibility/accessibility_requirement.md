# Feature: Accessibility & testing

Status: Implemented

## Goal

Show how semantics describe Compose UI to TalkBack and tests: font scale, merged elements, an adjustable control, live system settings, and test tags.

## Scope

### In scope

- The current font scale, an icon that scales with it, and a row that becomes a column at large scales.
- Merging a card into one element with `mergeDescendants` and hiding a decorative icon.
- A star rating as one adjustable element with a value, `setProgress`, and Increase/Decrease actions.
- A live readout of TalkBack, Remove animations, and font scale.
- A button and count with `testTag`s used by `ComposeTopicsTest`.

### Out of scope

- Switch access configuration.
- Screenshot testing.

## Behavior

- Changing TalkBack or Remove animations updates the readout without reopening the screen.
- With TalkBack, adjusting the rating changes it between 1 and 5 stars.
- Each tap on **Tap me** increments the count (*Tapped 1 time*, *Tapped 2 times*).

## UI & navigation

- Entry point: **Jetpack Compose** category → **Accessibility & testing** catalogue item (`FeatureRoute.Accessibility`).
- Five cards: **Font scale**, **Merging and decorative icons**, **Adjustable rating**, **System settings**, **UI test hooks**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- Test tags are defined once in `AccessibilityTestTags` and used by both the screen and the test.

## Platform limitations

- Android does not expose high-contrast text or colour-correction settings to apps, so they are not shown.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| A11Y-AC-01 | Set the font size to its largest | The font-scale row stacks vertically and the icon grows. |
| A11Y-AC-02 | TalkBack on the flight card | It is read as one element without the airplane icon. |
| A11Y-AC-03 | TalkBack adjust on the rating | The value changes by one star, between 1 and 5. |
| A11Y-AC-04 | Run `ComposeTopicsTest` | The tap count changes from *Tapped 0 times* to *Tapped 1 time*. |

## Technical implementation constraints

- Source lives in `features/compose/accessibility/`.
- `FeatureRoute.Accessibility` maps to `AccessibilityScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Accessibility`.
- No new dependencies.

## Related documents

- [Detailed design](accessibility_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
