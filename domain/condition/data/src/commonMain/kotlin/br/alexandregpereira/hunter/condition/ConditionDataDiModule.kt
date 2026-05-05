package br.alexandregpereira.hunter.condition

import br.alexandregpereira.hunter.condition.data.remote.ConditionRemoteDataSource
import br.alexandregpereira.hunter.condition.data.remote.ConditionRemoteDataSourceImpl
import org.koin.dsl.module

val conditionDataDiModule = module {
    factory< ConditionRemoteDataSource> {
        ConditionRemoteDataSourceImpl(
            client = get(),
            json = get(),
        )
    }
    factory<GetConditions> {
        GetConditionsImpl(
            remoteDataSource = get(),
            getLanguage = get(),
        )
    }
    factory<GetCondition> {
        GetConditionImpl(
            localDataSource = get(),
            syncConditions = get(),
        )
    }
    factory<SyncConditions> {
        SyncConditionsImpl(
            localDataSource = get(),
            getConditions = get(),
        )
    }
}
