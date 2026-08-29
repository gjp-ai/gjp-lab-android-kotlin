---
name: android-platform-privacy
description: Implement or review Android platform integrations involving runtime permissions, manifests, intents, app links, notifications, services, scheduled work, files, sensors, inter-app communication, API-level behavior, privacy, and secure component configuration. Use when a feature touches protected APIs, background execution, exported components, sensitive data, or user-visible permission flows. Do not use for pure Compose layout or repository-only work.
metadata:
  version: "0.1.0"
---

# Android Platform and Privacy

Integrate Android platform capabilities with the minimum privilege and exposure required for the user outcome. Treat manifest declarations, permission UX, background execution, and sensitive data handling as one end-to-end contract.

## Discover the host project

Before changing platform behavior:

- Read repository instructions, minimum and target SDKs, merged-manifest inputs, application and component declarations, existing permission launchers, platform wrappers, background work, and tests.
- Identify the protected capability, user-visible reason, data sensitivity, component exposure, API-level differences, and behavior when access is unavailable.
- Prefer a platform alternative that avoids a dangerous permission or long-running background component when it satisfies the outcome.
- Verify recently changed platform requirements in official Android documentation for the project's target SDK.

## Select a mode

- **Runtime permissions:** Read [the permission workflow](references/permissions.md).
- **Components or background work:** Read [the components and background guide](references/components-background.md).
- **Privacy or security review:** Read [the security review guide](references/security-review.md). Report findings without editing unless asked.

## Completion contract

- Request only the minimum capability and only when the user action requires it.
- Make denial, revocation, unavailable hardware, and relevant API-level differences safe and understandable.
- Declare component exposure explicitly and minimize data carried through intents, logs, files, notifications, and inter-process boundaries.
- Keep secrets out of source and generated artifacts; never weaken platform security merely to make development easier.
- Inspect the affected merged manifest or packaged behavior and run device-level checks when framework behavior cannot be proven locally.
