/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.shared

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import kotlinx.coroutines.flow.single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MonsterCompendiumFeature : KoinComponent {

    private val getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase by inject()

    suspend fun getMonsterCompendium(): MonsterCompendiumIos {
        return getMonsterCompendiumUseCase().single().run {
            MonsterCompendiumIos(
                items = items.map {
                    MonsterCompendiumItemIos(
                        title = it as? MonsterCompendiumItem.Title,
                        monster = (it as? MonsterCompendiumItem.Item)?.monster
                    )
                },
                tableContent = tableContent,
                alphabet = alphabet
            )
        }
    }
}

data class MonsterCompendiumIos(
    val items: List<MonsterCompendiumItemIos>,
    val tableContent: List<TableContentItem>,
    val alphabet: List<String>,
)

data class MonsterCompendiumItemIos(
    val title: MonsterCompendiumItem.Title?,
    val monster: Monster?
)
