---
name: android-data-concurrency
description: Design, implement, migrate, or review Android data layers using repositories, local and remote data sources, Room, DataStore, Kotlin coroutines, Flow, caching, synchronization, and error handling. Use for persistence, networking, offline behavior, background synchronization, source-of-truth decisions, or concurrency bugs. Do not use for UI-only or manifest/permission-only work.
metadata:
  version: "0.1.0"
---

# Android Data and Concurrency

Produce a data path with an explicit source of truth, predictable threading and cancellation, and error behavior the caller can handle. Fit the project's installed stack rather than assuming Room, Retrofit, or an offline-first design.

## Discover the host project

Before editing data or concurrency code:

- Read repository instructions, Gradle dependencies, models, repositories, local and remote sources, dispatcher conventions, serialization, dependency injection, callers, and tests.
- Identify the source of truth, freshness requirements, offline expectations, mutation ownership, error contract, cancellation boundary, and data sensitivity.
- Determine whether each operation is one-shot, observable, durable, or deferrable before choosing `suspend`, `Flow`, or scheduled work.
- Preserve existing public contracts unless changing them is necessary for the requested outcome.

## Select a mode

- **Repository, persistence, or network work:** Read [the repository workflow](references/repository-workflow.md).
- **Offline or synchronization work:** Also read [the offline and sync guide](references/offline-sync.md).
- **Review or concurrency diagnosis:** Read [the review guide](references/review.md). Diagnose first; edit only when asked to fix.

## Completion contract

- Keep blocking work off the main thread and make public data APIs safe for their documented caller context.
- Preserve structured concurrency and cancellation; never convert cancellation into an ordinary failure.
- Expose one authoritative value for mutable data and define cache freshness or conflict behavior when multiple sources exist.
- Map transport and storage details at boundaries; do not leak mutable or SDK-specific objects without intent.
- Use deterministic fakes for automated tests and report any behavior that still depends on a live service or device.
