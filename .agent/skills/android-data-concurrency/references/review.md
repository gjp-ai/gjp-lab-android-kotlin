# Data and concurrency review

## Trace the data path

- Identify every mutable source and determine which one is authoritative.
- Follow calls across UI, repository, transport, storage, and background boundaries, including dispatcher and scope changes.
- Check whether errors retain meaning and whether cancellation propagates unchanged.

## Look for failure modes

- Blocking work on the main thread, detached or leaking coroutine scopes, races around caches, and unsynchronized read-modify-write operations.
- Flows collected more than intended, expensive cold flows recreated repeatedly, inappropriate replay, lost emissions, and lifecycle-insensitive collection.
- Multiple writable sources of truth, undefined freshness, silent conflict resolution, and unbounded retries.
- Mutable transport or SDK objects leaking to callers, broad exception swallowing, sensitive logs, and tests coupled to live services.

## Report

- Order findings by data loss, user-visible failure, security exposure, and resource risk.
- Explain the timing or execution sequence that makes each issue reachable.
- Separate a demonstrable defect from an optional API simplification and note paths that could not be exercised.
