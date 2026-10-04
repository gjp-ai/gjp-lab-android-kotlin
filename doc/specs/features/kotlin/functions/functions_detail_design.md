# Functions & lambdas detailed design

Status: Implemented

Requirements: [Functions & lambdas](functions_requirement.md)

## Implementation goal

Each sample is a private function in `functions/FunctionsSamples.kt` whose body is the code shown on screen; `FunctionsScreen` passes `FunctionsSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`FunctionsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/functions/FunctionsScreen.kt) | Introduction and previews |
| [`FunctionsSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/functions/FunctionsSamples.kt) | Samples in display order (Named and default arguments, vararg, Lambdas and trailing lambdas, Capturing values, Function references and higher-order functions) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Functions` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `FunctionsScreen` |

## Ownership and state

- `FunctionsSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.Functions`; pushes nothing.

## Local functions

Every helper is a local function inside the sample function, so the snippet is complete and runnable as shown.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `lambdasKeepTheirOwnCapturedState`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: FUN-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
