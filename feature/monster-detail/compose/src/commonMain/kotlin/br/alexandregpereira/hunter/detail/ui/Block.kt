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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun Block(
    modifier: Modifier = Modifier,
    title: String? = null,
    contentPaddingBottom: Dp = 0.dp,
    contentTextPaddingBottom: Dp = 16.dp,
    contentHorizontalPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier.padding(top = 16.dp, bottom = 16.dp + contentPaddingBottom)
) {

    title?.let {
        BlockTitle(
            title = title,
            contentTextPaddingBottom = contentTextPaddingBottom
        )
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = contentHorizontalPadding),
        content = content
    )
}

@Composable
internal fun BlockTitle(
    title: String,
    modifier: Modifier = Modifier,
    contentTextPaddingBottom: Dp = 0.dp,
) {
    Text(
        text = title,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = contentTextPaddingBottom
        )
    )
}

@Preview
@Composable
private fun BlockPreview() = HunterTheme {
    Block(title = "Title") {

    }
}