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
        )
    }
}
