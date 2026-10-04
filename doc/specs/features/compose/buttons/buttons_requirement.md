# Feature: Buttons & actions

Status: Implemented

## Goal

Show Material 3 button styles and the ways people reach actions: confirmation dialogs, menus, gestures with accessible alternatives, async work, and icon buttons with proper touch targets.

## Scope

### In scope

- Filled, tonal, elevated, outlined, and text buttons.
- A destructive action with an `AlertDialog` confirmation.
- A `DropdownMenu` sort picker and a long-press menu on a card.
- A double-tap gesture with a matching named accessibility action.
- An async save button that shows progress and is disabled while running.
- An icon-only favourite toggle with a 48 dp target and a content description.

### Out of scope

- Floating action buttons and app bars.
- Haptic feedback.

## Behavior

- Every action writes a short description to **Last action** at the top of the screen.
- **Save** waits 1.5 seconds, during which it shows a spinner and cannot be pressed again.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Buttons & actions** catalogue item (`FeatureRoute.ButtonsActions`).
- A **Last action** row and five cards: **Styles and emphasis**, **Destructive action and confirmation**, **Menus**, **Gestures with accessible alternatives**, **Async action and touch target**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- The confirm button in the delete dialog uses the `error` colour.
- Every gesture-only action also has a named accessibility action.

## Platform limitations

- None.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| BTN-AC-01 | Tap **Delete draft**, then **Delete** | A confirmation dialog appears; Last action reads *Deleted draft*. |
| BTN-AC-02 | Choose a sort order from the menu | Last action reads *Sorted by …*. |
| BTN-AC-03 | Tap **Save** | A spinner shows and the button is disabled for about 1.5 seconds, then Last action reads *Saved*. |
| BTN-AC-04 | TalkBack on the heart button | It is read as *Add favourite* or *Remove favourite*, with its on/off state. |
| BTN-AC-05 | TalkBack actions on the photo | A *Like* action is offered and increases the like count. |

## Technical implementation constraints

- Source lives in `features/compose/buttons/`.
- `FeatureRoute.ButtonsActions` maps to `ButtonsScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.ButtonsActions`.
- No new dependencies.

## Related documents

- [Detailed design](buttons_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
