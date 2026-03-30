package br.alexandregpereira.hunter.featureFlag.di

import br.alexandregpereira.hunter.featureFlag.EmptyFeatureFlagProvider
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import org.koin.core.scope.Scope

internal actual fun Scope.createFeatureFlagProvider(amplitudeApiKey: String): FeatureFlagProvider {
    return EmptyFeatureFlagProvider()
}
