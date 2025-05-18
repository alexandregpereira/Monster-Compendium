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

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.folder.local.entity.MonsterFolderEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntityStatus
import br.alexandregpereira.hunter.database.MonsterFolderCompleteEntityView
import br.alexandregpereira.hunter.database.MonsterFolderQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.MonsterFolderEntity as MonsterFolderDatabaseEntity

internal class MonsterFolderDaoImpl(
    private val queries: MonsterFolderQueries,
    private val dispatcher: CoroutineDispatcher
) : MonsterFolderDao {

    override suspend fun addMonstersToFolder(
        folderCrossRefEntity: List<MonsterFolderEntity>
    ) = withContext(dispatcher) {
        queries.transaction {
            folderCrossRefEntity.forEach {
                queries.insert(
                    MonsterFolderDatabaseEntity(
                        folderName = it.folderName,
                        monsterIndex = it.monsterIndex,
                        createdAt = it.createdAt
                    )
                )
            }
        }
    }

    override suspend fun getMonsterFolders(): Map<MonsterFolderEntity, List<MonsterEntity>> =
        withContext(dispatcher) {
            queries.getMonsterFolders().executeAsList().groupByMonsterFolder()
        }

    override suspend fun getMonstersFromFolder(
        folderName: String
    ): Map<MonsterFolderEntity, List<MonsterEntity>> = withContext(dispatcher) {
        queries.getMonstersFromFolder(folderName).executeAsList().groupByMonsterFolder()
    }

    override suspend fun getMonstersFromFolders(
        foldersName: List<String>
    ): List<MonsterEntity> {
        return withContext(dispatcher) {
            queries.getMonstersFromFolders(foldersName).executeAsList().map {
                it.toMonsterEntity()
            }
        }
    }

    override suspend fun removeMonsterFromFolder(
        folderName: String,
        monsterIndexes: List<String>
    ) = withContext(dispatcher) {
        queries.removeMonsterFromFolder(folderName, monsterIndex = monsterIndexes)
    }

    override suspend fun removeMonsterFolders(folderNames: List<String>) = withContext(dispatcher) {
        queries.removeMonsterFolders(folderNames)
    }

    private fun List<MonsterFolderCompleteEntityView>.groupByMonsterFolder():
            Map<MonsterFolderEntity, List<MonsterEntity>> {
        return map {
            MonsterFolderEntity(
                folderName = it.folderName,
                monsterIndex = it.monsterIndex,
                createdAt = it.createdAt
            ) to it.toMonsterEntity()
        }.groupBy(keySelector = { it.first }, valueTransform = { it.second })
    }

    private fun MonsterFolderCompleteEntityView.toMonsterEntity(): MonsterEntity {
        return MonsterEntity(
            index = index,
            type = type,
            subtype = subtype,
            group = subtitle,
            challengeRating = challengeRating.toFloat(),
            name = name,
            subtitle = subtitle,
            imageUrl = imageUrl,
            backgroundColorLight = backgroundColorLight,
            backgroundColorDark = backgroundColorDark,
            isHorizontalImage = isHorizontalImage == 1L,
            size = size,
            alignment = alignment,
            armorClass = armorClass.toInt(),
            hitPoints = hitPoints.toInt(),
            hitDice = hitDice,
            senses = senses,
            languages = languages,
            sourceName = sourceName,
            status = MonsterEntityStatus.entries[isClone.toInt()],
            imageContentScale = imageContentScale?.toInt(),
        )
    }
}
