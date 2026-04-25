package br.alexandregpereira.hunter.featureFlag.di

import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagClient
import br.alexandregpereira.hunter.featureFlag.AmplitudeVariant
import org.koin.core.scope.Scope

internal actual fun Scope.createAmplitudeFeatureFlagClientFactory(): AmplitudeFeatureFlagClient.Factory {
    return object : AmplitudeFeatureFlagClient.Factory {
        override fun create(apiKey: String): AmplitudeFeatureFlagClient {
            return object : AmplitudeFeatureFlagClient {
                override suspend fun fetch() {}

                override fun variant(feature: String): AmplitudeVariant {
                    return AmplitudeVariant(value = "on")
                }
            }
        }
    }
}
