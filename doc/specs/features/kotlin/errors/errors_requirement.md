# Feature: Error handling

Status: Implemented

## Goal

Show how Kotlin reports and handles failures with exceptions, `Result`, preconditions, and automatic resource closing.

## Scope

### In scope

- `try`/`catch` as an expression.
- Custom exception types.
- `runCatching` and `Result`.
- Preconditions with `require`.
- `use` closing an `AutoCloseable` on success and failure.

### Out of scope

- Checked exceptions (Kotlin has none).
- Coroutine exception handlers.

## Behavior

- Opening the topic shows every sample with its code visible and an empty output area ("Tap Run to see the output").
- Tapping **Run** executes that sample's Kotlin code and shows the lines it logs below the code. Running again replaces the output.
- Output comes from executing the code, never from hard-coded text, and is discarded when the user leaves the topic.
- Every failure is caught inside the sample; no sample crashes the app.

## UI & navigation

- Entry point: **Kotlin** category → **Error handling** catalogue item (`FeatureRoute.ErrorHandling`).
- A one-line introduction, then one card per sample with a one-line explanation, the code in a monospaced font (selectable, scrolling sideways instead of wrapping), a **Run** button, and an output area.
- TalkBack reads the output when a run finishes.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Sample code compiles with the project's Kotlin version and runs on the app's minimum SDK.
- The code shown is the code that runs: each sample is a private function in the topic folder, stored next to the snippet text, and a unit test checks they match.
- No sample crashes, hangs, blocks the main thread, calls the network, writes files, or logs user data.
- Colours come from `MaterialTheme.colorScheme` roles; previews come in light and dark pairs.

## Platform limitations

- None.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| ERR-AC-01 | Open the topic | Every sample shows its code, an enabled **Run** button, and an empty output area. |
| ERR-AC-02 | Tap **Run** twice on a sample | Output appears after the first tap and is replaced, not appended, after the second. |
| ERR-AC-03 | Run use and finally | For an empty name, `close` is logged before `failed: empty`. |
| ERR-AC-04 | Run runCatching and Result | Division by zero is reported as `ArithmeticException` and `getOrDefault` returns 0. |
| ERR-AC-05 | Largest font size, then dark theme | Explanations and output wrap; code keeps its line breaks and scrolls sideways; everything stays readable. |
| ERR-AC-06 | Unit tests | Every sample runs, logs the same output twice, and matches its snippet; key lines are checked. |

## Technical implementation constraints

- Source lives in `features/kotlin/errors/`.
- `FeatureRoute.ErrorHandling` maps to `ErrorHandlingScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.ErrorHandling` in the **Kotlin** category (id `kotlin`).
- The page and cards come from the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md); the topic supplies only its `CodeSample` list.
- No new dependencies and no view models.

## Related documents

- [Detailed design](errors_detail_design.md)
- [Runnable code sample](../../../common/codesample/codesample_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
