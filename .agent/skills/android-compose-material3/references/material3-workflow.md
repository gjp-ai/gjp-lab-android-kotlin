# Material 3 implementation workflow

## Establish the UI contract

Before coding, write down the primary user action, supporting actions, displayed data, relevant UI states, and behavior that must not change. Keep the change small enough to verify. Do not combine a visual refactor with a navigation or state-management migration unless requested.

## Place state and events

- Follow the host project's state holder pattern. Hoist shared state to the lowest owner that needs to read and write it; expose immutable state and explicit events.
- Keep simple, private UI-element state local when no caller needs to control it. Use `rememberSaveable` for user input or navigation-relevant transient state that should survive recreation when appropriate.
- Keep network, database, Firebase, and platform operations outside leaf composables. Trigger work through callbacks or the project's presentation layer.
- Avoid side effects during ordinary composition. Use an appropriate effect API only when the effect is tied to composition lifecycle and keys.

## Build the Material hierarchy

- Use `MaterialTheme.colorScheme`, `typography`, and `shapes`. Prefer semantic pairs such as `primary`/`onPrimary`, `primaryContainer`/`onPrimaryContainer`, `surface`/`onSurface`, and `error`/`onError`.
- Preserve dynamic color when supported. Add or change a central palette only for an intentional product/brand decision.
- Use standard Material 3 components before custom equivalents. Select by interaction intent rather than appearance.

| Intent | Prefer |
| --- | --- |
| Main action | `Button`, or `FloatingActionButton` for a primary creation action |
| Secondary action | `OutlinedButton`, `TextButton`, or tonal button |
| Short selectable option | `FilterChip`, `AssistChip`, or `SuggestionChip` |
| Persistent top-level destination | The project's adaptive navigation component or `NavigationBar`/`NavigationRail` as appropriate |
| Related content with one action | `Card` or `ElevatedCard` |
| User-entered value | `OutlinedTextField` or `TextField` with a label and validation feedback |
| Brief post-action status | `SnackbarHost` |
| Focused secondary task | `ModalBottomSheet` |

## Layout and accessibility

- Apply `Scaffold` content padding once. Handle edge-to-edge system bars and IME insets without stacking duplicate padding.
- Prefer Material/Foundation components because they provide useful semantics. Add custom semantics only when the resulting semantics tree does not express the control correctly.
- Give standalone meaningful icons a concise description. Use `null` for icons whose meaning is already conveyed by surrounding text or merged component semantics.
- Keep controls comfortably tappable, preserve logical traversal, and allow text to wrap or reflow at larger font scales.
- Communicate error, selection, and status with text, iconography, role, or state in addition to color.

## Verify the outcome

- Add or update previews for materially changed UI, including light/dark and representative states. Add size-specific previews when layout behavior changes by width.
- Prefer behavior assertions through semantics in Compose tests. Do not lock tests to incidental layout details or exact implementation structure.
- Exercise the changed interaction on an emulator/device when it involves focus, keyboard, permissions, system bars, navigation, or lifecycle behavior.
- Run the host project's smallest relevant build and test commands and report any check that could not run.
