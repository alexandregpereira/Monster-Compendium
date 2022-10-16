/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.data.monster.local.MonsterLocalDataSource
import br.alexandregpereira.hunter.data.monster.local.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.local.mapper.toEntity
import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import br.alexandregpereira.hunter.domain.exception.MonstersSourceNotFoundedException
import br.alexandregpereira.hunter.domain.exception.MonstersSourceUnexpectedException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import retrofit2.HttpException

internal class MonsterRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val localDataSource: MonsterLocalDataSource,
) : MonsterRepository {

    override fun saveMonsters(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
        return localDataSource.saveMonsters(monsters.toEntity(), isSync)
    }

    override fun getRemoteMonsters(monsterImages: List<MonsterImage>): Flow<List<Monster>> {
        return remoteDataSource.getMonsters().toMonstersDomain(monsterImages)
    }

    override fun getRemoteMonsters(
        sourceAcronym: String,
        monsterImages: List<MonsterImage>
    ): Flow<List<Monster>> {
        return remoteDataSource.getMonsters(sourceAcronym)
            .toMonstersDomain(monsterImages)
            .catch { error ->
                throw when {
                    error is HttpException && error.code() == 404 -> {
                        MonstersSourceNotFoundedException(sourceAcronym)
                    }
                    else -> MonstersSourceUnexpectedException(sourceAcronym, error)
                }
            }
    }

    override fun getLocalMonsterPreviews(): Flow<List<Monster>> {
        return localDataSource.getMonsterPreviews().map { it.toDomain() }
    }

    override fun getLocalMonsters(): Flow<List<Monster>> {
        return localDataSource.getMonsters().map { it.toDomain() }
    }

    override fun getLocalMonster(index: String): Flow<Monster> {
        return localDataSource.getMonster(index).map { it.toDomain() }
    }

    override fun getLocalMonstersByQuery(query: String): Flow<List<Monster>> {
        return localDataSource.getMonstersByQuery(query).map { it.toDomain() }
    }

    override fun getRemoteMonsterImages(): Flow<List<MonsterImage>> {
        return remoteDataSource.getMonsterImages()
            .catch { emit(emptyList()) }
            .map { it.toDomain() }
    }

    private fun Flow<List<MonsterDto>>.toMonstersDomain(
        monsterImages: List<MonsterImage>
    ): Flow<List<Monster>> {
        return map { monsterDtos ->
            monsterDtos.toDomain(monsterImages)
        }
    }
}
