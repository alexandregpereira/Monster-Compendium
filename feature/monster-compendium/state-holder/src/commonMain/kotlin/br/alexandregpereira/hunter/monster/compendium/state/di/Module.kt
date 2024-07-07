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

package br.alexandregpereira.hunter.monster.compendium.state.di

import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAnalytics
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventListener
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
val monsterCompendiumStateModule = module {
    single(named(StateRecoveryQualifier)) {
        StateRecovery(
            MonsterCompendiumState.Empty,
        )
    }

    single {
        MonsterCompendiumStateHolder(
            getMonsterCompendiumUseCase = get(),
            getLastCompendiumScrollItemPositionUseCase = get(),
            saveCompendiumScrollItemPositionUseCase = get(),
            folderPreviewEventDispatcher = get(),
            folderPreviewResultListener = get(),
            monsterDetailEventDispatcher = get(),
            monsterDetailEventListener = get(),
            syncEventListener = get(),
            syncEventDispatcher = get(),
            monsterRegistrationEventListener = get<MonsterRegistrationEventListener>(),
            dispatcher = get(),
            analytics = MonsterCompendiumAnalytics(get()),
            appLocalization = get(),
            stateRecovery = get(named(StateRecoveryQualifier)),
        )
    }
}

const val StateRecoveryQualifier: String = "MonsterCompendiumStateRecovery"
