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

package br.alexandregpereira.hunter.folder.insert.di

import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.folder.insert.FolderInsertAnalytics
import br.alexandregpereira.hunter.folder.insert.FolderInsertEventManager
import br.alexandregpereira.hunter.folder.insert.FolderInsertStateHolder
import org.koin.dsl.module

val folderInsertModule = module {
    single { FolderInsertEventManager() }
    single<FolderInsertEventDispatcher> { get<FolderInsertEventManager>() }
    single<FolderInsertResultListener> { get<FolderInsertEventManager>() }

    single {
        FolderInsertStateHolder(
            getMonsterFolders = get(),
            getFolderMonsterPreviewsByIds = get(),
            addMonstersToFolder = get(),
            folderInsertEventManager = get(),
            dispatcher = get(),
            analytics = FolderInsertAnalytics(get()),
            appLocalization = get(),
            shareContentEventDispatcher = get(),
        )
    }
}
