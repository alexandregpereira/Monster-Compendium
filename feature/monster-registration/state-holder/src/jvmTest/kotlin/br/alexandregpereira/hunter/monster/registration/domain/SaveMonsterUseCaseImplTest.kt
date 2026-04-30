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
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
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

    private val useCase = SaveMonsterUseCaseImpl(
        saveMonsters = saveMonstersUseCase,
        monsterImageRepository = monsterImageRepository,
        saveMonstersLoreUseCase = saveMonstersLoreUseCase,
    )

    @Test
    fun `invoke When status is Original and monster changed Then saves with Edited status`() = runTest {
        val monster = createMonster(name = "Goblin Modified", status = MonsterStatus.Original)
        val originalMonster = createMonster(name = "Goblin", status = MonsterStatus.Original)
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
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster, emptyList(), null)

        coVerify {
            saveMonstersUseCase(listOf(monster.copy(status = MonsterStatus.Original)))
        }
    }

    @Test
    fun `invoke When status is Original and only imageUrl changed Then saves with Original status`() = runTest {
        val imageData = MonsterImageData(url = "old.png")
        val monster = createMonster(imageData = imageData.copy(url = "new.png"), status = MonsterStatus.Original)
        val originalMonster = createMonster(imageData = imageData, status = MonsterStatus.Original)
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
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, emptyList(), null)

        coVerify {
            saveMonstersUseCase(listOf(monster.copy(status = MonsterStatus.Edited)))
        }
    }

    @Test
    fun `invoke When status is Edited Then saves monster unchanged`() = runTest {
        val monster = createMonster(status = MonsterStatus.Edited)
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, emptyList(), null)

        coVerify { saveMonstersUseCase(listOf(monster)) }
    }

    @Test
    fun `invoke When status is Clone Then saves monster unchanged`() = runTest {
        val monster = createMonster(status = MonsterStatus.Clone)
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, emptyList(), null)

        coVerify { saveMonstersUseCase(listOf(monster)) }
    }

    @Test
    fun `invoke When status is Imported Then saves monster unchanged`() = runTest {
        val monster = createMonster(status = MonsterStatus.Imported)
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, emptyList(), null)

        coVerify { saveMonstersUseCase(listOf(monster)) }
    }

    @Test
    fun `invoke Then saves monster image with correct data`() = runTest {
        val imageData = MonsterImageData(
            url = "https://example.com/goblin.png",
            backgroundColor = Color(light = "#ffffff", dark = "#000000"),
            isHorizontal = true,
        )
        val monster = createMonster(imageData = imageData, status = MonsterStatus.Edited)
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, emptyList(), null)

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
    fun `invoke When lore entries changed Then saves lore with Edited status`() = runTest {
        val monster = createMonster(status = MonsterStatus.Edited)
        val newEntries = listOf(MonsterLoreEntry(index = "entry-1", description = "New lore"))
        val originalLore = MonsterLore(
            index = monster.index,
            name = monster.name,
            entries = listOf(MonsterLoreEntry(index = "entry-1", description = "Old lore")),
            status = MonsterLoreStatus.Original,
        )
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, newEntries, originalLore)

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
        every { saveMonstersUseCase(any()) } returns flowOf(Unit)
        every { saveMonstersLoreUseCase(any(), any()) } returns flowOf(Unit)

        useCase(monster, originalMonster = null, entries, originalLore)

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
        imageData: MonsterImageData = MonsterImageData(),
        status: MonsterStatus = MonsterStatus.Original,
    ) = Monster(index = index, name = name, imageData = imageData, status = status)
}
