# <Feature name> detailed design

Status: Planned | Partial | Implemented

Requirements: [<Feature name>](<feature>_requirement.md)

## Implementation goal

Describe in one or two sentences how the implementation satisfies the requirement, and the main design choice.

## Source map

| Source | Responsibility |
| --- | --- |
| [`<Feature>Activity.kt`](<relative link>) | Lifecycle, injected dependencies, permissions, and navigation |
| [`<Feature>Screen.kt`](<relative link>) | Compose layout; receives state and callbacks |
| [`FeatureRoute.kt`](<relative link>) | `FeatureRoute.<Route>` case |
| [`NavigationMenu.kt`](<relative link>) | Catalogue topic with `route = FeatureRoute.<Route>` |
| [`FeatureCatalogActivity.kt`](<relative link>) | Opens `<Feature>Activity` for the route |

## Ownership and state

- Who owns each piece of state (Activity field, `remember`, `StateFlow` in a Koin singleton, repository), and how long it lives.
- How the screen is reached and what it opens next (explicit `Intent`s, extras).
- Lifecycle, cancellation, and threading (`lifecycleScope`, `Dispatchers.IO`, SDK callbacks) where relevant.

## <Feature-specific section>

Add sections only where they explain something the code does not show at a glance: request flow, data sources, platform behavior, privacy and security, or stable contracts such as Firebase names. Delete this section if it is not needed.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| <What is missing or wrong> | <What the user or developer notices> | <Smallest fix> |

## Verification

- Build and test with the commands in [application architecture](<relative link to doc/architecture/application.md>#build-and-verification).
- Automated: name the JVM, Compose, or instrumented tests that cover this feature, or state that there are none.
- Manual: list the acceptance criteria (<PREFIX>-AC-01 …) to check, and on which devices; name anything that needs a physical device.
