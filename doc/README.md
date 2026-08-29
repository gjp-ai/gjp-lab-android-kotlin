# GJPLab documentation

This directory documents the Android lab as it exists today, the behavior it is intended to provide, and the reusable engineering practices exercised here. Source code remains authoritative for implementation; requirement documents are authoritative for intended product behavior.

## Document map

| Area | Canonical document | Purpose |
| --- | --- | --- |
| Application structure | [Application architecture](architecture/application.md) | Runtime flow, code boundaries, state ownership, and project constraints |
| Splash behavior | [Splash requirements](requirements/splash-screen.md) | Product rules, acceptance criteria, and open decisions |
| Splash implementation | [Splash technical design](features/splash-screen.md) | Current Android design, concurrency behavior, known gaps, and test strategy |
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
- Feature designs explain how the current Android implementation satisfies—or does not yet satisfy—those requirements.
- Architecture describes stable project-wide boundaries and links to feature details instead of duplicating them.
- Integration guides document project-specific external-service behavior, configuration, and operational risks.
- Practice guides describe repeatable learning workflows, not product requirements.

Use repository-relative links for source files and other documents. Prefer short excerpts or symbols over copied implementations, because duplicated code examples become stale quickly.

## Status language

Documents use these labels consistently:

| Label | Meaning |
| --- | --- |
| Implemented | Present in the current source and verifiable from the repository |
| Partial | Some required behavior exists, with named gaps |
| Planned | Approved requirement with no complete implementation yet |
| Open | Requires product, design, security, or architecture input |

## Maintenance

Update documentation in the same change when any of these contracts changes:

- user-visible behavior or acceptance criteria;
- activity, feature, integration, or dependency boundaries;
- manifest permissions or exported components;
- Remote Config keys, Analytics events, Crashlytics keys, traces, topics, or notification channels;
- build, test, or release verification commands;
- a known gap is resolved or a new material risk is discovered.

Before handoff, verify local Markdown links, compare implementation claims with source, and report checks that could not run.
