# Components and background execution

## Components and intents

- Declare exported state explicitly and expose a component only when another app or the system must reach it.
- Validate incoming actions, data, MIME types, URI grants, extras, and caller assumptions at the boundary.
- Prefer explicit intents for internal navigation. Carry identifiers or minimum required data rather than sensitive objects.
- Use immutable pending intents unless the receiving API requires mutation; scope mutability and intent contents narrowly.
- For app links or deep links, verify host ownership, route validation, authentication transitions, and safe fallback behavior.

## Background work

- Choose lifecycle-owned coroutines for immediate work, scheduled durable work for deferrable guaranteed execution, and foreground services only for qualifying user-visible ongoing work.
- Define cancellation, uniqueness, constraints, retry behavior, idempotency, and persisted inputs before scheduling durable work.
- Follow current target-SDK rules for foreground-service types, notification permission and channels, exact alarms, and background starts when relevant.
- Avoid storing secrets or large payloads in work inputs, intents, notifications, or saved state.

## Verify configuration

- Inspect merged manifests for permissions, exported components, providers, service types, and dependency-added declarations.
- Exercise process death, reboot or rescheduling, duplicate execution, notification interaction, and API-level restrictions relevant to the feature.
