package br.alexandregpereira.hunter.analytics.di

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.analytics.AndroidAmplitudeAnalytics
import br.alexandregpereira.hunter.analytics.EmptyAnalytics
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.autocaptureOptions
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope

internal actual fun Scope.createAmplitudeAnalytics(amplitudeApiKey: String): Analytics {
    return try {
        val amplitude = Amplitude(
            Configuration(
                apiKey = amplitudeApiKey,
                context = get(),
                autocapture = autocaptureOptions {
                    +sessions
                    +appLifecycles
                }
            )
        )
        AndroidAmplitudeAnalytics(amplitude)
    } catch (e: Throwable) {
        val firebaseAnalytics = get<Analytics>(qualifier(name = "FirebaseAnalytics"))
        firebaseAnalytics.logException(e)
        EmptyAnalytics()
    }
}
