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

package br.alexandregpereira.hunter.folder.insert.di

import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.folder.insert.EmptyFolderInsertStateRecovery
import br.alexandregpereira.hunter.folder.insert.FolderInsertAnalytics
import br.alexandregpereira.hunter.folder.insert.FolderInsertEventManager
import br.alexandregpereira.hunter.folder.insert.FolderInsertStateHolder
import org.koin.dsl.module

val featureFolderInsertModule = module {
    single { FolderInsertEventManager() }
    single<FolderInsertEventDispatcher> { get<FolderInsertEventManager>() }
    single<FolderInsertResultListener> { get<FolderInsertEventManager>() }

    factory {
        FolderInsertStateHolder(
            stateRecovery = getOrNull() ?: EmptyFolderInsertStateRecovery(),
            getMonsterFolders = get(),
            getFolderMonsterPreviewsByIds = get(),
            addMonstersToFolder = get(),
            folderInsertEventManager = get(),
            dispatcher = get(),
            analytics = FolderInsertAnalytics(get()),
        )
    }
}
