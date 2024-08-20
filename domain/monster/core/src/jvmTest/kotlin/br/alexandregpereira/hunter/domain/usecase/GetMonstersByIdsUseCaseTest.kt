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
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetMonstersByIdsUseCaseTest {

    private val repository: MonsterRepository = mockk()
    private val useCase = GetMonstersByIdsUseCase(repository)

    @Test
    fun `invoke Should return in the right order`() = runBlocking {
        val indexes = listOf("3", "1", "2")
        every { repository.getLocalMonsters(any()) } returns flowOf(
            listOf("1", "2", "3").map {
                Monster(index = it)
            }
        )

        val monsters = useCase(indexes).single()

        assertEquals(indexes, monsters.map { it.index })
    }
}
