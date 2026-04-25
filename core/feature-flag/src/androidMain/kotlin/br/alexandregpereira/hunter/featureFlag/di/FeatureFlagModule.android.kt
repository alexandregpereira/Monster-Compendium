package br.alexandregpereira.hunter.featureFlag.di

import android.app.Application
import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagAndroidClient
import br.alexandregpereira.hunter.featureFlag.AmplitudeFeatureFlagClient
import org.koin.core.scope.Scope

internal actual fun Scope.createAmplitudeFeatureFlagClientFactory(): AmplitudeFeatureFlagClient.Factory {
    return AmplitudeFeatureFlagAndroidClient.Factory(
        application = get<Application>(),
    )
}
