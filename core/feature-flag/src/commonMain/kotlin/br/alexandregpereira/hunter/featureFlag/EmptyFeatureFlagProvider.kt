package br.alexandregpereira.hunter.featureFlag

class EmptyFeatureFlagProvider : FeatureFlagProvider {

    override fun initialize() {
        // no-op
    }

    override suspend fun isFeatureEnabled(feature: String, defaultValue: Boolean): Boolean {
        return false
    }
}
