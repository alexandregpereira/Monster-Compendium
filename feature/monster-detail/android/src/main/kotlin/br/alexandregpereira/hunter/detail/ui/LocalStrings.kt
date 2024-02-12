package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStrings

internal val LocalStrings = compositionLocalOf { MonsterDetailStrings() }

internal val strings: MonsterDetailStrings
    @Composable
    get() = LocalStrings.current
