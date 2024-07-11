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
            ) to MonsterEntity(
                index = it.index,
                type = it.type,
                subtype = it.subtype,
                group = it.subtitle,
                challengeRating = it.challengeRating.toFloat(),
                name = it.name,
                subtitle = it.subtitle,
                imageUrl = it.imageUrl,
                backgroundColorLight = it.backgroundColorLight,
                backgroundColorDark = it.backgroundColorDark,
                isHorizontalImage = it.isHorizontalImage == 1L,
                size = it.size,
                alignment = it.alignment,
                armorClass = it.armorClass.toInt(),
                hitPoints = it.hitPoints.toInt(),
                hitDice = it.hitDice,
                senses = it.senses,
                languages = it.languages,
                sourceName = it.sourceName,
                status = MonsterEntityStatus.entries[it.isClone.toInt()],
            )
        }.groupBy(keySelector = { it.first }, valueTransform = { it.second })
    }
}
