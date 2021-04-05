/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.monster.compendium

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.alexandregpereira.hunter.domain.usecase.GetMonstersBySectionUseCase
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MonsterCompendiumViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val getMonstersUseCase: GetMonstersBySectionUseCase = mockk()
    private val getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = mockk()
    private val saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = mockk()
    private val stateLiveDataObserver: Observer<MonsterCompendiumViewState> = mockk(
        relaxUnitFun = true
    )
    private lateinit var viewModel: MonsterCompendiumViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun loadMonsters() {
        // Given
        val section = MonsterSection()
        val monster = Monster(
            index = "",
            type = MonsterType.ABERRATION,
            subtype = null,
            group = null,
            challengeRating = 0.0f,
            name = "",
            subtitle = "",
            imageData = MonsterImageData(
                url = "",
                backgroundColor = Color(light = "", dark = "")
            ),
            size = "",
            alignment = "",
            stats = Stats(armorClass = 0, hitPoints = 0, hitDice = ""),
            speed = Speed(hover = false, values = listOf())
        )
        val monstersBySection = mapOf(
            section to mapOf(monster to null)
        )
        every { getMonstersUseCase() } returns flowOf(monstersBySection)
        every { getLastScrollPositionUseCase() } returns flowOf(1)
        createViewModel()

        // When
        viewModel.loadMonsters()

        // Then
        verify { getMonstersUseCase() }
        verifyOrder {
            stateLiveDataObserver.onChanged(MonsterCompendiumViewState(isLoading = true))
            stateLiveDataObserver.onChanged(
                MonsterCompendiumViewState(
                    isLoading = false,
                    monstersBySection = mapOf(
                        section to mapOf(
                            MonsterCardItem(
                                index = "",
                                type = MonsterType.ABERRATION,
                                challengeRating = 0.0f,
                                name = "",
                                imageData = MonsterImageData(
                                    url = "",
                                    backgroundColor = Color(light = "", dark = "")
                                ),
                                group = null
                            ) to null
                        )
                    ),
                    initialScrollItemPosition = 1
                )
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = MonsterCompendiumViewModel(
            getMonstersBySectionUseCase = getMonstersUseCase,
            getLastScrollPositionUseCase,
            saveScrollPositionUseCase,
            loadOnInit = false
        )
        viewModel.stateLiveData.observeForever(stateLiveDataObserver)
    }
}