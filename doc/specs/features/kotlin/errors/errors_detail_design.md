# Error handling detailed design

Status: Implemented

Requirements: [Error handling](errors_requirement.md)

## Implementation goal

Each sample is a private function in `errors/ErrorHandlingSamples.kt` whose body is the code shown on screen; `ErrorHandlingScreen` passes `ErrorHandlingSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`ErrorHandlingScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/errors/ErrorHandlingScreen.kt) | Introduction and previews |
| [`ErrorHandlingSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/errors/ErrorHandlingSamples.kt) | Samples in display order (try/catch as an expression, Custom exceptions, runCatching and Result, Preconditions with require, use and finally) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.ErrorHandling` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `ErrorHandlingScreen` |

## Ownership and state

- `ErrorHandlingSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.ErrorHandling`; pushes nothing.

## use closes first

`use` closes the resource in its own `finally`, before the exception leaves the block, so `close ""` is logged before the outer `catch` logs `failed: empty`. Swift's `defer` in the iOS lab runs at the end of the scope instead, so the order differs.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `useClosesBeforeTheExceptionIsCaught`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: ERR-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
