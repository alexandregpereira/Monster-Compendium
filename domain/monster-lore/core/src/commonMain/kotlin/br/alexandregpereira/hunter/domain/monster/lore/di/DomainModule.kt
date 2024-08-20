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

package br.alexandregpereira.hunter.domain.monster.lore.di

import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreEdited
import br.alexandregpereira.hunter.domain.monster.lore.SaveMonstersLoreUseCase
import br.alexandregpereira.hunter.domain.monster.lore.SyncMonstersLoreUseCase
import org.koin.dsl.module

val monsterLoreDomainModule = module {
    factory { GetMonsterLoreUseCase(get()) }
    factory { GetMonstersLoreByIdsUseCase(get()) }
    factory { SyncMonstersLoreUseCase(get(), get(), get(), get(), get()) }
    factory { SaveMonstersLoreUseCase(get()) }
    factory { GetMonstersLoreEdited(get()) }
}
