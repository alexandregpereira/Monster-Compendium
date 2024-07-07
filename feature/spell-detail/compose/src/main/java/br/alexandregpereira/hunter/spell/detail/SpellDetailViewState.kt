/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.spell.detail

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic

internal data class SpellDetailViewState(
    val spell: SpellState = SpellState(),
    val showDetail: Boolean = false,
    val strings: SpellDetailStrings = SpellDetailStrings()
)

data class SpellState(
    val index: String = "",
    val name: String = "",
    val subtitle: String = "",
    val castingTime: String = "",
    val components: String = "",
    val duration: String = "",
    val range: String = "",
    val concentration: Boolean = false,
    val savingThrowType: String? = null,
    val school: SchoolOfMagic = SchoolOfMagic.ABJURATION,
    val description: String = "",
    val higherLevel: String? = null,
)

internal fun SavedStateHandle.getState(): SpellDetailViewState {
    return SpellDetailViewState(
        showDetail = this["showDetail"] ?: false
    )
}

internal fun SpellDetailViewState.saveState(
    savedStateHandle: SavedStateHandle
): SpellDetailViewState {
    savedStateHandle["showDetail"] = showDetail
    return this
}

internal fun SpellDetailViewState.changeSpell(
    spellState: SpellState,
    strings: SpellDetailStrings,
): SpellDetailViewState {
    return copy(spell = spellState, showDetail = true, strings = strings)
}

internal fun SpellDetailViewState.hideDetail(): SpellDetailViewState {
    return copy(showDetail = false)
}
