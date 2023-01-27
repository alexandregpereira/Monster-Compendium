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

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterCacheRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetMonsterPreviewsCacheUseCaseTest {

    private val getMonsterPreviewsUseCase: GetMonsterPreviewsUseCase = mockk()
    private val cacheRepository: MonsterCacheRepository = mockk()
    private val useCase = GetMonsterPreviewsCacheUseCase(
        cacheRepository = cacheRepository,
        getMonsterPreviewsUseCase = getMonsterPreviewsUseCase
    )

    @Test
    fun `invoke When has cache`() = runTest {
        every { getMonsterPreviewsUseCase() } returns flowOf(
            listOf(Monster(index = "2"))
        )
        every { cacheRepository.getMonsters() } returns flowOf(
            listOf(Monster(index = "1"))
        )

        val result = useCase().single()

        assertEquals(
            expected = listOf(Monster(index = "1")),
            actual = result
        )
    }

    @Test
    fun `invoke When has no cache`() = runTest {
        every { getMonsterPreviewsUseCase() } returns flowOf(
            listOf(Monster(index = "2"))
        )
        every { cacheRepository.getMonsters() } returns flowOf(emptyList())

        val result = useCase().single()

        assertEquals(
            expected = listOf(Monster(index = "2")),
            actual = result
        )
    }
}
