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

package br.alexandregpereira.hunter.shareContent

import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileManager
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileManagerImpl
import br.alexandregpereira.hunter.shareContent.domain.GetMonstersShareContent
import br.alexandregpereira.hunter.shareContent.domain.GetMonstersShareContentUseCase
import br.alexandregpereira.hunter.shareContent.domain.ImportContent
import br.alexandregpereira.hunter.shareContent.domain.ImportContentUseCase
import br.alexandregpereira.hunter.shareContent.state.ShareContentExportStateHolder
import br.alexandregpereira.hunter.shareContent.state.ShareContentImportStateHolder
import org.koin.dsl.module

val featureShareContentModule = module {
    single { ShareContentEventDispatcher() }
    factory<ImportContent> {
        ImportContentUseCase(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    factory<GetMonstersShareContent> {
        GetMonstersShareContentUseCase(
            get(),
            get(),
            get(),
        )
    }
    single<CompendiumFileManager> {
        CompendiumFileManagerImpl(
            get(),
        )
    }
    single {
        ShareContentExportStateHolder(
            get(),
            get(),
            get(),
            get(),
            get(),
            eventDispatcher = get<ShareContentEventDispatcher>(),
        )
    }
    single(createdAtStart = true) {
        ShareContentImportStateHolder(
            get(),
            get(),
            get<ShareContentEventDispatcher>(),
            get(),
            get(),
            get(),
        )
    }
}
