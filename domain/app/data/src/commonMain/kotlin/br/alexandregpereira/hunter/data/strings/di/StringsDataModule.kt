package br.alexandregpereira.hunter.data.strings.di

import br.alexandregpereira.hunter.data.Database
import br.alexandregpereira.hunter.data.di.getDispatcherIO
import br.alexandregpereira.hunter.data.strings.StringsLocalDataSource
import br.alexandregpereira.hunter.data.strings.StringsRemoteDataSource
import br.alexandregpereira.hunter.data.strings.StringsRepositoryImpl
import br.alexandregpereira.hunter.domain.strings.StringsRepository
import org.koin.dsl.module

val stringsDataModule = module {
    single<StringsRepository> {
        StringsRepositoryImpl(
            remote = StringsRemoteDataSource(get(), get()),
            local = StringsLocalDataSource(
                queries = get<Database>().stringEntityQueries,
                dispatcher = getDispatcherIO(),
            ),
        )
    }
}
