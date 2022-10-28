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

package br.alexandregpereira.hunter.search

import br.alexandregpereira.hunter.search.domain.SearchMonsterResult
import br.alexandregpereira.hunter.ui.compendium.monster.ColorState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterCardState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterImageState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterRowState
import br.alexandregpereira.hunter.ui.compendium.monster.MonsterTypeState
import br.alexandregpereira.hunter.ui.compendium.monster.and

internal fun List<SearchMonsterResult>.asState(): List<MonsterRowState> {
    return this.asMonsterCardStates().let { states ->
        (states.indices step 2).map { i ->
            val nextState = states.getOrNull(i + 1)
            val currentState = states[i].let { state ->
                if (nextState == null) {
                    state.copy(imageState = state.imageState.copy(isHorizontal = true))
                } else state
            }

            currentState and nextState
        }
    }
}

internal fun List<SearchMonsterResult>.asMonsterCardStates(): List<MonsterCardState> {
    return this.map { result ->
        MonsterCardState(
            index = result.index,
            name = result.name,
            imageState = MonsterImageState(
                url = result.imageUrl,
                type = MonsterTypeState.valueOf(result.type.name),
                backgroundColor = ColorState(
                    light = result.backgroundColorLight,
                    dark = result.backgroundColorDark
                ),
                challengeRating = result.challengeRating,
                isHorizontal = false,
                contentDescription = result.name
            ),
        )
    }
}
