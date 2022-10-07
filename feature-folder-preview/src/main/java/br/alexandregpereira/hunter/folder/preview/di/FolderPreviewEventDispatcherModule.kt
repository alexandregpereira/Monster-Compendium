/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview.di

import br.alexandregpereira.hunter.folder.preview.FolderPreviewConsumerEventDispatcherImpl
import br.alexandregpereira.hunter.folder.preview.FolderPreviewEventDispatcherImpl
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewConsumerEventListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventListener
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FolderPreviewEventDispatcherModuleImpl {

    @Singleton
    @Provides
    internal fun provideFolderPreviewEventDispatcherImpl(): FolderPreviewEventDispatcherImpl {
        return FolderPreviewEventDispatcherImpl()
    }

    @Singleton
    @Provides
    internal fun provideFolderPreviewConsumerEventDispatcherImpl(): FolderPreviewConsumerEventDispatcherImpl {
        return FolderPreviewConsumerEventDispatcherImpl()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FolderPreviewEventDispatcherModule {

    @Singleton
    @Binds
    internal abstract fun bindFolderPreviewEventDispatcher(
        folderPreviewEventDispatcher: FolderPreviewEventDispatcherImpl
    ): FolderPreviewEventDispatcher

    @Singleton
    @Binds
    internal abstract fun bindFolderPreviewEventListener(
        folderPreviewEventDispatcher: FolderPreviewEventDispatcherImpl
    ): FolderPreviewEventListener

    @Singleton
    @Binds
    internal abstract fun bindFolderPreviewConsumerEventDispatcher(
        folderPreviewConsumerEventDispatcher: FolderPreviewConsumerEventDispatcherImpl
    ): FolderPreviewConsumerEventDispatcher

    @Singleton
    @Binds
    internal abstract fun bindFolderPreviewConsumerEventListener(
        folderPreviewConsumerEventListener: FolderPreviewConsumerEventDispatcherImpl
    ): FolderPreviewConsumerEventListener
}
