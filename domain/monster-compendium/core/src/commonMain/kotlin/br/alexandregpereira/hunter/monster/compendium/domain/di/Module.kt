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

package br.alexandregpereira.hunter.monster.compendium.domain.di

import br.alexandregpereira.hunter.monster.compendium.domain.GetAlphabetUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumBaseUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterPreviewsBySectionUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonstersBySectionUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetRemoteMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetRemoteMonstersBySectionUseCase
import br.alexandregpereira.hunter.monster.compendium.domain.GetTableContentUseCase
import org.koin.dsl.module

val monsterCompendiumDomainModule = module {
    factory { GetAlphabetUseCase() }
    factory { GetMonsterCompendiumUseCase(get(), get()) }
    factory {
        GetMonsterPreviewsBySectionUseCase(
            getMonsterPreviewsUseCase = get(),
            getMonstersBySectionUseCase = GetMonstersBySectionUseCase()
        )
    }
    factory {
        GetRemoteMonstersBySectionUseCase(
            getRemoteMonstersBySourceUseCase = get(),
            getMonstersBySectionUseCase = GetMonstersBySectionUseCase(),
            getMonsterImagesUseCase = get()
        )
    }
    factory { GetTableContentUseCase() }
    factory { GetRemoteMonsterCompendiumUseCase(get(), get()) }
    factory { GetMonsterCompendiumBaseUseCase(get(), get()) }
}
