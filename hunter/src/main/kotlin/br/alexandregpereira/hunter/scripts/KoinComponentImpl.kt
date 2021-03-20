package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.data.monster.remote.MonsterRemoteDataSource
import br.alexandregpereira.hunter.data.monster.remote.remoteDataSourceModule
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

private class KoinComponentImpl(private val koinApplication: KoinApplication) : KoinComponent {

    override fun getKoin(): Koin {
        return koinApplication.koin
    }
}

fun <T> createKoinComponent(koinApplication: KoinApplication, block: KoinComponent.() -> T): T {
    return KoinComponentImpl(koinApplication).run(block)
}

fun monsterRemoteDataSource(): Lazy<MonsterRemoteDataSource> {
    val app = startKoin {
        modules(remoteDataSourceModule)
    }

    return lazyOf(
        createKoinComponent(app) {
            get()
        }
    )
}
