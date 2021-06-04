/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun Block(
    modifier: Modifier = Modifier,
    title: String? = null,
    contentPaddingBottom: Dp = 0.dp,
    contentTextPaddingBottom: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier.background(color = MaterialTheme.colors.surface)
        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp + contentPaddingBottom)
) {

    title?.let {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = contentTextPaddingBottom)
        )
    }

    Column(Modifier.fillMaxWidth(), content = content)
}

@Preview
@Composable
fun BlockPreview() = HunterTheme {
    Block(title = "Title") {

    }
}