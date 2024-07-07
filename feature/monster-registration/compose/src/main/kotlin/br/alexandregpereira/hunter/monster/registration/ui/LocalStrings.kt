package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationStrings

internal val LocalStrings = compositionLocalOf {
    MonsterRegistrationStrings()
}

internal val strings: MonsterRegistrationStrings
    @Composable
    get() = LocalStrings.current
