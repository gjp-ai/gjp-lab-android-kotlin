# Runtime permissions

## Minimize the request

- Confirm the capability cannot be delivered with a system picker, scoped API, delegated intent, or less sensitive data.
- Declare and request only permissions needed on the active API level and for the current feature.
- Keep permission checks next to each protected operation; prior grant state may change.

## Design the user flow

- Ask in context after the user initiates the feature, with a clear explanation of the benefit.
- Handle first request, rationale, denial, permanent denial, revocation, restricted device policy, and unavailable hardware as applicable.
- Keep the feature useful when possible without access. Do not loop requests or pressure the user toward Settings.
- Launch platform requests from an appropriate lifecycle owner and return a result to the UI as state or an event.

## API levels and verification

- Gate permission and platform calls by actual API behavior; do not infer behavior from the compile SDK alone.
- Check manifest declarations and merged-manifest results for unintended permissions introduced by dependencies.
- Test granted, denied, permanently denied, revoked, and interrupted flows on representative API levels. Re-check access immediately before the protected action.
