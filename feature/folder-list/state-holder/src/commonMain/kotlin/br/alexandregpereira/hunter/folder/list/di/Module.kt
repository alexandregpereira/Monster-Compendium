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

package br.alexandregpereira.hunter.folder.list.di

import br.alexandregpereira.hunter.event.folder.list.FolderListResultListener
import br.alexandregpereira.hunter.folder.list.FolderListAnalytics
import br.alexandregpereira.hunter.folder.list.FolderListEventManager
import br.alexandregpereira.hunter.folder.list.FolderListStateHolder
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val folderListModule = module {
    single { FolderListEventManager() }
    single<FolderListResultListener> { get<FolderListEventManager>() }

    single(named(FolderListStateRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        FolderListStateHolder(
            getMonsterFolders = get(),
            removeMonsterFolders = get(),
            folderInsertResultListener = get(),
            folderDetailEventDispatcher = get(),
            folderListEventManager = get(),
            dispatcher = get(),
            syncEventListener = get(),
            analytics = FolderListAnalytics(get()),
            appLocalization = get(),
            stateRecovery = get(named(FolderListStateRecoveryQualifier)),
        )
    }
}

const val FolderListStateRecoveryQualifier = "FolderListStateRecovery"
