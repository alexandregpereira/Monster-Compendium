package br.alexandregpereira.hunter.scripts

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module

private class KoinComponentImpl(private val koinApplication: KoinApplication) : KoinComponent {

    override fun getKoin(): Koin {
        return koinApplication.koin
    }
}

fun createKoinComponent(vararg modules: Module): KoinComponent {
    val app = startKoin {
        modules(*modules)
    }
    return KoinComponentImpl(app)
}
