# GJPLab documentation

This directory documents the Android lab as it exists today, the behavior it is intended to provide, and the reusable engineering practices exercised here. Source code remains authoritative for implementation; requirement documents are authoritative for intended product behavior.

## Document map

| Area | Canonical document | Purpose |
| --- | --- | --- |
| Application structure | [Application architecture](architecture/application.md) | Runtime flow, code boundaries, state ownership, and project constraints |
| Visual system | [Slate design system](architecture/design-system.md) | Material 3 color roles, launcher icon, dark mode, and usage rules |
| Requirement template | [Feature requirement template](requirements/FEATURE_REQUIREMENT_TEMPLATE.md) | Required structure for new feature requirements |
| Splash behavior | [Splash requirements](requirements/splash-screen.md) | Product rules and acceptance criteria |
| Splash implementation | [Splash detailed design](detail-design/splash-screen.md) | Current Android design, concurrency behavior, known gaps, and test strategy |
| Call blocking behavior | [Block App During Calls requirements](requirements/security/block_app_during_calls.md) | Privacy-control behavior, Android limitations, and acceptance criteria |
| Call blocking implementation | [Block App During Calls detailed design](detail-design/security/block_app_during_calls.md) | Source map, lifecycle, state, safeguards, and verification |
| Firebase | [Firebase integration](integrations/firebase.md) | SDK wiring, service behavior, privacy notes, and verification |
| AI-assisted Android practice | [Android agent skills](practices/android-agent-skills.md) | How this repository exercises and improves the portable skill library |

## Reading paths

- **New contributor:** application architecture → feature or integration document → relevant source files.
- **Product or QA:** requirement document → acceptance scenarios → implementation status in the linked feature design.
- **Android implementation agent:** [`AGENTS.md`](../AGENTS.md) → selected skill → relevant design and requirement documents.
- **Skill improvement:** Android agent skills practice → a real repository task → evidence-based skill revision.

## Documentation contract

Each fact has one owner:

- Requirements describe observable behavior and avoid prescribing Android classes.
- Detailed designs explain how the current Android implementation satisfies—or does not yet satisfy—requirements.
- Architecture describes stable project-wide boundaries and links to feature details instead of duplicating them.
- Integration guides document project-specific external-service behavior, configuration, and operational risks.
- Practice guides describe repeatable learning workflows, not product requirements.

Use repository-relative links and short symbol references rather than copied implementations.

## Status language

Documents use these labels consistently:

| Label | Meaning |
| --- | --- |
| Implemented | Present in the current source and verifiable from the repository |
| Partial | Some required behavior exists, with named gaps |
| Planned | Approved requirement with no complete implementation yet |
| Open | Requires product, design, security, or architecture input |

## Maintenance

New feature requirements must start from the [feature requirement template](requirements/FEATURE_REQUIREMENT_TEMPLATE.md). Add a detailed design before or alongside implementation when a feature has lifecycle, persistence, integration, platform, or security behavior.

Update this documentation in the same change when user-visible behavior, activity routes, state ownership, manifest permissions, Firebase contracts, build/test commands, or material limitations change. Before handoff, verify local Markdown links and report checks that could not run.
