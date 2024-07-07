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

import br.alexandregpereira.flow.test.assertFinalValue
import br.alexandregpereira.flow.test.assertHasNoMoreValues
import br.alexandregpereira.flow.test.assertNextValue
import br.alexandregpereira.flow.test.testFlows
import br.alexandregpereira.hunter.analytics.EmptyAnalytics
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.emptyMonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.emptyMonsterDetailEventListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.folder.preview.event.emptyFolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.emptyFolderPreviewResultListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendium
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Item
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAnalytics
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumItemState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.state.MonsterPreviewState
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import br.alexandregpereira.hunter.monster.registration.event.emptyMonsterRegistrationEventListener
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.sync.event.emptySyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.emptySyncEventListener
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MonsterCompendiumStateHolderTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher = emptyFolderPreviewEventDispatcher()
    private val folderPreviewResultListener: FolderPreviewResultListener = emptyFolderPreviewResultListener()
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher = emptyMonsterDetailEventDispatcher()
    private val monsterDetailEventListener: MonsterDetailEventListener = emptyMonsterDetailEventListener()
    private val syncEventDispatcher: SyncEventDispatcher = emptySyncEventDispatcher()
    private val syncEventListener: SyncEventListener = emptySyncEventListener()
    private val monsterRegistrationEventListener: MonsterRegistrationEventListener = emptyMonsterRegistrationEventListener()

    private lateinit var stateHolder: MonsterCompendiumStateHolder

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
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

        val getMonsterCompendiumUseCase = GetMonsterCompendiumUseCase {
            flowOf(monsterCompendium)
        }
        val getLastScrollPositionUseCase = GetLastCompendiumScrollItemPositionUseCase {
            flowOf(4)
        }
        createStateHolder(
            getMonsterCompendiumUseCase = getMonsterCompendiumUseCase,
            getLastScrollPositionUseCase = getLastScrollPositionUseCase,
        )

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action)

        // Then
        actions.assertHasNoMoreValues()
        assertEquals(expected = 4, actual = stateHolder.initialScrollItemPosition)

        states.assertNextValue(MonsterCompendiumState())
        states.assertFinalValue(
            MonsterCompendiumState().copy(
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

        val getMonsterCompendiumUseCase = GetMonsterCompendiumUseCase {
            flowOf(monsterCompendium)
        }
        createStateHolder(
            getMonsterCompendiumUseCase = getMonsterCompendiumUseCase,
        )

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.apply { advanceUntilIdle() }.onPopupOpened()
            advanceUntilIdle()
            stateHolder.onTableContentIndexClicked(3)
        }

        // Then
        assertEquals(
            expected = states[1].copy(popupOpened = true),
            actual = states[2]
        )
        assertEquals(
            expected = states[2].copy(popupOpened = false),
            actual = states[3]
        )
        actions.assertFinalValue(GoToCompendiumIndex(4))
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

        val getMonsterCompendiumUseCase = GetMonsterCompendiumUseCase {
            flowOf(monsterCompendium)
        }
        createStateHolder(
            getMonsterCompendiumUseCase = getMonsterCompendiumUseCase,
        )

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.apply { advanceUntilIdle() }.onFirstVisibleItemChange(5)
            advanceUntilIdle()
            stateHolder.onPopupOpened()
            advanceUntilIdle()
            stateHolder.onAlphabetIndexClicked(1)
            advanceUntilIdle()
        }

        // Then
        assertEquals(
            expected = states[3].copy(
                popupOpened = true,
                tableContentOpened = true,
                tableContentInitialIndex = 4
            ),
            actual = states[4]
        )

        actions.assertHasNoMoreValues()
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

        val getMonsterCompendiumUseCase = GetMonsterCompendiumUseCase {
            flowOf(monsterCompendium)
        }
        createStateHolder(
            getMonsterCompendiumUseCase = getMonsterCompendiumUseCase,
        )

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.apply { advanceUntilIdle() }.onFirstVisibleItemChange(5)
            advanceUntilIdle()
            stateHolder.onPopupOpened()
            advanceUntilIdle()
            stateHolder.onAlphabetIndexClicked(0)
            advanceUntilIdle()
        }

        // Then
        assertEquals(
            expected = states[3].copy(
                popupOpened = true,
                tableContentOpened = true,
                tableContentInitialIndex = 0
            ),
            actual = states[4]
        )

        actions.assertHasNoMoreValues()
    }

    private fun createStateHolder(
        getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase,
        getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = GetLastCompendiumScrollItemPositionUseCase { flowOf(0) },
        saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = SaveCompendiumScrollItemPositionUseCase { flowOf(Unit) }
    ) {
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
            dispatcher = testCoroutineDispatcher,
            analytics = MonsterCompendiumAnalytics(analytics = EmptyAnalytics()),
            appLocalization = object : AppLocalization {
                override fun getLanguage(): Language = Language.ENGLISH
            },
            stateRecovery = StateRecovery(initialState = MonsterCompendiumState.Empty)
        )
    }

    private fun Monster.asItem(): Item = Item(this)
}
