/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.R
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun SpellIconInfo(
    school: SchoolOfMagicState,
    modifier: Modifier = Modifier,
    name: String? = null,
    size: SpellIconSize = SpellIconSize.SMALL,
    onClick: () -> Unit = {}
) {
    val iconColor = if (isSystemInDarkTheme()) school.iconColorDark else school.iconColorLight
    IconInfo(
        title = name,
        painter = painterResource(school.iconRes),
        iconColor = iconColor.toColor(),
        iconAlpha = 1f,
        iconSize = size.value,
        modifier = modifier.animatePressed(
            pressedScale = 0.85f,
            onClick = onClick
        )
    )
}

enum class SpellIconSize(val value: Dp) {
    LARGE(72.dp), SMALL(56.dp)
}

enum class SchoolOfMagicState(val iconRes: Int, val iconColorLight: String, val iconColorDark: String) {
    ABJURATION(iconRes = R.drawable.ic_school_abjuration, iconColorLight = "#0013FF", iconColorDark = "#4A4AFF"),
    CONJURATION(iconRes = R.drawable.ic_school_conjuration, iconColorLight = "#6633CC", iconColorDark = "#B06CFF"),
    DIVINATION(iconRes = R.drawable.ic_school_divination, iconColorLight = "#FF9900", iconColorDark = "#FFAC3E"),
    ENCHANTMENT(iconRes = R.drawable.ic_school_enchantment, iconColorLight = "#CC00CC", iconColorDark = "#FF00FF"),
    EVOCATION(iconRes = R.drawable.ic_school_evocation, iconColorLight = "#CC0000", iconColorDark = "#F20C0C"),
    ILLUSION(iconRes = R.drawable.ic_school_illusion, iconColorLight = "#009DC1", iconColorDark = "#5ADCFF"),
    NECROMANCY(iconRes = R.drawable.ic_school_necromancy, iconColorLight = "#000000", iconColorDark = "#FFFFFF"),
    TRANSMUTATION(iconRes = R.drawable.ic_school_transmutation, iconColorLight = "#00C100", iconColorDark = "#00FF00"),
}
