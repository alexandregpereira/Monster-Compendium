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

package br.alexandregpereira.hunter.domain.folder.di

import br.alexandregpereira.hunter.domain.folder.AddMonsterToTemporaryFolderUseCase
import br.alexandregpereira.hunter.domain.folder.AddMonstersToFolderUseCase
import br.alexandregpereira.hunter.domain.folder.ClearTemporaryFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetFolderMonsterPreviewsByIdsUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonstersByTemporaryFolderUseCase
import br.alexandregpereira.hunter.domain.folder.RemoveMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.RemoveMonstersFromTemporaryFolderUseCase
import org.koin.dsl.module

val monsterFolderDomainModule = module {
    factory { AddMonstersToFolderUseCase(get()) }
    factory { AddMonsterToTemporaryFolderUseCase(get()) }
    factory { ClearTemporaryFolderUseCase(get()) }
    factory { GetFolderMonsterPreviewsByIdsUseCase(get()) }
    factory { GetMonsterFoldersUseCase(get()) }
    factory { GetMonstersByFolderUseCase(get()) }
    factory { GetMonstersByTemporaryFolderUseCase(get()) }
    factory { RemoveMonsterFoldersUseCase(get()) }
    factory { RemoveMonstersFromTemporaryFolderUseCase(get()) }
}
