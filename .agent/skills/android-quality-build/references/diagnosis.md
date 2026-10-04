# Diagnosis workflow

## Establish evidence

- Capture the exact failing behavior, variant, device or API level, command, stack trace, and first meaningful error.
- Reproduce with the smallest relevant target. If reproduction is unavailable, inspect logs and execution paths and label conclusions as hypotheses.
- Compare a known-good path or recent relevant change when available; do not assume temporal correlation proves causation.

## Isolate the cause

- Separate compilation, resource processing, manifest merge, dependency resolution, runtime, lifecycle, concurrency, device, and test-harness failures.
- Reduce variables one at a time. Inspect generated or merged artifacts when the failure occurs after source processing.
- Explain why the proposed cause produces the observed evidence and what evidence would disprove it.

## Fix and prevent regression

- When a fix is requested, change the smallest responsible boundary without suppressing the symptom.
- Add or update a deterministic regression test at the lowest layer that proves the failure. Use higher-level tests only when framework integration is essential.
- Re-run the reproduction, affected module checks, and a proportionate broader build.
- Report whether the root cause was confirmed, which evidence supports it, and what remains unverified.
