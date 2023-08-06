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

package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import kotlinx.coroutines.flow.Flow

class GetMonsterPreviewsBySectionUseCase internal constructor(
    private val getMonsterPreviewsUseCase: GetMonsterPreviewsUseCase,
    private val getMonstersBySectionUseCase: GetMonstersBySectionUseCase,
) {

    operator fun invoke(): Flow<List<MonsterCompendiumItem>> {
        return getMonstersBySectionUseCase(getMonsterPreviewsUseCase())
    }
}
