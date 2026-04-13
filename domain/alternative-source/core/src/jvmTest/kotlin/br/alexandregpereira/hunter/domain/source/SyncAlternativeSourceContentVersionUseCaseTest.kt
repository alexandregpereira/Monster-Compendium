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

package br.alexandregpereira.hunter.domain.source

import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("UnusedFlow")
class SyncAlternativeSourceContentVersionUseCaseTest {

    private val remoteRepository: AlternativeSourceRemoteRepository = mockk()
    private val localRepository: AlternativeSourceLocalRepository = mockk()
    private val settingsRepository: AlternativeSourceSettingsRepository = mockk()

    private val useCase = SyncAlternativeSourceContentVersionUseCase(
        remoteRepository = remoteRepository,
        localRepository = localRepository,
        settingsRepository = settingsRepository,
    )

    @Test
    fun `when no versions changed and no new default sources, emits false with empty map`() = runBlocking {
        val source = alternativeSource("MM", contentVersion = 1)
        val defaultSource = alternativeSource("PHB", contentVersion = 2, isDefault = true)

        setupRepositories(
            remoteSources = listOf(source),
            localSources = listOf(source),
            remoteDefaultSources = listOf(defaultSource),
            localDefaultSources = listOf(defaultSource),
        )

        val result = useCase().single()

        assertFalse(result.first)
        assertEquals(emptyMap(), result.second)
    }

    @Test
    fun `when remote non-default source has updated version, emits true with rollback map`() = runBlocking {
        val localSource = alternativeSource("MM", contentVersion = 1)
        val remoteSource = alternativeSource("MM", contentVersion = 2)

        setupRepositories(
            remoteSources = listOf(remoteSource),
            localSources = listOf(localSource),
        )
        every { localRepository.saveContentVersions(any()) } returns flowOf(Unit)

        val result = useCase().single()

        assertTrue(result.first)
        assertEquals(mapOf("MM" to 1), result.second)
        verify { localRepository.saveContentVersions(mapOf("MM" to 2)) }
    }

    @Test
    fun `when remote non-default source does not exist locally, it is excluded from changed map`() = runBlocking {
        val remoteSource = alternativeSource("NEW", contentVersion = 1)

        setupRepositories(
            remoteSources = listOf(remoteSource),
            localSources = emptyList(),
        )

        val result = useCase().single()

        assertFalse(result.first)
        assertEquals(emptyMap(), result.second)
        verify(exactly = 0) { localRepository.saveContentVersions(any()) }
    }

    @Test
    fun `when new default source appears remotely, it is saved and emits true`() = runBlocking {
        val remoteDefault = alternativeSource("NEW_DEFAULT", contentVersion = 1)

        setupRepositories(
            remoteDefaultSources = listOf(remoteDefault),
            localDefaultSources = emptyList(),
        )
        every { localRepository.saveDefaultSources(any()) } returns flowOf(Unit)

        val result = useCase().single()

        assertTrue(result.first)
        assertEquals(emptyMap(), result.second)
        verify { localRepository.saveDefaultSources(listOf(remoteDefault.copy(isDefault = true))) }
        verify(exactly = 0) { localRepository.saveContentVersions(any()) }
    }

    @Test
    fun `when remote default source has updated version, emits true with rollback map`() = runBlocking {
        val localDefault = alternativeSource("PHB", contentVersion = 1, isDefault = true)
        val remoteDefault = alternativeSource("PHB", contentVersion = 2, isDefault = true)

        setupRepositories(
            remoteDefaultSources = listOf(remoteDefault),
            localDefaultSources = listOf(localDefault),
        )
        every { localRepository.saveContentVersions(any()) } returns flowOf(Unit)

        val result = useCase().single()

        assertTrue(result.first)
        assertEquals(mapOf("PHB" to 1), result.second)
        verify { localRepository.saveContentVersions(mapOf("PHB" to 2)) }
    }

    @Test
    fun `when both non-default and default sources have updated versions, emits true with combined rollback map`() = runBlocking {
        val localSource = alternativeSource("MM", contentVersion = 1)
        val remoteSource = alternativeSource("MM", contentVersion = 2)
        val localDefault = alternativeSource("PHB", contentVersion = 3, isDefault = true)
        val remoteDefault = alternativeSource("PHB", contentVersion = 4, isDefault = true)

        setupRepositories(
            remoteSources = listOf(remoteSource),
            localSources = listOf(localSource),
            remoteDefaultSources = listOf(remoteDefault),
            localDefaultSources = listOf(localDefault),
        )
        every { localRepository.saveContentVersions(any()) } returns flowOf(Unit)

        val result = useCase().single()

        assertTrue(result.first)
        assertEquals(mapOf("MM" to 1, "PHB" to 3), result.second)
        verify { localRepository.saveContentVersions(mapOf("MM" to 2, "PHB" to 4)) }
    }

    private fun setupRepositories(
        remoteSources: List<AlternativeSource> = emptyList(),
        localSources: List<AlternativeSource> = emptyList(),
        remoteDefaultSources: List<AlternativeSource> = emptyList(),
        localDefaultSources: List<AlternativeSource> = emptyList(),
    ) {
        every { settingsRepository.getLanguage() } returns flowOf("en-US")
        every { remoteRepository.getAlternativeSources(any()) } returns flowOf(remoteSources)
        every { localRepository.getAlternativeSources() } returns flowOf(localSources)
        every { remoteRepository.getDefaultSources(any()) } returns flowOf(remoteDefaultSources)
        every { localRepository.getDefaultSources() } returns flowOf(localDefaultSources)
    }

    private fun alternativeSource(
        acronym: String,
        contentVersion: Int = 0,
        isDefault: Boolean = false,
    ) = AlternativeSource(
        source = Source(name = acronym, acronym = acronym, originalName = null),
        totalMonsters = 0,
        summary = "",
        coverImageUrl = "",
        isEnabled = false,
        isLoreEnabled = false,
        contentVersion = contentVersion,
        isDefault = isDefault,
    )
}
