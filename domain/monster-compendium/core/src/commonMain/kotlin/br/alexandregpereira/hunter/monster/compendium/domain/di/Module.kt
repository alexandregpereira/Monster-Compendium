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
