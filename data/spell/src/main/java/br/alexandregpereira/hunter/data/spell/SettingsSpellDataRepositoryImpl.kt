package br.alexandregpereira.hunter.data.spell

import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.domain.settings.SettingsSpellDataRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class SettingsSpellDataRepositoryImpl @Inject constructor(
    private val localDataSource: SpellLocalDataSource
) : SettingsSpellDataRepository {

    override fun deleteData(): Flow<Unit> {
        return localDataSource.deleteSpells()
    }
}
