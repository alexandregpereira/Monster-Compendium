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

package br.alexandregpereira.hunter.domain.di

import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterImagesUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsCacheUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersAroundIndexUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.GetRemoteMonstersBySourceUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveMonstersUseCase
import br.alexandregpereira.hunter.domain.usecase.SyncMonstersUseCase
import org.koin.dsl.module

val monsterDomainModule = module {
    factory { ChangeMonstersMeasurementUnitUseCase(get(), get(), get()) }
    factory { GetLastCompendiumScrollItemPositionUseCase(get()) }
    factory { GetMeasurementUnitUseCase(get()) }
    factory { GetMonsterImagesUseCase(get()) }
    factory { GetMonsterPreviewsCacheUseCase(get(), get()) }
    factory { GetMonsterPreviewsUseCase(get(), get()) }
    factory { GetMonstersByIdsUseCase(get()) }
    factory { GetMonstersAroundIndexUseCase(get(), get()) }
    factory { GetMonsterUseCase(get()) }
    factory { GetMonstersUseCase(get()) }
    factory { SaveCompendiumScrollItemPositionUseCase(get()) }
    factory { SaveMeasurementUnitUseCase(get(), get()) }
    factory { SaveMonstersUseCase(get(), get(), get()) }
    factory { SyncMonstersUseCase(get(), get(), get(), get(), get(), get(), get()) }
    factory { GetRemoteMonstersBySourceUseCase(get(), get()) }
}
