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

import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.spell.detail.ui.SavingThrowTypeState
import br.alexandregpereira.hunter.spell.detail.ui.SpellState
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState

internal fun Spell.asState(): SpellState {
    return SpellState(
        index = index,
        name = name,
        level = level,
        castingTime = castingTime,
        components = components,
        duration = duration,
        range = range,
        ritual = ritual,
        concentration = concentration,
        savingThrowType = savingThrowType?.let { SavingThrowTypeState.valueOf(it.name) },
        damageType = damageType,
        school = SchoolOfMagicState.valueOf(school.name),
        description = description,
        higherLevel = higherLevel
    )
}
