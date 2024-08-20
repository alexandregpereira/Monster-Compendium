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

package br.alexandregpereira.hunter.monster.detail.di

import br.alexandregpereira.hunter.monster.detail.MonsterDetailAnalytics
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.monster.detail.domain.CloneMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.DeleteMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.detail.domain.ResetMonsterToOriginal
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEventDispatcher
import br.alexandregpereira.hunter.ui.StateRecovery
import org.koin.core.qualifier.named
import org.koin.dsl.module

val monsterDetailModule = module {
    factory { GetMonsterDetailUseCase(get(), get(), get(), get(), get(), get()) }

    single(named(MonsterDetailStateRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        MonsterDetailStateHolder(
            getMonsterDetailUseCase = get(),
            cloneMonster = get(),
            deleteMonster = get(),
            spellDetailEventDispatcher = get(),
            monsterEventDispatcher = get(),
            monsterLoreDetailEventDispatcher = get(),
            folderInsertEventDispatcher = get(),
            monsterRegistrationEventDispatcher = get<MonsterRegistrationEventDispatcher>(),
            dispatcher = get(),
            analytics = MonsterDetailAnalytics(get()),
            appLocalization = get(),
            stateRecovery = get(named(MonsterDetailStateRecoveryQualifier)),
            resetMonsterToOriginal = get(),
            syncEventDispatcher = get(),
            shareContentEventDispatcher = get(),
        )
    }
    factory { CloneMonsterUseCase(get(), get(), get(), get()) }
    factory { DeleteMonsterUseCase(repository = get()) }
    factory { ResetMonsterToOriginal(get(), get()) }
}

const val MonsterDetailStateRecoveryQualifier = "MonsterDetailStateRecovery"
