package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun StatsBlock(
    stats: Stats,
    modifier: Modifier = Modifier
) = Block(modifier = modifier) {

    StatsGrid(stats = stats)
}

@Preview
@Composable
fun StatsBlockPreview() = Window {
    StatsBlock(stats = Stats(armorClass = 0, hitPoints = 0, hitDice = "teasdas"))
}
