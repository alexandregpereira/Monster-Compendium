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
