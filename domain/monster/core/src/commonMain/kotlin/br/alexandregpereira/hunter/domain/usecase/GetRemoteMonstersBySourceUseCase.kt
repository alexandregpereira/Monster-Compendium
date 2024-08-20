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
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
import br.alexandregpereira.hunter.domain.sort.sortMonstersByNameAndGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetRemoteMonstersBySourceUseCase internal constructor(
    private val repository: MonsterRemoteRepository,
    private val monsterSettingsRepository: MonsterSettingsRepository,
) {

    operator fun invoke(sourceAcronym: String): Flow<List<Monster>> {
        return monsterSettingsRepository.getLanguage().map {  lang: String ->
            repository.getMonsters(
                sourceAcronym = sourceAcronym,
                lang = lang
            ).single()
                .distinctBy { it.index }
                .sortMonstersByNameAndGroup()
        }
    }
}
