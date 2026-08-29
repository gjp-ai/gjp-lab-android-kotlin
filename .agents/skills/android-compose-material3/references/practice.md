# Practice guide

Improve the skill from evidence. A successful app change is a practice sample; it is not automatically a universal rule.

## Select a representative exercise

Prefer a real request that exercises one primary capability:

- build a screen or reusable component from an existing requirement;
- refactor an existing Compose screen to improve Material hierarchy or accessibility;
- add loading, empty, error, permission, or post-action feedback;
- review a screen and produce prioritized, evidence-backed findings;
- adapt navigation or content to more than one window size.

Keep the exercise contained. Record the starting behavior, one observable outcome, the host architecture, and the checks available before work begins.

## Evaluate the result

Score each applicable dimension from 0 to 2:

- **Fit:** preserves requested behavior and the host project's boundaries.
- **Material hierarchy:** components, colors, type, and shapes communicate intent semantically.
- **State:** state ownership, events, persistence, and side effects are appropriate.
- **Accessibility:** semantics, non-color cues, touch behavior, traversal, and text scaling are handled.
- **Layout:** insets and applicable window sizes are handled without clipping or wasted space.
- **Verification:** previews, tests, builds, and device checks are proportional to risk.

For a review task, replace implementation scoring with finding quality: correctness, evidence, prioritization, and actionable remediation.

## Revise carefully

- Identify the exact decision where the skill helped, failed, or lacked context.
- Change the reusable skill only when the lesson applies beyond the current repository. Put local architecture or command details in that repository's `AGENTS.md` or equivalent.
- Prefer a narrow correction to adding another universal rule. Remove guidance that causes overreach or duplicates agent knowledge.
- At handoff, report the observed lesson and whether it changed the skill. Do not create a separate learning-log file unless requested.
