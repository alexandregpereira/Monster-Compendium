/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
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
