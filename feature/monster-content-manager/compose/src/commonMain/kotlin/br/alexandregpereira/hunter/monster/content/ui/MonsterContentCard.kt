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

package br.alexandregpereira.hunter.monster.content.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerStrings
import br.alexandregpereira.hunter.strings.format
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppCard
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.AppSurface
import br.alexandregpereira.hunter.ui.compose.CoilImage
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.cardShape
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import br.alexandregpereira.hunter.ui.theme.Shapes

@Composable
internal fun MonsterContentCard(
    name: String,
    originalName: String?,
    totalMonsters: Int,
    totalSpells: Int,
    summary: String,
    coverImageUrl: String?,
    isAdded: Boolean,
    strings: MonsterContentManagerStrings,
    modifier: Modifier = Modifier,
    isDefault: Boolean = false,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) = AppCard(modifier = modifier, shape = cardShape) {
    Column(Modifier.padding(16.dp)) {
        Title(
            name = name,
            originalName = originalName,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        coverImageUrl?.let {
            Cover(
                coverImageUrl = coverImageUrl,
                name = name,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        Totals(
            totalMonsters = totalMonsters,
            totalSpells = totalSpells,
        )

        Spacer(Modifier.height(16.dp))

        Summary(summary = summary)

        Spacer(Modifier.height(8.dp))

        Buttons(
            isAdded = isAdded,
            isDefault = isDefault,
            removeText = strings.remove,
            addText = strings.add,
            previewLabel = strings.preview,
            previewEnabled = totalMonsters > 0,
            onAddClick = onAddClick,
            onRemoveClick = onRemoveClick,
            onPreviewClick = onPreviewClick,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun Cover(
    coverImageUrl: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilImage(
            imageUrl = coverImageUrl,
            contentDescription = name,
            shape = Shapes.large,
            contentScale = AppImageContentScale.Crop,
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface, shape = Shapes.large)
                .height(IMAGE_HEIGHT.dp)
                .width(172.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun Totals(
    totalMonsters: Int,
    totalSpells: Int,
) {
    if (totalMonsters <= 0 && totalSpells <= 0) return
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        totalMonsters.takeIf { it > 0 }?.let {
            Text(
                text = strings.totalMonsters.format(it),
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
        }

        totalSpells.takeIf { it > 0 }?.let {
            Text(
                text = strings.totalSpells.format(it),
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun Title(
    name: String,
    originalName: String?,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
) {
    SectionTitle(
        title = name,
        isHeader = false,
        modifier = Modifier
    )
    originalName?.let {
        Text(
            text = it,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
        )
    }
}

@Composable
private fun Summary(
    summary: String,
    modifier: Modifier = Modifier
) = Text(
    text = summary,
    fontWeight = FontWeight.Light,
    fontSize = 16.sp,
    modifier = modifier,
)

@Composable
private fun Buttons(
    isAdded: Boolean,
    removeText: String,
    addText: String,
    previewLabel: String,
    previewEnabled: Boolean,
    modifier: Modifier = Modifier,
    isDefault: Boolean = false,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {},
) {
    val text = if (isAdded) {
        removeText
    } else addText

    val click = if (isAdded) onRemoveClick else onAddClick

    Row(modifier) {
        AppButton(
            text = text,
            onClick = click,
            enabled = !isDefault,
            size = AppButtonSize.SMALL,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
        )

        AppButton(
            text = previewLabel,
            onClick = onPreviewClick,
            size = AppButtonSize.SMALL,
            enabled = previewEnabled,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
        )
    }
}

@Preview
@Composable
private fun MonsterContentCardPreview() = HunterTheme {
    AppSurface {
        MonsterContentCard(
            name = "Name",
            originalName = "Other name",
            totalMonsters = 10,
            totalSpells = 10,
            summary = "A menagerie of deadly monsters for the world's greatest roleplaying game. The Monster Manual presents a horde of classic Dungeons & Dragons creatures, including dragons, giants, mind flayers, and beholders—a monstrous feast for Dungeon Masters ready to challenge their players and populate their adventures.",
            coverImageUrl = "",
            isAdded = true,
            strings = MonsterContentManagerStrings(),
        )
    }
}

private const val IMAGE_HEIGHT = 220
