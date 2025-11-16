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

package br.alexandregpereira.hunter.app.di

import br.alexandregpereira.hunter.analytics.di.analyticsModule
import br.alexandregpereira.hunter.app.AppConfig
import br.alexandregpereira.hunter.app.MainViewModel
import br.alexandregpereira.hunter.app.event.appEventModule
import br.alexandregpereira.hunter.data.di.dataModules
import br.alexandregpereira.hunter.detail.di.featureMonsterDetailModule
import br.alexandregpereira.hunter.domain.di.domainModules
import br.alexandregpereira.hunter.folder.detail.di.featureFolderDetailModule
import br.alexandregpereira.hunter.folder.insert.di.featureFolderInsertModule
import br.alexandregpereira.hunter.folder.list.di.featureFolderListModule
import br.alexandregpereira.hunter.folder.preview.di.featureFolderPreviewModule
import br.alexandregpereira.hunter.localization.di.localizationModule
import br.alexandregpereira.hunter.monster.compendium.di.featureMonsterCompendiumModule
import br.alexandregpereira.hunter.monster.content.di.featureMonsterContentManagerModule
import br.alexandregpereira.hunter.monster.content.preview.di.featureMonsterContentPreviewModule
import br.alexandregpereira.hunter.monster.event.monsterEventModule
import br.alexandregpereira.hunter.monster.lore.detail.di.featureMonsterLoreDetailModule
import br.alexandregpereira.hunter.monster.registration.di.featureMonsterRegistrationModule
import br.alexandregpereira.hunter.search.di.featureSearchModule
import br.alexandregpereira.hunter.settings.di.featureSettingsModule
import br.alexandregpereira.hunter.shareContent.featureShareContentModule
import br.alexandregpereira.hunter.spell.compendium.di.featureSpellCompendiumModule
import br.alexandregpereira.hunter.spell.detail.di.featureSpellDetailModule
import br.alexandregpereira.hunter.sync.di.featureSyncModule
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun KoinApplication.initKoinModules() {
    val databaseName = "hunter-database" + AppConfig.VERSION_NAME_SUFFIX
    allowOverride(false)
    modules(domainModules)
    modules(dataModules(databaseName))
    modules(
        appModule,
        featureFolderDetailModule,
        featureFolderInsertModule,
        featureFolderListModule,
        featureFolderPreviewModule,
        featureMonsterCompendiumModule,
        featureMonsterDetailModule,
        featureMonsterLoreDetailModule,
        featureMonsterContentManagerModule,
        featureMonsterContentPreviewModule,
        featureSyncModule,
        featureMonsterRegistrationModule,
        featureSearchModule,
        featureSettingsModule,
        featureSpellCompendiumModule,
        featureSpellDetailModule,
        featureShareContentModule,
    )
    modules(
        analyticsModule,
        localizationModule,
        monsterEventModule,
        appEventModule,
    )
}

private val appModule = module {
    factory { Dispatchers.Default }

    single(named(AppStateRecoveryQualifier)) {
        StateRecovery()
    }

    single {
        MainViewModel(
            appLocalization = get(),
            stateRecovery = get(named(AppStateRecoveryQualifier)),
            appEventDispatcher = get(),
        )
    }
}

internal const val AppStateRecoveryQualifier: String = "AppStateRecovery"
