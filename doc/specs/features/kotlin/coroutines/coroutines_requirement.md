# Feature: Coroutines

Status: Implemented

## Goal

Show how coroutines run asynchronous work that reads top to bottom: suspending, running in parallel, staying structured, cancelling, switching threads, and sharing state safely.

## Scope

### In scope

- `suspend` functions and `delay`.
- `async`/`await` in parallel.
- Structured concurrency with `coroutineScope` and `awaitAll`.
- Cooperative cancellation with `isActive` and `cancelAndJoin`.
- `withContext(Dispatchers.Default)`.
- `Mutex` protecting a shared counter.

### Out of scope

- Flow and channels.
- Android lifecycle scopes (`viewModelScope`, `lifecycleScope`).

## Behavior

- Opening the topic shows every sample with its code visible and an empty output area ("Tap Run to see the output").
- Tapping **Run** executes that sample's Kotlin code and shows the lines it logs below the code. Running again replaces the output.
- Output comes from executing the code, never from hard-coded text, and is discarded when the user leaves the topic.
- Output never depends on which coroutine finishes first: parallel results are sorted, and timing is only compared against a generous limit.
- Samples run in the card's `LaunchedEffect` on the main thread; CPU work moves to `Dispatchers.Default`, so the UI never freezes.
- Leaving the topic cancels a running sample.

## UI & navigation

- Entry point: **Kotlin** category → **Coroutines** catalogue item (`FeatureRoute.Coroutines`).
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
| CRT-AC-01 | Open the topic | Every sample shows its code, an enabled **Run** button, and an empty output area. |
| CRT-AC-02 | Tap **Run** twice on a sample | Output appears after the first tap and is replaced, not appended, after the second. |
| CRT-AC-03 | Run async and await | The total is 140, and three 0.4-second waits overlap. |
| CRT-AC-04 | Run Cancellation | Output shows `stopped early: true`. |
| CRT-AC-05 | Run Mutex | Output shows `tickets sold: 1000`. |
| CRT-AC-06 | Largest font size, then dark theme | Explanations and output wrap; code keeps its line breaks and scrolls sideways; everything stays readable. |
| CRT-AC-07 | Unit tests | Every sample runs, logs the same output twice, and matches its snippet; key lines are checked. |

## Technical implementation constraints

- Source lives in `features/kotlin/coroutines/`.
- `FeatureRoute.Coroutines` maps to `CoroutinesScreen` in `FeatureDestination`; the topic in `shell/navigation/NavigationMenu.kt` carries `route = FeatureRoute.Coroutines` in the **Kotlin** category (id `kotlin`).
- The page and cards come from the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md); the topic supplies only its `CodeSample` list.
- No new dependencies and no view models.

## Related documents

- [Detailed design](coroutines_detail_design.md)
- [Runnable code sample](../../../common/codesample/codesample_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
