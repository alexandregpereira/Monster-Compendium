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

package br.alexandregpereira.hunter.folder.detail.di

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolder
import br.alexandregpereira.hunter.domain.folder.model.MonsterPreviewFolderType
import br.alexandregpereira.hunter.folder.detail.FolderDetailState
import br.alexandregpereira.hunter.folder.detail.FolderDetailStateRecovery
import br.alexandregpereira.hunter.folder.detail.FolderDetailViewModel
import br.alexandregpereira.hunter.folder.detail.getState
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val folderDetailModule = module {
    factory {
        val state = get<SavedStateHandle>().getState()
        FolderDetailStateRecovery {
            FolderDetailState(
                isOpen = state.isOpen,
                folderName = state.folderName,
                monsters = state.monsters.map {
                    MonsterPreviewFolder(
                        index = it.index,
                        name = it.name,
                        type = MonsterPreviewFolderType.valueOf(it.imageState.type.name),
                        challengeRating = it.imageState.challengeRating,
                        imageUrl = it.imageState.url,
                        backgroundColorLight = it.imageState.backgroundColor.light,
                        backgroundColorDark = it.imageState.backgroundColor.dark,
                        isHorizontalImage = it.imageState.isHorizontal

                    )
                }
            )
        }
    }
    viewModel {
        FolderDetailViewModel(get(), get())
    }
}
