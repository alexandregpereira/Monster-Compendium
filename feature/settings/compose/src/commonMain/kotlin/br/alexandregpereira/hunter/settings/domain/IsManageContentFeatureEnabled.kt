package br.alexandregpereira.hunter.settings.domain

import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import kotlinx.coroutines.flow.single

internal class IsManageContentFeatureEnabled(
    private val featureFlagProvider: FeatureFlagProvider,
    private val alternativeSourceLocalRepository: AlternativeSourceLocalRepository,
) {
    suspend operator fun invoke(): Boolean {
        val hasAdditionalContentInstalled = alternativeSourceLocalRepository.getAlternativeSources()
            .single().any { it.isEnabled }
        return featureFlagProvider.isFeatureEnabled(
            feature = "manage_content",
            defaultValue = hasAdditionalContentInstalled,
        )
    }
}
