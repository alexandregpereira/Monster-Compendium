package br.alexandregpereira.hunter.detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun AbilityScoreBlock(
    abilityScores: List<AbilityScore>,
    modifier: Modifier = Modifier,
) = Block(
    title = "Ability Scores",
    modifier = modifier,
) {

    AbilityScoreGrid(abilityScores)
}

@Preview
@Composable
fun AbilityScoreBlockPreview() = HunterTheme {
    AbilityScoreBlock(
        abilityScores = (0..5).map {
            AbilityScore(
                type = AbilityScoreType.CHARISMA,
                value = 20,
                modifier = 5
            )
        }
    )
}