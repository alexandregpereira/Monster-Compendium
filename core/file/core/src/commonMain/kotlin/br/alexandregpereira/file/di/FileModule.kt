package br.alexandregpereira.file.di

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileManagerImpl
import org.koin.core.scope.Scope
import org.koin.dsl.module

val fileModule = module {
    factory<FileManager> {
        FileManagerImpl(
            platformFileManager = createFileManager()
        )
    }
}

internal expect fun Scope.createFileManager(): FileManager
