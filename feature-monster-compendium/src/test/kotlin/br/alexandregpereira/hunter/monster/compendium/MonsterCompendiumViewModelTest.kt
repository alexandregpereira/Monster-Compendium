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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCardState
import br.alexandregpereira.hunter.ui.compose.ColorState
import br.alexandregpereira.hunter.ui.compose.MonsterImageState
import br.alexandregpereira.hunter.ui.compose.MonsterTypeState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MonsterCompendiumViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getMonsterPreviewsUseCase: GetMonsterPreviewsBySectionUseCase = mockk()
    private val getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = mockk()
    private val saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = mockk()
    private val stateLiveDataObserver: Observer<MonsterCompendiumViewState> = mockk(
        relaxUnitFun = true
    )
    private lateinit var viewModel: MonsterCompendiumViewModel

    @Test
    fun loadMonsters() {
        // Given
        val section = MonsterSection(title = "Any")
        val monster = MonsterPreview(
            index = "",
            type = MonsterType.ABERRATION,
            challengeRating = 0.0f,
            name = "",
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

        // When
        viewModel.loadMonsters()

        // Then
        verify { getMonsterPreviewsUseCase() }
        verifySequence {
            stateLiveDataObserver.onChanged(MonsterCompendiumViewState(isLoading = false))
            stateLiveDataObserver.onChanged(MonsterCompendiumViewState(isLoading = true))
            stateLiveDataObserver.onChanged(
                MonsterCompendiumViewState(
                    isLoading = false,
                    monstersBySection = mapOf(
                        SectionState(title = "Any") to listOf(
                            MonsterCardState(
                                index = "",
                                name = "",
                                imageState = MonsterImageState(
                                    url = "",
                                    type = MonsterTypeState.ABERRATION,
                                    challengeRating = 0.0f,
                                    backgroundColor = ColorState(light = "", dark = "")
                                ),
                            ) and null
                        )
                    ),
                    initialScrollItemPosition = 1
                )
            )
        }
    }

    private fun createViewModel() {
        viewModel = MonsterCompendiumViewModel(
            getMonsterPreviewsBySectionUseCase = getMonsterPreviewsUseCase,
            getLastScrollPositionUseCase,
            saveScrollPositionUseCase,
            loadOnInit = false,
            dispatcher = testCoroutineRule.testCoroutineDispatcher
        )
        viewModel.stateLiveData.observeForever(stateLiveDataObserver)
    }
}