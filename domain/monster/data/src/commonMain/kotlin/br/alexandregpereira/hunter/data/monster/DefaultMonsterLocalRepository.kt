/*
 * Copyright 2023 Alexandre Gomes Pereira
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
import br.alexandregpereira.hunter.data.monster.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.local.mapper.toDomainMonsterEntity
import br.alexandregpereira.hunter.data.monster.local.mapper.toEntity
import br.alexandregpereira.hunter.data.monster.local.mapper.toEntityStatus
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.settings.AppSettingsImageContentScale
import br.alexandregpereira.hunter.domain.settings.GetAppearanceSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal class DefaultMonsterLocalRepository(
    private val localDataSource: MonsterLocalDataSource,
    private val getAppearanceSettings: GetAppearanceSettings,
) : MonsterLocalRepository {

    override fun saveMonsters(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
        return localDataSource.saveMonsters(monsters.toEntity(), isSync)
    }

    override fun getMonsterPreviews(): Flow<List<Monster>> {
        return localDataSource.getMonsterPreviews().map {
            it.toDomainMonsterEntity(getImageContentScale())
        }
    }

    override fun getMonsterPreviewsEdited(): Flow<List<Monster>> {
        return localDataSource.getMonsterPreviewsEdited().map {
            it.toDomainMonsterEntity(getImageContentScale())
        }
    }

    override fun getMonsters(): Flow<List<Monster>> {
        return localDataSource.getMonsters().map { it.toDomain(getImageContentScale()) }
    }

    override fun getMonsters(indexes: List<String>): Flow<List<Monster>> {
        return localDataSource.getMonsters(indexes).map { it.toDomain(getImageContentScale()) }
    }

    override fun getMonster(index: String): Flow<Monster> {
        return localDataSource.getMonster(index).map { it.toDomain(getImageContentScale()) }
    }

    override fun getMonstersByQuery(query: String): Flow<List<Monster>> {
        return localDataSource.getMonstersByQuery(query).map {
            it.toDomainMonsterEntity(getImageContentScale())
        }
    }

    override fun deleteMonster(index: String): Flow<Unit> {
        return localDataSource.deleteMonster(index)
    }

    override fun getMonstersByStatus(status: Set<MonsterStatus>): Flow<List<Monster>> {
        return localDataSource.getMonstersByStatus(status.map { it.toEntityStatus() }.toSet())
            .map { it.toDomain(getImageContentScale()) }
    }

    private suspend fun getImageContentScale(): MonsterImageContentScale {
        return getAppearanceSettings().single().imageContentScale.let { imageContentScale ->
            when (imageContentScale) {
                AppSettingsImageContentScale.Fit -> MonsterImageContentScale.Fit
                AppSettingsImageContentScale.Crop -> MonsterImageContentScale.Crop
            }
        }
    }
}
