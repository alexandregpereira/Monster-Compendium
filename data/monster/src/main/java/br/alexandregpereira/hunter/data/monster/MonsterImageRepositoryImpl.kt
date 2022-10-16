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

import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterImageRepositoryImpl @Inject constructor(
    private val remoteDataSource: MonsterRemoteDataSource,
    private val getMonsterImageJsonUrlUseCase: GetMonsterImageJsonUrlUseCase
) : MonsterImageRepository {

    override fun getMonsterImages(jsonUrl: String): Flow<List<MonsterImage>> {
        return remoteDataSource.getMonsterImages(jsonUrl)
            .map { it.toDomain() }
    }

    override fun getMonsterImageJsonUrl(): Flow<String> {
        return getMonsterImageJsonUrlUseCase()
    }
}
