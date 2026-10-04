# GJPLab Android documentation

This directory documents the Android lab as it exists today and the behavior it is intended to provide. Source code remains authoritative for implementation; requirement documents are authoritative for intended product behavior.

## Layout

- `architecture/` holds project-wide documents that describe the whole app.
- `specs/` mirrors the package root `app/src/main/java/com/ganjianping/lab/ak/` exactly: the docs for `<package root>/<path>/` live in `doc/specs/<path>/`.
- `templates/` holds the starting point for new requirement and detail design documents.
- `decisions/` records project choices the code alone does not explain, and why they were made.

```
doc/
├── architecture/application.md           project-wide
├── decisions/                            0001-….md, one per decision
├── templates/                            requirement.md, detail_design.md
└── specs/                                mirrors the package root
    ├── shell/startup/                    ↔ shell/startup/
    │   ├── splash_requirement.md / splash_detail_design.md
    │   └── maintenance_requirement.md / maintenance_detail_design.md
    ├── shell/navigation/                 ↔ shell/navigation/
    │   ├── sidebar_requirement.md / sidebar_detail_design.md   (sidebar and pane layout)
    │   └── catalog_requirement.md / catalog_detail_design.md
    ├── common/theme/                     ↔ common/theme/
    │   └── theme_detail_design.md        (Slate design system)
    └── features/                         ↔ features/
        └── <category>/<feature>/
            ├── <feature>_requirement.md
            └── <feature>_detail_design.md
```

`ContentView` and `FeatureDestination` are documented in the sidebar detailed design; the other `shell/` root files (`GJPLabApplication`, `MainActivity`, `AppModule`) are documented in [application architecture](architecture/application.md). `common/config/` and `common/network/` hold small helpers and have no spec.

`<feature>` is the code folder name (for example `httpurlconnection`, `blockappduringcalls`). Every screen has both a requirement and a detail design; add them together, starting from [`templates/`](templates/). Shared code with no user-facing behavior, such as `common/theme/`, has a detail design only.

The folder structure and names follow the iOS lab (`gjp-lab-ios-swift`), with `shell/` in place of iOS `app/`; see [decision 0001](decisions/0001-flat-feature-folders.md).

## Document map

