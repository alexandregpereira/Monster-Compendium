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

package br.alexandregpereira.hunter.monster.content.di

import br.alexandregpereira.hunter.monster.content.MonsterContentManagerAnalytics
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerEventManager
import br.alexandregpereira.hunter.monster.content.MonsterContentManagerStateHolder
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventDispatcher
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventListener
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monsterContentManagerModule = module {
    single { MonsterContentManagerEventManager() }
    single<MonsterContentManagerEventDispatcher> { get<MonsterContentManagerEventManager>() }
    single<MonsterContentManagerEventListener> { get<MonsterContentManagerEventManager>() }

    single(named(MonsterContentManagerStateRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        MonsterContentManagerStateHolder(
            stateRecovery = get(named(MonsterContentManagerStateRecoveryQualifier)),
            getAlternativeSourcesUseCase = get(),
            addAlternativeSourceUseCase = get(),
            removeAlternativeSourceUseCase = get(),
            eventDispatcher = get(),
            eventListener = get(),
            dispatcher = get(),
            syncEventDispatcher = get(),
            analytics = MonsterContentManagerAnalytics(get()),
            monsterContentPreviewEventManager = get(),
            appLocalization = get(),
        )
    }
}

const val MonsterContentManagerStateRecoveryQualifier = "MonsterContentManagerStateRecovery"
