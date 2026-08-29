# Compose Material 3 review guide

Review the requested code without editing unless the user also asks for changes.

## Inspect

- Trace the screen from its navigation entry and state owner through the composables and event callbacks.
- Inspect theme tokens, component semantics, all reachable UI states, insets, relevant previews/tests, and behavior at supported sizes.
- Distinguish correctness or usability problems from personal style preferences.

## Report

Report only actionable findings, ordered by impact:

- **High:** broken behavior, inaccessible primary flow, data/state loss, unusable layout, or lifecycle bug.
- **Medium:** misleading hierarchy, missing important state, weak semantics, non-adaptive layout in supported contexts, or difficult-to-test coupling.
- **Low:** contained inconsistency that has a clear maintenance or usability cost.

For each finding, identify the exact code, user-visible consequence, triggering condition, and smallest safe remediation. If no material findings remain, say so and mention any verification gap.

## Review boundaries

- Do not require a ViewModel, navigation framework, custom design system, adaptive library, or dependency solely because it is common elsewhere.
- Do not flag intentional project conventions unless they cause a concrete issue in the requested scope.
- Do not propose broad redesigns when a targeted component, state, semantics, or layout change resolves the problem.
