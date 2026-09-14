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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.home.HomeStrings
import br.alexandregpereira.hunter.home.homeMockViewState
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.AppCircleButton
import br.alexandregpereira.hunter.ui.compose.FolderCard
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.SectionTitle
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.compose.plus
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreen(
    state: HomeViewState,
    strings: HomeStrings,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCategoryClick: (HomeCategoryType) -> Unit = {},
    onMonsterClick: (index: String) -> Unit = {},
    onRefreshRecentlyViewedClick: () -> Unit = {},
    onFolderClick: (name: String) -> Unit = {},
    onSeeAllFoldersClick: () -> Unit = {},
    onCreateMonsterClick: () -> Unit = {},
    onCreateSpellClick: () -> Unit = {},
    onManageExtraContentClick: () -> Unit = {},
) = Window(
    backgroundColor = MaterialTheme.colors.background,
    level = 0,
) {
    // Outside the scroll, so the loading and the content have the same size and only fade
    LoadingScreen(
        isLoading = state.isLoading,
        showCircularLoading = false,
    ) {
        val horizontalPadding = 16.dp
        val sectionModifier = Modifier.padding(horizontal = horizontalPadding)

        LazyColumn(
            contentPadding = contentPadding + PaddingValues(vertical = 16.dp),
            modifier = modifier.fillMaxSize(),
        ) {
            item(key = HOME_HEADER_KEY) {
                HomeHeader(
                    title = strings.title,
                    menuContentDescription = strings.menu,
                    onMenuClick = onMenuClick,
                    modifier = sectionModifier,
                )
            }

            // The key keeps the remembered state, like the rows scroll, with its section and
            // animates the section when the order changes
            items(state.sections, key = { it.key }, contentType = { it.key }) { section ->
                when (section) {
                    HomeSectionState.Search -> HomeSearchButton(
                        placeholder = strings.searchPlaceholder,
                        onClick = onSearchClick,
                        modifier = Modifier
                            .animateItem()
                            .then(sectionModifier)
                            .padding(top = 20.dp),
                    )

                    is HomeSectionState.Categories -> HomeCategoryGrid(
                        categories = section.categories,
                        strings = strings,
                        onCategoryClick = onCategoryClick,
                        modifier = Modifier
                            .animateItem()
                            .then(sectionModifier)
                            .padding(top = 20.dp),
                    )

                    is HomeSectionState.RecentlyViewed -> Column(Modifier.animateItem()) {
                        // A new monsters list creates a new scroll state, so the updated list
                        // starts at the first monster instead of keeping the previous first
                        // visible monster in place
                        val listState = rememberSaveable(
                            section.monsters,
                            saver = LazyListState.Saver,
                        ) { LazyListState() }
                        val coroutineScope = rememberCoroutineScope()
                        HomeSectionTitle(
                            title = strings.recentlyViewed,
                            modifier = sectionModifier,
                            action = {
                                HomeRefreshButton(
                                    contentDescription = strings.refresh,
                                    onClick = {
                                        // The list can be the same, so it's scrolled here too
                                        coroutineScope.launch { listState.animateScrollToItem(0) }
                                        onRefreshRecentlyViewedClick()
                                    },
                                )
                            },
                        )
                        LazyRow(
                            state = listState,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                        ) {
                            items(section.monsters, key = { it.index }) { monster ->
                                HomeMonsterCard(
                                    monster = monster,
                                    onClick = { onMonsterClick(monster.index) },
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }
                    }

                    is HomeSectionState.Folders -> Column(Modifier.animateItem()) {
                        HomeSectionTitle(title = strings.folders, modifier = sectionModifier)
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
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
                                    modifier = Modifier
                                        .animateItem()
                                        .width(180.dp),
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

                    HomeSectionState.Create -> Column(Modifier.animateItem()) {
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
                        }
                    }

                    is HomeSectionState.ExtraContent -> HomeExtraContentCard(
                        title = strings.extraContent,
                        progressText = strings.extraContentProgress(section.added, section.total),
                        buttonText = strings.manageExtraContent,
                        added = section.added,
                        total = section.total,
                        onButtonClick = onManageExtraContentClick,
                        modifier = Modifier
                            .animateItem()
                            .then(sectionModifier)
                            .padding(top = 32.dp),
                    )
                }
            }
        }
    }
}

/**
 * The lazy list keys must be saveable on Android, so each section has a string key instead of its
 * class.
 */
private val HomeSectionState.key: String
    get() = when (this) {
        HomeSectionState.Search -> "Search"
        is HomeSectionState.Categories -> "Categories"
        is HomeSectionState.RecentlyViewed -> "RecentlyViewed"
        is HomeSectionState.Folders -> "Folders"
        HomeSectionState.Create -> "Create"
        is HomeSectionState.ExtraContent -> "ExtraContent"
    }

private const val HOME_HEADER_KEY = "Header"

@Composable
private fun HomeSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .fillMaxWidth()
        .padding(top = 32.dp, bottom = 12.dp),
) {
    SectionTitle(
        title = title,
        isHeader = false,
        modifier = Modifier.weight(1f),
    )
    action?.invoke()
}

@Composable
private fun HomeRefreshButton(
    contentDescription: String,
    onClick: () -> Unit,
) = AppCircleButton(
    isPrimary = false,
    size = AppButtonSize.SMALL,
    onClick = onClick,
) {
    Icon(
        imageVector = Icons.Filled.Refresh,
        contentDescription = contentDescription,
        tint = LocalContentColor.current,
        modifier = Modifier.size(22.dp),
    )
}

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
private fun HomeScreenLoadingPreview() = HunterTheme(darkTheme = true) {
    HomeScreen(
        state = homeMockViewState.copy(isLoading = true),
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
