package br.alexandregpereira.hunter.app.di

import br.alexandregpereira.hunter.analytics.di.analyticsModule
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
    allowOverride(false)
    modules(domainModules)
    modules(dataModules)
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
