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

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.folder.insert.FolderInsertState
import br.alexandregpereira.hunter.folder.insert.FolderInsertStateRecovery
import br.alexandregpereira.hunter.folder.insert.FolderInsertViewModel
import br.alexandregpereira.hunter.folder.insert.getState
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val folderInsertModule = listOf(featureFolderInsertModule) + module {
    factory {
        val state = get<SavedStateHandle>().getState()
        FolderInsertStateRecovery {
            FolderInsertState(
                isOpen = state.isOpen,
                folderName = state.folderName,
                folderIndexSelected = state.folderIndexSelected,
                monsterIndexes = state.monsterIndexes,
                folders = state.folders,
                monsterPreviews = state.monsterPreviews.map {
                    MonsterPreviewFolder(
                        index = it.index,
                        name = it.name,
                        imageUrl = it.imageUrl,
                        backgroundColorLight = it.backgroundColorLight,
                        backgroundColorDark = it.backgroundColorDark,
                    )
                }
            )
        }
    }
    viewModel {
        FolderInsertViewModel(get(), get())
    }
}
