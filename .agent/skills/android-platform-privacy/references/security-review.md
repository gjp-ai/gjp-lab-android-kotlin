# Platform privacy and security review

## Inspect attack and privacy boundaries

- Review exported activities, services, receivers, and providers; intent filters; deep links; URI permissions; pending intents; and IPC inputs.
- Check runtime and install-time permissions against actual feature need, including declarations merged from dependencies.
- Trace credentials, tokens, identifiers, location, media, notification content, and personal data through storage, transport, logs, analytics, backups, and sharing.
- Check cleartext traffic, certificate handling, WebView exposure, file-provider paths, temporary files, clipboard use, screenshots, and debug-only configuration when present.
- Confirm development tokens, signing material, service credentials, and local properties are not committed or surfaced to users.

## Evaluate controls

- Prefer platform-provided secure storage and sharing mechanisms appropriate to the sensitivity and threat model.
- Validate all external input and authorization at the operation boundary; component visibility is not authorization.
- Ensure failure paths do not disclose sensitive values or silently fall back to weaker behavior.

## Report

- Lead with exploitable exposure, unintended data collection or disclosure, and target-SDK compliance risk.
- Describe the entry point, affected data or capability, required preconditions, and a proportionate remediation.
- Avoid claiming cryptographic or platform guarantees that were not verified.
