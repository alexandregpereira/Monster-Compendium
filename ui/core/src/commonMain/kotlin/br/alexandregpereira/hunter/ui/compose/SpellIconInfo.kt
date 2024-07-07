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
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_school_abjuration
import br.alexandregpereira.hunter.ui.resources.ic_school_conjuration
import br.alexandregpereira.hunter.ui.resources.ic_school_divination
import br.alexandregpereira.hunter.ui.resources.ic_school_enchantment
import br.alexandregpereira.hunter.ui.resources.ic_school_evocation
import br.alexandregpereira.hunter.ui.resources.ic_school_illusion
import br.alexandregpereira.hunter.ui.resources.ic_school_necromancy
import br.alexandregpereira.hunter.ui.resources.ic_school_transmutation
import br.alexandregpereira.hunter.ui.util.toColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SpellIconInfo(
    school: SchoolOfMagicState,
    modifier: Modifier = Modifier,
    name: String? = null,
    size: SpellIconSize = SpellIconSize.SMALL,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val iconColor = if (isSystemInDarkTheme()) school.iconColorDark else school.iconColorLight
    IconInfo(
        title = name,
        painter = painterResource(school.icon),
        iconColor = iconColor.toColor(),
        iconAlpha = 1f,
        iconSize = size.value.dp,
        modifier = modifier.animatePressed(
            pressedScale = 0.85f,
            onClick = onClick,
            onLongClick = onLongClick,
        )
    )
}

enum class SpellIconSize(val value: Int) {
    LARGE(72), SMALL(56)
}

enum class SchoolOfMagicState(val icon: DrawableResource, val iconColorLight: String, val iconColorDark: String) {
    ABJURATION(icon = Res.drawable.ic_school_abjuration, iconColorLight = "#0013FF", iconColorDark = "#4A4AFF"),
    CONJURATION(icon = Res.drawable.ic_school_conjuration, iconColorLight = "#6633CC", iconColorDark = "#B06CFF"),
    DIVINATION(icon = Res.drawable.ic_school_divination, iconColorLight = "#FF9900", iconColorDark = "#FFAC3E"),
    ENCHANTMENT(icon = Res.drawable.ic_school_enchantment, iconColorLight = "#CC00CC", iconColorDark = "#FF00FF"),
    EVOCATION(icon = Res.drawable.ic_school_evocation, iconColorLight = "#CC0000", iconColorDark = "#F20C0C"),
    ILLUSION(icon = Res.drawable.ic_school_illusion, iconColorLight = "#009DC1", iconColorDark = "#5ADCFF"),
    NECROMANCY(icon = Res.drawable.ic_school_necromancy, iconColorLight = "#000000", iconColorDark = "#FFFFFF"),
    TRANSMUTATION(icon = Res.drawable.ic_school_transmutation, iconColorLight = "#00C100", iconColorDark = "#00FF00"),
}
