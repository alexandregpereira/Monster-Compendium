# AGENTS.md

AI coding agent guide for Monster Compendium - a Kotlin Multiplatform D&D 5th edition monster app for Android, iOS, Windows, Mac, and Linux.

## Architecture Overview

**Modular Clean Architecture** with three main layers:
- **`domain/`** - Business logic, use cases, and repository interfaces (no framework dependencies)
- **`feature/`** - UI features split into `compose/`, `state-holder/`, and `event/` submodules
- **`core/`** - Shared utilities (analytics, events, localization, state-holder base)

Each feature follows the pattern: `feature/{name}/compose` → `feature/{name}/state-holder` → `domain/{name}/core` → `domain/{name}/data`

## Key Patterns

### State Management
- Use `UiModel<State>` base class from `core/state-holder` (see `StateHolder.kt`)
- State holders use `setState { copy(...) }` pattern with immutable data classes
- StateFlow for reactive state, collected in Compose via `stateHolder.state.collectAsState()`

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

## File Organization

When creating a new feature `foo`:
1. `feature/foo/event/` - Events if cross-feature communication needed
2. `feature/foo/state-holder/` - `FooStateHolder.kt`, `FooState.kt`, DI module
3. `feature/foo/compose/` - `FooFeature.kt` composable entry point, UI components
4. Register module in `AppModule.kt`

## API Keys

Configured via `local.properties` or environment variables:
- `AMPLITUDE_API_KEY` - Analytics
- `REVENUE_CAT_API_KEY` - In-app purchases

