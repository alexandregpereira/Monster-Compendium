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

package br.alexandregpereira.hunter.shareContent

import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.shareContent.domain.GetMonstersContentEditedToExport
import br.alexandregpereira.hunter.shareContent.domain.GetMonstersContentToExport
import br.alexandregpereira.hunter.shareContent.domain.ImportContent
import br.alexandregpereira.hunter.shareContent.state.ShareContentStateHolder
import org.koin.dsl.module

val featureShareContentModule = module {
    single { ShareContentEventDispatcher() }
    factory { ImportContent(get(), get(), get()) }
    factory { GetMonstersContentToExport(get(), get(), get(), get()) }
    factory { GetMonstersContentEditedToExport(get(), get(), get()) }
    single { ShareContentStateHolder(get(), get(), get(), get(), get()) }
}
