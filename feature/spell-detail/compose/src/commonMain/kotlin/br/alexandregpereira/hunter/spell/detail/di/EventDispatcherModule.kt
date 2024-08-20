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

package br.alexandregpereira.hunter.spell.detail.di

import br.alexandregpereira.hunter.spell.detail.SpellDetailAnalytics
import br.alexandregpereira.hunter.spell.detail.SpellDetailEventManager
import br.alexandregpereira.hunter.spell.detail.SpellDetailViewModel
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import org.koin.dsl.module

val featureSpellDetailModule = module {
    single { SpellDetailEventManager() }
    single<SpellDetailEventDispatcher> { get<SpellDetailEventManager>() }
    single<SpellDetailEventListener> { get<SpellDetailEventManager>() }

    single {
        SpellDetailViewModel(
            getSpell = get(),
            spellDetailEventListener = get(),
            dispatcher = get(),
            analytics = SpellDetailAnalytics(get()),
            appLocalization = get(),
        )
    }
}
