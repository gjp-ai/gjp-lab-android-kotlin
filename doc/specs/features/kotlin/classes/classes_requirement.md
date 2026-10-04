# Feature: Classes, data & sealed

Status: Implemented

## Goal

Show how Kotlin models state with classes, values with data classes, and fixed sets of cases with enums and sealed types.

## Scope

### In scope

- Classes with properties and a private setter.
- Data classes: equality, `copy`, `toString`, and destructuring.
- Independent data copies versus shared class references (`===`).
- Enums and exhaustive `when`.
- Sealed interfaces and `data object`.

### Out of scope

- Inheritance hierarchies and abstract classes.
- Value classes (`@JvmInline`).

## Behavior

- Opening the topic shows every sample with its code visible and an empty output area ("Tap Run to see the output").
- Tapping **Run** executes that sample's Kotlin code and shows the lines it logs below the code. Running again replaces the output.
- Output comes from executing the code, never from hard-coded text, and is discarded when the user leaves the topic.

## UI & navigation

- Entry point: **Kotlin** category → **Classes, data & sealed** catalogue item (`FeatureRoute.Classes`).
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
| CLS-AC-01 | Open the topic | Every sample shows its code, an enabled **Run** button, and an empty output area. |
| CLS-AC-02 | Tap **Run** twice on a sample | Output appears after the first tap and is replaced, not appended, after the second. |
| CLS-AC-03 | Run Copies and shared references | The data copy changes alone; the class alias changes the original; `same instance: true`. |
| CLS-AC-04 | Run Sealed types | Each payment kind is described without an `else` branch. |
| CLS-AC-05 | Largest font size, then dark theme | Explanations and output wrap; code keeps its line breaks and scrolls sideways; everything stays readable. |
| CLS-AC-06 | Unit tests | Every sample runs, logs the same output twice, and matches its snippet; key lines are checked. |

## Technical implementation constraints

- Source lives in `features/kotlin/classes/`.
- `FeatureRoute.Classes` maps to `ClassesScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Classes` in the **Kotlin** category (id `kotlin`).
- The page and cards come from the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md); the topic supplies only its `CodeSample` list.
- No new dependencies and no view models.

## Related documents

- [Detailed design](classes_detail_design.md)
- [Runnable code sample](../../../common/codesample/codesample_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
