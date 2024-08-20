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

package br.alexandregpereira.hunter.domain.folder.di

import br.alexandregpereira.hunter.domain.folder.AddMonsterToTemporaryFolderUseCase
import br.alexandregpereira.hunter.domain.folder.AddMonstersToFolderUseCase
import br.alexandregpereira.hunter.domain.folder.ClearTemporaryFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetFolderMonsterPreviewsByIdsUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolders
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
    factory { GetMonstersByFolders(get()) }
}
