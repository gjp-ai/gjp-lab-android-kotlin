# Extensions & scope functions detailed design

Status: Implemented

Requirements: [Extensions & scope functions](extensions_requirement.md)

## Implementation goal

Each sample is a private function in `extensions/ExtensionsSamples.kt` whose body is the code shown on screen; `ExtensionsScreen` passes `ExtensionsSamples.all` to the shared [runnable code sample](../../../common/codesample/codesample_detail_design.md) page, which runs a sample when the user taps **Run**.

## Source map

| Source | Responsibility |
| --- | --- |
| [`ExtensionsScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/extensions/ExtensionsScreen.kt) | Introduction and previews |
| [`ExtensionsSamples.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/kotlin/extensions/ExtensionsSamples.kt) | Samples in display order (Extension functions, Extension properties, let and also, apply and run, with and infix functions, Operator overloading) |
| [`CodeSampleCard.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/common/codesample/CodeSampleCard.kt) | Shared page, card, and run flow |
| [`FeatureRoute.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | `FeatureRoute.Extensions` |
| [`NavigationMenu.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Kotlin catalogue topic |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the route to `ExtensionsScreen` |

## Ownership and state

- `ExtensionsSamples.all` is an immutable list. Each `CodeSample` stores the snippet text and a lambda that calls the matching private function with a fresh `SampleLog`.
- The screen owns no state; each card owns its own output and running flag, discarded when the topic closes. Nothing is persisted, sent, or logged outside the sample output.
- Reached from `FeatureRoute.Extensions`; pushes nothing.

## Why this topic replaces Memory management

The iOS lab's ninth topic teaches ARC, retain cycles, and `weak`. The JVM uses a tracing garbage collector with no reference counting, and when it collects is not deterministic, so a runnable sample could not show reliable output. Extensions and scope functions are central to idiomatic Kotlin instead.

## File-level declarations

Extension properties and infix functions cannot be local, so `isEven` and `percentOf` are private file-level declarations shown above the code that uses them.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Snippet and function body are maintained by hand | Edits are made twice, although `KotlinTopicTest` fails if they differ | See the shared [code sample known gaps](../../../common/codesample/codesample_detail_design.md#known-gaps) |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Automated: `KotlinTopicTest` runs every sample (non-empty, same output twice, snippet matches its function) and checks key lines in `extensionsAndScopeFunctions`; `KotlinTopicsTest.everyKotlinTopicOpens` opens the screen from the catalogue.
- Manual: EXT-AC-01 to the last acceptance criterion on a phone emulator in light and dark themes and at the largest font size.
