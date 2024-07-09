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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun SearchBar(
    text: String,
    searchLabel: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {
    AppTextField(
        text = text,
        capitalize = false,
        onValueChange = onValueChange,
        label = searchLabel,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SearchBarPreview() = HunterTheme {
    Column(Modifier.padding(16.dp)) {
        SearchBar(text = "", searchLabel = "Search")
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            text = "Test",
            searchLabel = "Search"
        )
    }
}
