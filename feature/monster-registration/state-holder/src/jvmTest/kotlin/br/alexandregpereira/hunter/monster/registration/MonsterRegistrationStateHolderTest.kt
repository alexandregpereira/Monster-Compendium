/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.flow.test.assertFinalValue
import br.alexandregpereira.flow.test.assertHasNoMoreValues
import br.alexandregpereira.flow.test.testFlow
import br.alexandregpereira.flow.test.testFlows
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.event.EventManager
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.monster.registration.domain.MonsterRegistrationFileManager
import br.alexandregpereira.hunter.monster.registration.domain.NormalizeMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.domain.SaveMonsterUseCase
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.emptySpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.event.SpellResult
import br.alexandregpereira.hunter.state.StateHolderParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
@OptIn(ExperimentalCoroutinesApi::class)
internal class MonsterRegistrationStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val analytics: Analytics = mockk(relaxUnitFun = true)
    private val eventManager = EventManager<MonsterRegistrationEvent>()
    private val eventResultManager = EventManager<MonsterRegistrationResult>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Show event fetches monster and opens with loaded state`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()

        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        assertTrue(stateHolder.state.value.isOpen)
        assertFalse(stateHolder.state.value.isLoading)
        assertEquals("goblin", stateHolder.state.value.monster.index)
    }

    @Test
    fun `Hide event closes the state`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        eventManager.dispatchEvent(MonsterRegistrationEvent.Hide)
        advanceUntilIdle()

        assertFalse(stateHolder.state.value.isOpen)
    }

    @Test
    fun `Hide event resets isSaveButtonEnabled and calls fileManager clear`() = runTest {
        val fileManager: MonsterRegistrationFileManager = mockk(relaxed = true)
        val stateHolder = createStateHolder(fileManager = fileManager)
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()
        val modifiedMonster = stateHolder.state.value.monster.copy(
            info = stateHolder.state.value.monster.info.copy(name = "Hobgoblin")
        )
        stateHolder.onMonsterChanged(modifiedMonster)
        advanceUntilIdle()
        assertTrue(stateHolder.state.value.isSaveButtonEnabled)

        eventManager.dispatchEvent(MonsterRegistrationEvent.Hide)
        advanceUntilIdle()

        assertFalse(stateHolder.state.value.isSaveButtonEnabled)
        verify { fileManager.clear() }
    }

    @Test
    fun `onClose deletes last saved image and closes`() = runTest {
        val fileManager: MonsterRegistrationFileManager = mockk(relaxed = true)
        val stateHolder = createStateHolder(fileManager = fileManager)
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onClose()
        advanceUntilIdle()

        coVerify { fileManager.deleteLastSavedImageIfExists() }
        assertFalse(stateHolder.state.value.isOpen)
    }

    @Test
    fun `onSaved dispatches OnSaved result and closes`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        val (states, results) = testFlows(stateHolder.state, eventResultManager.events) {
            stateHolder.onSaved()
            advanceUntilIdle()
        }

        results.assertFinalValue(MonsterRegistrationResult.OnSaved(monsterIndex = "goblin"))
        assertFalse(states.last().isOpen)
    }

    @Test
    fun `onSaved when metadata monster is null logs exception`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        // Do NOT dispatch Show — metadata.monster stays null

        stateHolder.onSaved()
        advanceUntilIdle()

        verify { analytics.logException(any()) }
    }

    @Test
    fun `onSaved when normalizeMonster throws logs exception and keeps state open`() = runTest {
        val stateHolder = createStateHolder(
            normalizeMonster = NormalizeMonsterUseCase { flow { throw RuntimeException("normalize failed") } }
        )
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onSaved()
        advanceUntilIdle()

        verify { analytics.logException(any()) }
        assertTrue(stateHolder.state.value.isOpen)
    }

    @Test
    fun `onMonsterImagePickClick dispatches PickImage action`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        val actions = testFlow(stateHolder.action) {
            stateHolder.onMonsterImagePickClick()
            advanceUntilIdle()
        }

        actions.assertFinalValue(MonsterRegistrationAction.PickImage)
    }

    @Test
    fun `onMonsterImagePicked null does not call fileManager`() = runTest {
        val fileManager: MonsterRegistrationFileManager = mockk(relaxed = true)
        val stateHolder = createStateHolder(fileManager = fileManager)
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onMonsterImagePicked(null)
        advanceUntilIdle()

        coVerify(exactly = 0) { fileManager.saveImage(any(), any()) }
    }

    @Test
    fun `onMonsterImagePicked saves image and updates monster imageUrl`() = runTest {
        val imagePath = "file:///storage/goblin-123.png"
        val fileManager: MonsterRegistrationFileManager = mockk(relaxed = true) {
            coEvery { saveImage(any(), any()) } returns imagePath
        }
        val stateHolder = createStateHolder(fileManager = fileManager)
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        val file: FileEntry = mockk()
        stateHolder.onMonsterImagePicked(file)
        advanceUntilIdle()

        coVerify { fileManager.saveImage(any(), eq("goblin")) }
        assertEquals(imagePath, stateHolder.state.value.monster.info.imageUrl)
    }

    @Test
    fun `onMonsterImagePicked when fileManager throws logs exception`() = runTest {
        val fileManager: MonsterRegistrationFileManager = mockk(relaxed = true) {
            coEvery { saveImage(any(), any()) } throws RuntimeException("disk full")
        }
        val stateHolder = createStateHolder(fileManager = fileManager)
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onMonsterImagePicked(mockk())
        advanceUntilIdle()

        verify { analytics.logException(any()) }
    }

    @Test
    fun `onSpellClick dispatches SpellCompendium Show event`() = runTest {
        val spellCompendiumDispatcher: SpellCompendiumEventResultDispatcher = mockk {
            every { dispatchEventResult(any()) } returns emptyFlow()
        }
        val stateHolder = createStateHolder(spellCompendiumEventDispatcher = spellCompendiumDispatcher)
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onSpellClick("fireball")
        advanceUntilIdle()

        verify {
            spellCompendiumDispatcher.dispatchEventResult(
                SpellCompendiumEvent.Show(spellIndex = "fireball", selectedSpellIndexes = emptyList())
            )
        }
    }

    @Test
    fun `onSpellClick OnSpellLongClick result opens spell detail`() = runTest {
        val spellDetailDispatcher: SpellDetailEventDispatcher = mockk(relaxUnitFun = true)
        val spellCompendiumDispatcher: SpellCompendiumEventResultDispatcher = mockk {
            every { dispatchEventResult(any()) } returns flowOf(SpellCompendiumResult.OnSpellLongClick("fireball"))
        }
        val stateHolder = createStateHolder(
            spellCompendiumEventDispatcher = spellCompendiumDispatcher,
            spellDetailEventDispatcher = spellDetailDispatcher,
        )
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onSpellClick("fireball")
        advanceUntilIdle()

        verify { spellDetailDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell("fireball")) }
    }

    @Test
    fun `onSpellClick OnSpellClick result calls getSpell with new spell index`() = runTest {
        val getSpell: GetSpellUseCase = mockk(relaxed = true)
        val spellCompendiumDispatcher: SpellCompendiumEventResultDispatcher = mockk {
            every { dispatchEventResult(ofType<SpellCompendiumEvent.Show>()) } returns
                    flowOf(SpellCompendiumResult.OnSpellClick("fireball"))
            every { dispatchEventResult(SpellCompendiumEvent.Hide) } returns emptyFlow()
        }
        val stateHolder = createStateHolder(
            spellCompendiumEventDispatcher = spellCompendiumDispatcher,
            getSpell = getSpell,
        )
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onSpellClick("magic-missile")
        advanceUntilIdle()

        verify { getSpell.invoke("fireball") }
    }

    @Test
    fun `spell result OnChanged event refetches monster`() = runTest {
        val getMonster: GetMonsterUseCase = mockk<GetMonsterUseCase>().also {
            every { it.invoke(any()) } returns flowOf(MonsterFactory.createEmpty("goblin"))
        }
        val spellResultFlow = MutableSharedFlow<SpellResult>(extraBufferCapacity = 1)
        val spellResultListener = object : EventListener<SpellResult> {
            override val events: Flow<SpellResult> = spellResultFlow
        }
        createStateHolder(
            getMonster = getMonster,
            spellResultListener = spellResultListener,
        )
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        spellResultFlow.emit(SpellResult.OnChanged("fireball"))
        advanceUntilIdle()

        verify(exactly = 2) { getMonster.invoke("goblin") }
    }

    @Test
    fun `onTableContentOpen sets isTableContentOpen to true`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        stateHolder.onTableContentOpen()
        advanceUntilIdle()

        assertTrue(stateHolder.state.value.isTableContentOpen)
    }

    @Test
    fun `onTableContentClose sets isTableContentOpen to false`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()
        stateHolder.onTableContentOpen()
        advanceUntilIdle()

        stateHolder.onTableContentClose()
        advanceUntilIdle()

        assertFalse(stateHolder.state.value.isTableContentOpen)
    }

    @Test
    fun `onTableContentClick closes table content and sends GoToListPosition action`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()
        stateHolder.onTableContentOpen()
        advanceUntilIdle()

        val actions = testFlow(stateHolder.action) {
            stateHolder.onTableContentClick(SectionTitle.Image.name)
            advanceUntilIdle()
        }

        assertFalse(stateHolder.state.value.isTableContentOpen)
        val action = actions.removeAt(0) as MonsterRegistrationAction.GoToListPosition
        assertTrue(action.position >= 0)
        actions.assertHasNoMoreValues()
    }

    @Test
    fun `isSaveButtonEnabled is false after load and true after monster changes`() = runTest {
        val stateHolder = createStateHolder()
        advanceUntilIdle()
        eventManager.dispatchEvent(MonsterRegistrationEvent.Show("goblin"))
        advanceUntilIdle()

        assertFalse(stateHolder.state.value.isSaveButtonEnabled)

        val modifiedMonster = stateHolder.state.value.monster.copy(
            info = stateHolder.state.value.monster.info.copy(name = "Hobgoblin")
        )
        stateHolder.onMonsterChanged(modifiedMonster)
        advanceUntilIdle()

        assertTrue(stateHolder.state.value.isSaveButtonEnabled)
    }

    private fun createStateHolder(
        monsterIndex: String = "goblin",
        getMonster: GetMonsterUseCase? = null,
        getMonsterLore: GetMonsterLoreUseCase? = null,
        saveMonster: SaveMonsterUseCase = SaveMonsterUseCase { _, _, _, _ -> },
        normalizeMonster: NormalizeMonsterUseCase = NormalizeMonsterUseCase { monster -> flowOf(monster) },
        fileManager: MonsterRegistrationFileManager = mockk(relaxed = true),
        spellCompendiumEventDispatcher: SpellCompendiumEventResultDispatcher = mockk {
            every { dispatchEventResult(any()) } returns emptyFlow()
        },
        spellDetailEventDispatcher: SpellDetailEventDispatcher = emptySpellDetailEventDispatcher(),
        getSpell: GetSpellUseCase = mockk(relaxed = true),
        spellResultListener: EventListener<SpellResult> = mockk<EventListener<SpellResult>> {
            every { events } returns emptyFlow()
        },
    ): MonsterRegistrationStateHolder {
        val resolvedGetMonster = getMonster ?: mockk<GetMonsterUseCase>().also {
            every { it.invoke(any()) } returns flowOf(MonsterFactory.createEmpty(monsterIndex))
        }
        val resolvedGetMonsterLore = getMonsterLore ?: mockk<GetMonsterLoreUseCase>().also {
            every { it.invoke(any()) } returns flowOf(null)
        }
        return MonsterRegistrationStateHolder(
            dispatcher = testDispatcher,
            params = StateHolderParams(MonsterRegistrationParams(monsterIndex = monsterIndex)),
            eventManager = eventManager,
            eventResultManager = eventResultManager,
            getMonster = resolvedGetMonster,
            getMonsterLore = resolvedGetMonsterLore,
            saveMonster = saveMonster,
            normalizeMonster = normalizeMonster,
            analytics = analytics,
            spellCompendiumEventDispatcher = spellCompendiumEventDispatcher,
            spellDetailEventDispatcher = spellDetailEventDispatcher,
            getSpell = getSpell,
            appLocalization = mockk(relaxed = true),
            spellResultListener = spellResultListener,
            fileManager = fileManager,
        )
    }
}
