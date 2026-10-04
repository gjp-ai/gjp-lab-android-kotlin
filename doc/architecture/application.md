# Application architecture

Status: Implemented snapshot, 2026-10-04

## Purpose

GJPLab is a single-module Android learning application. It favors small, readable feature slices over production-scale abstraction: one Activity hosts a selection-driven navigation root (`ContentView`) that shows one, two, or three panes depending on the window width, Jetpack Compose renders every screen, Koin provides application-scoped dependencies, and external SDK access is kept outside composables.

## Runtime flow

```mermaid
flowchart LR
    Launcher[Android launcher] --> SplashActivity
    SplashActivity -->|maintenance extra| MainActivity
    MainActivity --> MaintenanceScreen
    MainActivity --> ContentView
    ContentView --> Sidebar[CategorySidebar]
    Sidebar --> Catalog[FeatureCatalogScreen]
    Catalog --> Destination[FeatureDestination]
    Destination --> Device[DeviceInfoScreen]
    Destination --> HTTP[HttpURLConnectionScreen]
    Destination --> Firebase[FirebaseFeatureScreen]
    Destination --> Calls[BlockAppDuringCallsScreen]
    HTTP -->|DetailRoute.Response| Response[HttpResponseScreen]
```

[`SplashActivity`](../../app/src/main/java/com/ganjianping/lab/ak/shell/startup/SplashActivity.kt) is the exported launcher. [`MainActivity`](../../app/src/main/java/com/ganjianping/lab/ak/shell/MainActivity.kt) is internal and is the only navigation Activity: categories, catalogue, and features are panes inside [`ContentView`](../../app/src/main/java/com/ganjianping/lab/ak/shell/ContentView.kt) ([decision 0005](../decisions/0005-adaptive-pane-navigation.md)). The Firebase messaging service is internal and is invoked through the Firebase messaging intent action.

## Code organization

| Path | Responsibility |
| --- | --- |
| `shell/` | `GJPLabApplication`, `MainActivity` (maintenance or `ContentView`), `ContentView` (navigation state and pane layout), `FeatureDestination` (route → screen), and the Koin `AppModule` |
| `shell/startup/` | `SplashActivity`, `SplashScreen`, and `MaintenanceScreen` |
| `shell/navigation/` | `NavigationMenu` (every category and topic), `FeatureRoute` and `DetailRoute`, `NavigationPane` (pane layout and top bar), `CategorySidebar`, and `FeatureCatalogScreen` |
| `features/<category>/<feature>/` | One flat folder per feature: Compose screens, repositories, controllers, and models |
| `features/integration/firebase/` | All Firebase code: lab screen, `FirebaseIntegration`, constants, Koin module, and messaging service. Startup depends on it, so unlike other features it cannot be removed on its own |
| `common/config/` | Stable application behavior constants |
| `common/network/` | Reusable Android connectivity checks |
| `common/theme/` | Slate Material 3 color, typography, `GJPLabTheme`, and `LabListCard` |

Paths are relative to the package root `app/src/main/java/com/ganjianping/lab/ak/`. The layout and type names mirror the iOS lab, with `shell/` in place of iOS `app/` ([decision 0001](../decisions/0001-flat-feature-folders.md)). Folder names are lowercase and do not repeat their parent (`httpclient/httpurlconnection`). New code should follow the nearest established feature. Reusable app code belongs in `common/`; SDK-specific behavior belongs in `features/integration/<sdk>/` ([decision 0002](../decisions/0002-sdk-code-in-integration-features.md)).

### Adding a feature

1. Write `doc/specs/features/<category>/<feature>/<feature>_requirement.md` from the [requirement template](../templates/requirement.md); add `<feature>_detail_design.md` beside it from the [detail design template](../templates/detail_design.md).
2. Add the screen under `features/<category>/<feature>/`. It draws only its content (no `Scaffold` or back button): the pane supplies the top app bar and title.
3. Add a `FeatureRoute` case and its screen in `FeatureDestination`; screens that push further add a `DetailRoute` case, drawn in `ContentView`.
4. In `shell/navigation/NavigationMenu.kt`, add the topic with `route = FeatureRoute.<Case>`, or give an existing planned topic the route. The topic title becomes the pane title. `NavigationMenuTest` fails if a route is missing or listed twice.
5. Register any repository in `shell/AppModule.kt`, inject it in `MainActivity`, and pass it through `FeatureDependencies`.
6. Add light and dark previews for each screen, and request any runtime permission from the screen with `rememberLauncherForActivityResult`, only after a user action.

## Dependency and event flow

```mermaid
flowchart TD
    Application[GJPLabApplication] --> Koin[AppModule]
    Koin --> Repositories
    Koin --> FirebaseModule
    Main[MainActivity] -->|inject| Repositories
    Main -->|inject| FirebaseIntegration
    Main -->|FeatureDependencies| Content[ContentView]
    Content -->|selection| Destination[FeatureDestination]
    Destination -->|repository or integration| Screen[Feature screen]
    Screen -->|onResponse| Content
```