| Area | Requirement | Detailed design |
| --- | --- | --- |
| Agent contract | [`AGENTS.md`](../AGENTS.md): project rules, commands, and skill routing | — |
| Application structure | — | [Application architecture](architecture/application.md) |
| Visual system | — | [Slate design system](specs/common/theme/theme_detail_design.md) |
| Decisions | [Decision records](decisions/README.md): why the project is shaped the way it is | — |
| Splash (startup) | [Splash requirement](specs/shell/startup/splash_requirement.md) | [Splash detailed design](specs/shell/startup/splash_detail_design.md) |
| Maintenance (startup) | [Maintenance requirement](specs/shell/startup/maintenance_requirement.md) | [Maintenance detailed design](specs/shell/startup/maintenance_detail_design.md) |
| Category sidebar and panes | [Sidebar requirement](specs/shell/navigation/sidebar_requirement.md) | [Sidebar detailed design](specs/shell/navigation/sidebar_detail_design.md) |
| Category catalogue | [Catalogue requirement](specs/shell/navigation/catalog_requirement.md) | [Catalogue detailed design](specs/shell/navigation/catalog_detail_design.md) |
| Jetpack Compose → Material 3 | [Requirement](specs/features/compose/material3/material3_requirement.md) | [Detailed design](specs/features/compose/material3/material3_detail_design.md) |
| Jetpack Compose → Layouts | [Requirement](specs/features/compose/layouts/layouts_requirement.md) | [Detailed design](specs/features/compose/layouts/layouts_detail_design.md) |
| Jetpack Compose → Text & input | [Requirement](specs/features/compose/textinput/textinput_requirement.md) | [Detailed design](specs/features/compose/textinput/textinput_detail_design.md) |
| Jetpack Compose → Buttons & actions | [Requirement](specs/features/compose/buttons/buttons_requirement.md) | [Detailed design](specs/features/compose/buttons/buttons_detail_design.md) |
| Jetpack Compose → Selection | [Requirement](specs/features/compose/selection/selection_requirement.md) | [Detailed design](specs/features/compose/selection/selection_detail_design.md) |
| Jetpack Compose → Lists & grids | [Requirement](specs/features/compose/lists/lists_requirement.md) | [Detailed design](specs/features/compose/lists/lists_detail_design.md) |
| Jetpack Compose → Navigation | [Requirement](specs/features/compose/navigation/navigation_requirement.md) | [Detailed design](specs/features/compose/navigation/navigation_detail_design.md) |
| Jetpack Compose → Animation | [Requirement](specs/features/compose/animation/animation_requirement.md) | [Detailed design](specs/features/compose/animation/animation_detail_design.md) |
| Jetpack Compose → Drawing & graphics | [Requirement](specs/features/compose/drawing/drawing_requirement.md) | [Detailed design](specs/features/compose/drawing/drawing_detail_design.md) |
| Jetpack Compose → Accessibility & testing | [Requirement](specs/features/compose/accessibility/accessibility_requirement.md) | [Detailed design](specs/features/compose/accessibility/accessibility_detail_design.md) |
| HTTP Client → HttpURLConnection | [Requirement](specs/features/httpclient/httpurlconnection/httpurlconnection_requirement.md) | [Detailed design](specs/features/httpclient/httpurlconnection/httpurlconnection_detail_design.md) |
| Security → Block App During Calls | [Requirement](specs/features/security/blockappduringcalls/blockappduringcalls_requirement.md) | [Detailed design](specs/features/security/blockappduringcalls/blockappduringcalls_detail_design.md) |
| Integration → Firebase | [Firebase lab requirement](specs/features/integration/firebase/firebase_requirement.md) | [Firebase detailed design](specs/features/integration/firebase/firebase_detail_design.md) |
| Others → OS & hardware | [Requirement](specs/features/others/deviceinfo/deviceinfo_requirement.md) | [Detailed design](specs/features/others/deviceinfo/deviceinfo_detail_design.md) |
| New features | [Requirement template](templates/requirement.md) | [Detail design template](templates/detail_design.md) |

## Reading paths

- **New contributor:** application architecture → feature requirement and detailed design → related Kotlin sources.
- **Product or QA:** requirements → acceptance criteria → implementation status and known gaps in the detailed design.
- **Android implementation agent:** [`AGENTS.md`](../AGENTS.md) → selected skill → relevant design and requirement documents.

## Documentation contract

Each fact has one owner:

- Requirements describe observable behavior and avoid prescribing Android classes.
- Detailed designs explain how the current Android implementation satisfies—or does not yet satisfy—requirements.
- Architecture documents stable project-wide boundaries and links to feature details instead of duplicating them.
- Integration designs (such as Firebase) document external-service behavior, configuration, and operational risks.

Use repository-relative links and short symbol references rather than copied implementations.

## Status language

| Label | Meaning |
| --- | --- |
| Implemented | Present in source and verifiable from the repository |
| Partial | Some required behavior exists, with named gaps |
| Planned | Approved requirement with no complete implementation yet |
| Open | Requires product, design, security, or architecture input |

## Maintenance

New feature docs start from the [requirement template](templates/requirement.md) and the [detail design template](templates/detail_design.md), and live at `specs/features/<category>/<feature>/<feature>_requirement.md`, next to `<feature>_detail_design.md`. When a change reverses or adds a project-wide choice, add a [decision record](decisions/README.md).

Update this documentation in the same change when user-visible behavior, routes, state ownership, manifest permissions, Firebase contracts, build/test commands, the toolchain or SDK levels, or material limitations change. Before handoff, verify local Markdown links and report checks that could not run.
