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

package br.alexandregpereira.hunter.data.monster.lore

import br.alexandregpereira.hunter.data.monster.lore.local.MonsterLoreLocalDataSource
import br.alexandregpereira.hunter.data.monster.lore.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.lore.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.monster.lore.MonsterLoreLocalRepository
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.strings.StringsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal class DefaultMonsterLoreLocalRepository(
    private val monsterLoreLocalDataSource: MonsterLoreLocalDataSource,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val stringsRepository: StringsRepository,
) : MonsterLoreLocalRepository {

    override fun getMonsterLore(index: String): Flow<MonsterLore?> {
        return monsterLoreLocalDataSource.getMonsterLore(index).map { it?.toDomain(getStrings()) }
    }

    override fun getLocalMonstersLore(indexes: List<String>): Flow<List<MonsterLore>> {
        return monsterLoreLocalDataSource.getMonstersLore(indexes).map { monsters ->
            val strings = getStrings()
            monsters.map { it.toDomain(strings) }
        }
    }

    override fun getMonstersLoreEdited(): Flow<List<MonsterLore>> {
        return monsterLoreLocalDataSource.getMonstersLoreEdited().map { monsters ->
            val strings = getStrings()
            monsters.map { it.toDomain(strings) }
        }
    }

    override fun save(monstersLore: List<MonsterLore>, isSync: Boolean): Flow<Unit> {
        return monsterLoreLocalDataSource.save(monstersLore.map { it.toEntity() }, isSync)
    }

    private suspend fun getStrings(): Map<String, String> {
        val lang = getLanguageUseCase().single()
        return stringsRepository.getStrings(lang)
    }
}
