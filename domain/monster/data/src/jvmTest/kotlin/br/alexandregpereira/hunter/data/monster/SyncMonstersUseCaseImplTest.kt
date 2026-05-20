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

package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.hunter.domain.model.factory.MonsterFactory
import br.alexandregpereira.hunter.domain.repository.MonsterLocalRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRemoteRepository
import br.alexandregpereira.hunter.domain.repository.MonsterSettingsRepository
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesAdded
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import br.alexandregpereira.hunter.domain.usecase.GetMonsterImagesUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SyncMonstersUseCaseImplTest {

    private val remoteRepository: MonsterRemoteRepository = mockk()
    private val localRepository: MonsterLocalRepository = mockk()
    private val getAlternativeSourcesAdded: GetAlternativeSourcesAdded = mockk()
    private val monsterSettingsRepository: MonsterSettingsRepository = mockk()
    private val getMonsterImages: GetMonsterImagesUseCase = mockk()
    private val saveMonstersUseCase: SaveMonstersUseCase = mockk()
    private val saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase = mockk()

    private val useCase = SyncMonstersUseCaseImpl(
        remoteRepository = remoteRepository,
        localRepository = localRepository,
        getAlternativeSourcesAdded = getAlternativeSourcesAdded,
        monsterSettingsRepository = monsterSettingsRepository,
        getMonsterImages = getMonsterImages,
        saveMonstersUseCase = saveMonstersUseCase,
        saveCompendiumScrollItemPositionUseCase = saveCompendiumScrollItemPositionUseCase,
    )

    @Test
    fun `source with totalMonsters equal to zero skips network call`() = runTest {
        val emptySource = alternativeSource(acronym = "EMPTY", totalMonsters = 0)
        coEvery { getAlternativeSourcesAdded() } returns listOf(emptySource)
        every { getMonsterImages() } returns flowOf(emptyList())
        every { localRepository.getMonsterPreviewsEdited() } returns flowOf(emptyList())
        every { saveMonstersUseCase(any(), any()) } returns flowOf(Unit)
        every { saveCompendiumScrollItemPositionUseCase(any()) } returns flowOf(Unit)

        useCase().single()

        verify(exactly = 0) { remoteRepository.getMonsters(any(), any()) }
    }

    @Test
    fun `source with negative totalMonsters skips network call`() = runTest {
        val emptySource = alternativeSource(acronym = "NONE", totalMonsters = -1)
        coEvery { getAlternativeSourcesAdded() } returns listOf(emptySource)
        every { getMonsterImages() } returns flowOf(emptyList())
        every { localRepository.getMonsterPreviewsEdited() } returns flowOf(emptyList())
        every { saveMonstersUseCase(any(), any()) } returns flowOf(Unit)
        every { saveCompendiumScrollItemPositionUseCase(any()) } returns flowOf(Unit)

        useCase().single()

        verify(exactly = 0) { remoteRepository.getMonsters(any(), any()) }
    }

    @Test
    fun `source with monsters triggers network call and saves results`() = runTest {
        val monster = MonsterFactory.createEmpty("goblin")
        val source = alternativeSource(acronym = "MM", totalMonsters = 5)
        coEvery { getAlternativeSourcesAdded() } returns listOf(source)
        every { getMonsterImages() } returns flowOf(emptyList())
        every { monsterSettingsRepository.getLanguage() } returns flowOf("en-us")
        every { remoteRepository.getMonsters("MM", "en-us") } returns flowOf(listOf(monster))
        every { localRepository.getMonsterPreviewsEdited() } returns flowOf(emptyList())
        every { saveMonstersUseCase(any(), isSync = true) } returns flowOf(Unit)
        every { saveCompendiumScrollItemPositionUseCase(0) } returns flowOf(Unit)

        useCase().single()

        verify { remoteRepository.getMonsters("MM", "en-us") }
        verify { saveMonstersUseCase(match { it.size == 1 && it[0].index == "goblin" }, isSync = true) }
        verify { saveCompendiumScrollItemPositionUseCase(0) }
    }

    @Test
    fun `edited monsters are excluded from saved results`() = runTest {
        val goblin = MonsterFactory.createEmpty("goblin")
        val orc = MonsterFactory.createEmpty("orc")
        val source = alternativeSource(acronym = "MM", totalMonsters = 2)
        coEvery { getAlternativeSourcesAdded() } returns listOf(source)
        every { getMonsterImages() } returns flowOf(emptyList())
        every { monsterSettingsRepository.getLanguage() } returns flowOf("en-us")
        every { remoteRepository.getMonsters("MM", "en-us") } returns flowOf(listOf(goblin, orc))
        every { localRepository.getMonsterPreviewsEdited() } returns flowOf(listOf(orc))
        every { saveMonstersUseCase(any(), isSync = true) } returns flowOf(Unit)
        every { saveCompendiumScrollItemPositionUseCase(0) } returns flowOf(Unit)

        useCase().single()

        verify { saveMonstersUseCase(match { monsters -> monsters.size == 1 && monsters[0].index == "goblin" }, isSync = true) }
    }

    @Test
    fun `mix of zero-monster and non-zero-monster sources only calls network for non-zero sources`() = runTest {
        val monster = MonsterFactory.createEmpty("goblin")
        val emptySource = alternativeSource(acronym = "EMPTY", totalMonsters = 0)
        val activeSource = alternativeSource(acronym = "MM", totalMonsters = 5)
        coEvery { getAlternativeSourcesAdded() } returns listOf(emptySource, activeSource)
        every { getMonsterImages() } returns flowOf(emptyList())
        every { monsterSettingsRepository.getLanguage() } returns flowOf("en-us")
        every { remoteRepository.getMonsters("MM", "en-us") } returns flowOf(listOf(monster))
        every { localRepository.getMonsterPreviewsEdited() } returns flowOf(emptyList())
        every { saveMonstersUseCase(any(), isSync = true) } returns flowOf(Unit)
        every { saveCompendiumScrollItemPositionUseCase(0) } returns flowOf(Unit)

        useCase().single()

        verify(exactly = 0) { remoteRepository.getMonsters("EMPTY", any()) }
        verify { remoteRepository.getMonsters("MM", "en-us") }
    }

    private fun alternativeSource(acronym: String, totalMonsters: Int) = AlternativeSource(
        source = Source(name = acronym, acronym = acronym, originalName = null, originalAcronym = null),
        totalMonsters = totalMonsters,
        totalSpells = 0,
        summary = "",
        coverImageUrl = "",
        isEnabled = true,
        isLoreEnabled = false,
        isDefault = false,
    )
}
