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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.home.HomeStrings
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.animatePressed
import br.alexandregpereira.hunter.ui.compose.cardShape
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_dragon
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun HomeCategoryGrid(
    categories: List<HomeCategoryState>,
    strings: HomeStrings,
    modifier: Modifier = Modifier,
    onCategoryClick: (HomeCategoryType) -> Unit = {},
) = Column(
    verticalArrangement = Arrangement.spacedBy(12.dp),
    modifier = modifier.fillMaxWidth(),
) {
    categories.chunked(2).forEach { rowCategories ->
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            rowCategories.forEach { category ->
                HomeCategoryCard(
                    icon = category.type.iconPainter(),
                    title = category.type.title(strings),
                    subtitle = strings.total(category.total),
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick(category.type) },
                )
            }
            if (rowCategories.size == 1) {
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HomeCategoryCard(
    icon: Painter,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) = AppCard(
    shape = cardShape,
    elevation = 0.dp,
    modifier = modifier.animatePressed(pressedScale = 0.96f, onClick = onClick),
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

/**
 * The creatures use the same dragon icon of the monster type badges.
 */
@Composable
private fun HomeCategoryType.iconPainter(): Painter = when (this) {
    HomeCategoryType.CREATURES -> painterResource(Res.drawable.ic_dragon)
    HomeCategoryType.SPELLS -> rememberVectorPainter(Icons.Outlined.AutoAwesome)
    HomeCategoryType.CONDITIONS -> rememberVectorPainter(Icons.Outlined.Schedule)
}

private fun HomeCategoryType.title(strings: HomeStrings): String = when (this) {
    HomeCategoryType.CREATURES -> strings.creatures
    HomeCategoryType.SPELLS -> strings.spells
    HomeCategoryType.CONDITIONS -> strings.conditions
}

@Preview
@Composable
private fun HomeCategoryGridPreview() = PreviewWindow(darkTheme = true) {
    HomeCategoryGrid(
        categories = listOf(
            HomeCategoryState(type = HomeCategoryType.CREATURES, total = 400),
            HomeCategoryState(type = HomeCategoryType.SPELLS, total = 500),
            HomeCategoryState(type = HomeCategoryType.CONDITIONS, total = 10),
        ),
        strings = HomeStrings(),
        modifier = Modifier.padding(16.dp),
    )
}
