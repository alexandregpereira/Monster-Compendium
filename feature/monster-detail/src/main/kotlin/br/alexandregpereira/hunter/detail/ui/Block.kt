/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
fun BlockTitle(
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
fun BlockPreview() = HunterTheme {
    Block(title = "Title") {

    }
}