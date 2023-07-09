package br.alexandregpereira.hunter.data.source

import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.source.AlternativeSourceSettingsRepository
import kotlinx.coroutines.flow.Flow

internal class AlternativeSourceSettingsRepositoryImpl(
    private val getLanguageUseCase: GetLanguageUseCase
) : AlternativeSourceSettingsRepository {

    override fun getLanguage(): Flow<String> {
        return getLanguageUseCase()
    }
}
