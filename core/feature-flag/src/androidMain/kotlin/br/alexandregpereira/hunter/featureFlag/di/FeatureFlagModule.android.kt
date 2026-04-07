package br.alexandregpereira.hunter.featureFlag.di

import android.app.Application
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagProvider
import br.alexandregpereira.hunter.featureFlag.EmptyFeatureFlagProvider
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import org.koin.core.scope.Scope

internal actual fun Scope.createFeatureFlagProvider(amplitudeApiKey: String): FeatureFlagProvider {
    val analytics: Analytics = get()
    return try {
        AmplitudeFeatureFlagProvider(
            application = get<Application>(),
            apiKey = amplitudeApiKey,
            analytics = analytics,
            networkManager = get(),
        )
    } catch (cause: Exception) {
        analytics.logException(cause)
        EmptyFeatureFlagProvider()
    }
}
