package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import br.alexandregpereira.hunter.spell.detail.SpellDetailStrings

internal val LocalStrings = compositionLocalOf { SpellDetailStrings() }

internal val strings: SpellDetailStrings
    @Composable
    get() = LocalStrings.current
