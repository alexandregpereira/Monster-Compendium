/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.app

import android.app.Application
import br.alexandregpereira.hunter.analytics.di.analyticsModule
import br.alexandregpereira.hunter.data.di.dataModules
import br.alexandregpereira.hunter.detail.di.monsterDetailModule
import br.alexandregpereira.hunter.domain.di.domainModules
import br.alexandregpereira.hunter.folder.detail.di.folderDetailModule
import br.alexandregpereira.hunter.folder.insert.di.folderInsertModule
import br.alexandregpereira.hunter.folder.list.di.folderListModule
import br.alexandregpereira.hunter.folder.preview.di.folderPreviewModule
import br.alexandregpereira.hunter.monster.compendium.di.monsterCompendiumModule
import br.alexandregpereira.hunter.monster.content.di.monsterContentManagerModule
import br.alexandregpereira.hunter.monster.lore.detail.di.monsterLoreDetailModule
import br.alexandregpereira.hunter.search.di.searchModule
import br.alexandregpereira.hunter.settings.di.settingsModule
import br.alexandregpereira.hunter.spell.detail.di.spellDetailModule
import br.alexandregpereira.hunter.sync.di.syncModule
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

class HunterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@HunterApplication)
            modules(
                module {
                    factory { Firebase.analytics }
                    factory { Firebase.crashlytics }
                }
            )
            initKoinModules()
        }
    }

    internal companion object {
        private val appModule = module {
            factory { Dispatchers.Default }
            viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
        }

        fun KoinApplication.initKoinModules() {
            modules(domainModules)
            modules(dataModules)
            modules(
                folderDetailModule +
                        folderInsertModule +
                        folderListModule +
                        folderPreviewModule +
                        monsterLoreDetailModule +
                        monsterContentManagerModule +
                        syncModule
            )
            modules(
                analyticsModule,
                appModule,
                monsterCompendiumModule,
                monsterDetailModule,
                searchModule,
                settingsModule,
                spellDetailModule,
            )
        }
    }
}
