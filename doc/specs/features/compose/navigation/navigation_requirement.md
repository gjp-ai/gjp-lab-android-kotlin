# Feature: Navigation

Status: Implemented

## Goal

Show how navigation works as state in this app: pushing and popping screens through `ContentView`, bottom sheets, full-screen dialogs, and adaptive panes.

## Scope

### In scope

- Pushing numbered levels (`DetailRoute.NavigationLevel`) to any depth, back one level, and pop to root.
- A `ModalBottomSheet` that opens half-way and can be dragged to full height.
- A full-screen `Dialog` with a close button.
- An explanation of the one-, two-, and three-pane layouts.

### Out of scope

- Navigation libraries, deep links, and bottom navigation bars.
- Animated transitions between levels.

## Behavior

- Each pushed level shows its number and offers **Push level N+1**, **Back one level**, and **Pop to root**.
- **Pop to root** returns to the Navigation topic screen in one step.
- Selecting another topic clears the pushed levels.
- The sheet closes by dragging, tapping outside, Back, or its **Close** button; the dialog by its close button or Back.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Navigation** catalogue item (`FeatureRoute.NavigationPatterns`).
- Four cards: **Push and pop**, **Bottom sheet**, **Full-screen dialog**, **Adaptive panes**. Pushed levels appear in their own pane titled *Level N*.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- The topic screen never navigates itself: it calls `onPush`, and `ContentView` owns the stack.

## Platform limitations

- Whether the sheet opens half-way depends on the window height.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| NAV-AC-01 | Tap **Push level 1**, then **Push level 2** | Level 2 is shown; Back returns to level 1. |
| NAV-AC-02 | On level 3, tap **Pop to root** | The Navigation topic screen is shown. |
| NAV-AC-03 | Open the bottom sheet | It opens part-way and can be dragged up. |
| NAV-AC-04 | Select another topic while on level 2 | The stack is cleared and the new topic shows its root. |

## Technical implementation constraints

- Source lives in `features/compose/navigation/`.
- `FeatureRoute.NavigationPatterns` maps to `NavigationPatternsScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.NavigationPatterns`.
- No new dependencies.

## Related documents

- [Detailed design](navigation_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
