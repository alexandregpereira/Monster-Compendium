package br.alexandregpereira.hunter.monster.content.preview.di

import br.alexandregpereira.hunter.monster.content.preview.MonsterContentPreviewStateRecovery
import org.koin.core.scope.Scope

internal actual fun Scope.createStateRecovery(): MonsterContentPreviewStateRecovery? = null
