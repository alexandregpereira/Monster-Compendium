package br.alexandregpereira.file.di

import android.app.Application
import br.alexandregpereira.file.AndroidImageFileManager
import br.alexandregpereira.file.ImageFileManager
import org.koin.core.scope.Scope

internal actual fun Scope.createFileManager(): ImageFileManager {
    return AndroidImageFileManager(
        app = get<Application>(),
    )
}
