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

package br.alexandregpereira.hunter.domain.folder

import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RecentlyViewedMonstersTest {

    private val repository = FakeMonsterFolderRepository()
    private val addMonsterToRecentlyViewed = AddMonsterToRecentlyViewedUseCase(repository)
    private val getRecentlyViewedMonsters = GetRecentlyViewedMonstersUseCase(
        getMonstersByFolder = GetMonstersByFolderUseCase(repository),
    )

    @Test
    fun `recently viewed monsters are ordered from the most recent`() = runTest {
        listOf("goblin", "orc", "dragon").forEach { addMonsterToRecentlyViewed(it).single() }

        assertEquals(
            expected = listOf("dragon", "orc", "goblin"),
            actual = getRecentlyViewedMonsters().first().map { it.index },
        )
    }

    @Test
    fun `viewing a monster again moves it to the top`() = runTest {
        listOf("goblin", "orc", "dragon", "goblin").forEach { addMonsterToRecentlyViewed(it).single() }

        assertEquals(
            expected = listOf("goblin", "dragon", "orc"),
            actual = getRecentlyViewedMonsters().first().map { it.index },
        )
    }

    @Test
    fun `only the most recent monsters within the limit are kept`() = runTest {
        val indexes = (1..RECENTLY_VIEWED_MONSTERS_LIMIT + 2).map { "monster-$it" }
        indexes.forEach { addMonsterToRecentlyViewed(it).single() }

        val expected = indexes.reversed().take(RECENTLY_VIEWED_MONSTERS_LIMIT)
        assertEquals(
            expected = expected,
            actual = getRecentlyViewedMonsters().first().map { it.index },
        )
        assertEquals(
            expected = expected,
            actual = repository.getMonstersFromFolder(RECENTLY_VIEWED_FOLDER_NAME).first()
                ?.monsters?.map { it.index },
        )
    }

    @Test
    fun `recently viewed folder is not listed with the user folders`() = runTest {
        addMonsterToRecentlyViewed("goblin").single()
        repository.addMonsters(folderName = "Boss Fights", indexes = listOf("dragon")).single()

        assertEquals(
            expected = listOf("Boss Fights"),
            actual = GetMonsterFoldersUseCase(repository)().first().map { it.name },
        )
    }

    /**
     * Keeps the entries of each folder from the most recently saved, like the database.
     */
    private class FakeMonsterFolderRepository : MonsterFolderRepository {

        private val folders = mutableMapOf<String, MutableList<String>>()

        override fun addMonsters(folderName: String, indexes: List<String>): Flow<Unit> = flow {
            val folder = folders.getOrPut(folderName) { mutableListOf() }
            indexes.forEach { index ->
                folder.remove(index)
                folder.add(0, index)
            }
            emit(Unit)
        }

        override fun removeMonsters(folderName: String, indexes: List<String>): Flow<Unit> = flow {
            folders[folderName]?.removeAll(indexes)
            emit(Unit)
        }

        override fun getMonsterFolders(): Flow<List<MonsterFolder>> = flow {
            emit(folders.keys.mapNotNull { it.toMonsterFolder() })
        }

        override fun getMonstersFromFolder(folderName: String): Flow<MonsterFolder?> = flow {
            emit(folderName.toMonsterFolder())
        }

        override fun getMonstersFromFolders(foldersName: List<String>): Flow<List<MonsterPreviewFolder>> = flow {
            emit(foldersName.mapNotNull { it.toMonsterFolder() }.flatMap { it.monsters })
        }

        override fun removeMonsterFolders(folderNames: List<String>): Flow<Unit> = flow {
            folderNames.forEach { folders.remove(it) }
            emit(Unit)
        }

        private fun String.toMonsterFolder(): MonsterFolder? {
            val indexes = folders[this]?.takeIf { it.isNotEmpty() } ?: return null
            return MonsterFolder(
                name = this,
                monsters = indexes.map { index ->
                    MonsterPreviewFolder(
                        index = index,
                        backgroundColorLight = "",
                        backgroundColorDark = "",
                        imageContentScale = MonsterPreviewFolderImageContentScale.Fit,
                    )
                },
            )
        }
    }
}
