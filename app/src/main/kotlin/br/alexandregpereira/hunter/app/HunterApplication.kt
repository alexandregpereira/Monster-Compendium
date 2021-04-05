/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.app

import android.app.Application
import br.alexandregpereira.hunter.data.di.dataModule
import br.alexandregpereira.hunter.detail.monsterDetailModule
import br.alexandregpereira.hunter.domain.di.domainModule
import br.alexandregpereira.hunter.monster.compendium.monsterCompendiumModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HunterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HunterApplication)
            modules(
                dataModule +
                    domainModule +
                    monsterCompendiumModule +
                    monsterDetailModule
            )
        }
    }
}