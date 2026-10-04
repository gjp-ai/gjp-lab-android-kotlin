# Architecture review

Review observable correctness and maintainability risks, not personal style preferences.

## Inspect

- Trace state from each source of truth to rendering and events back to the responsible owner.
- Check for duplicated mutable state, invalid state combinations, stale captures, unbounded event replay, and one-time effects encoded as durable state.
- Check configuration changes, process recreation assumptions, back navigation, deep links, route arguments, and saved-state compatibility.
- Verify dependency scopes and construction boundaries. Flag framework, platform, or data implementations leaking into UI contracts.
- Look for layers that contain policy and transformations versus layers that only forward calls.
- Inspect tests for meaningful state transitions, navigation contracts, and lifecycle behavior.

## Report

- Lead with concrete findings ordered by user impact and regression risk.
- Cite the affected path and explain the execution path that causes the issue.
- Distinguish correctness defects from optional simplification.
- If no material issue is found, state that and identify any lifecycle or navigation path that was not verifiable.
