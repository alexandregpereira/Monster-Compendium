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

package br.alexandregpereira.hunter.data.source

import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@Suppress("UnusedFlow")
class GetAlternativeSourcesAddedImplTest {

    private val getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase = mockk()
    private val localRepository: AlternativeSourceLocalRepository = mockk()

    private val useCase = GetAlternativeSourcesAddedImpl(
        getAlternativeSourcesUseCase = getAlternativeSourcesUseCase,
        localRepository = localRepository,
    )

    @Test
    fun `when source only exists under old acronym, adds new acronym and removes old`() = runBlocking {
        val source = alternativeSource(acronym = "NEW", originalAcronym = "OLD", isAdded = true)
        every { getAlternativeSourcesUseCase() } returns flowOf(listOf(source))
        coEvery { localRepository.getContentSource("OLD") } returns alternativeSource(acronym = "OLD")
        coEvery { localRepository.getContentSource("NEW") } returns null
        every { localRepository.addAlternativeSource("NEW") } returns flowOf(Unit)
        every { localRepository.removeAlternativeSource("OLD") } returns flowOf(Unit)

        val result = useCase()

        assertEquals(listOf(source), result)
        verify { localRepository.addAlternativeSource("NEW") }
        verify { localRepository.removeAlternativeSource("OLD") }
    }

    @Test
    fun `when source already exists under new acronym, skips add but still removes old acronym`() = runBlocking {
        val source = alternativeSource(acronym = "NEW", originalAcronym = "OLD", isAdded = true)
        every { getAlternativeSourcesUseCase() } returns flowOf(listOf(source))
        coEvery { localRepository.getContentSource("OLD") } returns alternativeSource(acronym = "OLD")
        coEvery { localRepository.getContentSource("NEW") } returns alternativeSource(acronym = "NEW")
        every { localRepository.removeAlternativeSource("OLD") } returns flowOf(Unit)

        val result = useCase()

        assertEquals(listOf(source), result)
        verify(exactly = 0) { localRepository.addAlternativeSource(any()) }
        verify { localRepository.removeAlternativeSource("OLD") }
    }

    @Test
    fun `when old acronym does not exist locally, skips add but still removes old acronym`() = runBlocking {
        val source = alternativeSource(acronym = "NEW", originalAcronym = "OLD", isAdded = true)
        every { getAlternativeSourcesUseCase() } returns flowOf(listOf(source))
        coEvery { localRepository.getContentSource("OLD") } returns null
        every { localRepository.removeAlternativeSource("OLD") } returns flowOf(Unit)

        val result = useCase()

        assertEquals(listOf(source), result)
        verify(exactly = 0) { localRepository.addAlternativeSource(any()) }
        verify { localRepository.removeAlternativeSource("OLD") }
    }

    @Test
    fun `when source has no original acronym, no migration is attempted`() = runBlocking {
        val source = alternativeSource(acronym = "MM", originalAcronym = null, isAdded = true)
        every { getAlternativeSourcesUseCase() } returns flowOf(listOf(source))

        val result = useCase()

        assertEquals(listOf(source), result)
        coVerify(exactly = 0) { localRepository.getContentSource(any()) }
        verify(exactly = 0) { localRepository.addAlternativeSource(any()) }
        verify(exactly = 0) { localRepository.removeAlternativeSource(any()) }
    }

    @Test
    fun `returns only sources that are added`() = runBlocking {
        val addedSource = alternativeSource(acronym = "MM", isAdded = true)
        val notAddedSource = alternativeSource(acronym = "PHB", isAdded = false)
        every { getAlternativeSourcesUseCase() } returns flowOf(listOf(addedSource, notAddedSource))

        val result = useCase()

        assertEquals(listOf(addedSource), result)
    }

    private fun alternativeSource(
        acronym: String,
        originalAcronym: String? = null,
        isAdded: Boolean = false,
    ) = AlternativeSource(
        source = Source(
            name = acronym,
            acronym = acronym,
            originalName = null,
            originalAcronym = originalAcronym,
        ),
        totalMonsters = 10,
        totalSpells = 0,
        summary = "",
        coverImageUrl = "",
        isEnabled = true,
        isLoreEnabled = false,
        isDefault = false,
        isAdded = isAdded,
    )
}
