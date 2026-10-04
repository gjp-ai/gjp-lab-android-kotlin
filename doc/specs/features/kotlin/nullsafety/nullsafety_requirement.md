# Feature: Null safety

Status: Implemented

## Goal

Show how Kotlin puts null in the type system and the safe ways to work with nullable values.

## Scope

### In scope

- Nullable types (`Int?`) from `toIntOrNull`.
- Smart casts after a null check.
- The Elvis operator `?:`, including `?: return`.
- Safe calls `?.` through nested values and `?.let`.
- The not-null assertion `!!`, explained with a safe alternative.

### Out of scope

- Platform types from Java code.
- `lateinit` and delegated properties.

## Behavior

- Opening the topic shows every sample with its code visible and an empty output area ("Tap Run to see the output").
- Tapping **Run** executes that sample's Kotlin code and shows the lines it logs below the code. Running again replaces the output.
- Output comes from executing the code, never from hard-coded text, and is discarded when the user leaves the topic.
- Each null-handling sample runs once with a value and once with null, so both paths appear in the output.
- The `!!` sample never asserts on null; it explains the exception and uses `?:`.

## UI & navigation

- Entry point: **Kotlin** category → **Null safety** catalogue item (`FeatureRoute.NullSafety`).
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
| NUL-AC-01 | Open the topic | Every sample shows its code, an enabled **Run** button, and an empty output area. |
| NUL-AC-02 | Tap **Run** twice on a sample | Output appears after the first tap and is replaced, not appended, after the second. |
| NUL-AC-03 | Run Elvis and early return | Output shows the normal line for "36" and the early-return line for "abc". |
| NUL-AC-04 | Run Not-null assertion !! | The app does not crash; the output explains what `!!` would do. |
| NUL-AC-05 | Largest font size, then dark theme | Explanations and output wrap; code keeps its line breaks and scrolls sideways; everything stays readable. |
| NUL-AC-06 | Unit tests | Every sample runs, logs the same output twice, and matches its snippet; key lines are checked. |

## Technical implementation constraints

- Source lives in `features/kotlin/nullsafety/`.
- `FeatureRoute.NullSafety` maps to `NullSafetyScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.NullSafety` in the **Kotlin** category (id `kotlin`).
- The page and cards come from the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md); the topic supplies only its `CodeSample` list.
- No new dependencies and no view models.

## Related documents

- [Detailed design](nullsafety_detail_design.md)
- [Runnable code sample](../../../common/codesample/codesample_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
