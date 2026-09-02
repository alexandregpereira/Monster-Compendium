package br.alexandregpereira.hunter.featureFlag

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.ktx.runCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class AmplitudeFeatureFlagProvider(
    private val clientFactory: AmplitudeFeatureFlagClient.Factory,
    private val apiKey: String,
    private val analytics: Analytics,
) : FeatureFlagProvider {

    private var client: AmplitudeFeatureFlagClient? = null
    private var fetched = false
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutex = Mutex()

    override fun initialize() {
        try {
            client = clientFactory.create(apiKey).also {
                fetch(client = it)
            }
        } catch (cause: Throwable) {
            analytics.logException(
                AmplitudeFeatureFlagProviderException(
                    message = "Fail to initialize Amplitude Experiment, cause -> ${cause.message}",
                    cause = cause,
                )
            )
        }
    }

    override suspend fun isFeatureEnabled(feature: String, defaultValue: Boolean): Boolean {
        val client = client
        if (client == null) {
            analytics.logException(
                AmplitudeFeatureFlagProviderException(
                    message = "Failed to check feature flag $feature: client was not initialized, " +
                            "returning the default value = $defaultValue",
                )
            )
            return defaultValue
        }
        return runCatching {
            fetchSuspending(client)
            val variant = withContext(Dispatchers.IO) { client.variant(feature) }
            variant.value == "on"
        }.getOrElse { cause ->
            val error = AmplitudeFeatureFlagProviderException(
                message = "Failed to the check the feature flag $feature, " +
                        "returning the default value = $defaultValue, " +
                        "cause -> ${cause.message}",
                cause = cause,
            )
            logUnlessNetworkFailure(error, cause)
            defaultValue
        }
    }

    private fun fetch(client: AmplitudeFeatureFlagClient) {
        scope.launch {
            runCatching {
                fetchSuspending(client)
            }.onFailure { cause ->
                val error = AmplitudeFeatureFlagProviderException(
                    message = "Failed to fetch the flags, cause -> ${cause.message}",
                    cause = cause,
                )
                logUnlessNetworkFailure(error, cause)
            }
        }
    }

    private fun logUnlessNetworkFailure(error: Throwable, cause: Throwable) {
        if (cause.isNetworkFailure()) return
        analytics.logException(error)
    }

    /**
     * Throws when the flags could not be fetched, so that callers can tell a disabled flag apart
     * from an unknown one and fall back to their default value instead of silently reading an
     * empty variant.
     */
    private suspend fun fetchSuspending(client: AmplitudeFeatureFlagClient) = mutex.withLock {
        if (!fetched) {
            withContext(Dispatchers.IO) { client.fetch() }
            fetched = true
        }
    }

    private class AmplitudeFeatureFlagProviderException(
        message: String,
        cause: Throwable? = null
    ) : RuntimeException(message, cause)
}

internal interface AmplitudeFeatureFlagClient {
    suspend fun fetch()
    fun variant(feature: String): AmplitudeVariant

    interface Factory {
        fun create(apiKey: String): AmplitudeFeatureFlagClient
    }
}

internal data class AmplitudeVariant(
    val value: String,
)
