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
import br.alexandregpereira.hunter.domain.usecase.ResetMonsterImage
import br.alexandregpereira.hunter.event.folder.insert.emptyFolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.lore.detail.emptyMonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.detail.domain.CloneMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.DeleteMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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

    private fun createStateHolder(
        resetMonsterImage: ResetMonsterImage = ResetMonsterImage { },
    ) = MonsterDetailStateHolder(
        getMonsterDetailUseCase = mockk<GetMonsterDetailUseCase>(relaxed = true),
        cloneMonster = mockk<CloneMonsterUseCase>(relaxed = true),
        deleteMonster = mockk<DeleteMonsterUseCase>(relaxed = true),
        resetMonsterToOriginal = { emptyFlow() },
        resetMonsterImage = resetMonsterImage,
        spellDetailEventDispatcher = emptySpellDetailEventDispatcher(),
        monsterEventDispatcher = emptyMonsterEventDispatcher(),
        shareContentEventDispatcher = ShareContentEventDispatcher(),
        monsterLoreDetailEventDispatcher = emptyMonsterLoreDetailEventDispatcher(),
        folderInsertEventDispatcher = emptyFolderInsertEventDispatcher(),
        monsterRegistrationEventDispatcher = emptyMonsterRegistrationEventDispatcher(),
        syncEventDispatcher = emptySyncEventDispatcher(),
        dispatcher = testDispatcher,
        analytics = MonsterDetailAnalytics(analytics),
        appLocalization = mockk<AppLocalization>(relaxed = true),
        stateRecovery = StateRecovery(),
        spellResultListener = mockk<EventListener<SpellResult>> { every { events } returns emptyFlow() },
        getCondition = mockk<GetCondition>(relaxed = true),
    )
}
