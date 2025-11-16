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

import br.alexandregpereira.hunter.data.monster.local.dao.MonsterImageDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterImageEntity
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterImageRepositoryImpl(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val getMonsterImageJsonUrlUseCase: GetMonsterImageJsonUrlUseCase,
    private val monsterImageDao: MonsterImageDao,
) : MonsterImageRepository {

    override fun getMonsterImages(jsonUrl: String): Flow<List<MonsterImage>> {
        return remoteDataSource.getMonsterImages(jsonUrl)
            .map { it.toDomain() }
    }

    override fun getMonsterImageJsonUrl(): Flow<String> {
        return getMonsterImageJsonUrlUseCase()
    }

    override suspend fun saveMonsterImages(monsterImages: List<MonsterImage>) {
        monsterImageDao.insert(monsterImages = monsterImages.map { it.toEntity() })
    }

    override suspend fun saveMonsterImage(monsterImage: MonsterImage) {
        saveMonsterImages(monsterImages = listOf(monsterImage))
    }

    private fun MonsterImage.toEntity(): MonsterImageEntity {
        return MonsterImageEntity(
            monsterIndex = monsterIndex,
            imageUrl = imageUrl,
            backgroundColorLight = backgroundColor.light,
            backgroundColorDark = backgroundColor.dark,
            isHorizontalImage = isHorizontalImage,
            imageContentScale = when (contentScale) {
                MonsterImageContentScale.Fit -> 0
                MonsterImageContentScale.Crop -> 1
                else -> null
            },
        )
    }
}
