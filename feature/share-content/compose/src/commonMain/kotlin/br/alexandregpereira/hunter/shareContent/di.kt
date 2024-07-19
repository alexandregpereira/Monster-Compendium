package br.alexandregpereira.hunter.shareContent

import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.shareContent.domain.GetMonsterContentToExport
import br.alexandregpereira.hunter.shareContent.domain.ImportContent
import br.alexandregpereira.hunter.shareContent.state.ShareContentStateHolder
import org.koin.dsl.module

val featureShareContentModule = module {
    single { ShareContentEventDispatcher() }
    factory { ImportContent(get(), get(), get()) }
    factory { GetMonsterContentToExport(get(), get(), get()) }
    single { ShareContentStateHolder(get(), get(), get(), get(), get()) }
}
