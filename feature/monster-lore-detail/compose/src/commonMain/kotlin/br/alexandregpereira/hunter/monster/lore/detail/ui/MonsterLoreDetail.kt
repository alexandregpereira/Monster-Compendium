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

package br.alexandregpereira.hunter.monster.lore.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.Window
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MonsterLoreDetail(
    monsterLore: MonsterLore
) = SelectionContainer {
    Column(modifier = Modifier.padding(16.dp)) {
        ScreenHeader(
            title = monsterLore.name,
            modifier = Modifier
        )

        monsterLore.entries.forEach { entry ->
            MonsterLoreEntryBlock(
                description = entry.description,
                title = entry.title,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MonsterLoreDetailPreview() = Window {
    MonsterLoreDetail(
        monsterLore = MonsterLore(
            index = "",
            name = "Lich",
            entries = listOf(
                MonsterLoreEntry(
                    description = "asdas asdasd asd asd \nas dasasdas as x as asd d as"
                ),
                MonsterLoreEntry(
                    title = "Title",
                    description = "asdas asdasd asd asd \nas dasasdas as x as asd d as"
                )
            ),
            status = MonsterLoreStatus.Original
        )
    )
}
