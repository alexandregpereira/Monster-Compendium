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

package br.alexandregpereira.hunter.data.spell

import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesAdded
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import br.alexandregpereira.hunter.domain.source.model.Source
import br.alexandregpereira.hunter.domain.spell.GetSpellsEdited
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Test

@Suppress("UnusedFlow")
class SyncSpellsUseCaseImplTest {

    private val getLanguageUseCase: GetLanguageUseCase = mockk()
    private val repository: SpellRepository = mockk()
    private val getSpellsEdited: GetSpellsEdited = mockk()
    private val getAlternativeSourcesAdded: GetAlternativeSourcesAdded = mockk()

    private val useCase = SyncSpellsUseCaseImpl(
        getLanguageUseCase = getLanguageUseCase,
        repository = repository,
        getSpellsEdited = getSpellsEdited,
        getAlternativeSourcesAdded = getAlternativeSourcesAdded,
    )

    @Test
    fun `source with totalSpells equal to zero skips network call`() = runTest {
        val emptySource = alternativeSource(acronym = "EMPTY", totalSpells = 0)
        coEvery { getAlternativeSourcesAdded() } returns listOf(emptySource)
        every { getLanguageUseCase() } returns flowOf("en-us")
        every { getSpellsEdited() } returns flowOf(emptyList())
        every { repository.deleteLocalSpells() } returns flowOf(Unit)
        every { repository.saveSpells(emptyList()) } returns flowOf(Unit)

        useCase().single()

        verify(exactly = 0) { repository.getRemoteSpells(any(), any()) }
    }

    @Test
    fun `source with spells triggers network call and saves results`() = runTest {
        val spell = createSpell("fireball")
        val source = alternativeSource(acronym = "PHB", totalSpells = 3)
        coEvery { getAlternativeSourcesAdded() } returns listOf(source)
        every { getLanguageUseCase() } returns flowOf("en-us")
        every { repository.getRemoteSpells("PHB", "en-us") } returns flowOf(listOf(spell))
        every { getSpellsEdited() } returns flowOf(emptyList())
        every { repository.deleteLocalSpells() } returns flowOf(Unit)
        every { repository.saveSpells(listOf(spell)) } returns flowOf(Unit)

        useCase().single()

        verify { repository.getRemoteSpells("PHB", "en-us") }
        verify { repository.saveSpells(listOf(spell)) }
    }

    @Test
    fun `edited spells are excluded from saved results`() = runTest {
        val fireball = createSpell("fireball")
        val magicMissile = createSpell("magic-missile")
        val source = alternativeSource(acronym = "PHB", totalSpells = 2)
        coEvery { getAlternativeSourcesAdded() } returns listOf(source)
        every { getLanguageUseCase() } returns flowOf("en-us")
        every { repository.getRemoteSpells("PHB", "en-us") } returns flowOf(listOf(fireball, magicMissile))
        every { getSpellsEdited() } returns flowOf(listOf(magicMissile))
        every { repository.deleteLocalSpells() } returns flowOf(Unit)
        every { repository.saveSpells(listOf(fireball)) } returns flowOf(Unit)

        useCase().single()

        verify { repository.saveSpells(listOf(fireball)) }
    }

    @Test
    fun `spells from multiple sources are combined before saving`() = runTest {
        val fireball = createSpell("fireball")
        val shillelagh = createSpell("shillelagh")
        val source1 = alternativeSource(acronym = "PHB", totalSpells = 1)
        val source2 = alternativeSource(acronym = "TCE", totalSpells = 1)
        coEvery { getAlternativeSourcesAdded() } returns listOf(source1, source2)
        every { getLanguageUseCase() } returns flowOf("en-us")
        every { repository.getRemoteSpells("PHB", "en-us") } returns flowOf(listOf(fireball))
        every { repository.getRemoteSpells("TCE", "en-us") } returns flowOf(listOf(shillelagh))
        every { getSpellsEdited() } returns flowOf(emptyList())
        every { repository.deleteLocalSpells() } returns flowOf(Unit)
        every { repository.saveSpells(any()) } returns flowOf(Unit)

        useCase().single()

        verify { repository.saveSpells(match { it.containsAll(listOf(fireball, shillelagh)) }) }
    }

    @Test
    fun `network error causes source to be skipped silently`() = runTest {
        val spell = createSpell("fireball")
        val failingSource = alternativeSource(acronym = "BAD", totalSpells = 1)
        val goodSource = alternativeSource(acronym = "PHB", totalSpells = 1)
        coEvery { getAlternativeSourcesAdded() } returns listOf(failingSource, goodSource)
        every { getLanguageUseCase() } returns flowOf("en-us")
        every { repository.getRemoteSpells("BAD", "en-us") } returns flow { throw RuntimeException("network error") }
        every { repository.getRemoteSpells("PHB", "en-us") } returns flowOf(listOf(spell))
        every { getSpellsEdited() } returns flowOf(emptyList())
        every { repository.deleteLocalSpells() } returns flowOf(Unit)
        every { repository.saveSpells(listOf(spell)) } returns flowOf(Unit)

        useCase().single()

        verify { repository.saveSpells(listOf(spell)) }
    }

    @Test
    fun `existing local spells are deleted before saving new ones`() = runTest {
        val source = alternativeSource(acronym = "PHB", totalSpells = 1)
        val spell = createSpell("fireball")
        coEvery { getAlternativeSourcesAdded() } returns listOf(source)
        every { getLanguageUseCase() } returns flowOf("en-us")
        every { repository.getRemoteSpells("PHB", "en-us") } returns flowOf(listOf(spell))
        every { getSpellsEdited() } returns flowOf(emptyList())
        every { repository.deleteLocalSpells() } returns flowOf(Unit)
        every { repository.saveSpells(any()) } returns flowOf(Unit)

        useCase().single()

        verify { repository.deleteLocalSpells() }
        verify { repository.saveSpells(any()) }
    }

    private fun alternativeSource(acronym: String, totalSpells: Int) = AlternativeSource(
        source = Source(name = acronym, acronym = acronym, originalName = null, originalAcronym = null),
        totalMonsters = 0,
        totalSpells = totalSpells,
        summary = "",
        coverImageUrl = "",
        isEnabled = true,
        isLoreEnabled = false,
        isDefault = false,
    )

    private fun createSpell(index: String) = Spell(
        index = index,
        name = index,
        level = 1,
        castingTime = "1 action",
        components = "V, S",
        duration = "Instantaneous",
        range = "150 feet",
        ritual = false,
        concentration = false,
        savingThrowType = null,
        damageType = null,
        school = SchoolOfMagic.EVOCATION,
        description = "",
        higherLevel = null,
        status = SpellStatus.Original,
    )
}
