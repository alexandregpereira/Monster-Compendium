# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kotlin Multiplatform D&D 5th edition monster compendium app for Android, iOS, Windows, macOS, and Linux. UI built with Compose Multiplatform. Data served from static JSON files at [hunter-api](https://github.com/alexandregpereira/hunter-api).

## Build Commands

```bash
# Run desktop app (JVM)
./gradlew app:jvmRun -DmainClass=MainKt --quiet

# Android
./gradlew app:assembleDebug
./gradlew app:assembleRelease

# Desktop packages
./gradlew :app:packageDmg    # macOS
./gradlew :app:packageMsi    # Windows
./gradlew :app:packageDeb    # Linux DEB
./gradlew :app:packageRpm    # Linux RPM

# Tests
./gradlew jvmTest
./gradlew testDebugUnitTest

# Verify database migrations
./scripts/sqldelight/verifyDatabaseMigration
```

## Architecture

**Modular Clean Architecture** with three primary layers:

- **`domain/`** — Business logic, use cases, repository interfaces. No framework dependencies. Each domain area is split into `domain/{name}/core` (interfaces/models/use cases) and `domain/{name}/data` (repository implementations, DTO mappings).
- **`feature/`** — UI features split into three submodules each:
  - `compose/` — Composable UI, entry point is `FooFeature.kt`
  - `state-holder/` — `FooStateHolder.kt` + `FooState.kt`, state management
  - `event/` — `FooEvent.kt` for cross-feature communication (only if needed)
- **`core/`** — Cross-cutting concerns: analytics, event bus, localization, state-holder base, flow test utilities
- **`ui/`** — Shared UI components (compendium layouts, monster cards, etc.)

Data flow: `compose/` → `state-holder/` → `domain/{name}/core` → `domain/{name}/data`

## Key Patterns

### State Management

State holders extend `UiModel<State>` from `core/state-holder`. State is an immutable data class updated via `setState { copy(...) }`. Compose collects state with `stateHolder.state.collectAsState()`.

### Inter-Feature Communication

Use `EventDispatcher<Event>` / `EventListener<Event>` from `core/event`. Features dispatch events through Koin-injected dispatchers (e.g., `SpellDetailEventDispatcher`). Each feature's `event/` module defines its event sealed class.

### Dependency Injection (Koin)

- Define Koin modules in `di/` subdirectories within each module
- Use `factory` for use cases, `single` for repositories and event managers
- All modules assembled in `app/src/commonMain/.../di/AppModule.kt` via `initKoinModules()`

### Localization

Strings are typed per-feature: create a `*Strings.kt` file with a sealed class per language variant. Access via `AppLocalization.getStrings()`.

## Testing Conventions

- Unit tests in `src/commonTest/` (multiplatform) or `src/jvmTest/` (JVM-only)
- Use `testFlow()` from `core/flow/test` for single-flow testing
- Use `testFlows()` for StateHolder tests that observe multiple flows simultaneously
- Flow assertions: `assertNextValue()`, `assertFinalValue()`, `assertHasNoMoreValues()`
- Mock with MockK; test coroutines with `runTest` and custom test dispatchers

## Adding a New Feature

1. `feature/foo/event/` — Define `FooEvent.kt` sealed class (only if cross-feature communication needed)
2. `feature/foo/state-holder/` — `FooState.kt` (data class), `FooStateHolder.kt` (extends `UiModel<FooState>`), Koin DI module
3. `feature/foo/compose/` — `FooFeature.kt` composable entry point and UI components
4. Register the Koin module in `AppModule.kt`

## Database (SQLDelight)

Schema `.sq` files live in `domain/app/data/src/commonMain/sqldelight/br/alexandregpereira/hunter/database/`. Always run `./scripts/sqldelight/verifyDatabaseMigration` after schema changes.

## API Keys

Configure via `local.properties` or environment variables:
- `AMPLITUDE_API_KEY` — Production analytics
- `AMPLITUDE_SANDBOX_API_KEY` — Dev/debug analytics
- `REVENUE_CAT_API_KEY` — In-app purchases
- `ADMOB_APP_ID` — Ad network

Keys are injected at build time by `buildSrc/src/main/kotlin/GenerateAppConfigTask.kt`, which generates `AppConfig.kt`.
