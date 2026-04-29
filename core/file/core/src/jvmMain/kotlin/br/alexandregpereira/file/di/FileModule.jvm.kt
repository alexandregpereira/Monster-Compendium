package br.alexandregpereira.file.di

import br.alexandregpereira.file.ImageFileManager
import br.alexandregpereira.file.JvmImageFileManager
import org.koin.core.scope.Scope

internal actual fun Scope.createFileManager(): ImageFileManager {
    return JvmImageFileManager()
}
