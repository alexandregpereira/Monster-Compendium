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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.flow.Flow

/**
 * Just a delegate to avoid changes when removing it.
 * Use [MonsterRemoteRepository] and [MonsterLocalRepository] instead.
 */
internal class MonsterRepositoryImpl(
    private val remoteRepository: MonsterRemoteRepository,
    private val localRepository: MonsterLocalRepository,
) : MonsterRepository {

    override fun saveMonsters(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
        return localRepository.saveMonsters(monsters, isSync)
    }

    override fun getRemoteMonsters(lang: String): Flow<List<Monster>> {
        return remoteRepository.getMonsters(lang)
    }

    override fun getRemoteMonsters(sourceAcronym: String, lang: String): Flow<List<Monster>> {
        return remoteRepository.getMonsters(sourceAcronym, lang)
    }

    override fun getLocalMonsterPreviews(): Flow<List<Monster>> {
        return localRepository.getMonsterPreviews()
    }

    override fun getLocalMonsters(): Flow<List<Monster>> {
        return localRepository.getMonsters()
    }

    override fun getLocalMonsters(indexes: List<String>): Flow<List<Monster>> {
        return localRepository.getMonsters(indexes)
    }

    override fun getLocalMonster(index: String): Flow<Monster> {
        return localRepository.getMonster(index)
    }

    override fun getLocalMonstersByQuery(query: String): Flow<List<Monster>> {
        return localRepository.getMonstersByQuery(query)
    }
}
