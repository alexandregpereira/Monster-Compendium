/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.AppBarIcon
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor
import org.jetbrains.compose.resources.painterResource
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState as UiSchoolOfMagicState

@Composable
internal fun SpellHeader(
    title: String,
    subtitle: String,
    schoolIcon: UiSchoolOfMagicState,
    modifier: Modifier = Modifier,
    onOptions: () -> Unit = {},
) = Layout(
    modifier = modifier,
    content = {
        val iconColor = if (isSystemInDarkTheme()) {
            schoolIcon.iconColorDark
        } else schoolIcon.iconColorLight
        val iconAlpha = 0.2f

        Icon(
            painter = painterResource(schoolIcon.icon),
            contentDescription = subtitle,
            tint = iconColor.toColor(),
            modifier = Modifier.alpha(iconAlpha)
        )

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
        ) {
            ScreenHeader(
                title = title,
                subTitle = subtitle,
                modifier = Modifier.weight(1f),
            )
            AppBarIcon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "",
                onClicked = onOptions,
                modifier = Modifier
                    .size(24.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }

        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        )
    }
) { measurables, constraints ->
    val titlePlaceable = measurables[1].measure(constraints)

    val iconHeight = titlePlaceable.height * 2
    val iconPlaceable = measurables[0].measure(
        Constraints.fixed(width = iconHeight, height = iconHeight)
    )
    val dividerHeight = measurables[2].measure(
        Constraints.fixed(
            width = constraints.maxWidth - iconPlaceable.width + 8.dp.toPx().toInt(),
            height = 1.dp.toPx().toInt()
        )
    )

    val outOfBoundsDistance = 24.dp.toPx().toInt()

    layout(constraints.maxWidth, titlePlaceable.height + dividerHeight.height) {
        iconPlaceable.placeRelative(
            x = constraints.maxWidth - (iconPlaceable.width - outOfBoundsDistance),
            y = -(titlePlaceable.height / 2)
        )

        titlePlaceable.placeRelative(
            x = 0,
            y = 0
        )

        dividerHeight.placeRelative(
            x = 0,
            y = titlePlaceable.height
        )
    }
}

@Preview
@Composable
private fun SpellHeaderPreview() = Window {
    SpellHeader(
        title = "Detect Good and Evil asda asdasd",
        subtitle = "Level 1",
        schoolIcon = UiSchoolOfMagicState.ABJURATION
    )
}
