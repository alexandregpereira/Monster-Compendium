/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAlphabetUseCase @Inject constructor() {

    operator fun invoke(items: List<MonsterCompendiumItem>): Flow<List<String>> {
        return flow {
            emit(items.mapToFirstLetters().toSortedSet().toList())
        }
    }

    private fun List<MonsterCompendiumItem>.mapToFirstLetters(): List<String> {
        var lastLetter: Char? = null
        return map { item ->
            when (item) {
                is MonsterCompendiumItem.Title -> {
                    item.value.first().also { lastLetter = it }.toString()
                }
                is MonsterCompendiumItem.Item -> lastLetter?.toString()
                    ?: throw IllegalArgumentException("Letter not initialized")
            }
        }
    }
}
