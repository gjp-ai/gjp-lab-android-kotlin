# Feature: Text & input

Status: Implemented

## Goal

Show how Compose displays styled and formatted text and how text fields hold input, move focus, and validate, using a small sign-up form.

## Scope

### In scope

- `buildAnnotatedString` with bold, italic, and code styles; locale-aware currency and date formatting; `maxLines` with an ellipsis toggle.
- A sign-up form: name, email, and password fields chained with `FocusRequester` and keyboard actions.
- Validation messages and a disabled submit button until the form is valid.
- A growing multi-line field with a 140-character limit and counter.

### Out of scope

- Rich text editing and autofill services.
- Sending the form anywhere.

## Behavior

- The keyboard's Next action moves focus Name → Email → Password; Done on Password submits.
- Validation rules (in `SignUpForm`): name not blank; email matches `x@y.z` without spaces; password at least 8 characters.
- Submitting a valid form clears focus and shows a confirmation naming the user; nothing leaves the device.
- Typing past 140 characters in the bio is trimmed to 140.

## UI & navigation

- Entry point: **Jetpack Compose** category → **Text & input** catalogue item (`FeatureRoute.TextInput`).
- Three cards: **Styled and formatted text**, **Sign-up form**, **Multi-line input with a limit**.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public Jetpack Compose and Material 3 APIs from the project's Compose BOM; experimental APIs are opted in per file.
- Sample data stays in memory; nothing is persisted, sent over the network, or logged.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.
- The password field hides its characters with `PasswordVisualTransformation`.
- The email field uses the email keyboard; the name field capitalizes words.

## Platform limitations

- Keyboard layout and the actions shown on it depend on the installed keyboard app.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| TXT-AC-01 | Open the screen | Three validation messages are shown and **Create account** is disabled. |
| TXT-AC-02 | Enter a valid name, email, and 8-character password | Messages disappear and the button is enabled. |
| TXT-AC-03 | Press the keyboard action in each field | Focus moves Name → Email → Password, then the form submits. |
| TXT-AC-04 | Type more than 140 characters in the bio | Text stops at 140 and the counter reads 140 of 140. |

## Technical implementation constraints

- Source lives in `features/compose/textinput/`.
- `FeatureRoute.TextInput` maps to `TextInputScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.TextInput`.
- No new dependencies.

## Related documents

- [Detailed design](textinput_detail_design.md)
- [Slate design system](../../../common/theme/theme_detail_design.md) (`LabDemoPage` and `LabDemoSection`)
- [Application architecture](../../../../architecture/application.md)
