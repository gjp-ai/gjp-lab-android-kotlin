---
name: android-quality-build
description: Diagnose, test, verify, and prepare Android changes using JVM tests, Compose tests, instrumented tests, lifecycle tests, Gradle, dependency management, lint, CI, performance checks, and release validation. Use for bug fixes, regressions, build failures, flaky tests, dependency upgrades, CI failures, or release readiness. Do not use for feature implementation unless verification or build work is the primary task.
metadata:
  version: "0.1.0"
---

# Android Quality and Build

Produce evidence that the requested Android behavior works and that the affected build path remains healthy. Match verification depth to risk, and diagnose before changing code or configuration.

## Discover the host project

Before selecting checks:

- Read repository instructions, Gradle wrapper and settings, plugins or version catalog, modules, build variants, CI configuration, test source sets, lint configuration, and nearby test patterns.
- Identify the failing or changed behavior, smallest reproduction, affected variants and API levels, environment prerequisites, and release impact.
- Preserve the project's build and test conventions. Do not perform broad upgrades or formatting as incidental repair.
- Use official compatibility information for uncertain or recently changed Android, Kotlin, Gradle, and Compose tooling.

## Select a mode

- **Diagnose a failure or regression:** Read [the diagnosis workflow](references/diagnosis.md). Editing is authorized only when the user asks for a fix or implementation.
- **Design or add tests:** Read [the test selection guide](references/test-selection.md).
- **Gradle, dependency, CI, or release work:** Read [the build and release guide](references/gradle-release.md).

## Completion contract

- Reproduce or otherwise establish evidence before selecting a cause; distinguish root cause from secondary failures.
- Add the cheapest deterministic test that fails for the defect and proves the required behavior when practical.
- Run focused checks first, then broaden according to affected modules, variants, API levels, and release risk.
- Never conceal failures by disabling checks, weakening assertions, pinning arbitrary versions, or adding blanket retries.
- Report commands run, outcomes, skipped checks and prerequisites, residual risk, and any environment-specific limitation.
