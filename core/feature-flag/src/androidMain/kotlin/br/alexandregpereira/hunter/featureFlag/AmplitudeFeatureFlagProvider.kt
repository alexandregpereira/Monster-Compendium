package br.alexandregpereira.hunter.featureFlag

import android.app.Application
import com.amplitude.experiment.Experiment
import com.amplitude.experiment.ExperimentClient
import com.amplitude.experiment.ExperimentConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AmplitudeFeatureFlagProvider(
    private val application: Application,
    private val apiKey: String,
) : FeatureFlagProvider {

    private var client: ExperimentClient? = null
    private var fetched = false

    override fun initialize() {
        if (apiKey.isEmpty()) return
        client = Experiment.initializeWithAmplitudeAnalytics(
            application,
            apiKey,
            ExperimentConfig(),
        )
    }

    override suspend fun isFeatureEnabled(feature: String): Boolean {
        val client = client ?: return false
        return withContext(Dispatchers.IO) {
            try {
                if (!fetched) {
                    client.fetch(null).get()
                    fetched = true
                }
                val variant = client.variant(feature)
                variant.value == "on"
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}

