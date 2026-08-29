# Repository workflow

## Define the contract

- State what data the caller needs, whether it is a snapshot or stream, how freshness is determined, and which failures are actionable.
- Use `suspend` for one-shot asynchronous work and `Flow` for values that genuinely change over time, following the host project's conventions.
- Preserve cancellation. Model expected domain outcomes explicitly; retain useful causes for diagnostics.

## Separate responsibilities

- Let data sources own transport or storage mechanics and repositories coordinate policy between them.
- Map network, database, SDK, and domain models where their lifecycles or meanings differ. Avoid mapping layers with no semantic value.
- Keep mutable caches private and synchronize access when multiple coroutines can mutate them.
- Inject dispatchers or clocks when deterministic tests or policy require them; do not abstract every platform primitive by default.

## Threading and streams

- Move blocking calls to an appropriate dispatcher at the boundary that knows they block.
- Keep coroutine work inside a lifecycle or application scope with explicit ownership. Avoid detached scopes for convenience.
- Make flows cold or shared intentionally. Define replay and sharing behavior when it affects correctness or resource use.
- Avoid swallowing exceptions in intermediate operators. Re-throw cancellation and translate only errors the layer understands.

## Verify

- Test success, empty data, actionable errors, cancellation, stale data, and ordering or race behavior relevant to the feature.
- Use fake data sources and virtual time where practical. Do not make local tests depend on production endpoints.
- Inspect callers for duplicate collection, incorrect lifecycle scope, and assumptions invalidated by the new contract.
