package br.alexandregpereira.hunter.domain.settings

import br.alexandregpereira.hunter.domain.settings.SaveBaseUrlsUseCase.Companion.IMAGE_BASE_URL_KEY
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetImageBaseUrlUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<String> {
        return settingsRepository.getSettingsValue(
            key = IMAGE_BASE_URL_KEY,
            defaultValue = DEFAULT_IMAGE_BASE_URL
        )
    }

    companion object {
        private const val DEFAULT_IMAGE_BASE_URL =
            "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/images/"
    }
}
