package br.alexandregpereira.hunter.domain.spell

import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class SyncSpellsUseCase @Inject constructor(
    private val repository: SpellRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return repository.getRemoteSpells().flatMapLatest { spells ->
            repository.deleteLocalSpells().flatMapLatest {
                repository.saveSpells(spells)
            }
        }
    }
}
