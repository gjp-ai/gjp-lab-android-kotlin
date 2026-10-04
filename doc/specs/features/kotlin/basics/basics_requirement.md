# Feature: Values & types

Status: Implemented

## Goal

Show how Kotlin declares values, infers their types, handles number limits, builds strings, and branches with when.

## Scope

### In scope

- `val` versus `var`.
- Type inference and explicit number conversion (`toDouble()`).
- Int overflow wrapping, `Math.addExact`, and `Long`.
- String templates with `$name` and `${…}`.
- Pairs, destructuring, and `when` as an expression.

### Out of scope

- Unsigned types and `BigInteger`.
- Reflection beyond `::class.simpleName`.

## Behavior

- Opening the topic shows every sample with its code visible and an empty output area ("Tap Run to see the output").
- Tapping **Run** executes that sample's Kotlin code and shows the lines it logs below the code. Running again replaces the output.
- Output comes from executing the code, never from hard-coded text, and is discarded when the user leaves the topic.
- The overflow sample shows the wrapped value and the exception type; it never crashes.

## UI & navigation

- Entry point: **Kotlin** category → **Values & types** catalogue item (`FeatureRoute.KotlinBasics`).
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
| VAL-AC-01 | Open the topic | Every sample shows its code, an enabled **Run** button, and an empty output area. |
| VAL-AC-02 | Tap **Run** twice on a sample | Output appears after the first tap and is replaced, not appended, after the second. |
| VAL-AC-03 | Run Type inference | Output shows `42: Int`, `3.5: Double`, and `42.0: Double`. |
| VAL-AC-04 | Run Overflow | Output shows `Int.MAX_VALUE + 1 = -2147483648` and `Math.addExact: ArithmeticException`. |
| VAL-AC-05 | Largest font size, then dark theme | Explanations and output wrap; code keeps its line breaks and scrolls sideways; everything stays readable. |
| VAL-AC-06 | Unit tests | Every sample runs, logs the same output twice, and matches its snippet; key lines are checked. |

## Technical implementation constraints

- Source lives in `features/kotlin/basics/`.
- `FeatureRoute.KotlinBasics` maps to `KotlinBasicsScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.KotlinBasics` in the **Kotlin** category (id `kotlin`).
- The page and cards come from the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md); the topic supplies only its `CodeSample` list.
- No new dependencies and no view models.

## Related documents

- [Detailed design](basics_detail_design.md)
- [Runnable code sample](../../../common/codesample/codesample_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
