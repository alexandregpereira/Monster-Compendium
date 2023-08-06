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

package br.alexandregpereira.hunter.monster.detail.di

import br.alexandregpereira.hunter.monster.detail.MonsterDetailEventManager
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.monster.detail.MonsterDetailAnalytics
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import org.koin.dsl.module
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
val monsterDetailStateModule = module {
    single { MonsterDetailEventManager() }
    single<MonsterDetailEventDispatcher> { get<MonsterDetailEventManager>() }
    single<MonsterDetailEventListener> { get<MonsterDetailEventManager>() }
    factory { GetMonsterDetailUseCase(get(), get(), get(), get(), get(), get()) }
    factory {
        MonsterDetailStateHolder(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            analytics = MonsterDetailAnalytics(get()),
        )
    }
}
