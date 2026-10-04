# Classes, data & sealed detailed design

Status: Implemented

Requirements: [Classes, data & sealed](classes_requirement.md)

## Implementation goal

Each sample is a private function in `classes/ClassesSamples.kt` whose body is the code shown on screen; `ClassesScreen` passes `ClassesSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`ClassesScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/classes/ClassesScreen.kt) | Introduction and previews |
| [`ClassesSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/classes/ClassesSamples.kt) | Samples in display order (Classes and properties, Data classes, Copies and shared references, Enums and when, Sealed types) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Classes` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `ClassesScreen` |

## Ownership and state

- `ClassesSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.Classes`; pushes nothing.

## File-level declarations

Kotlin does not allow enums or sealed types inside a function, so `Planet`, `Payment`, `Card`, `Cash`, and `Voucher` are private file-level declarations. The snippet shows them above the code that uses them.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `dataCopiesAreIndependentAndClassesAreShared`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: CLS-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
