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

package br.alexandregpereira.hunter.folder.preview.di

import br.alexandregpereira.hunter.folder.preview.FolderPreviewAnalytics
import br.alexandregpereira.hunter.folder.preview.FolderPreviewEventManager
import br.alexandregpereira.hunter.folder.preview.FolderPreviewStateHolder
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.ClearFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val folderPreviewModule = module {
    single { FolderPreviewEventManager() }
    single<FolderPreviewEventDispatcher> { get<FolderPreviewEventManager>() }
    single<FolderPreviewResultListener> { get<FolderPreviewEventManager>() }
    factory { GetMonstersFromFolderPreviewUseCase(get()) }
    factory { AddMonsterToFolderPreviewUseCase(get(), get()) }
    factory { ClearFolderPreviewUseCase(get()) }
    factory { RemoveMonsterFromFolderPreviewUseCase(get(), get()) }

    single(named(FolderPreviewStateRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        FolderPreviewStateHolder(
            stateRecovery = get(named(FolderPreviewStateRecoveryQualifier)),
            folderPreviewEventManager = get(),
            getMonstersFromFolderPreview = get(),
            addMonsterToFolderPreview = get(),
            removeMonsterFromFolderPreview = get(),
            clearFolderPreviewUseCase = get(),
            monsterEventDispatcher = get(),
            folderInsertEventDispatcher = get(),
            dispatcher = get(),
            analytics = FolderPreviewAnalytics(get()),
            appLocalization = get(),
        )
    }
}

const val FolderPreviewStateRecoveryQualifier = "FolderPreviewStateRecovery"
