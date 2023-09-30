package br.alexandregpereira.hunter.event.systembar

import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import org.koin.dsl.module

val bottomBarEventModule = module {
    single { BottomBarEventManager() }
    factory<EventDispatcher<BottomBarEvent>> {
        get<BottomBarEventManager>()
    }
    factory<EventListener<BottomBarEvent>> {
        get<BottomBarEventManager>()
    }
}
