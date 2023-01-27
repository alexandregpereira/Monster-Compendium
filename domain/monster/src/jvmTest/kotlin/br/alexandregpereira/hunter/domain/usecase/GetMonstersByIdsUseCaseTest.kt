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
