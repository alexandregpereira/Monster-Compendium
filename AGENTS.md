# AGENTS.md

AI coding agent guide for Monster Compendium - a Kotlin Multiplatform D&D 5th edition monster app for Android, iOS, Windows, Mac, and Linux.

## Architecture Overview

**Modular Clean Architecture** with three main layers:
- **`domain/`** - Business logic, use cases, and repository interfaces (no framework dependencies)
- **`feature/`** - UI features with up to two submodules: `compose/` (UI + state holder) and `event/` (cross-feature events, only if needed)
- **`core/`** - Shared utilities (analytics, events, localization, state-holder base)
- **`ui/`** - Shared UI components (compendium layouts, monster cards, etc.)

Each feature follows the pattern: `feature/{name}/compose` → `domain/{name}/core` → `domain/{name}/data`

## Key Patterns

### State Management
- Use `UiModel<State>` base class from `core/state-holder` (see `StateHolder.kt`)
- State holders use `setState { copy(...) }` pattern with immutable data classes
- StateFlow for reactive state, collected in Compose via `stateHolder.state.collectAsState()`
- `FooStateHolder.kt` and `FooState.kt` live inside `feature/{name}/compose/` — there is no separate `state-holder/` submodule

### Inter-Feature Communication
- Event-driven via `EventDispatcher<Event>` / `EventListener<Event>` pattern in `core/event`
- Each feature can have an `event/` module with its events (e.g., `feature/spell-detail/event/`)
- Events dispatched through Koin-injected dispatchers (e.g., `SpellDetailEventDispatcher`)

### Dependency Injection
- Koin modules defined in `di/` directories (e.g., `syncModule`, `spellDomainModule`)
- All modules assembled in `app/src/commonMain/.../di/AppModule.kt` via `initKoinModules()`
- Use `factory` for use cases, `single` for repositories and event managers

### Localization
- Language-specific strings via extension `AppLocalization.getStrings()` returning typed string classes
- Pattern: create `*Strings.kt` file with sealed class per language (see `SearchStrings.kt`)

## Build Commands

```bash
# Desktop (JVM)
./gradlew app:jvmRun -DmainClass=MainKt --quiet

# Android
./gradlew app:assembleDebug

# Desktop packages
./gradlew :app:packageDmg    # macOS
./gradlew :app:packageMsi    # Windows
./gradlew :app:packageRpm # Linux

# Tests
./gradlew jvmTest testDebugUnitTest
```

## Database (SQLDelight)

- Schema files in `domain/app/data/src/commonMain/sqldelight/br/alexandregpereira/hunter/database/*.sq`
- Run `./scripts/sqldelight/verifyDatabaseMigration` to validate migrations

## Testing Conventions

- Unit tests in `jvmTest/` directories using MockK and JUnit
- Use `testFlow()` helper from `core/flow/test` for Flow testing
- StateHolder tests use `testFlows()` for observing multiple flows

## Module Coupling Rules

These rules must be strictly followed. Check existing `build.gradle.kts` files when in doubt.

## Module Coupling Rules

These rules must be followed strictly. When in doubt, check the existing `build.gradle.kts` files.

| Module | Can only depend on                                            |
|--------|---------------------------------------------------------------|
| `:app` | Everyone                                                      | 
| `:core/*` | Other `:core/*` modules only                                  |
| `:domain:{name}:core` | Nothing (stdlib/coroutines only)                              |
| `:domain:{name}:data` | `:domain/*:core`, `:core/*`                                   |
| `:feature:{name}:compose` | `:domain/*:core`, `:feature:{name}:event`, `:core/*`, `:ui/*` |
| `:feature:{name}:event` | `:core/event` (only)                                          |
| `:ui/*` | `:ui/*`                                                       |

## File Organization

When creating a new feature `foo`:
1. `feature/foo/event/` - Events if cross-feature communication needed
2. `feature/foo/compose/` - `FooState.kt`, `FooStateHolder.kt`, `FooFeature.kt` composable entry point, UI components, and Koin DI module
3. Register module in `AppModule.kt`

Do **not** create a separate `state-holder/` submodule.

## API Keys

Configured via `local.properties` or environment variables:
- `AMPLITUDE_API_KEY` - Analytics
- `REVENUE_CAT_API_KEY` - In-app purchases

