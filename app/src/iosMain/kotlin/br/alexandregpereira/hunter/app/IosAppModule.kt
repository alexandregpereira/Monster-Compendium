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

package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.app.di.initKoinModules
import br.alexandregpereira.hunter.featureFlag.FeatureFlagProvider
import org.koin.core.Koin
import org.koin.core.context.startKoin

private lateinit var koinInstance: Koin

internal fun appKoin(): Koin = koinInstance

fun initKoin() {
    koinInstance = startKoin {
        initKoinModules()
    }.koin
    koinInstance.get<FeatureFlagProvider>().initialize()
}
