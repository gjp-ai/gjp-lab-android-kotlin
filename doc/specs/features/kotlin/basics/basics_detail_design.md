# Values & types detailed design

Status: Implemented

Requirements: [Values & types](basics_requirement.md)

## Implementation goal

Each sample is a private function in `basics/BasicsSamples.kt` whose body is the code shown on screen; `KotlinBasicsScreen` passes `BasicsSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`KotlinBasicsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/basics/KotlinBasicsScreen.kt) | Introduction and previews |
| [`BasicsSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/basics/BasicsSamples.kt) | Samples in display order (val and var, Type inference, Overflow, String templates, Pairs, destructuring, and when) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.KotlinBasics` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `KotlinBasicsScreen` |

## Ownership and state

- `BasicsSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.KotlinBasics`; pushes nothing.

## No silent widening

Kotlin never converts `Int` to `Double` implicitly. The Type inference sample keeps the line that would not compile as a comment and uses `toDouble()` instead.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `basicsShowsInferredTypesAndOverflow`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: VAL-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
