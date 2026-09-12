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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.home.HomeStrings
import br.alexandregpereira.hunter.home.homeMockViewState
import br.alexandregpereira.hunter.ui.compose.FolderCard
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun HomeScreen(
    state: HomeViewState,
    strings: HomeStrings,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCategoryClick: (HomeCategoryType) -> Unit = {},
    onMonsterClick: (index: String) -> Unit = {},
    onFolderClick: (name: String) -> Unit = {},
    onSeeAllFoldersClick: () -> Unit = {},
    onCreateMonsterClick: () -> Unit = {},
    onCreateSpellClick: () -> Unit = {},
    onCreateFolderClick: () -> Unit = {},
    onManageExtraContentClick: () -> Unit = {},
) = Window(
    backgroundColor = MaterialTheme.colors.background,
    level = 0,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(vertical = 16.dp),
    ) {
        val horizontalPadding = 16.dp
        val sectionModifier = Modifier.padding(horizontal = horizontalPadding)

        HomeHeader(
            title = strings.title,
            notificationsContentDescription = strings.notifications,
            menuContentDescription = strings.menu,
            hasUnreadNotifications = state.hasUnreadNotifications,
            onNotificationClick = onNotificationClick,
            onMenuClick = onMenuClick,
            modifier = sectionModifier,
        )

        state.sections.forEach { section ->
            // Keeps the remembered state, like the rows scroll, with its section when the order changes
            key(section::class) {
                when (section) {
                    HomeSectionState.Search -> HomeSearchButton(
                        placeholder = strings.searchPlaceholder,
                        onClick = onSearchClick,
                        modifier = sectionModifier.padding(top = 20.dp),
                    )

                    is HomeSectionState.Categories -> HomeCategoryGrid(
                        categories = section.categories,
                        strings = strings,
                        onCategoryClick = onCategoryClick,
                        modifier = sectionModifier.padding(top = 20.dp),
                    )

                    is HomeSectionState.RecentlyViewed -> {
                        HomeSectionTitle(title = strings.recentlyViewed, modifier = sectionModifier)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                        ) {
                            items(section.monsters, key = { it.index }) { monster ->
                                HomeMonsterCard(
                                    monster = monster,
                                    onClick = { onMonsterClick(monster.index) },
                                )
                            }
                        }
                    }

                    is HomeSectionState.Folders -> {
                        HomeSectionTitle(title = strings.folders, modifier = sectionModifier)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                        ) {
                            items(section.folders, key = { it.name }) { folder ->
                                FolderCard(
                                    folderName = folder.name,
                                    image1 = folder.image1,
                                    image2 = folder.image2,
                                    image3 = folder.image3,
                                    fontSize = 18.sp,
                                    modifier = Modifier.width(180.dp),
                                    onCLick = { onFolderClick(folder.name) },
                                )
                            }
                        }
                        HomePillButton(
                            text = strings.seeAllFolders,
                            onClick = onSeeAllFoldersClick,
                            modifier = sectionModifier.padding(top = 16.dp),
                        )
                    }

                    HomeSectionState.Create -> {
                        HomeSectionTitle(title = strings.create, modifier = sectionModifier)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = sectionModifier,
                        ) {
                            HomePillButton(
                                text = strings.monster,
                                icon = Icons.Filled.Add,
                                onClick = onCreateMonsterClick,
                            )
                            HomePillButton(
                                text = strings.spell,
                                icon = Icons.Filled.Add,
                                onClick = onCreateSpellClick,
                            )
                            HomePillButton(
                                text = strings.folder,
                                icon = Icons.Filled.Add,
                                onClick = onCreateFolderClick,
                            )
                        }
                    }

                    is HomeSectionState.ExtraContent -> HomeExtraContentCard(
                        title = strings.extraContent,
                        progressText = strings.extraContentProgress(section.added, section.total),
                        buttonText = strings.manageExtraContent,
                        added = section.added,
                        total = section.total,
                        onButtonClick = onManageExtraContentClick,
                        modifier = sectionModifier.padding(top = 32.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) = SectionTitle(
    title = title,
    isHeader = false,
    modifier = modifier.padding(top = 32.dp, bottom = 12.dp),
)

@Preview
@Composable
private fun HomeScreenDarkPreview() = HunterTheme(darkTheme = true) {
    HomeScreen(
        state = homeMockViewState,
        strings = HomeStrings(),
    )
}

@Preview
@Composable
private fun HomeScreenLightPreview() = HunterTheme(darkTheme = false) {
    HomeScreen(
        state = homeMockViewState,
        strings = HomeStrings(),
    )
}

@Preview
@Composable
private fun HomeScreenReorderedSectionsPreview() = HunterTheme(darkTheme = true) {
    val sections = homeMockViewState.sections
    HomeScreen(
        state = homeMockViewState.copy(
            sections = listOfNotNull(
                sections.filterIsInstance<HomeSectionState.ExtraContent>().firstOrNull(),
                sections.filterIsInstance<HomeSectionState.Folders>().firstOrNull(),
                HomeSectionState.Search,
            ),
        ),
        strings = HomeStrings(),
    )
}
