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

package br.alexandregpereira.hunter.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.search.SearchTip

@Composable
internal fun SearchTips(
    title: String,
    tips: List<SearchTip>,
    contentPaddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = contentPaddingValues.calculateTopPadding() + 96.dp + 8.dp + 32.dp,
                bottom = contentPaddingValues.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))

        tips.forEach { tip ->
            tip.searchQuery?.let {
                Card {
                    Text(
                        text = tip.searchQuery,
                        style = MaterialTheme.typography.body2.copy(fontFamily = FontFamily.Monospace),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tip.searchQueryExplanation,
                style = MaterialTheme.typography.body2.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
