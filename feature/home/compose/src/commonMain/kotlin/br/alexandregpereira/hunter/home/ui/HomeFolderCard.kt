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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.MonsterCoilImage
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.animatePressed

@Composable
internal fun HomeFolderCard(
    folder: HomeFolderState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = Column(
    modifier = modifier
        .width(136.dp)
        .animatePressed(onClick = onClick),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(homeCardShape),
    ) {
        HomeFolderImage(
            image = folder.images.getOrNull(0),
            modifier = Modifier.fillMaxWidth().weight(0.6f),
        )
        Row(Modifier.fillMaxWidth().weight(0.4f)) {
            HomeFolderImage(
                image = folder.images.getOrNull(1),
                modifier = Modifier.fillMaxHeight().weight(1f),
            )
            HomeFolderImage(
                image = folder.images.getOrNull(2),
                modifier = Modifier.fillMaxHeight().weight(1f),
            )
        }
    }
    Text(
        text = folder.name,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 8.dp),
    )
}

@Composable
private fun HomeFolderImage(
    image: MonsterImageState?,
    modifier: Modifier = Modifier,
) = Box(modifier.background(MaterialTheme.colors.surface)) {
    if (image != null) {
        MonsterCoilImage(
            imageUrl = image.url,
            contentDescription = image.contentDescription,
            backgroundColor = image.backgroundColor.getColor(isSystemInDarkTheme()),
            contentScale = AppImageContentScale.Crop,
        )
    }
}

@Preview
@Composable
private fun HomeFolderCardPreview() = PreviewWindow(darkTheme = true) {
    HomeFolderCard(
        folder = HomeFolderState(
            name = "Boss Fights",
            images = listOf(
                previewFolderImage(MonsterTypeState.DRAGON, "#E8DCE0"),
                previewFolderImage(MonsterTypeState.FIEND, "#DADBE6"),
                previewFolderImage(MonsterTypeState.UNDEAD, "#DCE3E8"),
            ),
        ),
        modifier = Modifier.padding(16.dp),
    )
}

private fun previewFolderImage(type: MonsterTypeState, color: String) = MonsterImageState(
    url = "",
    type = type,
    backgroundColor = ColorState(light = color, dark = color),
    challengeRating = "",
    contentScale = AppImageContentScale.Fit,
)
