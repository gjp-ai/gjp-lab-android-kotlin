# Text & input detailed design

Status: Implemented, with known gaps

Requirements: [Text & input](textinput_requirement.md)

## Implementation goal

Keep the form's rules in a plain data class (`SignUpForm`) that the screen copies on every edit, so validation is testable without Compose; chain focus with one `FocusRequester` per field.

## Source map

| Source | Responsibility |
| --- | --- |
| [`TextInputScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/textinput/TextInputScreen.kt) | Screen, keyboard options and actions, focus chain, and the bio limit (`BioLimit`) |
| [`SignUpForm.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/compose/textinput/SignUpForm.kt) | Form values, `problems`, and `isValid` |
| [`LabDemoSection.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | `LabDemoPage` (scrolling page) and `LabDemoSection` (titled card) |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.TextInput` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Jetpack Compose catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `TextInputScreen` for the route |

## Ownership and state

- `form` (`SignUpForm`) is held with `remember`; `confirmation`, `bio`, and the line-limit switch use `rememberSaveable`.
- Two `FocusRequester`s (email, password) and the `LocalFocusManager` move and clear focus.
- Reached from `FeatureRoute.TextInput`; pushes nothing.

## Focus chain

Each field sets `imeAction` (`Next` or `Done`) and a matching `KeyboardActions` callback: Name's `onNext` requests focus on Email, Email's on Password, and Password's `onDone` calls `submit()`, which clears focus and shows the confirmation only if `form.isValid`.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Form values are not saved | Rotating the device clears the sign-up fields | Save name, email, and password separately with `rememberSaveable` |
| Messages show before the user types | A new form opens with three errors | Show a field's message only after it has been edited |
| No autofill hints | Password managers cannot fill the form | Add `contentType` semantics for username and password |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `ComposeFeatureTest` checks `SignUpForm` (empty form, valid form, invalid emails, blank name and short password); `ComposeTopicsTest.everyComposeTopicOpens` opens the screen.
- Previews: light and dark pairs in `TextInputScreen.kt`.
- Manual: TXT-AC-01 to TXT-AC-04 on a phone emulator in light and dark themes and at the largest font size.
