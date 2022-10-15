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

package br.alexandregpereira.hunter.spell.detail.di

import br.alexandregpereira.hunter.spell.detail.SpellDetailEventManager
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class EventDispatcherModuleImpl {

    @Singleton
    @Provides
    internal fun provideEventDispatcherImpl(): SpellDetailEventManager {
        return SpellDetailEventManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class EventDispatcherModule {

    @Singleton
    @Binds
    internal abstract fun bindEventDispatcher(
        spellDetailEventManager: SpellDetailEventManager
    ): SpellDetailEventDispatcher

    @Singleton
    @Binds
    internal abstract fun bindEventListener(
        spellDetailEventManager: SpellDetailEventManager
    ): SpellDetailEventListener
}
