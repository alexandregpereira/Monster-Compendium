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
