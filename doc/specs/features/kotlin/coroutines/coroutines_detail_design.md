# Coroutines detailed design

Status: Implemented

Requirements: [Coroutines](coroutines_requirement.md)

## Implementation goal

Each sample is a private function in `coroutines/CoroutinesSamples.kt` whose body is the code shown on screen; `CoroutinesScreen` passes `CoroutinesSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`CoroutinesScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/coroutines/CoroutinesScreen.kt) | Introduction and previews |
| [`CoroutinesSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/coroutines/CoroutinesSamples.kt) | Samples in display order (suspend and delay, async and await, Structured concurrency, Cancellation, Dispatchers and withContext, Mutex) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Coroutines` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `CoroutinesScreen` |

## Ownership and state

- `CoroutinesSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.Coroutines`; pushes nothing.

## Threads and logging

The Dispatchers sample logs from a `Dispatchers.Default` worker. `SampleLog` is synchronized, so lines from background threads are recorded safely. The sample checks the worker's name rather than the main thread, so it gives the same answer in the app and in JVM unit tests.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `coroutineResultsDoNotDependOnTiming`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: CRT-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
