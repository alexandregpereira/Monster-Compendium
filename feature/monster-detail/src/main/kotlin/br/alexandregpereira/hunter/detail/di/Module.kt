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

package br.alexandregpereira.hunter.detail.di

import br.alexandregpereira.hunter.detail.MonsterDetailEventManager
import br.alexandregpereira.hunter.detail.MonsterDetailViewModel
import br.alexandregpereira.hunter.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val monsterDetailModule = module {
    single { MonsterDetailEventManager() }
    single<MonsterDetailEventDispatcher> { get<MonsterDetailEventManager>() }
    single<MonsterDetailEventListener> { get<MonsterDetailEventManager>() }
    factory { GetMonsterDetailUseCase(get(), get(), get(), get(), get(), get()) }

    viewModel {
        MonsterDetailViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
}
