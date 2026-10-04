# GJPLab Android Feature Lab

GJPLab is a small Jetpack Compose app for learning current Android fundamentals by reading, running, and extending self-contained working features. Each feature is a short, readable slice of real code with a matching requirement and design document. It is the Android counterpart of the iOS lab (`gjp-lab-ios-swift`) and shares its folder structure and names.

## Features

The dashboard lists the categories; each category's catalogue marks topics as available (arrow) or planned (clock). The dashboard grid shows 2, 3, or 5 columns depending on the window width, so it works on phones, foldables, and tablets.

| Category | Available | Planned |
| --- | --- | --- |
| Jetpack Compose | — | Material 3, layouts, text and input, buttons, selection, lists and grids, navigation, animation, drawing, accessibility |
| HTTP Client | **HttpURLConnection**: build and send a request, inspect status, JSON body, and headers | Retrofit |
| Security | **Block App During Calls**: block the app while Android reports an active call | Screenshot, screen sharing, and screen recording detection |
| Integration | **Firebase**: Analytics, Crashlytics, Remote Config, Performance Monitoring, and Cloud Messaging demos | — |
| Others | **OS & hardware**: Android version, screen, manufacturer, model, CPU, memory, and ABIs | — |

App-wide behavior: a branded splash screen, a Remote Config maintenance mode, and a Slate light/dark Material 3 design system.

## Requirements

- Android Studio with Android Gradle Plugin 9.3 support, and the Android SDK for API 37
- JDK 21 for the Gradle daemon (downloaded automatically through the Foojay toolchain resolver if missing)
- An emulator or device running Android 11 (API 30) or later
- Dependencies resolve automatically on first build (Compose BOM, Koin, Firebase Android BoM)

## Getting started

1. Open the project in Android Studio and run the **app** configuration on an emulator.
2. Or build and test from the command line:

   ```bash
   ./gradlew assembleDebug
   ./gradlew test
   ./gradlew connectedDebugAndroidTest   # needs a running emulator or device
   ```

`app/google-services.json` is Firebase client configuration for the author's project. To send data to your own Firebase project, replace it with your own file. Call detection, notification permission, and push delivery are best checked on a physical device.

## Project structure

```
app/src/main/java/com/ganjianping/lab/ak/
├── shell/        application, MainActivity, Koin AppModule;
│                 startup/ (splash, maintenance), navigation/ (NavigationMenu, dashboard, catalogue)
├── features/     <category>/<feature>/, one flat folder per feature (Firebase in integration/firebase/)
└── common/       config/, network/, and theme/ (GJPLabTheme)
app/src/test/         JUnit unit tests
app/src/androidTest/  instrumented and Compose UI tests
doc/                  architecture/, decisions/, templates/, and specs/ (mirrors the package root)
```

`shell/` corresponds to `app/` in the iOS lab; see [decision 0001](doc/decisions/0001-folder-structure-mirrors-ios.md).

## Documentation

Start with the [documentation index](doc/README.md). Project-wide docs live in `doc/architecture/`; each screen's requirement and detailed design live in `doc/specs/` at the same path as its code, for example [`doc/specs/features/httpclient/httpurlconnection/`](doc/specs/features/httpclient/httpurlconnection/).

## Working with coding agents

[`AGENTS.md`](AGENTS.md) holds the project rules, commands, and skill routing. Codex and other agents read it directly; [`CLAUDE.md`](CLAUDE.md) imports it for Claude Code. Reusable skills live in [`.agent/skills/`](.agent/skills/); `.claude/skills` is a symlink to the same folder so Claude Code discovers them too.
