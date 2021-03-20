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

package br.alexandregpereira.hunter.data.remote

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MonsterRemoteDataSourceTest {

    private val fileManager: FileManager = mockk()
    private val dataSource = MonsterRemoteDataSourceImpl(fileManager)

    @Test
    fun getMonsters() = runBlocking {
        // Given
        every { fileManager.readText() } returns flowOf("[{\"index\":\"lich\",\"type\":\"UNDEAD\",\"name\":\"Lich\",\"subtitle\":\"Medium undead, any evil alignment\",\"image_url\":\"https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/lich.png\",\"size\":\"Medium\",\"alignment\":\"any evil alignment\",\"subtype\":null,\"armor_class\":17,\"hit_points\":135,\"hit_dice\":\"18d8\",\"speed\":{\"hover\":false,\"value\":[{\"type\":\"WALK\",\"measurement_unit\":\"FEET\",\"value\":30,\"value_formatted\":\"30 ft.\"}]},\"ability_scores\":[{\"type\":\"STRENGTH\",\"value\":11,\"modifier\":0},{\"type\":\"DEXTERITY\",\"value\":16,\"modifier\":3},{\"type\":\"CONSTITUTION\",\"value\":16,\"modifier\":3},{\"type\":\"INTELLIGENCE\",\"value\":20,\"modifier\":5},{\"type\":\"WISDOM\",\"value\":14,\"modifier\":2},{\"type\":\"CHARISMA\",\"value\":16,\"modifier\":3}],\"saving_throws\":[{\"type\":\"CONSTITUTION\",\"modifier\":10},{\"type\":\"INTELLIGENCE\",\"modifier\":12},{\"type\":\"WISDOM\",\"modifier\":9}],\"skills\":[{\"index\":\"arcana\",\"modifier\":18},{\"index\":\"history\",\"modifier\":12},{\"index\":\"insight\",\"modifier\":9},{\"index\":\"perception\",\"modifier\":9}],\"damage_vulnerabilities\":[],\"damage_resistances\":[{\"index\":\"cold\",\"type\":\"COLD\",\"name\":\"Cold\"},{\"index\":\"lightning\",\"type\":\"LIGHTNING\",\"name\":\"Lightning\"},{\"index\":\"necrotic\",\"type\":\"NECROTIC\",\"name\":\"Necrotic\"}],\"damage_immunities\":[{\"index\":\"poison\",\"type\":\"POISON\",\"name\":\"Poison\"},{\"index\":\"bludgeoning*\",\"type\":\"BLUDGEONING\",\"name\":\"Bludgeoning*\"},{\"index\":\"piercing*\",\"type\":\"PIERCING\",\"name\":\"Piercing*\"},{\"index\":\"slashing*\",\"type\":\"SLASHING\",\"name\":\"Slashing*\"},{\"index\":\"*bludgeoning, piercing, and slashing from nonmagical weapons\",\"type\":\"OTHER\",\"name\":\"*bludgeoning, piercing, and slashing from nonmagical weapons\"}]}]")

        // When
        val monsters = dataSource.getMonsters().single()

        // Then
        assertEquals(1, monsters.size)
        verify { fileManager.readText() }
        confirmVerified(fileManager)
    }

    @Test
    fun getMonsters_should_throw_JsonDecodingException() = runBlocking {
        // Given
        every { fileManager.readText() } returns flowOf("{}")

        // When
        val error = dataSource.getMonsters().runCatching {
            this.single()
        }.exceptionOrNull()

        // Then
        assertNotNull(error)
    }
}