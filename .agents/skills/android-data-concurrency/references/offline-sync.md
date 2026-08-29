# Offline and synchronization

Use this guide only when the product requires offline availability, caching across sessions, or durable synchronization.

## Choose the source of truth

- Prefer a local durable source as the observable source of truth for offline-first reads; synchronize remote results into it.
- If a memory or HTTP cache is sufficient, define its lifetime and invalidation instead of introducing a database.
- Define freshness, staleness visibility, and whether users may act on stale data.

## Design synchronization

- Identify sync triggers, ownership, ordering, idempotency, retry limits, and conflict policy.
- Treat connectivity signals as hints, not proof that a request will succeed.
- Use scheduled durable work only when work must survive process death or run later. Keep immediate user work in an owned coroutine when durability is unnecessary.
- Use bounded retries with backoff for transient failures. Do not retry authentication, validation, or permanent server errors blindly.
- Preserve pending mutations until acknowledged or explicitly rejected. Make duplicate delivery safe where possible.

## Observe and verify

- Record enough structured, non-sensitive context to diagnose retries and conflicts without logging credentials or personal data.
- Test restart, duplicate execution, partial failure, cancellation, stale reads, conflict resolution, and recovery after connectivity returns.
- Verify platform background restrictions and battery impact when scheduled work is introduced.
