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

package br.alexandregpereira.hunter.monster.compendium

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendium
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Item
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumItemState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateRecovery
import br.alexandregpereira.hunter.monster.compendium.state.MonsterPreviewState
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MonsterCompendiumViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val savedStateHandle: SavedStateHandle = mockk(relaxUnitFun = true)
    private val getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase = mockk()
    private val getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = mockk()
    private val saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = mockk()
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher = mockk()
    private val folderPreviewResultListener: FolderPreviewResultListener = mockk()
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher = mockk()
    private val monsterDetailEventListener: MonsterDetailEventListener = mockk()
    private val syncEventDispatcher: SyncEventDispatcher = mockk()
    private val syncEventListener: SyncEventListener = mockk()
    private val monsterRegistrationEventListener: MonsterRegistrationEventListener = mockk()
    private lateinit var viewModel: MonsterCompendiumViewModel

    @Before
    fun setup() {
        every { syncEventListener.events } returns flowOf()
    }

    @Test
    fun loadMonsters() = runTest {
        // Given
        val monsterCompendium = MonsterCompendium(
            items = listOf(
                Title(id = "da", value = "Any", isHeader = true),
                Title(id = "da2", value = "Any", isHeader = false),
                Title(id = "da3", value = "Any3", isHeader = false),
                Monster(
                    index = "zariel1",
                    name = "Zariel",
                    challengeRating = 0.5f,
                ).asItem(),
                Monster(
                    index = "zariel2",
                    name = "Zariel",
                    challengeRating = 1f,
                ).asItem()
            ),
            tableContent = listOf(
                TableContentItem(text = "Any", type = TableContentItemType.HEADER1),
                TableContentItem(text = "Any", type = TableContentItemType.HEADER2),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel1"),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel2"),
            ),
            alphabet = listOf("A", "Z")
        )

        every { folderPreviewResultListener.result } returns flowOf()
        every { monsterDetailEventListener.events } returns flowOf()
        every { monsterRegistrationEventListener.events } returns flowOf()
        every { getMonsterCompendiumUseCase() } returns flowOf(monsterCompendium)
        every { getLastScrollPositionUseCase() } returns flowOf(4)
        every { savedStateHandle.get<Boolean>(any()) } returns null
        createViewModel()

        val results = mutableListOf<MonsterCompendiumState>()
        val job = launch {
            viewModel.state.toList(results)
        }

        // When
        viewModel.loadMonsters()

        advanceUntilIdle()

        // Then
        verify { getMonsterCompendiumUseCase() }

        assertEquals(expected = 2, actual = results.size)
        assertEquals(expected = 4, actual = viewModel.initialScrollItemPosition)
        assertEquals(expected = MonsterCompendiumState(), actual = results[0])
        assertEquals(
            actual = results[1],
            expected = MonsterCompendiumState().copy(
                isLoading = false,
                items = listOf(
                    MonsterCompendiumItemState.Title(value = "Any", id = "da", isHeader = true),
                    MonsterCompendiumItemState.Title(value = "Any", id = "da2", isHeader = false),
                    MonsterCompendiumItemState.Title(value = "Any3", id = "da3", isHeader = false),
                    MonsterCompendiumItemState.Item(
                        monster = MonsterPreviewState(
                            index = "zariel1",
                            name = "Zariel",
                            imageUrl = "",
                            type = MonsterType.ABERRATION,
                            challengeRating = "1/2",
                            backgroundColorLight = "",
                            backgroundColorDark = "",
                            isImageHorizontal = false,
                        )
                    ),
                    MonsterCompendiumItemState.Item(
                        monster = MonsterPreviewState(
                            index = "zariel2",
                            name = "Zariel",
                            imageUrl = "",
                            type = MonsterType.ABERRATION,
                            challengeRating = "1",
                            backgroundColorLight = "",
                            backgroundColorDark = "",
                            isImageHorizontal = false,
                        )
                    ),
                ),
                alphabet = listOf("A", "Z"),
                alphabetSelectedIndex = 0,
                tableContent = listOf(
                    TableContentItem(text = "Any", type = TableContentItemType.HEADER1),
                    TableContentItem(text = "Any", type = TableContentItemType.HEADER2),
                    TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel1"),
                    TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel2"),
                ),
                tableContentIndex = 3
            )
        )

        job.cancel()
    }

    @Test
    fun onTableContentIndexClicked() = runTest {
        // Given
        val monsterCompendium = MonsterCompendium(
            items = listOf(
                Title(id = "da", value = "Any", isHeader = true),
                Title(id = "da2", value = "Any", isHeader = false),
                Title(id = "da3", value = "Any3", isHeader = false),
                Monster(
                    index = "zariel1",
                    name = "Zariel",
                ).asItem(),
                Monster(
                    index = "zariel2",
                    name = "Zariel",
                ).asItem()
            ),
            tableContent = listOf(
                TableContentItem(text = "Any", type = TableContentItemType.HEADER1),
                TableContentItem(text = "Any", type = TableContentItemType.HEADER2),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel1"),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel2"),
            ),
            alphabet = listOf("A", "Z")
        )

        every { folderPreviewResultListener.result } returns flowOf()
        every { monsterDetailEventListener.events } returns flowOf()
        every { monsterRegistrationEventListener.events } returns flowOf()
        every { getMonsterCompendiumUseCase() } returns flowOf(monsterCompendium)
        every { getLastScrollPositionUseCase() } returns flowOf(0)
        every { savedStateHandle.get<Boolean>(any()) } returns null
        createViewModel()


        val results = mutableListOf<MonsterCompendiumState>()
        val job = launch {
            viewModel.state.toList(results)
        }

        val actions = mutableListOf<MonsterCompendiumAction>()
        val actionJob = launch {
            viewModel.action.toList(actions)
        }

        // When
        viewModel.loadMonsters()
        advanceUntilIdle()
        viewModel.onPopupOpened()
        advanceUntilIdle()
        viewModel.onTableContentIndexClicked(3)
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = results[1].copy(popupOpened = true),
            actual = results[2]
        )
        assertEquals(
            expected = results[2].copy(popupOpened = false),
            actual = results[3]
        )
        assertEquals(
            expected = GoToCompendiumIndex(4),
            actual = actions.firstOrNull()
        )

        job.cancel()
        actionJob.cancel()
    }

    @Test
    fun `onAlphabetIndexClicked When is letter is already selected`() = runTest {
        // Given
        val monsterCompendium = MonsterCompendium(
            items = listOf(
                Title(id = "da", value = "Any", isHeader = true),
                Title(id = "da2", value = "Any", isHeader = false),
                Title(id = "da3", value = "Any3", isHeader = false),
                Title(id = "z", value = "Z", isHeader = false),
                Monster(
                    index = "zariel1",
                    name = "Zariel",
                ).asItem(),
                Monster(
                    index = "zariel2",
                    name = "Zariel",
                ).asItem()
            ),
            tableContent = listOf(
                TableContentItem(text = "A", type = TableContentItemType.HEADER1),
                TableContentItem(text = "Any", type = TableContentItemType.HEADER2),
                TableContentItem(text = "Z", type = TableContentItemType.HEADER2),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel1"),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel2"),
            ),
            alphabet = listOf("A", "Z")
        )

        every { saveScrollPositionUseCase(any()) } returns flowOf(Unit)
        every { folderPreviewResultListener.result } returns flowOf()
        every { monsterDetailEventListener.events } returns flowOf()
        every { monsterRegistrationEventListener.events } returns flowOf()
        every { getMonsterCompendiumUseCase() } returns flowOf(monsterCompendium)
        every { getLastScrollPositionUseCase() } returns flowOf(0)
        every { savedStateHandle.get<Boolean>(any()) } returns null
        createViewModel()


        val results = mutableListOf<MonsterCompendiumState>()
        val job = launch {
            viewModel.state.toList(results)
        }

        // When
        viewModel.loadMonsters()
        advanceUntilIdle()
        viewModel.onFirstVisibleItemChange(5)
        advanceUntilIdle()
        viewModel.onPopupOpened()
        advanceUntilIdle()
        viewModel.onAlphabetIndexClicked(1)
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = results[3].copy(
                popupOpened = true,
                tableContentOpened = true,
                tableContentInitialIndex = 4
            ),
            actual = results[4]
        )

        job.cancel()
    }

    @Test
    fun `onAlphabetIndexClicked When is letter is not selected`() = runTest {
        // Given
        val monsterCompendium = MonsterCompendium(
            items = listOf(
                Title(id = "da", value = "Any", isHeader = true),
                Title(id = "da2", value = "Any", isHeader = false),
                Title(id = "da3", value = "Any3", isHeader = false),
                Title(id = "z", value = "Z", isHeader = false),
                Monster(
                    index = "zariel1",
                    name = "Zariel",
                ).asItem(),
                Monster(
                    index = "zariel2",
                    name = "Zariel",
                ).asItem()
            ),
            tableContent = listOf(
                TableContentItem(text = "A", type = TableContentItemType.HEADER1),
                TableContentItem(text = "Any", type = TableContentItemType.HEADER2),
                TableContentItem(text = "Z", type = TableContentItemType.HEADER2),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel1"),
                TableContentItem(text = "Zariel", type = TableContentItemType.BODY, id = "zariel2"),
            ),
            alphabet = listOf("A", "Z")
        )

        every { saveScrollPositionUseCase(any()) } returns flowOf(Unit)
        every { folderPreviewResultListener.result } returns flowOf()
        every { monsterDetailEventListener.events } returns flowOf()
        every { monsterRegistrationEventListener.events } returns flowOf()
        every { getMonsterCompendiumUseCase() } returns flowOf(monsterCompendium)
        every { getLastScrollPositionUseCase() } returns flowOf(0)
        every { savedStateHandle.get<Boolean>(any()) } returns null
        createViewModel()


        val results = mutableListOf<MonsterCompendiumState>()
        val job = launch {
            viewModel.state.toList(results)
        }

        // When
        viewModel.loadMonsters()
        advanceUntilIdle()
        viewModel.onFirstVisibleItemChange(5)
        advanceUntilIdle()
        viewModel.onPopupOpened()
        advanceUntilIdle()
        viewModel.onAlphabetIndexClicked(0)
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = results[3].copy(
                popupOpened = true,
                tableContentOpened = true,
                tableContentInitialIndex = 0
            ),
            actual = results[4]
        )

        job.cancel()
    }

    private fun createViewModel() {
        viewModel = MonsterCompendiumViewModel(
            stateHolder = MonsterCompendiumStateHolder(
                getMonsterCompendiumUseCase = getMonsterCompendiumUseCase,
                getLastCompendiumScrollItemPositionUseCase = getLastScrollPositionUseCase,
                saveCompendiumScrollItemPositionUseCase = saveScrollPositionUseCase,
                folderPreviewEventDispatcher = folderPreviewEventDispatcher,
                folderPreviewResultListener = folderPreviewResultListener,
                monsterDetailEventDispatcher = monsterDetailEventDispatcher,
                monsterDetailEventListener = monsterDetailEventListener,
                syncEventDispatcher = syncEventDispatcher,
                syncEventListener = syncEventListener,
                monsterRegistrationEventListener = monsterRegistrationEventListener,
                loadOnInit = false,
                dispatcher = testCoroutineRule.testCoroutineDispatcher,
                analytics = mockk(relaxed = true),
                appLocalization = object : AppLocalization {
                    override fun getLanguage(): Language = Language.ENGLISH
                },
                stateRecovery = MonsterCompendiumStateRecovery()
            )
        )
    }

    private fun Monster.asItem(): Item = Item(this)
}
