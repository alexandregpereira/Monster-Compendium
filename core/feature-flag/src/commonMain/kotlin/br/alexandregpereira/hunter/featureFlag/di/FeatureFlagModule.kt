package br.alexandregpereira.hunter.featureFlag.di

import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun featureFlagModule(amplitudeApiKey: String) = module {
    single<FeatureFlagProvider> {
        createFeatureFlagProvider(amplitudeApiKey)
    }
}

internal expect fun Scope.createFeatureFlagProvider(amplitudeApiKey: String): FeatureFlagProvider
