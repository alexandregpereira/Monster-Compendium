/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByStatus
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
    factory { GetMonstersByStatus(get()) }
}
