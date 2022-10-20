/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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

import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.single

class GetMonsterImagesUseCase @Inject constructor(
    private val repository: MonsterImageRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<MonsterImage>> {
        return repository.getMonsterImageJsonUrl().flatMapLatest { url ->
            repository.getMonsterImages(url).catch {
                emit(repository.getMonsterImages(DEFAULT_MONSTER_IMAGES_JSON_URL).single())
            }
        }.catch { emit(emptyList()) }
    }

    companion object {
        private const val DEFAULT_MONSTER_IMAGES_JSON_URL =
            "https://raw.githubusercontent.com/alexandregpereira/hunter-api/main/json/" +
                    "monster-images.json"
    }
}
