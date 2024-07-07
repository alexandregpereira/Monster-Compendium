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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.monster.detail.ProficiencyState

@Composable
internal fun ProficiencyBlock(
    title: String,
    proficiencies: List<ProficiencyState>,
    modifier: Modifier = Modifier
) = Block(modifier = modifier, title = title) {
    ProficiencyGrid(proficiencies)
}

@Composable
private fun ProficiencyGrid(
    proficiencies: List<ProficiencyState>,
) = Grid {

    proficiencies.forEach { proficiency ->
        Bonus(
            value = proficiency.modifier,
            name = proficiency.name,
            modifier = Modifier.width(GridItemWidth),
        )
    }
}
