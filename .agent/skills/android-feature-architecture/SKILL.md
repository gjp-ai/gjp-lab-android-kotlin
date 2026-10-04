---
name: android-feature-architecture
description: Design, implement, migrate, or review Android feature architecture, including UI state, events, state holders, navigation, lifecycle persistence, dependency injection, and module boundaries. Use when adding an end-to-end feature, changing state ownership, introducing a ViewModel, changing navigation, or restructuring feature wiring. Do not use for UI styling-only or data-source-only work.
metadata:
  version: "0.1.0"
---

# Android Feature Architecture

Build a feature that fits the host app and makes state ownership, events, lifecycle behavior, and boundaries explicit. Do not impose a preferred architecture merely because it is common elsewhere.

## Discover the host project

Before making architecture decisions:

- Read repository instructions, module boundaries, the closest existing feature, application and activity setup, navigation, dependency injection, state holders, lifecycle handling, and relevant tests.
- Identify the user outcome, current state owner, sources of truth, event paths, process or configuration-change requirements, and public contracts that must remain compatible.
- Preserve established patterns unless the requested outcome exposes a concrete limitation or the user asks for migration.
- Add dependencies or layers only when they solve a demonstrated requirement.

## Select a mode

- **Build or extend a feature:** Read [the feature workflow](references/feature-workflow.md).
- **Migrate architecture:** Read [the migration guide](references/migration.md). Use this only for an explicit migration or a limitation that cannot be addressed safely in place.
- **Review architecture:** Read [the review guide](references/review.md). Report findings without editing unless asked.

## Completion contract

- Give every mutable value one clear owner and expose immutable state across boundaries.
- Make user events and one-time effects distinguishable from durable screen state.
- Preserve lifecycle, back-stack, saved-state, deep-link, and dependency-scope behavior relevant to the change.
- Keep UI, domain, and data boundaries proportional to the app; avoid pass-through abstractions.
- Verify the smallest relevant compile path and tests, then report unverified lifecycle or navigation risk.
