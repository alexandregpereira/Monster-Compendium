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

package br.alexandregpereira.hunter.monster.lore.detail.di

import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventListener
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailAnalytics
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailEventManager
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailStateHolder
import br.alexandregpereira.hunter.monster.lore.detail.domain.GetMonsterLoreDetailUseCase
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monsterLoreDetailModule = module {
    single { MonsterLoreDetailEventManager() }
    single<MonsterLoreDetailEventDispatcher> { get<MonsterLoreDetailEventManager>() }
    single<MonsterLoreDetailEventListener> { get<MonsterLoreDetailEventManager>() }
    factory { GetMonsterLoreDetailUseCase(get(), get()) }

    single(named(MonsterLoreDetailRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        MonsterLoreDetailStateHolder(
            stateRecovery = get(named(MonsterLoreDetailRecoveryQualifier)),
            getMonsterLoreUseCase = get(),
            monsterLoreDetailEventListener = get(),
            dispatcher = get(),
            analytics = MonsterLoreDetailAnalytics(get()),
        )
    }
}

const val MonsterLoreDetailRecoveryQualifier: String = "MonsterLoreDetailRecovery"
