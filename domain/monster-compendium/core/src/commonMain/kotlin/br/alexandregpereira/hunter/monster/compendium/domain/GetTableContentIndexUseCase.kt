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
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<List<TableContentItem>>.getTableContentIndexFromAlphabetIndex(
    alphabet: List<String>,
    alphabetIndex: Int,
    currentAlphabetIndex: Int,
    currentTableContentIndex: Int
): Flow<Int> {
    return map { tableContent ->
        val letter = alphabet[alphabetIndex]
        if (currentAlphabetIndex == alphabetIndex) {
            currentTableContentIndex
        } else {
            tableContent.indexOfFirst { it.text == letter }
        }
    }
}

fun Flow<List<MonsterCompendiumItem>>.getCompendiumIndexFromTableContentIndex(
    tableContent: List<TableContentItem>,
    tableContentIndex: Int,
): Flow<Int> {
    return map { items ->
        val content = tableContent[tableContentIndex]
        items.indexOfFirst { item ->
            val value = when (item) {
                is MonsterCompendiumItem.Title -> item.id
                is MonsterCompendiumItem.Item -> item.monster.index
            }
            content.id == value
        }
    }
}

fun List<TableContentItem>.getTableContentIndexFromCompendiumItemIndex(
    itemIndex: Int,
    items: List<MonsterCompendiumItem>,
): Int {
    val compendiumItem = items.getOrNull(itemIndex) ?: return -1
    return indexOfFirst { content ->
        val value = when (compendiumItem) {
            is MonsterCompendiumItem.Title -> compendiumItem.id
            is MonsterCompendiumItem.Item -> compendiumItem.monster.index
        }
        content.id == value
    }
}

fun List<String>.getAlphabetIndexFromCompendiumItemIndex(
    itemIndex: Int,
    items: List<MonsterCompendiumItem>,
): Int {
    if (items.isEmpty()) return -1
    val monsterFirstLetters = items.mapToFirstLetters()
    return indexOf(monsterFirstLetters[itemIndex])
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
