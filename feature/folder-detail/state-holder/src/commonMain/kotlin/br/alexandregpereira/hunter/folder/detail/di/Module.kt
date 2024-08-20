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

package br.alexandregpereira.hunter.folder.detail.di

import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResultListener
import br.alexandregpereira.hunter.folder.detail.FolderDetailAnalytics
import br.alexandregpereira.hunter.folder.detail.FolderDetailEventManager
import br.alexandregpereira.hunter.folder.detail.FolderDetailStateHolder
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val folderDetailModule = module {
    single { FolderDetailEventManager() }
    single<FolderDetailEventDispatcher> { get<FolderDetailEventManager>() }
    single<FolderDetailResultListener> { get<FolderDetailEventManager>() }

    single(named(FolderDetailStateRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        FolderDetailStateHolder(
            stateRecovery = get(named(FolderDetailStateRecoveryQualifier)),
            getMonstersByFolder = get(),
            folderDetailEventManager = get(),
            folderPreviewEventDispatcher = get(),
            folderInsertResultListener = get(),
            monsterEventDispatcher = get(),
            dispatcher = get(),
            analytics = FolderDetailAnalytics(get()),
        )
    }
}

const val FolderDetailStateRecoveryQualifier = "FolderDetailStateRecovery"
