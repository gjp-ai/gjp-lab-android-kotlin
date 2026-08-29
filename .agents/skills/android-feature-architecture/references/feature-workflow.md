# Feature workflow

Use this workflow for a new feature or a contained extension to an existing feature.

## Define the vertical slice

- Describe the user-visible entry point, successful outcome, recoverable failures, and exit or back behavior.
- Identify which existing modules and components own the feature's UI, state, data access, navigation, and dependencies.
- Prefer extending a nearby pattern over creating a parallel architecture.

## Model state and events

- Represent only states the product can actually reach. Make mutually exclusive states explicit when that prevents invalid combinations.
- Keep durable screen state separate from transient effects such as navigation, snackbars, or permission launches.
- Expose immutable state and named user events. Avoid callbacks that leak framework or data-layer details across boundaries.
- Place state in the lowest owner that satisfies sharing and lifecycle requirements: composable-local, activity/fragment, state holder, or ViewModel.
- Use saved state only for small information needed to reconstruct the experience; reload durable data from its source of truth.

## Wire boundaries

- Define navigation inputs and results as stable contracts. Preserve back-stack behavior, deep links, and argument compatibility.
- Scope dependencies to their actual lifetime. Keep construction in the host project's composition root or dependency-injection mechanism.
- Keep platform and data implementations behind their existing boundaries. Introduce a domain layer only when it contains reusable policy or meaningful transformation.
- Package by feature when the host project does so; do not move unrelated code to make the new structure look uniform.

## Implement and verify

1. Establish contracts and state transitions.
2. Wire dependencies and data sources.
3. Connect the UI and navigation entry points.
4. Add tests at the cheapest boundary that proves state transitions and critical lifecycle behavior.
5. Compile the affected variant and exercise configuration change, back navigation, and state restoration when relevant.
