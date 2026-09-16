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

package br.alexandregpereira.hunter.monster.compendium

import br.alexandregpereira.flow.test.assertFinalValue
import br.alexandregpereira.flow.test.assertHasNoMoreValues
import br.alexandregpereira.flow.test.assertNextValue
import br.alexandregpereira.flow.test.testFlows
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.CompendiumSortType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumSortTypeUseCase
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendium
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Item
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem.Title
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItemType
import br.alexandregpereira.hunter.monster.compendium.event.MonsterCompendiumEvent
import br.alexandregpereira.hunter.monster.compendium.event.MonsterCompendiumEventDispatcher
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAnalytics
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumItemState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.state.MonsterPreviewState
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.event.v2.EventDispatcher
import br.alexandregpereira.hunter.search.event.SearchEvent
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.emptySyncEventDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
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

    private val folderPreviewEvents = mutableListOf<FolderPreviewEvent>()
    private val folderPreviewEventDispatcher = object : FolderPreviewEventDispatcher {
        override fun dispatchEvent(event: FolderPreviewEvent) {
            folderPreviewEvents.add(event)
        }
    }
    private val folderDetailEvents = mutableListOf<FolderDetailEvent>()
    private val folderDetailEventDispatcher = object : FolderDetailEventDispatcher {
        override fun dispatchEvent(event: FolderDetailEvent) {
            folderDetailEvents.add(event)
        }
    }
    private val folderPreviewResultDispatcher = FolderPreviewResultDispatcher()
    private val monsterEvents = mutableListOf<MonsterEvent>()
    private val monsterDetailEventDispatcher = object : MonsterEventDispatcher {
        override val events: Flow<MonsterEvent> = emptyFlow()

        override fun dispatchEvent(event: MonsterEvent) {
            monsterEvents.add(event)
        }
    }
    private val syncEventDispatcher: SyncEventDispatcher = emptySyncEventDispatcher()
    private val monsterCompendiumEventDispatcher = MonsterCompendiumEventDispatcher()
    private val searchEvents = mutableListOf<SearchEvent>()
    private val searchEventDispatcher = object : EventDispatcher<SearchEvent> {
        override val events: Flow<SearchEvent> = emptyFlow()

        override fun dispatchEvent(event: SearchEvent) {
            searchEvents.add(event)
        }
    }

    private val analytics = FakeAnalytics()
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
                MonsterFactory.createEmpty(
                    index = "zariel1",
                ).copy(
                    name = "Zariel",
                    challengeRatingData = ChallengeRating.create(0.5f),
                ).asItem(),
                MonsterFactory.createEmpty(
                    index = "zariel2",
                ).copy(
                    name = "Zariel",
                    challengeRatingData = ChallengeRating.create(1f),
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
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            showCompendium()
        }

        // Then
        actions.assertHasNoMoreValues()
        assertEquals(expected = 4, actual = stateHolder.initialScrollItemPosition)

        states.assertNextValue(MonsterCompendiumState())
        states.assertNextValue(MonsterCompendiumState(isShowing = true))
        states.assertFinalValue(
            MonsterCompendiumState().copy(
                isShowing = true,
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
                MonsterFactory.createEmpty(
                    index = "zariel1",
                ).copy(
                    name = "Zariel",
                ).asItem(),
                MonsterFactory.createEmpty(
                    index = "zariel2",
                ).copy(
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

        showCompendium()

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.onPopupOpened()
            advanceUntilIdle()
            stateHolder.onTableContentIndexClicked(3)
        }

        // Then
        assertEquals(
            expected = states[0].copy(popupOpened = true),
            actual = states[1]
        )
        assertEquals(
            expected = states[1].copy(popupOpened = false),
            actual = states[2]
        )
        actions.assertFinalValue(GoToCompendiumIndex(4, shouldAnimate = false))
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
                MonsterFactory.createEmpty(
                    index = "zariel1",
                ).copy(
                    name = "Zariel",
                ).asItem(),
                MonsterFactory.createEmpty(
                    index = "zariel2",
                ).copy(
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
        showCompendium()

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.onFirstVisibleItemChange(5)
            advanceUntilIdle()
            stateHolder.onPopupOpened()
            advanceUntilIdle()
            stateHolder.onAlphabetIndexClicked(1)
            advanceUntilIdle()
        }

        // Then
        assertEquals(
            expected = states[2].copy(
                popupOpened = true,
                tableContentOpened = true,
                tableContentInitialIndex = 4
            ),
            actual = states[3]
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
                MonsterFactory.createEmpty(
                    index = "zariel1",
                ).copy(
                    name = "Zariel",
                ).asItem(),
                MonsterFactory.createEmpty(
                    index = "zariel2",
                ).copy(
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
        showCompendium()

        // When
        val (states, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.onFirstVisibleItemChange(5)
            advanceUntilIdle()
            stateHolder.onPopupOpened()
            advanceUntilIdle()
            stateHolder.onAlphabetIndexClicked(0)
            advanceUntilIdle()
        }

        // Then
        assertEquals(
            expected = states[2].copy(
                popupOpened = true,
                tableContentOpened = true,
                tableContentInitialIndex = 0
            ),
            actual = states[3]
        )

        actions.assertHasNoMoreValues()
    }

    @Test
    fun onSearchClick() = runTest {
        // Given
        createStateHolder(
            getMonsterCompendiumUseCase = {
                flowOf(
                    MonsterCompendium(
                        items = emptyList(),
                        tableContent = emptyList(),
                        alphabet = emptyList(),
                    )
                )
            },
        )

        // When
        stateHolder.onSearchClick()

        // Then
        assertEquals(expected = listOf<SearchEvent>(SearchEvent.Show), actual = searchEvents)
    }

    @Test
    fun `Show event opens the compendium and onClose closes it`() = runTest {
        // Given
        createStateHolder(
            getMonsterCompendiumUseCase = {
                flowOf(
                    MonsterCompendium(
                        items = emptyList(),
                        tableContent = emptyList(),
                        alphabet = emptyList(),
                    )
                )
            },
        )
        advanceUntilIdle()

        // When
        monsterCompendiumEventDispatcher.dispatchEvent(MonsterCompendiumEvent.Show())
        advanceUntilIdle()
        val isShowingAfterShowEvent = stateHolder.state.value.isShowing
        stateHolder.onClose()

        // Then
        assertEquals(expected = true, actual = isShowingAfterShowEvent)
        assertEquals(expected = false, actual = stateHolder.state.value.isShowing)
    }

    @Test
    fun onSortOptionSelected() = runTest {
        // Given
        val savedSortTypes = mutableListOf<CompendiumSortType>()
        createStateHolder(
            getMonsterCompendiumUseCase = {
                flowOf(
                    MonsterCompendium(
                        items = emptyList(),
                        tableContent = emptyList(),
                        alphabet = emptyList(),
                    )
                )
            },
            saveSortTypeUseCase = { sortType ->
                savedSortTypes.add(sortType)
                flowOf(Unit)
            },
        )

        // When
        val (_, actions) = testFlows(stateHolder.state, stateHolder.action) {
            stateHolder.apply { advanceUntilIdle() }.onSortOptionSelected(1)
            advanceUntilIdle()
        }

        // Then
        assertEquals(expected = listOf(CompendiumSortType.CHALLENGE_RATING_ASC), actual = savedSortTypes)
        actions.assertFinalValue(GoToCompendiumIndex(0, shouldAnimate = false))
    }

    @Test
    fun `onFolderCreationClick enables the folder creation mode and onFolderCreationClose disables it`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())

        // When
        stateHolder.onFolderCreationClick()
        val isFolderCreationModeAfterClick = stateHolder.state.value.isFolderCreationMode
        stateHolder.onFolderCreationClose()

        // Then
        assertEquals(expected = true, actual = isFolderCreationModeAfterClick)
        assertEquals(expected = false, actual = stateHolder.state.value.isFolderCreationMode)
        assertEquals(expected = emptyList(), actual = folderPreviewEvents)
    }

    @Test
    fun `onItemClick adds the monster to the folder preview When the folder creation mode is enabled`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        stateHolder.onFolderCreationClick()

        // When
        stateHolder.onItemClick("zariel")

        // Then
        assertEquals(
            expected = listOf<FolderPreviewEvent>(FolderPreviewEvent.AddMonster("zariel")),
            actual = folderPreviewEvents,
        )
        assertEquals(expected = emptyList(), actual = monsterEvents)
    }

    @Test
    fun `onItemLongClick shows the monster detail When the folder creation mode is enabled`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        stateHolder.onFolderCreationClick()

        // When
        stateHolder.onItemLongClick("zariel")

        // Then
        assertEquals(
            expected = listOf<MonsterEvent>(
                Show("zariel", enableMonsterPageChangesEventDispatch = true)
            ),
            actual = monsterEvents,
        )
        assertEquals(expected = emptyList(), actual = folderPreviewEvents)
    }

    @Test
    fun `onItemClick shows the monster detail and onItemLongClick adds it to the folder preview When the folder creation mode is disabled`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())

        // When
        stateHolder.onItemClick("zariel1")
        stateHolder.onItemLongClick("zariel2")

        // Then
        assertEquals(
            expected = listOf<MonsterEvent>(
                Show("zariel1", enableMonsterPageChangesEventDispatch = true)
            ),
            actual = monsterEvents,
        )
        assertEquals(
            expected = listOf<FolderPreviewEvent>(FolderPreviewEvent.AddMonster("zariel2")),
            actual = folderPreviewEvents,
        )
    }

    @Test
    fun `onFolderCreationConfirm saves the folder preview and keeps the folder creation mode until the folder is saved`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        showCompendium()
        stateHolder.onFolderCreationClick()

        // When
        stateHolder.onFolderCreationConfirm()
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = listOf<FolderPreviewEvent>(FolderPreviewEvent.Save),
            actual = folderPreviewEvents,
        )
        assertEquals(expected = true, actual = stateHolder.state.value.isFolderCreationMode)
        assertEquals(expected = emptyList(), actual = folderDetailEvents)
    }

    @Test
    fun `When the folder preview is saved Then the folder is opened and the folder creation mode is disabled`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        showCompendium()
        stateHolder.onFolderCreationClick()

        // When
        folderPreviewResultDispatcher.dispatchEvent(FolderPreviewResult.OnSaved(folderName = "Dragons"))
        advanceUntilIdle()

        // Then
        assertEquals(expected = false, actual = stateHolder.state.value.isFolderCreationMode)
        assertEquals(
            expected = listOf<FolderDetailEvent>(FolderDetailEvent.Show("Dragons")),
            actual = folderDetailEvents,
        )
    }

    @Test
    fun `When the folder preview is saved after the compendium is shown twice Then the folder is opened once`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        showCompendium()
        showCompendium()

        // When
        folderPreviewResultDispatcher.dispatchEvent(FolderPreviewResult.OnSaved(folderName = "Dragons"))
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = listOf<FolderDetailEvent>(FolderDetailEvent.Show("Dragons")),
            actual = folderDetailEvents,
        )
    }

    @Test
    fun `When the folder preview is saved after the state holder is cleared while open Then the folder is opened`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        showCompendium()
        stateHolder.onFolderCreationClick()

        // When
        // Like the screen leaving the composition on a rotation while the compendium is open
        stateHolder.onCleared()
        folderPreviewResultDispatcher.dispatchEvent(FolderPreviewResult.OnSaved(folderName = "Dragons"))
        advanceUntilIdle()

        // Then
        assertEquals(expected = false, actual = stateHolder.state.value.isFolderCreationMode)
        assertEquals(
            expected = listOf<FolderDetailEvent>(FolderDetailEvent.Show("Dragons")),
            actual = folderDetailEvents,
        )
    }

    @Test
    fun `When the folder preview is saved after the compendium is closed Then the folder is not opened`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        showCompendium()
        stateHolder.onFolderCreationClick()

        // When
        stateHolder.onClose()
        folderPreviewResultDispatcher.dispatchEvent(FolderPreviewResult.OnSaved(folderName = "Dragons"))
        advanceUntilIdle()

        // Then
        assertEquals(expected = false, actual = stateHolder.state.value.isFolderCreationMode)
        assertEquals(expected = emptyList(), actual = folderDetailEvents)
    }

    @Test
    fun `Show event with folder creation opens the compendium with the folder creation mode enabled`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = emptyMonsterCompendiumUseCase())
        advanceUntilIdle()

        // When
        monsterCompendiumEventDispatcher.dispatchEvent(
            MonsterCompendiumEvent.Show(showFolderCreation = true)
        )
        advanceUntilIdle()

        // Then
        assertEquals(expected = true, actual = stateHolder.state.value.isShowing)
        assertEquals(expected = false, actual = stateHolder.state.value.isLoading)
        assertEquals(expected = true, actual = stateHolder.state.value.isFolderCreationMode)
    }

    @Test
    fun `scrolling down tracks one event per depth threshold reached`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = monsterCompendiumUseCaseOf(itemsSize = 100))
        showCompendium()
        analytics.events.clear()

        // When
        listOf(0, 10, 20, 45, 70, 90).forEach { scrollTo(it) }
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = listOf(
                "MonsterCompendium - scroll started" to null,
                "MonsterCompendium - scroll depth reached" to 25,
                "MonsterCompendium - scroll depth reached" to 50,
                "MonsterCompendium - scroll depth reached" to 75,
                "MonsterCompendium - scroll depth reached" to 100,
            ),
            actual = analytics.scrollEvents(),
        )
        assertEquals(
            expected = mapOf(
                "direction" to "DOWN",
                "depthPercent" to 100,
                "itemIndex" to 99,
                "itemsSize" to 100,
                "baselineDepthPercent" to 10,
                "thresholdPercent" to 100,
                "sortType" to CompendiumSortType.ALPHABETICAL.name,
            ),
            actual = analytics.events.last().second,
        )
    }

    @Test
    fun `scrolling up tracks one event per depth threshold reached`() = runTest {
        // Given
        createStateHolder(
            getMonsterCompendiumUseCase = monsterCompendiumUseCaseOf(itemsSize = 100),
            getLastScrollPositionUseCase = GetLastCompendiumScrollItemPositionUseCase { flowOf(90) },
        )
        showCompendium()
        analytics.events.clear()

        // When
        listOf(90, 85, 70, 45, 20, 0).forEach { scrollTo(it) }
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = listOf(
                "MonsterCompendium - scroll started" to null,
                "MonsterCompendium - scroll depth reached" to 75,
                "MonsterCompendium - scroll depth reached" to 50,
                "MonsterCompendium - scroll depth reached" to 25,
                "MonsterCompendium - scroll depth reached" to 0,
            ),
            actual = analytics.scrollEvents(),
        )
        assertEquals(
            expected = "UP",
            actual = analytics.events.last().second["direction"],
        )
    }

    @Test
    fun `browsing back over what was already seen is not tracked again`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = monsterCompendiumUseCaseOf(itemsSize = 100))
        showCompendium()
        listOf(45, 70, 20).forEach { scrollTo(it) }
        advanceUntilIdle()
        analytics.events.clear()

        // When
        listOf(70, 45, 20, 40, 65).forEach { scrollTo(it) }
        advanceUntilIdle()

        // Then
        assertEquals(expected = emptyList(), actual = analytics.eventNames())
    }

    @Test
    fun `closing tracks how far the visit browsed in both directions`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = monsterCompendiumUseCaseOf(itemsSize = 100))
        showCompendium()
        listOf(45, 70, 20).forEach { scrollTo(it) }
        advanceUntilIdle()
        analytics.events.clear()

        // When
        stateHolder.onClose()
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = listOf(
                "MonsterCompendium - closed" to mapOf<String, Any?>(
                    "didScroll" to true,
                    "didScrollDown" to true,
                    "didScrollUp" to true,
                    "maxDepthPercent" to 80,
                    "minTopPercent" to 20,
                    "maxItemIndex" to 79,
                    "minItemIndex" to 20,
                    "itemsSize" to 100,
                    "baselineDepthPercent" to 55,
                    "baselineTopPercent" to 45,
                )
            ),
            actual = analytics.events,
        )
    }

    /**
     * A restored scroll position is not somewhere the user scrolled to, so the thresholds it
     * already passed must not be tracked when the compendium opens on it.
     */
    @Test
    fun `a restored scroll position is not tracked as scrolling`() = runTest {
        // Given
        createStateHolder(
            getMonsterCompendiumUseCase = monsterCompendiumUseCaseOf(itemsSize = 100),
            getLastScrollPositionUseCase = GetLastCompendiumScrollItemPositionUseCase { flowOf(50) },
        )
        showCompendium()
        analytics.events.clear()

        // When
        scrollTo(50)
        advanceUntilIdle()

        // Then
        assertEquals(expected = emptyList(), actual = analytics.eventNames())

        // And the thresholds it already passed are still to be reached, in both directions
        scrollTo(70)
        scrollTo(20)
        advanceUntilIdle()
        assertEquals(
            expected = listOf(
                "MonsterCompendium - scroll started" to null,
                "MonsterCompendium - scroll depth reached" to 75,
                "MonsterCompendium - scroll depth reached" to 25,
            ),
            actual = analytics.scrollEvents(),
        )
    }

    @Test
    fun `reopening the compendium tracks the depth thresholds again`() = runTest {
        // Given
        createStateHolder(getMonsterCompendiumUseCase = monsterCompendiumUseCaseOf(itemsSize = 100))
        showCompendium()
        listOf(0, 90).forEach { scrollTo(it) }
        advanceUntilIdle()
        stateHolder.onClose()
        advanceUntilIdle()
        analytics.events.clear()

        // When
        showCompendium()
        listOf(0, 90).forEach { scrollTo(it) }
        advanceUntilIdle()

        // Then
        assertEquals(
            expected = listOf(
                "MonsterCompendium - scroll started" to null,
                "MonsterCompendium - scroll depth reached" to 100,
            ),
            actual = analytics.scrollEvents(),
        )
    }

    /**
     * Scrolls so [firstVisibleItemIndex] is at the top of a screen showing [VIEWPORT_ITEMS] items,
     * like a real viewport with a frontier at each end.
     */
    private fun scrollTo(firstVisibleItemIndex: Int) {
        val itemsSize = stateHolder.state.value.items.size
        stateHolder.onVisibleItemsChange(
            firstVisibleItemIndex = firstVisibleItemIndex,
            lastVisibleItemIndex = (firstVisibleItemIndex + VIEWPORT_ITEMS - 1)
                .coerceAtMost(itemsSize - 1),
            itemsSize = itemsSize,
        )
    }

    /**
     * A compendium of [itemsSize] items: a section title followed by monsters. The title is
     * required, since the alphabet index of an item is resolved from the title above it.
     */
    private fun monsterCompendiumUseCaseOf(itemsSize: Int) = GetMonsterCompendiumUseCase {
        flowOf(
            MonsterCompendium(
                items = listOf(Title(id = "a", value = "A", isHeader = true)) +
                        (1 until itemsSize).map { index ->
                            MonsterFactory.createEmpty(index = "monster$index").asItem()
                        },
                tableContent = listOf(
                    TableContentItem(text = "A", type = TableContentItemType.HEADER1, id = "a"),
                ),
                alphabet = listOf("A"),
            )
        )
    }

    private fun emptyMonsterCompendiumUseCase() = GetMonsterCompendiumUseCase {
        flowOf(
            MonsterCompendium(
                items = emptyList(),
                tableContent = emptyList(),
                alphabet = emptyList(),
            )
        )
    }

    /**
     * The compendium is loaded only when it is shown. The event is dispatched after the state
     * holder starts listening, since the event is not replayed.
     */
    private fun TestScope.showCompendium() {
        advanceUntilIdle()
        monsterCompendiumEventDispatcher.dispatchEvent(MonsterCompendiumEvent.Show())
        advanceUntilIdle()
    }

    private fun createStateHolder(
        getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase,
        getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = GetLastCompendiumScrollItemPositionUseCase { flowOf(0) },
        saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = SaveCompendiumScrollItemPositionUseCase { flowOf(Unit) },
        saveSortTypeUseCase: SaveCompendiumSortTypeUseCase = SaveCompendiumSortTypeUseCase { flowOf(Unit) },
    ) {
        stateHolder = MonsterCompendiumStateHolder(
            getMonsterCompendiumUseCase = getMonsterCompendiumUseCase,
            getLastCompendiumScrollItemPositionUseCase = getLastScrollPositionUseCase,
            saveCompendiumScrollItemPositionUseCase = saveScrollPositionUseCase,
            saveCompendiumSortTypeUseCase = saveSortTypeUseCase,
            folderPreviewEventDispatcher = folderPreviewEventDispatcher,
            monsterEventDispatcher = monsterDetailEventDispatcher,
            syncEventDispatcher = syncEventDispatcher,
            searchEventDispatcher = searchEventDispatcher,
            dispatcher = testCoroutineDispatcher,
            analytics = MonsterCompendiumAnalytics(analytics = analytics),
            appLocalization = object : AppReactiveLocalization {
                override val languageFlow: Flow<Language> = flowOf(Language.ENGLISH)
                override fun getLanguage(): Language = Language.ENGLISH
            },
            isFirstTime = { false },
            monsterCompendiumEventListener = monsterCompendiumEventDispatcher,
            folderPreviewResultListener = folderPreviewResultDispatcher,
            folderDetailEventDispatcher = folderDetailEventDispatcher,
        )
    }

    private fun Monster.asItem(): Item = Item(this)

    private companion object {
        const val VIEWPORT_ITEMS = 10
    }

    private class FakeAnalytics : Analytics {

        val events = mutableListOf<Pair<String, Map<String, Any?>>>()

        fun eventNames(): List<String> = events.map { it.first }

        /** The scroll events with the threshold each one reached, null when it started one. */
        fun scrollEvents(): List<Pair<String, Int?>> = events.filter { it.first.contains("scroll") }
            .map { (name, params) -> name to params["thresholdPercent"] as Int? }

        override fun track(eventName: String, params: Map<String, Any?>) {
            events.add(eventName to params)
        }

        override fun setUserProperty(name: String, value: Any) {}

        override fun getDeviceId(): String? = null

        override fun logException(throwable: Throwable) {}
    }
}
