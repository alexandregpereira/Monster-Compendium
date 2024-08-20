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

package br.alexandregpereira.hunter.sync.di

import br.alexandregpereira.hunter.sync.SyncAnalytics
import br.alexandregpereira.hunter.sync.SyncEventManager
import br.alexandregpereira.hunter.sync.SyncStateHolder
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import org.koin.dsl.module

val syncModule = module {
    single { SyncEventManager() }
    single<SyncEventListener> { get<SyncEventManager>() }
    single<SyncEventDispatcher> { get<SyncEventManager>() }

    factory {
        SyncStateHolder(
            dispatcher = get(),
            syncEventManager = get(),
            syncUseCase = get(),
            analytics = SyncAnalytics(get()),
        )
    }
}
