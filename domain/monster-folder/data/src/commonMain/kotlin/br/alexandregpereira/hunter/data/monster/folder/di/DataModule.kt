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

package br.alexandregpereira.hunter.data.monster.folder.di

import br.alexandregpereira.hunter.data.monster.folder.DefaultFolderRepository
import br.alexandregpereira.hunter.data.monster.folder.local.DefaultMonsterFolderLocalDataSource
import br.alexandregpereira.hunter.data.monster.folder.local.MonsterFolderLocalDataSource
import br.alexandregpereira.hunter.domain.folder.FolderMonsterPreviewRepository
import br.alexandregpereira.hunter.domain.folder.MonsterFolderRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val monsterFolderDataModule = module {
    factory<MonsterFolderLocalDataSource> { DefaultMonsterFolderLocalDataSource(get(), get()) }
    factory {
        createMonsterFolderRepository() ?: DefaultFolderRepository(get(), get())
    }
    factory {
        createFolderMonsterPreviewRepository() ?: DefaultFolderRepository(get(), get())
    }
}.apply { includes(getAdditionalModule()) }

expect fun getAdditionalModule(): Module

expect fun Scope.createMonsterFolderRepository(): MonsterFolderRepository?

expect fun Scope.createFolderMonsterPreviewRepository(): FolderMonsterPreviewRepository?
