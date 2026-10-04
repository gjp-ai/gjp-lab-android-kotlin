# Runnable code sample detailed design

Status: Implemented

Used by: every topic in the **Kotlin** category ([requirements](../../features/kotlin/)).

## Implementation goal

Show a piece of Kotlin code next to the output it really produces. A sample is a value that holds the code text and the function that is that code; a shared card shows it, runs it on request, and shows the output.

## Source map

| Source | Responsibility |
| --- | --- |
| [`CodeSample.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSample.kt) | `CodeSample` (title, explanation, code text, suspend `run` function, `output()`) and `SampleLog` |
| [`CodeSampleCard.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | `CodeSamplePage` (a `LabDemoPage` of cards), `CodeSampleCard` (code, **Run**, output), and `CodeSampleTestTags` |
| [`LabDemoSection.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabDemoSection.kt) | The page and card layout the sample views build on |

## Ownership and state

- `CodeSample` is immutable. `run` is `suspend (SampleLog) -> Unit`, so a sample can be ordinary or suspending; each topic passes a lambda that calls a private function (`run = { lists(it) }`).
- `SampleLog` collects lines through `invoke`, so sample code reads `log("…")` like `println("…")`. It is synchronized because coroutine samples may log from `Dispatchers.Default`. A new log is created for every run.
- `CodeSampleCard` owns `output` (`List<String>?`, `null` until the first run) and `isRunning` with `remember`, and `runCount` with `rememberSaveable`. Output is discarded when the topic closes.

## Run flow

1. **Run** increments `runCount`.
2. `LaunchedEffect(runCount)` sets `isRunning`, calls `sample.output()`, stores the lines, and clears `isRunning`.
3. The effect is tied to the card, so leaving the topic cancels a running sample. **Run** is disabled while a run is in progress, so runs never overlap, and each new run replaces the previous output.

## Layout

- Code: `bodySmall` monospace, selectable, `softWrap = false` inside a horizontal scroll on `surfaceContainer`, so lines keep their breaks and scroll sideways at large font sizes.
- **Run**: a filled `Button`, with a spinner and "Running…" while running.
- Output: an "Output" label, then the lines joined by newlines in monospace, inside a hairline `outlineVariant` border; "Tap Run to see the output" before the first run. The output text is a polite live region, so TalkBack reads it when a run finishes.
- UI-test tags: `codeSample.run` (each **Run** button) and `codeSample.output` (each output text once shown).

## Keeping code and output in sync

Each topic's `<Topic>Samples` object stores the snippet as a raw string (`"""…""".trimIndent()`) next to a private function whose body is the same code. Inside a raw string `$` starts a template, so the snippet writes `${'$'}` where the code has `$`. Enums, sealed types, interfaces, extension properties, and inline or infix functions cannot be declared inside a function, so they live at file level as `private` declarations and the snippet shows them above the code that uses them. A snippet that itself contains a raw string is written as an ordinary string instead.

`KotlinTopicTest.everySnippetMatchesTheFunctionThatRuns` reads each topic's source file, extracts the body of every `private fun name(log: SampleLog)` in file order, and checks that each sample's code ends with the matching body. A snippet and its function cannot drift apart without failing the build's unit tests.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet text is still written twice | Edits must be made in two places, although the test catches a mismatch | Generate the snippet from the function at build time |
| No syntax highlighting | Code is harder to scan | Colour keywords with an `AnnotatedString` using theme roles |
| The live region reads the whole output | Long outputs are tedious to hear | Announce "Output updated, N lines" and let users read the text |

## Verification

- Build and test with the commands in [application architecture](../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function); `KotlinTopicsTest.runShowsTheSampleOutput` taps **Run** and reads `codeSample.output`.
- Manual: run a Coroutines sample and go back before it finishes; reopen the topic and confirm no output is shown.
