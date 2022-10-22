/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
