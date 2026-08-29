# Gradle, dependencies, CI, and release

## Make scoped build changes

- Inspect the wrapper, plugin management, repositories, version catalog, module build files, Java or Kotlin toolchain, and CI before changing versions.
- Change only dependencies or plugins required for the outcome. Confirm compatibility in official release notes or documentation when versions may have changed.
- Keep repository declarations centralized according to the host project. Avoid dynamic versions and duplicated declarations.
- Do not regenerate signing material, expose credentials, or modify release publication without explicit authorization.

## Verify in increasing scope

1. Run the focused compilation, test, lint, or dependency-resolution target that exercises the change.
2. Build the affected variant and run its relevant local tests.
3. Run instrumented checks when platform fidelity is required and a device is available.
4. Exercise CI or release tasks when the change affects shared build logic, shrinking, packaging, signing configuration, or publication.

Inspect merged manifests, packaged resources, baseline profiles, shrinker output, or dependency graphs when those generated artifacts determine correctness.

## Release readiness

- Check versioning, target-SDK obligations, release manifest, permissions, exported components, resource shrinking, obfuscation rules, startup and critical-path performance, and upgrade compatibility as relevant.
- Ensure debug-only logging, endpoints, tokens, menus, and flags cannot enter release artifacts.
- Record environment-specific skipped checks and avoid presenting a debug build as release validation.
