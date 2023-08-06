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

package br.alexandregpereira.hunter.monster.content.preview.di

import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewAnalytics
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewDefaultStateRecovery
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewEventManager
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewStateHolder
import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewStateRecovery
import org.koin.core.scope.Scope
import org.koin.dsl.module

val featureMonsterContentPreviewModule = module {

    single { MonsterContentPreviewEventManager() }

    factory {
        MonsterContentPreviewStateHolder(
            stateRecovery = createStateRecovery() ?: MonsterContentPreviewDefaultStateRecovery(),
            analytics = MonsterContentPreviewAnalytics(get()),
            dispatcher = get(),
            getRemoteMonsterCompendiumUseCase = get(),
            monsterContentPreviewEventManager = get(),
        )
    }
}

internal expect fun Scope.createStateRecovery(): MonsterContentPreviewStateRecovery?
