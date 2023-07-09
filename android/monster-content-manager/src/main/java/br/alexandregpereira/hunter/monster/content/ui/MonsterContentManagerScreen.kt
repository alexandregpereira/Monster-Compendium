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

package br.alexandregpereira.hunter.monster.content.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerViewState
import br.alexandregpereira.hunter.monster.content.R
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterContentManagerScreen(
    state: MonsterContentManagerViewState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {},
    onAddClick: (String) -> Unit = {},
    onRemoveClick: (String) -> Unit = {},
) {
    BackHandler(enabled = state.isOpen, onBack = onClose)

    SwipeVerticalToDismiss(visible = state.isOpen, onClose = onClose) {
        Window(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(16.dp)
            ) {
                item(key = "title") {
                    Text(
                        text = stringResource(R.string.monster_content_manager_manage_monster_content),
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )
                }

                items(state.monsterContents, key = { it.acronym }) { monsterContent ->
                    MonsterContentCard(
                        name = monsterContent.name,
                        originalName = monsterContent.originalName,
                        totalMonsters = monsterContent.totalMonsters,
                        summary = monsterContent.summary,
                        coverImageUrl = monsterContent.coverImageUrl,
                        isEnabled = monsterContent.isEnabled,
                        onAddClick = { onAddClick(monsterContent.acronym) },
                        onRemoveClick = { onRemoveClick(monsterContent.acronym) },
                    )
                    Spacer(modifier = Modifier.padding(bottom = 32.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MonsterContentManagerScreenPreview() {
    MonsterContentManagerScreen(
        state = MonsterContentManagerViewState(
            monsterContents = (0..10).map {
                MonsterContentState(
                    acronym = "ACR",
                    name = "Monster Content",
                    originalName = "Other Name",
                    totalMonsters = 10,
                    summary = "Summary",
                    coverImageUrl = "https://www.google.com.br/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                    isEnabled = true
                )
            },
            isOpen = true
        ),
    )
}
