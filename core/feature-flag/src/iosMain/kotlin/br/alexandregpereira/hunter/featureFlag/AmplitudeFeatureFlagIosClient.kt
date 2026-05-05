package br.alexandregpereira.hunter.featureFlag

import cocoapods.AmplitudeExperiment.Experiment
import cocoapods.AmplitudeExperiment.ExperimentClientProtocol
import cocoapods.AmplitudeExperiment.ExperimentConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
internal class AmplitudeFeatureFlagIosClient(
    private val client: ExperimentClientProtocol,
) : AmplitudeFeatureFlagClient {

    override suspend fun fetch() {
        client.fetchSuspending()
    }

    override fun variant(feature: String): AmplitudeVariant {
        return AmplitudeVariant(
            value = client.variant(feature).value().orEmpty(),
        )
    }

    private suspend fun ExperimentClientProtocol.fetchSuspending() {
        suspendCancellableCoroutine { continuation ->
            fetchWithUser(null) { _, error ->
                if (error != null) {
                    continuation.resumeWithException(Exception(error.localizedDescription))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    class Factory : AmplitudeFeatureFlagClient.Factory {
        override fun create(apiKey: String): AmplitudeFeatureFlagClient {
            return AmplitudeFeatureFlagIosClient(
                client = Experiment.initializeWithAmplitudeAnalyticsWithApiKey(
                    apiKey = apiKey,
                    config = ExperimentConfig(),
                ),
            )
        }
    }
}
