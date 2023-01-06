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

package br.alexandregpereira.hunter.spell.detail.ui

import br.alexandregpereira.hunter.spell.detail.R
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState

data class SpellState(
    val index: String,
    val name: String,
    val level: Int,
    val castingTime: String,
    val components: String,
    val duration: String,
    val range: String,
    val ritual: Boolean,
    val concentration: Boolean,
    val savingThrowType: SavingThrowTypeState?,
    val school: SchoolOfMagicState,
    val description: String,
    val higherLevel: String?,
    val damageType: String? = null,
)

enum class SavingThrowTypeState(val stringRes: Int) {
    STRENGTH(R.string.spell_detail_saving_throw_strength),
    DEXTERITY(R.string.spell_detail_saving_throw_dexterity),
    CONSTITUTION(R.string.spell_detail_saving_throw_constitution),
    INTELLIGENCE(R.string.spell_detail_saving_throw_intelligence),
    WISDOM(R.string.spell_detail_saving_throw_wisdom),
    CHARISMA(R.string.spell_detail_saving_throw_charisma)
}
