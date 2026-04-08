package br.alexandregpereira.hunter.domain.strings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SyncStringsUseCase(
    private val stringsRepository: StringsRepository,
) {
    operator fun invoke(lang: String): Flow<Unit> = flow {
        stringsRepository.syncStrings(lang)
        emit(Unit)
    }
}
