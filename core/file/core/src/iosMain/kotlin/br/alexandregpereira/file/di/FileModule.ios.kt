package br.alexandregpereira.file.di

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.IosFileManager
import org.koin.core.scope.Scope

internal actual fun Scope.createFileManager(): FileManager {
    return IosFileManager()
}
