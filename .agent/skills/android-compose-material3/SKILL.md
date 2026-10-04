---
name: android-compose-material3
description: Design, implement, refactor, or review Android interfaces built with Jetpack Compose and Material Design 3. Use for Compose screens, themes, forms, UI states, navigation surfaces, accessibility, adaptive layouts, previews, and UI tests. Do not use for XML/View-only UI, backend-only work, or architecture changes without a UI outcome.
metadata:
  version: "0.2.0"
---

# Android Compose Material 3

Produce a coherent, accessible Material 3 result that fits the host app. The repository's instructions and installed dependencies define the architecture and available APIs; this skill supplies the reusable UI workflow and quality bar.

## Discover the host project

Before proposing or editing UI:

- Read the repository instructions, Gradle configuration or version catalog, theme entry point, target screen, state owner, navigation entry point, previews, and relevant tests.
- Identify the observable user outcome, states that must be represented, behavior that must remain unchanged, and supported device/window scope.
- Use APIs available in the installed dependency versions. Verify uncertain or recently changed APIs in official Android documentation rather than adding a dependency speculatively.
- Preserve the project's established state, navigation, dependency-injection, and data boundaries unless the user explicitly requests an architecture change.

## Select a mode

- **Build or refactor:** Read [the implementation workflow](references/material3-workflow.md), then implement and verify the requested UI outcome.
- **Review or audit:** Read [the review guide](references/review.md). Report evidence-backed findings; do not edit unless asked.
- **Adaptive UI:** Also read [the adaptive-layout guide](references/adaptive.md) when the request involves tablets, foldables, desktop windows, orientation changes, multi-pane layouts, or adaptive navigation.
- **Practice or skill improvement:** Read [the practice guide](references/practice.md). Use a representative task and revise this skill only from observed, reusable lessons.

## Completion contract

- Keep rendering declarative: state flows down and events flow up at the boundaries appropriate to the host project.
- Use semantic theme roles and standard Material 3 behavior before custom styling or low-level interaction.
- Cover loading, empty, content, error, disabled, and permission states when they are relevant to the feature; do not invent states the product does not need.
- Verify accessibility semantics, text scaling, edge-to-edge/insets, light and dark appearance, and applicable window sizes in proportion to the change.
- Run the smallest relevant build and tests. At handoff, state what changed, what was verified, and any unverified risk.
