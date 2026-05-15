package br.alexandregpereira.hunter.data.spell

import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesAdded
import br.alexandregpereira.hunter.domain.spell.GetSpellsEdited
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.SyncSpellsUseCase
import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

@OptIn(ExperimentalCoroutinesApi::class)
internal class SyncSpellsUseCaseImpl(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val repository: SpellRepository,
    private val getSpellsEdited: GetSpellsEdited,
    private val getAlternativeSourcesAdded: GetAlternativeSourcesAdded,
) : SyncSpellsUseCase {

    override operator fun invoke(): Flow<Unit> {
        return getLanguageUseCase().flatMapLatest { lang ->
            val sources = getAlternativeSourcesAdded()
            val spells = sources.mapNotNull { source ->
                if (source.totalSpells <= 0) return@mapNotNull null
                repository.getRemoteSpells(source.acronym, lang)
                    .catch { emit(emptyList()) }
                    .firstOrNull()
            }.flatten()
            flowOf(spells)
        }.removeSpellsEdited().flatMapLatest { spells ->
            repository.deleteLocalSpells().flatMapLatest {
                repository.saveSpells(spells)
            }
        }
    }

    private fun Flow<List<Spell>>.removeSpellsEdited(): Flow<List<Spell>> = map { spells ->
        val spellsEditedIndexes = getSpellsEdited().single().map { it.index }.toSet()
        spells.filterNot { it.index in spellsEditedIndexes }
    }
}
