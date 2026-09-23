# core:analytics

A small Kotlin Multiplatform analytics facade for Android, iOS and JVM desktop.

You depend on a single interface — `Analytics` — and the module decides, per platform, which SDKs
receive the event. Today every event is sent to **Amplitude** and **Firebase Analytics** at once, but
your call sites never mention either SDK, so a provider can be added or removed without touching
feature code.

| Platform | Amplitude | Firebase Analytics |
|----------|-----------|--------------------|
| Android  | `com.amplitude:analytics-android` | Yes |
| iOS      | `AmplitudeSwift` (CocoaPods) | Yes (`FirebaseAnalytics` pod) |
| JVM (desktop) | `com.amplitude:java-sdk` | No |

## Installation

### Gradle

```kotlin
// build.gradle.kts of the consuming module
multiplatform {
    commonMain {
        implementation(project(":core:analytics"))
    }
}
```

The module ships its own transitive SDKs (Amplitude, Firebase BoM, Firebase Analytics/Crashlytics on
Android; the Amplitude Java SDK on the JVM), so nothing else has to be declared for those. Koin is
the only requirement you must already have in place — the module publishes its wiring as a Koin
module.

### Platform setup

**Android** — the module resolves three dependencies from your Koin graph, so register them before
`analyticsModule`:

```kotlin
startKoin {
    androidContext(app)                 // android.content.Context
    modules(
        module {
            factory { Firebase.analytics }    // com.google.firebase.analytics.FirebaseAnalytics
            factory { Firebase.crashlytics }  // com.google.firebase.crashlytics.FirebaseCrashlytics
        },
    )
}
```

Firebase itself is configured the usual way, with a `google-services.json` in the application module
and the Google Services / Crashlytics Gradle plugins applied.

**iOS** — the module is exposed through CocoaPods and links `AmplitudeSwift`, `FirebaseAnalytics`
and `FirebaseCrashlytics` as a **static** framework. A dynamic framework links its pods with `ld`,
which cannot see the pods that those pods depend on (for example `FirebaseCore` under
`FirebaseAnalytics`), so the app consumes the static framework and lets Xcode link everything from
the `Podfile`. Declare the same pods in the app target:

```ruby
platform :ios, '14.0'
use_frameworks!

target 'YourApp' do
  pod 'AmplitudeSwift', '~> 1.10'
  pod 'FirebaseAnalytics'
  pod 'FirebaseCrashlytics'
end
```

Then generate the framework and install the pods:

```bash
./gradlew :core:analytics:generateDummyFramework
```

```bash
cd iosApp && pod install
```

Ship a `GoogleService-Info.plist` and call `FirebaseApp.configure()` at app start. Minimum
deployment target is iOS 14.0.

**JVM (desktop)** — the desktop Amplitude client stamps each event with your app version, so provide
a `JvmAnalyticsProvider`:

```kotlin
internal val jvmAnalyticsModule = module {
    factory<JvmAnalyticsProvider> {
        object : JvmAnalyticsProvider {
            override fun getVersionName(): String = AppConfig.VERSION_NAME
        }
    }
}
```

### Registering the Koin module

```kotlin
modules(
    analyticsModule(amplitudeApiKey = AppConfig.AMPLITUDE_API_KEY),
)
```

`analyticsModule` registers:

- `Analytics` (unqualified) — the fan-out instance you inject everywhere.
- `Analytics` qualified `"FirebaseAnalytics"` — the Firebase provider for that platform.
- `Analytics` qualified `"AmplitudeAnalytics"` — the Amplitude provider for that platform.

If Amplitude fails to initialize (bad or empty API key, SDK failure), the module degrades to a no-op
provider instead of crashing, and the app keeps running with Firebase only.

## Usage

Inject `Analytics` and call it:

