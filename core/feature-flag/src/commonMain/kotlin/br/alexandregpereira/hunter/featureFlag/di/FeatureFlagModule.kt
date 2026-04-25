package br.alexandregpereira.hunter.featureFlag.di

import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagClient
import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagProvider
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun featureFlagModule(amplitudeApiKey: String) = module {
    single<FeatureFlagProvider> {
        AmplitudeFeatureFlagProvider(
            clientFactory = get(),
            apiKey = amplitudeApiKey,
            analytics = get(),
        )
    }
    factory<AmplitudeFeatureFlagClient.Factory> {
        createAmplitudeFeatureFlagClientFactory()
    }
}

internal expect fun Scope.createAmplitudeFeatureFlagClientFactory(): AmplitudeFeatureFlagClient.Factory
