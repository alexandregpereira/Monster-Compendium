package br.alexandregpereira.hunter.featureFlag

import android.app.Application
import com.amplitude.experiment.Experiment
import com.amplitude.experiment.ExperimentClient
import com.amplitude.experiment.ExperimentConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AmplitudeFeatureFlagAndroidClient(
    private val client: ExperimentClient,
) : AmplitudeFeatureFlagClient {

    override suspend fun fetch() {
        withContext(Dispatchers.IO) {
            client.fetch(null).get()
        }
    }

    override fun variant(feature: String): AmplitudeVariant {
        return AmplitudeVariant(
            value = client.variant(key = feature).value.orEmpty()
        )
    }

    class Factory(
        private val application: Application,
    ) : AmplitudeFeatureFlagClient.Factory {
        override fun create(apiKey: String): AmplitudeFeatureFlagClient {
            return AmplitudeFeatureFlagAndroidClient(
                client = Experiment.initializeWithAmplitudeAnalytics(
                    application,
                    apiKey,
                    ExperimentConfig(),
                )
            )
        }
    }
}
