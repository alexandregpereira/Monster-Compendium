package br.alexandregpereira.hunter.featureFlag

interface FeatureFlagProvider {

    fun initialize()

    suspend fun isFeatureEnabled(feature: String, defaultValue: Boolean = false): Boolean
}
