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

package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.analytics.EmptyAnalytics
import br.alexandregpereira.hunter.domain.folder.AddMonsterToRecentlyViewedUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetRecentlyViewedMonstersUseCase
import br.alexandregpereira.hunter.domain.folder.MonsterFolderRepository
import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.home.domain.HomeContentTotals
import br.alexandregpereira.hunter.home.domain.HomeExtraContentProgress
import br.alexandregpereira.hunter.home.event.HomeEvent
import br.alexandregpereira.hunter.home.event.HomeEventDispatcher
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class HomeStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository = FakeMonsterFolderRepository()
    private val addMonsterToRecentlyViewed = AddMonsterToRecentlyViewedUseCase(repository)
    private val homeEventDispatcher = HomeEventDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `recently viewed monsters shown are not updated when the content changes`() = runTest {
        viewMonsters("goblin", "orc")
        val stateHolder = createStateHolder()
        advanceUntilIdle()

        viewMonsters("dragon", "goblin")
        repository.addMonsters(folderName = "Boss Fights", indexes = listOf("dragon")).single()
        homeEventDispatcher.dispatchEvent(HomeEvent.OnContentChanged())
        advanceUntilIdle()

        assertEquals(expected = listOf("orc", "goblin"), actual = stateHolder.recentlyViewedIndexes())
        assertEquals(expected = listOf("Boss Fights"), actual = stateHolder.folderNames())
    }

    @Test
    fun `recently viewed monsters are updated on refresh`() = runTest {
        viewMonsters("goblin", "orc")
        val stateHolder = createStateHolder()
        advanceUntilIdle()

        viewMonsters("dragon", "goblin")
        stateHolder.onRefreshRecentlyViewed()
        advanceUntilIdle()

        assertEquals(
            expected = listOf("goblin", "dragon", "orc"),
            actual = stateHolder.recentlyViewedIndexes(),
        )
    }

    @Test
    fun `recently viewed monsters are shown when the content changes after the first view`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        assertNull(stateHolder.recentlyViewedIndexes())

        viewMonsters("goblin")
        homeEventDispatcher.dispatchEvent(HomeEvent.OnContentChanged())
        advanceUntilIdle()

        assertEquals(expected = listOf("goblin"), actual = stateHolder.recentlyViewedIndexes())
    }

    @Test
    fun `recently viewed monsters shown are reloaded when a creature is deleted`() = runTest {
        viewMonsters("goblin", "orc")
        val stateHolder = createStateHolder()
        advanceUntilIdle()

        viewMonsters("dragon")
        repository.deleteMonster("orc")
        homeEventDispatcher.dispatchEvent(HomeEvent.OnContentChanged(wasCreatureDeleted = true))
        advanceUntilIdle()

        assertEquals(expected = listOf("dragon", "goblin"), actual = stateHolder.recentlyViewedIndexes())
    }

    @Test
    fun `recently viewed monsters shown are reloaded when a creature deletion is followed by another change`() = runTest {
        viewMonsters("goblin", "orc")
        val stateHolder = createStateHolder()
        advanceUntilIdle()

        repository.deleteMonster("orc")
        // Like the monster detail closing right after the monster deletion
        homeEventDispatcher.dispatchEvent(HomeEvent.OnContentChanged(wasCreatureDeleted = true))
        homeEventDispatcher.dispatchEvent(HomeEvent.OnContentChanged())
        advanceUntilIdle()

        assertEquals(expected = listOf("goblin"), actual = stateHolder.recentlyViewedIndexes())
    }

    private suspend fun viewMonsters(vararg indexes: String) {
        indexes.forEach { addMonsterToRecentlyViewed(it).single() }
    }

    private fun createStateHolder() = HomeStateHolder(
        appLocalization = FakeAppLocalization(),
        homeIntentHandler = object : HomeIntentHandler {
            override suspend fun onIntent(intent: HomeIntent) = Unit
        },
        getHomeContentTotals = {
            flowOf(HomeContentTotals(monsters = 10, spells = 10))
        },
        getHomeExtraContentProgress = {
            flowOf(HomeExtraContentProgress(added = 0, total = 0))
        },
        getMonsterFolders = GetMonsterFoldersUseCase(repository),
        getRecentlyViewedMonsters = GetRecentlyViewedMonstersUseCase(
            getMonstersByFolder = GetMonstersByFolderUseCase(repository),
        ),
        homeEventListener = homeEventDispatcher,
        analytics = HomeAnalytics(EmptyAnalytics()),
        dispatcher = testDispatcher,
    )

    private fun HomeStateHolder.recentlyViewedIndexes(): List<String>? {
        return state.value.viewState.sections
            .filterIsInstance<HomeSectionState.RecentlyViewed>()
            .firstOrNull()
            ?.monsters
            ?.map { it.index }
    }

    private fun HomeStateHolder.folderNames(): List<String>? {
        return state.value.viewState.sections
            .filterIsInstance<HomeSectionState.Folders>()
            .firstOrNull()
            ?.folders
            ?.map { it.name }
    }

    private class FakeAppLocalization : AppReactiveLocalization {

        override val languageFlow: Flow<Language> = flowOf(Language.ENGLISH)

        override fun getLanguage(): Language = Language.ENGLISH
    }

    /**
     * Keeps the entries of each folder from the most recently saved, like the database.
     */
    private class FakeMonsterFolderRepository : MonsterFolderRepository {

        private val folders = mutableMapOf<String, MutableList<String>>()
        private val deletedIndexes = mutableSetOf<String>()

        /**
         * Like the database, the folders don't return a deleted monster.
         */
        fun deleteMonster(index: String) {
            deletedIndexes.add(index)
        }

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
            val indexes = folders[this]
                ?.filterNot { it in deletedIndexes }
                ?.takeIf { it.isNotEmpty() }
                ?: return null
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
