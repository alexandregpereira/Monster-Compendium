/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
