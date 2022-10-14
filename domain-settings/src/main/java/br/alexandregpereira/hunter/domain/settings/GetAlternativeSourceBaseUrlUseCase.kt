package br.alexandregpereira.hunter.domain.settings

import br.alexandregpereira.hunter.domain.settings.SaveBaseUrlsUseCase.Companion.ALTERNATIVE_SOURCE_BASE_URL_KEY
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAlternativeSourceBaseUrlUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<String> {
        return settingsRepository.getSettingsValue(ALTERNATIVE_SOURCE_BASE_URL_KEY)
    }
}
