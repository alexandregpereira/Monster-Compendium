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

package br.alexandregpereira.hunter.folder.preview.di

import br.alexandregpereira.hunter.folder.preview.FolderPreviewAnalytics
import br.alexandregpereira.hunter.folder.preview.FolderPreviewEventManager
import br.alexandregpereira.hunter.folder.preview.FolderPreviewStateHolder
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.ClearFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import org.koin.dsl.module

val folderPreviewModule = module {
    single { FolderPreviewEventManager() }
    single<FolderPreviewEventDispatcher> { get<FolderPreviewEventManager>() }
    factory { GetMonstersFromFolderPreviewUseCase(get()) }
    factory { AddMonsterToFolderPreviewUseCase(get(), get()) }
    factory { ClearFolderPreviewUseCase(get()) }
    factory { RemoveMonsterFromFolderPreviewUseCase(get(), get()) }

    single {
        FolderPreviewStateHolder(
            folderPreviewEventManager = get(),
            getMonstersFromFolderPreview = get(),
            addMonsterToFolderPreview = get(),
            removeMonsterFromFolderPreview = get(),
            clearFolderPreviewUseCase = get(),
            monsterEventDispatcher = get(),
            folderInsertEventDispatcher = get(),
            dispatcher = get(),
            analytics = FolderPreviewAnalytics(get()),
        )
    }
}
