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

package br.alexandregpereira.hunter.data.monster.remote

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MonsterRemoteDataSourceTest {

    private val api: MonsterApi = mockk()
    private val dataSource = MonsterRemoteDataSourceImpl(api)

    @Test
    fun getMonsters() = runBlocking {
        // Given
        coEvery { api.getMonsters() } returns listOf(mockk())

        // When
        val monsters = dataSource.getMonsters().single()

        // Then
        assertEquals(1, monsters.size)
        coVerify { api.getMonsters() }
        confirmVerified(api)
    }

    @Test
    fun getMonsters_should_throw_error() = runBlocking {
        // Given
        coEvery { api.getMonsters() } throws Throwable("error")

        // When
        val error = dataSource.getMonsters().runCatching {
            this.single()
        }.exceptionOrNull()

        // Then
        assertNotNull(error)
    }
}
