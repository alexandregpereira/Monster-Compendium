/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import br.alexandregpereira.hunter.monster.compendium.ui.Loading
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCardState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumViewState
import br.alexandregpereira.hunter.monster.compendium.ui.SectionState
import br.alexandregpereira.hunter.monster.compendium.ui.and
import br.alexandregpereira.hunter.monster.compendium.ui.complete
import br.alexandregpereira.hunter.ui.compose.ColorState
import br.alexandregpereira.hunter.ui.compose.MonsterImageState
import br.alexandregpereira.hunter.ui.compose.MonsterTypeState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MonsterCompendiumViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getMonsterPreviewsUseCase: GetMonsterPreviewsBySectionUseCase = mockk()
    private val getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = mockk()
    private val saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = mockk()
    private val syncMonstersUseCase: SyncMonstersUseCase = mockk(relaxUnitFun = true)
    private lateinit var viewModel: MonsterCompendiumViewModel

    @Test
    fun loadMonsters() = runBlockingTest {
        // Given
        val section = MonsterSection(title = "Any")
        val monster = MonsterPreview(
            index = "",
            type = MonsterType.ABERRATION,
            challengeRating = 0.0f,
            name = "A",
            imageData = MonsterImageData(
                url = "",
                backgroundColor = Color(light = "", dark = "")
            ),
        )
        val monstersBySection = mapOf(
            section to listOf(monster to null)
        )
        every { getMonsterPreviewsUseCase() } returns flowOf(monstersBySection)
        every { getLastScrollPositionUseCase() } returns flowOf(1)
        createViewModel()

        val results = mutableListOf<MonsterCompendiumViewState>()
        val job = launch {
            viewModel.state.toList(results)
        }

        // When
        viewModel.loadMonsters()

        // Then
        verify { syncMonstersUseCase() }
        verify { getMonsterPreviewsUseCase() }

        assertEquals(3, results.size)
        assertEquals(MonsterCompendiumViewState.Initial, results[0])
        assertEquals(MonsterCompendiumViewState.Initial.Loading, results[1])
        assertEquals(
            MonsterCompendiumViewState.Initial.complete(
                monstersBySection = mapOf(
                    SectionState(title = "Any") to listOf(
                        MonsterCardState(
                            index = "",
                            name = "A",
                            imageState = MonsterImageState(
                                url = "",
                                type = MonsterTypeState.ABERRATION,
                                challengeRating = 0.0f,
                                backgroundColor = ColorState(light = "", dark = "")
                            ),
                        ) and null
                    )
                ),
                initialScrollItemPosition = 1,
                alphabet = listOf('A'),
                alphabetIndex = 0
            ),
            results[2]
        )

        job.cancel()
    }

    private fun createViewModel() {
        every { syncMonstersUseCase() } returns flowOf(Unit)

        viewModel = MonsterCompendiumViewModel(
            syncMonstersUseCase = syncMonstersUseCase,
            getMonsterPreviewsBySectionUseCase = getMonsterPreviewsUseCase,
            getLastCompendiumScrollItemPositionUseCase = getLastScrollPositionUseCase,
            saveCompendiumScrollItemPositionUseCase = saveScrollPositionUseCase,
            loadOnInit = false,
            dispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }
}