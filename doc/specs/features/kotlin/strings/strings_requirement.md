# Feature: Strings & regex

Status: Implemented

## Goal

Show how Kotlin strings store Unicode text, and the tools for building, matching, and comparing it.

## Scope

### In scope

- UTF-16 length versus code points versus UTF-8 bytes.
- Raw strings and `trimIndent`.
- `Regex` with named groups and `findAll`.
- `buildString`.
- Comparing strings with `==`, Unicode normalization, and `ignoreCase`.

### Out of scope

- Grapheme cluster segmentation (`BreakIterator`).
- Localization and plurals.

## Behavior

- Opening the topic shows every sample with its code visible and an empty output area ("Tap Run to see the output").
- Tapping **Run** executes that sample's Kotlin code and shows the lines it logs below the code. Running again replaces the output.
- Output comes from executing the code, never from hard-coded text, and is discarded when the user leaves the topic.

## UI & navigation

- Entry point: **Kotlin** category → **Strings & regex** catalogue item (`FeatureRoute.StringsRegex`).
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
| STR-AC-01 | Open the topic | Every sample shows its code, an enabled **Run** button, and an empty output area. |
| STR-AC-02 | Tap **Run** twice on a sample | Output appears after the first tap and is replaced, not appended, after the second. |
| STR-AC-03 | Run Characters and Unicode | The thumbs-up with a skin tone has length 4, 2 code points, and 8 UTF-8 bytes. |
| STR-AC-04 | Run Comparing strings | `==` is false for the two spellings of café, and true after NFC normalization. |
| STR-AC-05 | Largest font size, then dark theme | Explanations and output wrap; code keeps its line breaks and scrolls sideways; everything stays readable. |
| STR-AC-06 | Unit tests | Every sample runs, logs the same output twice, and matches its snippet; key lines are checked. |

## Technical implementation constraints

- Source lives in `features/kotlin/strings/`.
- `FeatureRoute.StringsRegex` maps to `StringsRegexScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.StringsRegex` in the **Kotlin** category (id `kotlin`).
- The page and cards come from the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md); the topic supplies only its `CodeSample` list.
- No new dependencies and no view models.

## Related documents

- [Detailed design](strings_detail_design.md)
- [Runnable code sample](../../../common/codesample/codesample_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
