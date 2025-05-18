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

package br.alexandregpereira.hunter.data.monster.lore.local.dao

import br.alexandregpereira.hunter.data.monster.lore.local.entity.MonsterLoreCompleteEntity

interface MonsterLoreDao {

    suspend fun getMonstersLore(indexes: List<String>): List<MonsterLoreCompleteEntity>

    suspend fun getMonsterLore(monsterIndex: String): MonsterLoreCompleteEntity?

    suspend fun getMonstersLoreEdited(): List<MonsterLoreCompleteEntity>

    suspend fun insert(
        monstersLore: List<MonsterLoreCompleteEntity>,
        deleteAll: Boolean
    )
}
