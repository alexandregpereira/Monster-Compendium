package br.alexandregpereira.hunter.featureFlag.di

import android.app.Application
import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagProvider
import br.alexandregpereira.hunter.featureFlag.EmptyFeatureFlagProvider
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import org.koin.core.scope.Scope

internal actual fun Scope.createFeatureFlagProvider(amplitudeApiKey: String): FeatureFlagProvider {
    return try {
        AmplitudeFeatureFlagProvider(
            application = get<Application>(),
            apiKey = amplitudeApiKey,
        )
    } catch (e: Exception) {
        e.printStackTrace()
        EmptyFeatureFlagProvider()
    }
}


