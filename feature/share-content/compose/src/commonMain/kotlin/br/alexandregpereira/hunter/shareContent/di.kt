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
