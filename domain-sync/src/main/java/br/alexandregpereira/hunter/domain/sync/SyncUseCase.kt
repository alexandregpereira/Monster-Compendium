package br.alexandregpereira.hunter.domain.sync

import br.alexandregpereira.hunter.domain.spell.SyncSpellsUseCase
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class SyncUseCase @Inject constructor(
    private val syncMonsters: SyncMonstersUseCase,
    private val syncSpells: SyncSpellsUseCase
) {

    @OptIn(FlowPreview::class)
    operator fun invoke(): Flow<Unit> {
        return syncMonsters().flatMapMerge { syncSpells() }
    }
}
