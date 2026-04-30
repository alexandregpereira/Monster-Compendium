package br.alexandregpereira.file.di

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.JvmFileManager
import org.koin.core.scope.Scope

internal actual fun Scope.createFileManager(): FileManager {
    return JvmFileManager()
}
