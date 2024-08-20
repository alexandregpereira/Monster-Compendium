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

package br.alexandregpereira.hunter.monster.lore.detail.di

import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventListener
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailAnalytics
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailEventManager
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailStateHolder
import br.alexandregpereira.hunter.monster.lore.detail.domain.GetMonsterLoreDetailUseCase
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monsterLoreDetailModule = module {
    single { MonsterLoreDetailEventManager() }
    single<MonsterLoreDetailEventDispatcher> { get<MonsterLoreDetailEventManager>() }
    single<MonsterLoreDetailEventListener> { get<MonsterLoreDetailEventManager>() }
    factory { GetMonsterLoreDetailUseCase(get(), get()) }

    single(named(MonsterLoreDetailRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        MonsterLoreDetailStateHolder(
            stateRecovery = get(named(MonsterLoreDetailRecoveryQualifier)),
            getMonsterLoreUseCase = get(),
            monsterLoreDetailEventListener = get(),
            dispatcher = get(),
            analytics = MonsterLoreDetailAnalytics(get()),
        )
    }
}

const val MonsterLoreDetailRecoveryQualifier: String = "MonsterLoreDetailRecovery"
