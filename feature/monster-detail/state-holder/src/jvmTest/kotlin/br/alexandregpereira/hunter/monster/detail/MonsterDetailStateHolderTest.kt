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

package br.alexandregpereira.hunter.monster.detail

import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.condition.GetCondition
import br.alexandregpereira.hunter.domain.folder.AddMonsterToRecentlyViewedUseCase
import br.alexandregpereira.hunter.domain.usecase.ResetMonsterImage
import br.alexandregpereira.hunter.event.folder.insert.emptyFolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.lore.detail.emptyMonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.home.event.HomeEvent
import br.alexandregpereira.hunter.home.event.HomeEventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.monster.detail.domain.CloneMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.DeleteMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.event.MonsterEvent
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.emptyMonsterEventDispatcher
import br.alexandregpereira.hunter.monster.registration.event.emptyMonsterRegistrationEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.emptySpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.event.SpellResult
import br.alexandregpereira.hunter.sync.event.emptySyncEventDispatcher
import br.alexandregpereira.hunter.ui.StateRecovery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MonsterDetailStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val analytics: Analytics = mockk(relaxUnitFun = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onResetImageConfirmed When resetMonsterImage throws Then logs exception`() = runTest {
        val stateHolder = createStateHolder(
            resetMonsterImage = { throw RuntimeException("reset failed") }
        )

        stateHolder.onResetImageConfirmed()
        advanceUntilIdle()

        verify { analytics.logException(any()) }
    }

    @Test
    fun `Hide When a monster was viewed Then notifies the Home once the detail closes`() = runTest {
        val monsterEventDispatcher = FakeMonsterEventDispatcher()
        val homeEventDispatcher = HomeEventDispatcher()
        val homeEvents = mutableListOf<HomeEvent>()
        // Unconfined, since advanceUntilIdle doesn't run the background tasks alone
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            homeEventDispatcher.events.toList(homeEvents)
        }
        val addMonsterToRecentlyViewed = mockk<AddMonsterToRecentlyViewedUseCase> {
            every { this@mockk.invoke(any()) } returns flowOf(Unit)
        }
        createStateHolder(
            monsterEventDispatcher = monsterEventDispatcher,
            homeEventDispatcher = homeEventDispatcher,
            addMonsterToRecentlyViewed = addMonsterToRecentlyViewed,
        )
        advanceUntilIdle()

        monsterEventDispatcher.dispatchEvent(MonsterEvent.OnVisibilityChanges.Show(index = "goblin"))
        advanceUntilIdle()

        verify { addMonsterToRecentlyViewed("goblin") }
        // The Home is covered by the detail, so it's not notified yet
        assertEquals(emptyList<HomeEvent>(), homeEvents)

        monsterEventDispatcher.dispatchEvent(MonsterEvent.OnVisibilityChanges.Hide)
        advanceUntilIdle()

        assertEquals(listOf<HomeEvent>(HomeEvent.OnContentChanged()), homeEvents)
    }

    private fun createStateHolder(
        resetMonsterImage: ResetMonsterImage = ResetMonsterImage { },
        monsterEventDispatcher: MonsterEventDispatcher = emptyMonsterEventDispatcher(),
        homeEventDispatcher: HomeEventDispatcher = HomeEventDispatcher(),
        addMonsterToRecentlyViewed: AddMonsterToRecentlyViewedUseCase = mockk(relaxed = true),
    ) = MonsterDetailStateHolder(
        getMonsterDetailUseCase = mockk<GetMonsterDetailUseCase>(relaxed = true),
        cloneMonster = mockk<CloneMonsterUseCase>(relaxed = true),
        deleteMonster = mockk<DeleteMonsterUseCase>(relaxed = true),
        resetMonsterToOriginal = { emptyFlow() },
        resetMonsterImage = resetMonsterImage,
        spellDetailEventDispatcher = emptySpellDetailEventDispatcher(),
        monsterEventDispatcher = monsterEventDispatcher,
        shareContentEventDispatcher = ShareContentEventDispatcher(),
        monsterLoreDetailEventDispatcher = emptyMonsterLoreDetailEventDispatcher(),
        folderInsertEventDispatcher = emptyFolderInsertEventDispatcher(),
        monsterRegistrationEventDispatcher = emptyMonsterRegistrationEventDispatcher(),
        syncEventDispatcher = emptySyncEventDispatcher(),
        dispatcher = testDispatcher,
        analytics = MonsterDetailAnalytics(analytics),
        appLocalization = mockk<AppLocalization> { every { getLanguage() } returns Language.ENGLISH },
        stateRecovery = StateRecovery(),
        spellResultListener = mockk<EventListener<SpellResult>> { every { events } returns emptyFlow() },
        getCondition = mockk<GetCondition>(relaxed = true),
        homeEventDispatcher = homeEventDispatcher,
        addMonsterToRecentlyViewed = addMonsterToRecentlyViewed,
    )

    private class FakeMonsterEventDispatcher : MonsterEventDispatcher {

        private val _events = MutableSharedFlow<MonsterEvent>(extraBufferCapacity = 10)
        override val events: Flow<MonsterEvent> = _events

        override fun dispatchEvent(event: MonsterEvent) {
            _events.tryEmit(event)
        }
    }
}
