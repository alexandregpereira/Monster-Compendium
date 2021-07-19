package br.alexandregpereira.hunter.app

import android.util.Log
import br.alexandregpereira.hunter.domain.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class LogModule {

    @Binds
    abstract fun bindLogger(loggerImpl: LoggerImpl) : Logger
}

class LoggerImpl @Inject constructor() : Logger {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}
