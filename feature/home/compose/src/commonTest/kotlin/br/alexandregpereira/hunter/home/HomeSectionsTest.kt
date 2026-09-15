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

package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.domain.folder.model.MonsterFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderImageContentScale
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderType
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import br.alexandregpereira.hunter.home.ui.HomeFolderState
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.compose.FolderImageState
import br.alexandregpereira.hunter.home.domain.HomeContentTotals
import br.alexandregpereira.hunter.home.domain.HomeExtraContentProgress
import br.alexandregpereira.hunter.home.domain.toHomeExtraContentProgress
import br.alexandregpereira.hunter.home.ui.HomeCategoryState
import br.alexandregpereira.hunter.home.ui.HomeCategoryType
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HomeSectionsTest {

    private val categories = HomeSectionState.Categories(
        categories = listOf(HomeCategoryState(type = HomeCategoryType.CREATURES, total = 1)),
    )
    private val recentlyViewed = HomeSectionState.RecentlyViewed(monsters = emptyList())
    private val folders = HomeSectionState.Folders(folders = emptyList())

    @Test
    fun `extra content without content added is placed after the categories`() {
        val extraContent = HomeSectionState.ExtraContent(added = 0, total = 8)

        val sections = buildHomeSections(
            categories = categories,
            extraContent = extraContent,
            recentlyViewed = recentlyViewed,
            folders = folders,
        )

        assertEquals(
            expected = listOf(
                HomeSectionState.Search,
                categories,
                extraContent,
                folders,
                recentlyViewed,
                HomeSectionState.Create,
            ),
            actual = sections,
        )
    }

    @Test
    fun `extra content with content added is placed at the end`() {
        val extraContent = HomeSectionState.ExtraContent(added = 1, total = 8)

        val sections = buildHomeSections(
            categories = categories,
            extraContent = extraContent,
            recentlyViewed = recentlyViewed,
            folders = folders,
        )

        assertEquals(
            expected = listOf(
                HomeSectionState.Search,
                categories,
                folders,
                recentlyViewed,
                HomeSectionState.Create,
                extraContent,
            ),
            actual = sections,
        )
    }

    @Test
    fun `sections not loaded are not shown`() {
        val sections = buildHomeSections(
            categories = null,
            extraContent = null,
            recentlyViewed = null,
            folders = null,
        )

        assertEquals(
            expected = listOf(HomeSectionState.Search, HomeSectionState.Create),
            actual = sections,
        )
    }

    @Test
    fun `categories are not shown when there is no content`() {
        assertNull(HomeContentTotals(monsters = 0, spells = 0).toCategoriesSection())
    }

    @Test
    fun `categories show the monsters and spells totals`() {
        assertEquals(
            expected = HomeSectionState.Categories(
                categories = listOf(
                    HomeCategoryState(type = HomeCategoryType.CREATURES, total = 400),
                    HomeCategoryState(type = HomeCategoryType.SPELLS, total = 500),
                ),
            ),
            actual = HomeContentTotals(monsters = 400, spells = 500).toCategoriesSection(),
        )
    }

    @Test
    fun `extra content is not shown when there is no extra content`() {
        assertNull(HomeExtraContentProgress(added = 0, total = 0).toExtraContentSection())
    }

    @Test
    fun `extra content progress ignores the default source`() {
        val sources = listOf(
            alternativeSource(acronym = "SRD", isDefault = true, isAdded = true),
            alternativeSource(acronym = "MM", isAdded = true),
            alternativeSource(acronym = "PHB", isAdded = false),
            alternativeSource(acronym = "VGM", isAdded = false),
        )

        assertEquals(
            expected = HomeExtraContentProgress(added = 1, total = 3),
            actual = sources.toHomeExtraContentProgress(),
        )
    }

    @Test
    fun `empty folders are placed where the folders are`() {
        val sections = buildHomeSections(
            categories = categories,
            extraContent = null,
            recentlyViewed = recentlyViewed,
            folders = HomeSectionState.EmptyFolders,
        )

        assertEquals(
            expected = listOf(
                HomeSectionState.Search,
                categories,
                HomeSectionState.EmptyFolders,
                recentlyViewed,
                HomeSectionState.Create,
            ),
            actual = sections,
        )
    }

    @Test
    fun `empty folders are shown when there is no folder with monsters`() {
        assertEquals(
            expected = HomeSectionState.EmptyFolders,
            actual = emptyList<MonsterFolder>().toFoldersSection(),
        )
        assertEquals(
            expected = HomeSectionState.EmptyFolders,
            actual = listOf(MonsterFolder(name = "Empty", monsters = emptyList())).toFoldersSection(),
        )
    }

    @Test
    fun `folders keep the folder list order, skip empty folders and are limited to five`() {
        val folders = listOf(
            monsterFolder(name = "Folder 1"),
            MonsterFolder(name = "Empty", monsters = emptyList()),
            monsterFolder(name = "Folder 2"),
            monsterFolder(name = "Folder 3"),
            monsterFolder(name = "Folder 4"),
            monsterFolder(name = "Folder 5"),
            monsterFolder(name = "Folder 6"),
        )

        val section = folders.toFoldersSection()

        assertEquals(
            expected = listOf("Folder 1", "Folder 2", "Folder 3", "Folder 4", "Folder 5"),
            actual = (section as? HomeSectionState.Folders)?.folders?.map { it.name },
        )
    }

    @Test
    fun `folder shows the first three monster images`() {
        val folder = MonsterFolder(
            name = "Boss Fights",
            monsters = listOf("dragon", "lich", "tarrasque", "kraken").map { monsterPreview(index = it) },
        )

        val section = listOf(folder).toFoldersSection()

        assertEquals(
            expected = HomeSectionState.Folders(
                folders = listOf(
                    HomeFolderState(
                        name = "Boss Fights",
                        image1 = folderImage(index = "dragon"),
                        image2 = folderImage(index = "lich"),
                        image3 = folderImage(index = "tarrasque"),
                    ),
                ),
            ),
            actual = section,
        )
    }

    @Test
    fun `recently viewed is not shown when no monster was viewed`() {
        assertNull(emptyList<MonsterPreviewFolder>().toRecentlyViewedSection())
    }

    @Test
    fun `recently viewed keeps the order and maps the monsters to cards`() {
        val dragon = MonsterPreviewFolder(
            index = "dragon",
            name = "Ancient Red Dragon",
            type = MonsterPreviewFolderType.DRAGON,
            challengeRating = "24",
            imageUrl = "https://images/dragon.png",
            backgroundColorLight = "#FFFFFF",
            backgroundColorDark = "#000000",
            imageContentScale = MonsterPreviewFolderImageContentScale.Crop,
            isHorizontalImage = true,
        )

        val section = listOf(dragon, monsterPreview(index = "goblin")).toRecentlyViewedSection()

        assertEquals(expected = listOf("dragon", "goblin"), actual = section?.monsters?.map { it.index })
        assertEquals(
            expected = MonsterCardState(
                index = "dragon",
                name = "Ancient Red Dragon",
                imageState = MonsterImageState(
                    url = "https://images/dragon.png",
                    type = MonsterTypeState.DRAGON,
                    backgroundColor = ColorState(light = "#FFFFFF", dark = "#000000"),
                    challengeRating = "24",
                    contentScale = AppImageContentScale.Crop,
                    isHorizontal = true,
                    contentDescription = "Ancient Red Dragon",
                ),
            ),
            actual = section?.monsters?.first(),
        )
    }

    private fun monsterFolder(name: String) = MonsterFolder(
        name = name,
        monsters = listOf(monsterPreview(index = "$name-monster")),
    )

    private fun monsterPreview(index: String) = MonsterPreviewFolder(
        index = index,
        name = index,
        imageUrl = "https://images/$index.png",
        backgroundColorLight = "#FFFFFF",
        backgroundColorDark = "#000000",
        imageContentScale = MonsterPreviewFolderImageContentScale.Fit,
    )

    private fun folderImage(index: String) = FolderImageState(
        url = "https://images/$index.png",
        contentDescription = index,
        isHorizontalImage = false,
        backgroundColorLight = "#FFFFFF",
        backgroundColorDark = "#000000",
    )

    private fun alternativeSource(
        acronym: String,
        isDefault: Boolean = false,
        isAdded: Boolean = false,
    ) = AlternativeSource(
        source = Source(
            name = acronym,
            acronym = acronym,
            originalAcronym = null,
            originalName = null,
        ),
        totalMonsters = 10,
        totalSpells = 0,
        summary = "",
        coverImageUrl = "",
        isEnabled = true,
        isLoreEnabled = false,
        isDefault = isDefault,
        isAdded = isAdded,
    )
}