- [`GJPLabApplication`](../../app/src/main/java/com/ganjianping/lab/ak/shell/GJPLabApplication.kt) starts Koin and initializes Firebase once per application process.
- `MainActivity` owns maintenance state, call monitoring (`onResume`/`onPause`), and the Koin-injected dependencies. `ContentView` owns navigation state.
- Feature screens receive their repository, controller, or `FirebaseIntegration` as a parameter. They hold their own screen state with `remember`/`rememberSaveable` and request runtime permissions with `rememberLauncherForActivityResult`. They never construct repositories or start Activities, and they reach SDKs only through `FirebaseIntegration`.
- [`AppModule`](../../app/src/main/java/com/ganjianping/lab/ak/shell/AppModule.kt) composes application dependencies, including the Firebase module.

## State and lifecycle model

The project intentionally uses Compose state (`remember`, `rememberSaveable`, `mutableStateOf`) and a few Koin singletons rather than ViewModels. This is appropriate for the current lab size but has consequences:

- `ContentView` keeps the selected category id and `FeatureRoute` with `rememberSaveable`, so they survive rotation and window resizing; the pushed `detailPath` does not.
- Screen state held with `remember` resets on configuration changes; long-running work is tied to the composition (`rememberCoroutineScope`, `LaunchedEffect`) or to an SDK callback.
- Navigation is selection state in `ContentView`, not a navigation graph or back stack of Activities. `BackHandler` undoes one selection step at a time.
- Category and topic content (titles, descriptions, icons, and each topic's `FeatureRoute`) lives only in [`NavigationMenu`](../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt). A topic without a route is shown as planned; `NavigationMenuTest` fails if a route is missing or listed twice.
- Process-death restoration beyond the selection and multi-screen shared state are not general project guarantees.

Do not introduce a ViewModel, navigation framework, domain layer, or module split as an incidental refactor. Introduce one only when a feature requirement demonstrates the need and include migration tests.

## Platform and security boundaries

The manifest currently declares Internet, network-state, notification, and phone-state permissions. Runtime notification permission is requested by the Firebase screen on Android 13 and newer; `READ_PHONE_STATE` is requested only after the user enables Block App During Calls. Network traffic is constrained by [`network_security_config.xml`](../../app/src/main/res/xml/network_security_config.xml).

Keep protected API checks in repositories, controllers, or `FirebaseIntegration`, and request permissions from the screen that needs them, after a user action. Keep credentials and server authority outside the mobile application. Firebase client configuration is not a server credential, but service-account files, server keys, OAuth secrets, and App Check debug tokens must never be committed.

## Build and verification

The app uses one `app` module, a version catalog, Compose Material 3, Koin, and the Firebase Android BoM. The supported application range is API 30 through target SDK 37.

Use the smallest relevant checks first:

```bash
./gradlew test
./gradlew assembleDebug
./gradlew connectedDebugAndroidTest
```

The connected test requires an emulator or device. Current checked-in tests are starter coverage only; feature work should add deterministic tests at the lowest layer that proves the behavior.

## Known architectural constraints

| Constraint | Consequence | Revisit when |
| --- | --- | --- |
| Single app module | Simple discovery; weak compile-time feature boundaries | Build time or ownership becomes a problem |
| Compose-held screen state | Low ceremony; limited recreation guarantees | State must survive recreation or be shared |
| Hand-built adaptive panes ([decision 0005](../decisions/0005-adaptive-pane-navigation.md)) | Readable and dependency-free; no pane animations, predictive-back previews, or saved pushed screens | Deep links, animated transitions, or deeper push stacks are needed |
| SDK callbacks in integration layer | Small API surface; cancellation and rich errors are limited | Callers need structured concurrency or failure types |
| Minimal automated tests | Fast experimentation; regression confidence is low | Any behavior becomes important to preserve |

Feature-specific behavior belongs in the linked documents rather than this overview:

- [Slate design system](../specs/common/theme/theme_detail_design.md)
- [Splash detailed design](../specs/shell/startup/splash_detail_design.md)
- [Block App During Calls detailed design](../specs/features/security/blockappduringcalls/blockappduringcalls_detail_design.md)
- [Maintenance detailed design](../specs/shell/startup/maintenance_detail_design.md)
- [Sidebar detailed design](../specs/shell/navigation/sidebar_detail_design.md) (pane layout and navigation state) and [catalogue detailed design](../specs/shell/navigation/catalog_detail_design.md)
- [HttpURLConnection detailed design](../specs/features/httpclient/httpurlconnection/httpurlconnection_detail_design.md)
- [OS & hardware detailed design](../specs/features/others/deviceinfo/deviceinfo_detail_design.md)
- [Firebase detailed design](../specs/features/integration/firebase/firebase_detail_design.md)
