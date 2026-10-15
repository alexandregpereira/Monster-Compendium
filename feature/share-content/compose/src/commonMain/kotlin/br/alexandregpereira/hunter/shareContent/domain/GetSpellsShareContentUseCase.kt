package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import kotlinx.coroutines.flow.first

internal fun interface GetSpellsShareContent {
    suspend operator fun invoke(spellIndexes: List<String>): ShareContent
}

internal class GetSpellsShareContentUseCase(
    private val getSpellsByIds: GetSpellsByIdsUseCase,
) : GetSpellsShareContent {

    override suspend fun invoke(spellIndexes: List<String>): ShareContent {
        val spells = getSpellsByIds(spellIndexes).first().takeIf { it.isNotEmpty() }
        return ShareContent(
            monsters = null,
            monstersLore = null,
            spells = spells,
            monsterImages = null,
        )
    }
}
