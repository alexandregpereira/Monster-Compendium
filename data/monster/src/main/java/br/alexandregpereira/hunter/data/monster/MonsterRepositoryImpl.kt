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
