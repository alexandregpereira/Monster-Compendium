/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022. Alexandre Gomes Pereira.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
