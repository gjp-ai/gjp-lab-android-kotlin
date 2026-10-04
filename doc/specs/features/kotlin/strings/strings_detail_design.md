# Strings & regex detailed design

Status: Implemented

Requirements: [Strings & regex](strings_requirement.md)

## Implementation goal

Each sample is a private function in `strings/StringsRegexSamples.kt` whose body is the code shown on screen; `StringsRegexScreen` passes `StringsRegexSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`StringsRegexScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/strings/StringsRegexScreen.kt) | Introduction and previews |
| [`StringsRegexSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/strings/StringsRegexSamples.kt) | Samples in display order (Characters and Unicode, Raw strings and trimIndent, Regex and named groups, Building strings, Comparing strings) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.StringsRegex` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `StringsRegexScreen` |

## Ownership and state

- `StringsRegexSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.StringsRegex`; pushes nothing.

## UTF-16, not characters

Unlike Swift's `String.count`, Kotlin's `length` counts UTF-16 units. The Unicode sample shows an accent typed as one or two code points and an emoji that takes four units, so readers see why `length` is not "number of characters".

## Snippets with raw strings

The raw-string sample's snippet contains `"""`, which would end the surrounding raw string, so that one snippet is written as an ordinary concatenated string.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `stringsCountUtf16UnitsNotCharacters`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: STR-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
