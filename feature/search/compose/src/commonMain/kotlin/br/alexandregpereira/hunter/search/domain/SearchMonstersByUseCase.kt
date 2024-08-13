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

package br.alexandregpereira.hunter.search.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsCacheUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import br.alexandregpereira.hunter.search.removeAccents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

internal class SearchMonstersByUseCase internal constructor(
    private val getMonsterPreviewsCacheUseCase: GetMonsterPreviewsCacheUseCase,
    private val getMonstersUseCase: GetMonstersUseCase,
    private val getSpellsByIdsUseCase: GetSpellsByIdsUseCase,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(value: String): Flow<List<SearchMonsterResult>> {
        if (value.isBlank()) return flowOf(emptyList())

        return flow { emit(getKeySearchValueMap(value)) }
            .flatMapLatest { keySearchValueMap ->
                if (keySearchValueMap.containsKey(KeySearch.SPELL)) {
                    getMonstersUseCase().appendSpellcastings()
                } else {
                    getMonsterPreviewsCacheUseCase()
                }
            }
            .map { monsters ->
                val keySearchValueMap = getKeySearchValueMap(value)
                monsters.filter { monster ->
                    keySearchValueMap.map { (key, value) ->
                        monster.containsByKey(key, value)
                    }.all { it }
                }
            }
            .catch { error ->
                throw SearchMonstersByNameUnexpectedException(cause = error)
            }
            .map { monsters ->
                monsters.map { monster ->
                    SearchMonsterResult(
                        index = monster.index,
                        name = monster.name,
                        type = monster.type,
                        challengeRating = monster.challengeRatingFormatted,
                        imageUrl = monster.imageData.url,
                        backgroundColorLight = monster.imageData.backgroundColor.light,
                        backgroundColorDark = monster.imageData.backgroundColor.dark,
                        isHorizontalImage = monster.imageData.isHorizontal,
                    )
                }
            }
    }

    //TODO It is duplicated with GetMonsterDetailUseCase logic
    private fun Flow<List<Monster>>.appendSpellcastings(): Flow<List<Monster>> {
        return map { monsters ->
            monsters to monsters.map { it.spellcastings }
                .takeIf { it.isNotEmpty() }
                ?.reduce { acc, values -> acc + values }
                ?.map { it.usages }
                ?.takeIf { it.isNotEmpty() }
                ?.reduce { acc, values -> acc + values }
                ?.map { it.spells }
                ?.takeIf { it.isNotEmpty() }
                ?.reduce { acc, values -> acc + values }
                ?.map { it.index }
                ?.let { spellIndexes ->
                    getSpellsByIdsUseCase(spellIndexes).single()
                }.orEmpty()
        }.map { (monsters, spells) ->
            monsters.appendSpells(spells)
        }
    }

    //TODO It is duplicated with GetMonsterDetailUseCase logic
    private fun List<Monster>.appendSpells(spells: List<Spell>): List<Monster> {
        return map { monstersWithSpells ->
            monstersWithSpells.copy(
                spellcastings = monstersWithSpells.spellcastings.mapNotNull { spellcasting ->
                    spellcasting.copy(
                        usages = spellcasting.usages.mapNotNull { spellUsage ->
                            spellUsage.copy(
                                spells = spellUsage.spells.mapNotNull { spellPreview ->
                                    spells.find { spellPreview.index == it.index }?.run {
                                        spellPreview.copy(
                                            name = name,
                                            level = level,
                                            school = SchoolOfMagic.valueOf(school.name)
                                        )
                                    }
                                }
                            ).takeIf { it.spells.isNotEmpty() }
                        }
                    ).takeIf { it.usages.isNotEmpty() }
                }
            )
        }
    }

    private fun getKeySearchValueMap(name: String): Map<KeySearch, String> {
        return name.split("&").map { it.trim() }.map { value ->
            val valueSplit = value.split(":")
            if (valueSplit.size == 2) {
                val keySearch = when (valueSplit.first()) {
                    "type" -> KeySearch.TYPE
                    "cr" -> KeySearch.CHALLENGE_RATING
                    "spell" -> KeySearch.SPELL
                    else -> KeySearch.NAME
                }
                keySearch to valueSplit.last()
            } else {
                KeySearch.NAME to value
            }
        }.toMap()
    }

    private fun Monster.containsByKey(keySearch: KeySearch, value: String): Boolean {
        return when (keySearch) {
            KeySearch.NAME -> containsByName(value)
            KeySearch.TYPE -> containsByType(value)
            KeySearch.CHALLENGE_RATING -> containsByChallengeRating(value)
            KeySearch.SPELL -> containsBySpell(value)
        }
    }

    private fun Monster.containsByName(value: String): Boolean {
        return name.removeAccents()
            .contains(value.removeAccents(), ignoreCase = true) ||
                index.replace("-", " ")
                    .contains(value.removeAccents(), ignoreCase = true)
    }

    private fun Monster.containsByType(value: String): Boolean {
        return type.name.removeAccents()
            .contains(value.removeAccents(), ignoreCase = true)
    }

    private fun Monster.containsByChallengeRating(value: String): Boolean {
        return challengeRating.toString() == value
    }

    private fun Monster.containsBySpell(value: String): Boolean {
        return spellcastings.map { it.usages }
            .takeIf { it.isNotEmpty() }
            ?.reduce { acc, spellUsages -> acc + spellUsages }
            ?.map { it.spells }
            ?.takeIf { it.isNotEmpty() }
            ?.reduce { acc, spellPreviews -> acc + spellPreviews }
            ?.any { spellPreview ->
                spellPreview.name.removeAccents()
                    .contains(value.removeAccents(), ignoreCase = true) ||
                        spellPreview.index.replace("-", " ")
                            .contains(value, ignoreCase = true)
            } ?: false
    }

    private enum class KeySearch {
        NAME, TYPE, CHALLENGE_RATING, SPELL
    }
}
