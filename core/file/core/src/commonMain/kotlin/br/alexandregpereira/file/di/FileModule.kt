package br.alexandregpereira.file.di

import br.alexandregpereira.file.ImageFileManager
import br.alexandregpereira.file.ImageFileManagerImpl
import org.koin.core.scope.Scope
import org.koin.dsl.module

val fileModule = module {
    factory<ImageFileManager> {
        ImageFileManagerImpl(
            platformImageFileManager = createFileManager()
        )
    }
}

internal expect fun Scope.createFileManager(): ImageFileManager
