package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.detail.R
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.Window
import br.alexandregpereira.hunter.ui.util.toColor

@Composable
fun SpellHeader(
    spell: SpellState,
    modifier: Modifier = Modifier
) = Layout(
    modifier = modifier,
    content = {
        val iconColor = if (isSystemInDarkTheme()) {
            spell.school.iconColorDark
        } else spell.school.iconColorLight
        val iconAlpha = 0.2f

        Icon(
            painter = painterResource(spell.school.iconRes),
            contentDescription = spell.school.name,
            tint = iconColor.toColor(),
            modifier = Modifier
                .alpha(iconAlpha)
        )

        val ritualLabel = if (spell.ritual) {
            "(${stringResource(R.string.spell_detail_ritual)})"
        } else ""
        val subtitle = "Level ${spell.level} ${spell.school.name.lowercase()} $ritualLabel"
        ScreenHeader(
            title = spell.name,
            subTitle = subtitle,
            modifier = Modifier
                .padding(16.dp)
        )

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
        spell = SpellState(
            index = "",
            name = "Detect Good and Evil asda",
            level = 0,
            castingTime = "",
            components = "",
            duration = "",
            range = "",
            ritual = true,
            concentration = false,
            savingThrowType = null,
            damageType = null,
            school = SchoolOfMagicState.CONJURATION,
            description = "",
            higherLevel = null
        )
    )
}
