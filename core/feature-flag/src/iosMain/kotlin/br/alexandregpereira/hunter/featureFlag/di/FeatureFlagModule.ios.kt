package br.alexandregpereira.hunter.featureFlag.di

import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagClient
import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagIosClient
import org.koin.core.scope.Scope

internal actual fun Scope.createAmplitudeFeatureFlagClientFactory(): AmplitudeFeatureFlagClient.Factory {
    return AmplitudeFeatureFlagIosClient.Factory()
}
