# Test selection

Choose the cheapest test that exercises the behavior and fails for the intended regression.

## Select the layer

- Use local JVM tests for pure state transitions, mapping, policies, repositories with fakes, and coroutine behavior that does not require Android framework fidelity.
- Use Compose UI tests for semantics, interaction, rendering state, focus, scrolling, and accessibility behavior that depends on Compose.
- Use instrumented or device tests for platform components, permissions, actual resources, database integration, process or activity lifecycle, and APIs not faithfully represented on the JVM.
- Use a small end-to-end path only for a critical contract spanning boundaries that lower-level tests cannot prove.

## Keep tests trustworthy

- Assert observable behavior and stable semantics, not implementation details or pixel coordinates unless visual fidelity is the contract.
- Control clocks, dispatchers, randomness, storage, and network inputs. Use fakes rather than production services.
- Avoid fixed delays; wait on observable idleness or state with a bounded timeout.
- Cover the relevant failure, empty, cancellation, recreation, and accessibility paths without duplicating equivalent assertions across layers.
- Treat configuration change as different from process death; test the lifecycle guarantee the product actually requires.

## Verify the suite

- Run the narrow test repeatedly when diagnosing flakiness, then run its containing suite.
- State device, emulator, API, locale, font scale, theme, or network prerequisites that materially affect the result.
