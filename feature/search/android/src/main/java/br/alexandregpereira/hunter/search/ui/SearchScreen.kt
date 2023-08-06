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

package br.alexandregpereira.hunter.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun SearchScreen(
    state: SearchViewState,
    contentPaddingValues: PaddingValues = PaddingValues(),
    onSearchValueChange: (String) -> Unit = {},
    onCardClick: (String) -> Unit = {},
    onCardLongClick: (String) -> Unit = {},
) = HunterTheme {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier) {
            Box {
                val focusManager = LocalFocusManager.current
                SearchGrid(
                    monsterRows = state.monsterRows,
                    totalResults = state.totalResults,
                    contentPadding = PaddingValues(
                        top = contentPaddingValues.calculateTopPadding() + 56.dp + 8.dp,
                        bottom = contentPaddingValues.calculateBottomPadding()
                    ),
                    onCardClick = {
                        focusManager.clearFocus()
                        onCardClick(it)
                    },
                    onCardLongClick = {
                        focusManager.clearFocus()
                        onCardLongClick(it)
                    }
                )

                SearchBar(
                    text = state.searchValue,
                    onValueChange = onSearchValueChange,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp + contentPaddingValues.calculateTopPadding())
                )
            }
            Spacer(modifier = Modifier.height(contentPaddingValues.calculateBottomPadding()))
        }
    }
}
