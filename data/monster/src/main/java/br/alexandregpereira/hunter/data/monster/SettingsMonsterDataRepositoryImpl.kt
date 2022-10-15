package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.preferences.PreferencesRepository
import br.alexandregpereira.hunter.domain.settings.SettingsMonsterDataRepository
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

internal class SettingsMonsterDataRepositoryImpl @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val localDataSource: MonsterLocalDataSource
) : SettingsMonsterDataRepository {

    @OptIn(FlowPreview::class)
    override fun deleteData(): Flow<Unit> {
        return localDataSource.deleteAll().flatMapMerge {
            preferencesRepository.saveCompendiumScrollItemPosition(position = 0)
        }
    }
}
