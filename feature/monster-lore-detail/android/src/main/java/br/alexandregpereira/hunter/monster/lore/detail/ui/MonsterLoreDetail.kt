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

package br.alexandregpereira.hunter.monster.lore.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterLoreDetail(
    monsterLore: MonsterLoreState
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
        monsterLore = MonsterLoreState(
            index = "",
            name = "Lich",
            entries = listOf(
                MonsterLoreEntryState(
                    description = "asdas asdasd asd asd \nas dasasdas as x as asd d as"
                ),
                MonsterLoreEntryState(
                    title = "Title",
                    description = "asdas asdasd asd asd \nas dasasdas as x as asd d as"
                )
            )
        )
    )
}
