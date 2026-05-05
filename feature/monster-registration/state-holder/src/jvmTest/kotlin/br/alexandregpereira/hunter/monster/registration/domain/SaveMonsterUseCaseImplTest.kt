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

@file:Suppress("UnusedFlow")

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.settings.AppSettingsImageContentScale
import br.alexandregpereira.hunter.domain.settings.AppearanceSettings
import br.alexandregpereira.hunter.domain.settings.GetAppearanceSettings
import br.alexandregpereira.hunter.domain.usecase.ResetMonsterImage
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class SaveMonsterUseCaseImplTest {

    private val saveMonstersUseCase: SaveMonstersUseCase = mockk()
    private val monsterImageRepository: MonsterImageRepository = mockk(relaxUnitFun = true)
    private val saveMonstersLoreUseCase: SaveMonstersLoreUseCase = mockk()
    private val monsterLocalRepository: MonsterLocalRepository = mockk()
    private val resetMonsterImage: ResetMonsterImage = mockk(relaxUnitFun = true)
    private val getAppearanceSettings: GetAppearanceSettings = mockk()

    private val useCase = SaveMonsterUseCaseImpl(
        saveMonsters = saveMonstersUseCase,
        monsterImageRepository = monsterImageRepository,
        saveMonstersLoreUseCase = saveMonstersLoreUseCase,
        monsterLocalRepository = monsterLocalRepository,
        resetMonsterImage = resetMonsterImage,
        getAppearanceSettings = getAppearanceSettings,
    )

    @Test
    fun `invoke When status is Original and monster changed Then saves with Edited status`() = runTest {
        val monster = createMonster(name = "Goblin Modified", status = MonsterStatus.Original)
        val originalMonster = createMonster(name = "Goblin", status = MonsterStatus.Original)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster, emptyList(), null)

        coVerify {
            saveMonstersUseCase(listOf(monster.copy(status = MonsterStatus.Edited)))
        }
    }

    @Test
    fun `invoke When status is Original and monster unchanged Then saves with Original status`() = runTest {
        val monster = createMonster(status = MonsterStatus.Original)
        val originalMonster = createMonster(status = MonsterStatus.Original)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster, emptyList(), null)

        coVerify {
            saveMonstersUseCase(listOf(monster.copy(status = MonsterStatus.Original)))
        }
    }

    @Test
    fun `invoke When status is Original and only imageUrl changed Then saves with Original status`() = runTest {
        val imageData = createMonsterImageData(url = "old.png")
        val monster = createMonster(imageData = imageData.copy(url = "new.png"), status = MonsterStatus.Original)
        val originalMonster = createMonster(imageData = imageData, status = MonsterStatus.Original)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster, emptyList(), null)

        coVerify {
            saveMonstersUseCase(listOf(monster.copy(status = MonsterStatus.Original)))
        }
    }

    @Test
    fun `invoke When status is Original and originalMonster is null Then saves with Edited status`() = runTest {
        val monster = createMonster(status = MonsterStatus.Original)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            saveMonstersUseCase(listOf(monster.copy(status = MonsterStatus.Edited)))
        }
    }

    @Test
    fun `invoke When status is Edited Then saves monster unchanged`() = runTest {
        val monster = createMonster(status = MonsterStatus.Edited)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify { saveMonstersUseCase(listOf(monster)) }
    }

    @Test
    fun `invoke When status is Clone Then saves monster unchanged`() = runTest {
        val monster = createMonster(status = MonsterStatus.Clone)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify { saveMonstersUseCase(listOf(monster)) }
    }

    @Test
    fun `invoke When status is Imported Then saves monster unchanged`() = runTest {
        val monster = createMonster(status = MonsterStatus.Imported)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify { saveMonstersUseCase(listOf(monster)) }
    }

    @Test
    fun `invoke When monster already exists and image is unchanged Then does not save monster image`() = runTest {
        val imageData = createMonsterImageData(url = "same.png")
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        val previousMonster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns createMonster()
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster, emptyList(), null)

        coVerify(exactly = 0) { monsterImageRepository.saveMonsterImage(any()) }
    }

    @Test
    fun `invoke Then saves monster image with correct data`() = runTest {
        val imageData = MonsterImageData(
            url = "https://example.com/goblin.png",
            backgroundColor = Color(light = "#ffffff", dark = "#000000"),
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Crop,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns createMonster()
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns null
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Fit)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            monsterImageRepository.saveMonsterImage(
                MonsterImage(
                    monsterIndex = monster.index,
                    backgroundColor = imageData.backgroundColor,
                    isHorizontalImage = imageData.isHorizontal,
                    imageUrl = imageData.url,
                    contentScale = imageData.contentScale,
                )
            )
        }
    }

    @Test
    fun `invoke When image data changed and all values revert to local monster values Then resets monster image`() = runTest {
        val imageData = MonsterImageData(
            url = "original.png",
            backgroundColor = Color(light = "#ffffff", dark = "#000000"),
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Crop,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData)
        val previousMonster = createMonster(imageData = createMonsterImageData(url = "changed.png"), status = MonsterStatus.Edited)
        val localMonster = createMonster(imageData = imageData)
        val currentLocalMonsterImage = MonsterImage(
            monsterIndex = monster.index,
            backgroundColor = imageData.backgroundColor,
            isHorizontalImage = imageData.isHorizontal,
            imageUrl = imageData.url,
            contentScale = imageData.contentScale,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns localMonster
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns currentLocalMonsterImage
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Fit)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster, emptyList(), null)

        coVerify { resetMonsterImage(monsterIndex = monster.index) }
        coVerify(exactly = 0) { monsterImageRepository.saveMonsterImage(any()) }
    }

    @Test
    fun `invoke When contentScale matches settings Then saves without contentScale`() = runTest {
        val imageData = MonsterImageData(
            url = "goblin.png",
            backgroundColor = Color(light = "#ff0000", dark = "#000000"),
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Fit,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData)
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns createMonster()
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns null
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Fit)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            monsterImageRepository.saveMonsterImage(
                MonsterImage(
                    monsterIndex = monster.index,
                    backgroundColor = imageData.backgroundColor,
                    isHorizontalImage = imageData.isHorizontal,
                    imageUrl = imageData.url,
                    contentScale = null,
                )
            )
        }
    }

    @Test
    fun `invoke When backgroundColor matches local monster image value Then saves without backgroundColor`() = runTest {
        val imageData = MonsterImageData(
            backgroundColor = Color(light = "#ffffff", dark = "#000000"),
            url = "new.png",
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Crop,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        val localMonster = createMonster(imageData = MonsterImageData(
            backgroundColor = Color(light = "#ffffff", dark = "#000000"),
            url = "old.png",
            isHorizontal = false,
            contentScale = null,
            isImageDataFromCustomDatabase = false,
        ))
        val currentLocalMonsterImage = MonsterImage(
            monsterIndex = monster.index,
            backgroundColor = Color(light = "#ffffff", dark = "#000000"),
            imageUrl = "other.png",
            isHorizontalImage = false,
            contentScale = MonsterImageContentScale.Fit,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns localMonster
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns currentLocalMonsterImage
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Fit)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            monsterImageRepository.saveMonsterImage(
                MonsterImage(
                    monsterIndex = monster.index,
                    backgroundColor = null,
                    isHorizontalImage = true,
                    imageUrl = "new.png",
                    contentScale = MonsterImageContentScale.Crop,
                )
            )
        }
    }

    @Test
    fun `invoke When isHorizontalImage matches local monster image value Then saves without isHorizontalImage`() = runTest {
        val imageData = MonsterImageData(
            backgroundColor = Color(light = "#ff0000", dark = "#000000"),
            url = "new.png",
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Crop,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        val localMonster = createMonster(imageData = MonsterImageData(
            backgroundColor = Color(light = "#old", dark = "#000000"),
            url = "old.png",
            isHorizontal = true,
            contentScale = null,
            isImageDataFromCustomDatabase = false,
        ))
        val currentLocalMonsterImage = MonsterImage(
            monsterIndex = monster.index,
            backgroundColor = Color(light = "#diff", dark = "#000000"),
            imageUrl = "other.png",
            isHorizontalImage = true,
            contentScale = MonsterImageContentScale.Fit,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns localMonster
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns currentLocalMonsterImage
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Fit)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            monsterImageRepository.saveMonsterImage(
                MonsterImage(
                    monsterIndex = monster.index,
                    backgroundColor = Color(light = "#ff0000", dark = "#000000"),
                    isHorizontalImage = null,
                    imageUrl = "new.png",
                    contentScale = MonsterImageContentScale.Crop,
                )
            )
        }
    }

    @Test
    fun `invoke When imageUrl matches local monster image value Then saves without imageUrl`() = runTest {
        val imageData = MonsterImageData(
            backgroundColor = Color(light = "#ff0000", dark = "#000000"),
            url = "same.png",
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Crop,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        val localMonster = createMonster(imageData = MonsterImageData(
            backgroundColor = Color(light = "#old", dark = "#000000"),
            url = "same.png",
            isHorizontal = false,
            contentScale = null,
            isImageDataFromCustomDatabase = false,
        ))
        val currentLocalMonsterImage = MonsterImage(
            monsterIndex = monster.index,
            backgroundColor = Color(light = "#diff", dark = "#000000"),
            imageUrl = "same.png",
            isHorizontalImage = false,
            contentScale = MonsterImageContentScale.Fit,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns localMonster
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns currentLocalMonsterImage
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Fit)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            monsterImageRepository.saveMonsterImage(
                MonsterImage(
                    monsterIndex = monster.index,
                    backgroundColor = Color(light = "#ff0000", dark = "#000000"),
                    isHorizontalImage = true,
                    imageUrl = null,
                    contentScale = MonsterImageContentScale.Crop,
                )
            )
        }
    }

    @Test
    fun `invoke When contentScale matches local monster image value Then saves without contentScale`() = runTest {
        val imageData = MonsterImageData(
            backgroundColor = Color(light = "#ff0000", dark = "#000000"),
            url = "new.png",
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Fit,
            isImageDataFromCustomDatabase = false,
        )
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        val localMonster = createMonster(imageData = MonsterImageData(
            backgroundColor = Color(light = "#old", dark = "#000000"),
            url = "old.png",
            isHorizontal = false,
            contentScale = MonsterImageContentScale.Fit,
            isImageDataFromCustomDatabase = false,
        ))
        val currentLocalMonsterImage = MonsterImage(
            monsterIndex = monster.index,
            backgroundColor = Color(light = "#diff", dark = "#000000"),
            imageUrl = "other.png",
            isHorizontalImage = false,
            contentScale = MonsterImageContentScale.Fit,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns localMonster
        coEvery { monsterImageRepository.getLocalMonsterImage(any()) } returns currentLocalMonsterImage
        every { getAppearanceSettings() } returns flowOf(
            AppearanceSettings(defaultLightBackground = "", defaultDarkBackground = "", imageContentScale = AppSettingsImageContentScale.Crop)
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, emptyList(), null)

        coVerify {
            monsterImageRepository.saveMonsterImage(
                MonsterImage(
                    monsterIndex = monster.index,
                    backgroundColor = Color(light = "#ff0000", dark = "#000000"),
                    isHorizontalImage = true,
                    imageUrl = "new.png",
                    contentScale = null,
                )
            )
        }
    }

    @Test
    fun `invoke When lore entries changed Then saves lore with Edited status`() = runTest {
        val monster = createMonster(status = MonsterStatus.Edited)
        val newEntries = listOf(MonsterLoreEntry(index = "entry-1", description = "New lore"))
        val originalLore = MonsterLore(
            index = monster.index,
            name = monster.name,
            entries = listOf(MonsterLoreEntry(index = "entry-1", description = "Old lore")),
            status = MonsterLoreStatus.Original,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, newEntries, originalLore)

        coVerify {
            saveMonstersLoreUseCase(
                monsterLore = listOf(
                    MonsterLore(
                        index = monster.index,
                        name = monster.name,
                        entries = newEntries,
                        status = MonsterLoreStatus.Edited,
                    )
                ),
                isSync = false,
            )
        }
    }

    @Test
    fun `invoke When lore entries unchanged Then saves lore with original status`() = runTest {
        val monster = createMonster(status = MonsterStatus.Edited)
        val entries = listOf(MonsterLoreEntry(index = "entry-1", description = "Same lore"))
        val originalLore = MonsterLore(
            index = monster.index,
            name = monster.name,
            entries = entries,
            status = MonsterLoreStatus.Original,
        )
        coEvery { monsterLocalRepository.getMonsterPreview(any()) } returns null
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, previousMonster = null, entries, originalLore)

        coVerify {
            saveMonstersLoreUseCase(
                monsterLore = listOf(
                    MonsterLore(
                        index = monster.index,
                        name = monster.name,
                        entries = entries,
                        status = MonsterLoreStatus.Original,
                    )
                ),
                isSync = false,
            )
        }
    }

    private fun createMonster(
        index: String = "goblin",
        name: String = "Goblin",
        imageData: MonsterImageData = createMonsterImageData(),
        status: MonsterStatus = MonsterStatus.Original,
    ) = MonsterFactory.createEmpty(
        index = index,
    ).copy(
        name = name, imageData = imageData, status = status
    )

    private fun createMonsterImageData(
        url: String = "",
    ): MonsterImageData = MonsterImageData(
        url = url,
        backgroundColor = Color(
            light = "",
            dark = "",
        ),
        isHorizontal = false,
        contentScale = null,
        isImageDataFromCustomDatabase = false,
    )
}