```kotlin
class MyStateHolder(
    private val analytics: Analytics,
) {

    fun onScreenOpened() {
        analytics.track(eventName = "My screen - opened")
    }

    fun onItemClicked(id: String, index: Int) {
        analytics.track(
            eventName = "My screen - item click",
            params = mapOf(
                "id" to id,
                "index" to index,
            ),
        )
    }
}
```

### API

| Member | Description |
|--------|-------------|
| `track(eventName: String, params: Map<String, Any?> = emptyMap())` | Sends an event to every provider on the current platform. |
| `setUserProperty(name: String, value: Any)` | Sets a property on the current user/device profile. |
| `getDeviceId(): String?` | The Amplitude device id, useful to quote in support emails. Returns `null` when Amplitude is unavailable. |

### Recommended pattern: one analytics class per feature

Rather than scattering `track` calls with inline string literals, wrap `Analytics` in a small
feature-scoped class. Event names and parameter shapes then live in one file per feature, which
keeps the taxonomy reviewable:

```kotlin
internal class SettingsAnalytics(
    private val analytics: Analytics,
) {

    val deviceId: String? get() = analytics.getDeviceId()

    fun trackSaveButtonClick(state: SettingsViewState) {
        analytics.track(
            eventName = "Settings - save button click",
            params = mapOf(
                "imageBaseUrl" to state.imageBaseUrl,
                "alternativeSourceBaseUrl" to state.alternativeSourceBaseUrl,
            ),
        )
    }
}
```

```kotlin
factory {
    SettingsStateHolder(
        analytics = SettingsAnalytics(get()),
    )
}
```

### Event names and parameters

- Use human-readable names such as `"Settings - save button click"`. Amplitude stores them as
  written; the Firebase providers normalize them by replacing spaces with `_` and dropping `-`, so
  the example arrives as `Settings___save_button_click`.
- Firebase on Android only forwards `String`, `Int`, `Boolean`, `Long`, `Float` and `Double`
  parameters — values of other types are silently dropped from the Firebase payload. Amplitude
  receives the map as-is. Prefer primitives.
- `params` is optional; omit it for plain events.

### Debug builds

On Android debug builds (`FLAG_DEBUGGABLE`), Firebase events are logged to Logcat under the
`FirebaseAnalytics` tag instead of being sent. The iOS and desktop providers print the event to the
console and still send it. Point the module at a sandbox Amplitude key for development.

## Testing

`EmptyAnalytics` is a public no-op implementation — use it wherever a test or a preview needs an
`Analytics` without a backend:

```kotlin
val stateHolder = MyStateHolder(analytics = EmptyAnalytics())
```

## Architecture

```
            Analytics (interface, commonMain)
                      ▲
            AnalyticsProviders  ── fans out to ──┬── Firebase provider (expect/actual)
                                                 └── Amplitude provider (expect/actual)
```

`AnalyticsProviders` implements `Analytics` and forwards `track` and `setUserProperty` to both
providers; `getDeviceId` is answered by Amplitude. The two providers are created by the
`expect`/`actual` factories `createAnalytics()` and `createAmplitudeAnalytics(amplitudeApiKey)`,
declared in `AnalyticsModule.kt` and implemented per platform:

| Platform | `createAnalytics()` | `createAmplitudeAnalytics()` |
|----------|---------------------|------------------------------|
| Android | `FirebaseAnalytics` wrapper (debug-aware) | `AndroidAmplitudeAnalytics`, with session and app-lifecycle autocapture |
| iOS | `IosFirebaseAnalytics` | `IosAmplitudeAnalytics` |
| JVM | `EmptyAnalytics` (no Firebase desktop SDK) | `JvmAmplitudeAnalytics` |

The desktop provider has no platform device id, so it generates a UUID on first run and stores it at
`~/.monster-compendium/device_id`, falling back to a per-session UUID if the file cannot be written.
It also fills in app version, OS name/version, architecture, locale and a session id on every event.

## License

MIT — see [LICENSE](LICENSE). This module is licensed independently of the rest of the repository,
which is GPL-3.0.
