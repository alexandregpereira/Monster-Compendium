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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonType
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.cardShape

@Composable
internal fun HomeExtraContentCard(
    title: String,
    progressText: String,
    buttonText: String,
    added: Int,
    total: Int,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
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
        Text(
            text = progressText,
            fontSize = 13.sp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp),
        )
        HomeSegmentedProgressBar(
            filled = added,
            total = total,
            modifier = Modifier.padding(top = 16.dp),
        )
        AppButton(
            text = buttonText,
            type = AppButtonType.PRIMARY,
            onClick = onButtonClick,
            modifier = Modifier.padding(top = 20.dp),
        )
    }
}

@Composable
private fun HomeSegmentedProgressBar(
    filled: Int,
    total: Int,
    modifier: Modifier = Modifier,
) = Row(
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier.fillMaxWidth(),
) {
    val onSurface = MaterialTheme.colors.onSurface
    repeat(total) { index ->
        val color = if (index < filled) onSurface else onSurface.copy(alpha = 0.15f)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .background(color = color, shape = CircleShape)
        )
    }
}

@Preview
@Composable
private fun HomeExtraContentCardPreview() = PreviewWindow(darkTheme = true) {
    HomeExtraContentCard(
        title = "Extra content",
        progressText = "3 of 8 extra contents added",
        buttonText = "Manage extra content",
        added = 3,
        total = 8,
        modifier = Modifier.padding(16.dp),
    )
}
