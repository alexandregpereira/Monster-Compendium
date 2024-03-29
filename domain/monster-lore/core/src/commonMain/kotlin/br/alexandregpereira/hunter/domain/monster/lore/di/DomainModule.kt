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

package br.alexandregpereira.hunter.domain.monster.lore.di

import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.SyncMonstersLoreUseCase
import org.koin.dsl.module

val monsterLoreDomainModule = module {
    factory { GetMonsterLoreUseCase(get()) }
    factory { GetMonstersLoreByIdsUseCase(get()) }
    factory { SyncMonstersLoreUseCase(get(), get(), get(), get()) }
    factory { SaveMonstersLoreUseCase(get()) }
}
