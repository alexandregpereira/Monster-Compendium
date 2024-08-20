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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppTextField

@Composable
internal fun SearchBar(
    text: TextFieldValue,
    searchLabel: String,
    modifier: Modifier = Modifier,
    isSearching: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit = {}
) = Box {
    AppTextField(
        text = text,
        capitalize = false,
        onValueChange = onValueChange,
        label = searchLabel,
        modifier = modifier,
    )
    AnimatedVisibility(
        visible = isSearching,
        enter = fadeIn(animationSpec = spring()),
        exit = fadeOut(animationSpec = spring()),
        modifier = Modifier.align(Alignment.BottomStart)
    ) {
        LinearProgressIndicator(
            color = MaterialTheme.colors.onSurface,
            strokeCap = StrokeCap.Square,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}
