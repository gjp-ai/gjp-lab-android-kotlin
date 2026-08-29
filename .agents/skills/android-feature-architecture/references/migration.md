# Architecture migration

Use migration only when explicitly requested or when a demonstrated requirement cannot be met safely within the current structure.

## Establish the baseline

- Record current user behavior, public contracts, state ownership, lifecycle behavior, navigation routes, dependency scopes, and test coverage.
- State the concrete limitation being removed and the success criteria. Do not use architectural fashion as the rationale.

## Create a safe seam

- Choose a boundary that permits incremental replacement: one screen, state holder, route, repository contract, or dependency registration.
- Keep old and new paths compatible only as long as needed. Avoid two writable sources of truth.
- Add characterization tests around behavior most likely to regress before moving it.

## Migrate incrementally

- Move one responsibility at a time and keep the project buildable between steps.
- Preserve route arguments, saved-state keys, analytics contracts, and external interfaces unless their change is part of the request.
- Remove obsolete adapters, registrations, and code after all callers move; do not leave an indefinite dual architecture.

## Stop conditions

- Stop if migration requires a product decision, public API break, data migration, or dependency change outside the user's scope.
- At handoff, identify completed seams, remaining compatibility code, rollback options, and verification performed.
