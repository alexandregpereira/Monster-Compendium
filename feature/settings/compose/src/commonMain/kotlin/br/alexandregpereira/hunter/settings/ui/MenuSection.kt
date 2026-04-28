/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessAlarm
import androidx.compose.material.icons.outlined.Podcasts
import androidx.compose.material.icons.outlined.Propane
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MenuSection(
    sectionTitle: String,
    items: ImmutableList<MenuSectionItemState>,
    modifier: Modifier = Modifier,
    onItemClicked: (index: Int) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = sectionTitle,
            modifier = Modifier.alpha(.7f),
        )
        MenuSectionBlock(
            items = items,
            modifier = Modifier,
            onItemClicked = onItemClicked,
        )
    }
}

internal data class MenuSectionItemState(
    val id: String = "",
    val iconPainter: Painter? = null,
    val text: String = "",
)

@Composable
internal fun MenuSectionBlock(
    items: ImmutableList<MenuSectionItemState>,
    modifier: Modifier = Modifier,
    onItemClicked: (index: Int) -> Unit = {},
) = AppCard(
    modifier = modifier,
) {
    Column {
        items.forEachIndexed { index, item ->
            if (index != 0) {
                Divider()
            }
            MenuSectionBlockItem(
                iconPainter = item.iconPainter,
                text = item.text,
                onClick = { onItemClicked(index) },
            )
        }
    }
}

@Composable
private fun MenuSectionBlockItem(
    text: String,
    iconPainter: Painter? = null,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .animatePressed(onClick = onClick),
    contentAlignment = Alignment.CenterStart,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        val iconSize = 24.dp
        if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                modifier = Modifier
                    .size(iconSize),
                contentDescription = null,
            )
        } else {
            Box(Modifier.size(iconSize))
        }
        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(1f),
        )
    }
}

@Composable
internal fun <T> rememberMenuItems(
    items: ImmutableList<T>,
    transform: (T) -> MenuSectionItemState,
): ImmutableList<MenuSectionItemState> {
    return remember(items) {
        items.map {
            transform(it)
        }.toImmutableList()
    }
}

@Preview
@Composable
private fun MenuSectionPreview() = HunterTheme {
    MenuSection(
        modifier = Modifier.background(MaterialTheme.colors.background),
        sectionTitle = "Section 1",
        items = persistentListOf(
            MenuSectionItemState(
                id = "1",
                text = "Item 1",
                iconPainter = rememberVectorPainter(image = Icons.Outlined.Podcasts),
            ),
            MenuSectionItemState(
                id = "2",
                iconPainter = rememberVectorPainter(image = Icons.Outlined.Propane),
                text = "Item 2",
            ),
            MenuSectionItemState(
                id = "3",
                iconPainter = rememberVectorPainter(image = Icons.Outlined.AccessAlarm),
                text = "Item 3",
            )
        ),
    )
}
