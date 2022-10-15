package br.alexandregpereira.hunter.domain.settings

import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge

class SaveBaseUrlsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val settingsMonsterDataRepository: SettingsMonsterDataRepository,
    private val settingsSpellDataRepository: SettingsSpellDataRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    operator fun invoke(imageBaseUrl: String, alternativeSourceBaseUrl: String): Flow<Unit> {
        return settingsRepository.saveSettings(
            mapOf(
                IMAGE_BASE_URL_KEY to imageBaseUrl.normalizeUrl(),
                ALTERNATIVE_SOURCE_BASE_URL_KEY to alternativeSourceBaseUrl.normalizeUrl()
            )
        ).flatMapLatest {
            settingsMonsterDataRepository.deleteData()
                .flatMapMerge { settingsSpellDataRepository.deleteData() }
        }
    }

    private fun String.normalizeUrl(): String {
        return this.removeSuffix("/")
    }

    companion object {
        internal const val IMAGE_BASE_URL_KEY = "imageBaseUrl"
        internal const val ALTERNATIVE_SOURCE_BASE_URL_KEY = "alternativeSourceBaseUrl"
    }
}
