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
import br.alexandregpereira.hunter.search.ui.MonsterCardState
import br.alexandregpereira.hunter.search.ui.MonsterTypeState

internal fun List<SearchMonsterResult>.asState(): List<MonsterCardState> {
    return this.map { result ->
        MonsterCardState(
            index = result.index,
            name = result.name,
            imageUrl = result.imageUrl,
            type = MonsterTypeState.valueOf(result.type.name),
            challengeRating = result.challengeRating,
            contentDescription = result.name,
            backgroundColorLight = result.backgroundColorLight,
            backgroundColorDark = result.backgroundColorDark
        )
    }
}
