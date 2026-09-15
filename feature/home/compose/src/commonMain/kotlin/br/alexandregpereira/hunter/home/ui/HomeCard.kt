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

package br.alexandregpereira.hunter.home.ui

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppButtonType
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.cardShape

/**
 * A Home card with a [title], a customizable body [content] and a primary button below it.
 */
@Composable
internal fun HomeCard(
    title: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) = AppCard(
    shape = cardShape,
    elevation = 0.dp,
    modifier = modifier.fillMaxWidth(),
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        content()
        AppButton(
            text = buttonText,
            type = AppButtonType.PRIMARY,
            size = AppButtonSize.SMALL,
            onClick = onButtonClick,
            modifier = Modifier.padding(top = 20.dp),
        )
    }
}

/**
 * The secondary text shown right below the [HomeCard] title.
 */
@Composable
internal fun HomeCardDescription(
    text: String,
    modifier: Modifier = Modifier,
) = Text(
    text = text,
    fontSize = 13.sp,
    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
    modifier = modifier.padding(top = 4.dp),
)

@Preview
@Composable
private fun HomeCardPreview() = PreviewWindow(darkTheme = true) {
    HomeCard(
        title = "Folders",
        buttonText = "Create a folder",
        modifier = Modifier.padding(16.dp),
    ) {
        HomeCardDescription(
            text = "Create folders to plan your encounters and keep your favorite creatures close at hand.",
        )
    }
}
