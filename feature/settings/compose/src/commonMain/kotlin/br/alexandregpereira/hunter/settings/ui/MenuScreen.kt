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

package br.alexandregpereira.hunter.settings.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.settings.MenuItemIdState
import br.alexandregpereira.hunter.settings.MenuItemState
import br.alexandregpereira.hunter.settings.ui.resources.Res
import br.alexandregpereira.hunter.settings.ui.resources.ic_github
import br.alexandregpereira.hunter.settings.ui.resources.ic_import
import br.alexandregpereira.hunter.settings.ui.resources.ic_language
import br.alexandregpereira.hunter.settings.ui.resources.ic_monster
import br.alexandregpereira.hunter.settings.ui.resources.ic_moon
import br.alexandregpereira.hunter.settings.ui.resources.ic_settings
import br.alexandregpereira.hunter.settings.ui.resources.ic_spell
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun MenuScreen(
    menuItemsGroupBySection: ImmutableMap<String, ImmutableList<MenuItemState>>,
    versionName: String,
    showPremium: Boolean,
    contentPadding: PaddingValues = PaddingValues(),
    onItemClicked: (id: MenuItemIdState) -> Unit = {},
    onPremiumClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SectionTitle(
                title = "Menu",
                isHeader = true,
                modifier = Modifier.padding(
                    top = 16.dp
                ),
            )
            AnimatedContent(
                targetState = showPremium,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(220)
                    ).togetherWith(
                        exit = fadeOut(
                            animationSpec = tween(110)
                        )
                    )
                },
                contentAlignment = Alignment.Center,
                label = "ShowPremium",
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (showPremium) {
                    PremiumCard(
                        onClick = onPremiumClick,
                    )
                }
            }
            menuItemsGroupBySection.forEach { (section, items) ->
                val iconsById = items.associateBy { it.id }.mapValues {
                    it.value.id.toIcon()
                }
                MenuSection(
                    sectionTitle = section,
                    items = rememberMenuItems(items) {
                        MenuSectionItemState(
                            id = it.id.name,
                            iconPainter = iconsById[it.id],
                            text = it.text,
                        )
                    },
                    onItemClicked = { index ->
                        onItemClicked(items[index].id)
                    },
                )
            }

            if (versionName.isNotBlank()) {
                Text(
                    text = "v$versionName",
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .alpha(.7f)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MenuItemIdState.toIcon(): Painter {
    val icon: DrawableResource = when (this) {
        MenuItemIdState.OPEN_GITHUB_PROJECT -> Res.drawable.ic_github
        MenuItemIdState.SETTINGS -> Res.drawable.ic_language
        MenuItemIdState.ADVANCED_SETTINGS -> Res.drawable.ic_settings
        MenuItemIdState.APPEARANCE_SETTINGS -> Res.drawable.ic_moon
        MenuItemIdState.IMPORT_CONTENT -> Res.drawable.ic_import
        MenuItemIdState.SPELLS -> Res.drawable.ic_spell
        MenuItemIdState.MANAGE_MONSTER_CONTENT -> Res.drawable.ic_monster
    }
    return painterResource(resource = icon)
}

@Preview
@Composable
private fun MenuScreenPreview() = HunterTheme {
    val menuItems = MenuItemIdState.entries.mapIndexed { index, id ->
        val section = when (index % 3) {
            0 -> "Section 1"
            1 -> "Section 2"
            else -> "Section 3"
        }
        MenuItemState(
            id = id,
            text = id.name,
            section = section,
        )
    }.toImmutableList()
    var showPremium by remember {
        mutableStateOf(false)
    }
    MenuScreen(
        menuItemsGroupBySection = menuItems.groupBy { it.section }
            .mapValues { it.value.toImmutableList() }.toImmutableMap(),
        versionName = "1.0.0",
        showPremium = showPremium,
        onPremiumClick = {
            showPremium = false
        },
        onItemClicked = {
            showPremium = true
        }
    )
}
