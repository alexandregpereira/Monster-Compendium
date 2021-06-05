/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.di

import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { ChangeMonstersMeasurementUnitUseCase(get(), get(), get()) }
    factory { GetLastCompendiumScrollItemPositionUseCase(get()) }
    factory { GetMeasurementUnitUseCase(get()) }
    factory { GetMonsterDetailUseCase(get(), get()) }
    factory { GetMonsterPreviewsBySectionUseCase(get(), get()) }
    factory { GetMonstersUseCase(get()) }
    factory { SaveCompendiumScrollItemPositionUseCase(get()) }
    factory { SaveMeasurementUnitUseCase(get(), get()) }
    factory { SaveMonstersUseCase(get(), get(), get()) }
    factory { SyncMonstersUseCase(get(), get()) }
}