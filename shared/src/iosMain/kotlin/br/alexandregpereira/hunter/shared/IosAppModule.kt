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
import br.alexandregpereira.hunter.shared.di.appModules
import kotlinx.coroutines.flow.single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class IosAppModuleHelper : KoinComponent {

    private val getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase by inject()

    suspend fun getMonsters(): List<Monster> {
        return getMonsterCompendiumUseCase().single().items.mapNotNull {
            (it as? MonsterCompendiumItem.Item)?.monster
        }
    }
}

fun initKoin() {
    startKoin {
        modules(appModules())
    }
}
