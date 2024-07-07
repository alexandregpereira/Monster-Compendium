package br.alexandregpereira.hunter.app.di

import br.alexandregpereira.hunter.app.MainViewModel
import br.alexandregpereira.hunter.app.MainViewState
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
val appModule = module {
    factory { Dispatchers.Default }

    single(named(AppStateRecoveryQualifier)) {
        StateRecovery(MainViewState())
    }

    single {
        MainViewModel(
            monsterDetailEventListener = get(),
            folderDetailResultListener = get(),
            folderListResultListener = get(),
            monsterContentManagerEventListener = get(),
            folderPreviewEventDispatcher = get(),
            bottomBarEventManager = get(),
            appLocalization = get(),
            stateRecovery = get(named(AppStateRecoveryQualifier)),
        )
    }
}

const val AppStateRecoveryQualifier: String = "AppStateRecovery"
