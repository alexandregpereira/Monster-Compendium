/*
 * Copyright 2022 Alexandre Gomes Pereira
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
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.ui.compendium.SectionState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MonsterCompendiumViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val savedStateHandle: SavedStateHandle = mockk(relaxUnitFun = true)
    private val getMonsterPreviewsUseCase: GetMonsterPreviewsBySectionUseCase = mockk()
    private val getLastScrollPositionUseCase: GetLastCompendiumScrollItemPositionUseCase = mockk()
    private val saveScrollPositionUseCase: SaveCompendiumScrollItemPositionUseCase = mockk()
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher = mockk()
    private val folderPreviewResultListener: FolderPreviewResultListener = mockk()
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher = mockk()
    private lateinit var viewModel: MonsterCompendiumViewModel

    @Test
    fun loadMonsters() = runTest {
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
            section to listOf(monster)
        )
        every { getMonsterPreviewsUseCase() } returns flowOf(monstersBySection)
        every { getLastScrollPositionUseCase() } returns flowOf(1)
        every { savedStateHandle.get<Boolean>(any()) } returns null
        createViewModel()

        val results = mutableListOf<MonsterCompendiumViewState>()
        val job = launch {
            viewModel.state.toList(results)
        }

        // When
        viewModel.loadMonsters()

        advanceUntilIdle()

        // Then
        verify { getMonsterPreviewsUseCase() }

        assertEquals(2, results.size)
        assertEquals(MonsterCompendiumViewState(), results[0])
        assertEquals(
            MonsterCompendiumViewState().complete(
                monstersBySection = mapOf(
                    SectionState(title = "Any") to listOf(
                        MonsterCardState(
                            index = "",
                            name = "A",
                            imageState = MonsterImageState(
                                url = "",
                                type = MonsterTypeState.ABERRATION,
                                challengeRating = 0.0f,
                                backgroundColor = ColorState(light = "", dark = ""),
                            ),
                        )
                    )
                ),
                alphabet = listOf('A'),
                alphabetIndex = 0
            ),
            results[1]
        )
        assertEquals(1, viewModel.initialScrollItemPosition)

        job.cancel()
    }

    private fun createViewModel() {
        viewModel = MonsterCompendiumViewModel(
            savedStateHandle = savedStateHandle,
            getMonsterPreviewsBySectionUseCase = getMonsterPreviewsUseCase,
            getLastCompendiumScrollItemPositionUseCase = getLastScrollPositionUseCase,
            saveCompendiumScrollItemPositionUseCase = saveScrollPositionUseCase,
            folderPreviewEventDispatcher = folderPreviewEventDispatcher,
            folderPreviewResultListener = folderPreviewResultListener,
            monsterDetailEventDispatcher = monsterDetailEventDispatcher,
            loadOnInit = false,
            dispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }
}
