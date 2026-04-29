package br.alexandregpereira.file.di

import br.alexandregpereira.file.ImageFileManager
import br.alexandregpereira.file.IosImageFileManager
import org.koin.core.scope.Scope

internal actual fun Scope.createFileManager(): ImageFileManager {
    return IosImageFileManager()
}
