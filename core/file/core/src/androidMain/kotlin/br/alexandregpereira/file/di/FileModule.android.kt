package br.alexandregpereira.file.di

import android.app.Application
import br.alexandregpereira.file.AndroidFileManager
import br.alexandregpereira.file.FileManager
import org.koin.core.scope.Scope

internal actual fun Scope.createFileManager(): FileManager {
    return AndroidFileManager(
        app = get<Application>(),
    )
}
