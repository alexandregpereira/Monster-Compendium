/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.folder.preview.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderPreview(
    monsters: List<MonsterFolderPreview>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (index: String) -> Unit = {},
    onLongClick: (index: String) -> Unit = {},
) {
    Box(modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .height(40.dp + contentPadding.calculateBottomPadding())
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            elevation = 0.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.surface),
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.5f)
        ) {}
        Column {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(monsters, key = { it.index }) { state ->
                    CircleImage(
                        imageUrl = state.imageUrl,
                        backgroundColor = if (isSystemInDarkTheme()) {
                            state.backgroundColorDark
                        } else {
                            state.backgroundColorLight
                        },
                        contentDescription = state.name,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onClick(state.index) },
                        onLongClick = { onLongClick(state.index) }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp + contentPadding.calculateBottomPadding())
            )
        }
    }
}

@Composable
private fun CircleImage(
    imageUrl: String,
    backgroundColor: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    MonsterCoilImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        shape = CircleShape,
        backgroundColor = backgroundColor,
        modifier = modifier
            .size(48.dp)
            .animatePressed(
                pressedScale = 0.8f,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )
    )
}

@Preview
@Composable
private fun FolderPreviewPreview() = HunterTheme {
    FolderPreview(
        listOf(
            MonsterFolderPreview(
                index = "",
                name = "",
                imageUrl = "",
                backgroundColorLight = "",
                backgroundColorDark = "",
            ),
            MonsterFolderPreview(
                index = "",
                name = "",
                imageUrl = "",
                backgroundColorLight = "",
                backgroundColorDark = "",
            )
        )
    )
}
