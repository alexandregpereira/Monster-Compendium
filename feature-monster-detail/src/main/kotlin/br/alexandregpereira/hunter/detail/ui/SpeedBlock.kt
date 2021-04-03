package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.ui.theme.HunterTheme


@Composable
fun SpeedBlock(
    speed: Speed,
    modifier: Modifier = Modifier,
    contentPaddingBottom: Dp = 0.dp,
) {
    val prefixTitle = stringResource(R.string.monster_detail_speed_title)
    val title = if (speed.hover) {
        "$prefixTitle (${stringResource(R.string.monster_detail_speed_hover)})"
    } else prefixTitle
    Block(title = title, modifier = modifier, contentPaddingBottom = contentPaddingBottom) {

        SpeedGrid(speed)
    }
}

@Preview
@Composable
fun SpeedBlockPreview() = HunterTheme {
    SpeedBlock(
        speed = Speed(
            hover = true, values = listOf(
                SpeedValue(
                    type = SpeedType.WALK,
                    measurementUnit = MeasurementUnit.METER,
                    value = 0,
                    valueFormatted = "10m"
                )
            )
        )
    )
}