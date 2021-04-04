package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun AbilityScoreGrid(
    abilityScores: List<AbilityScore>,
    modifier: Modifier = Modifier,
) = Column(modifier) {

    Grid {
        abilityScores.getOrNull(0)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(1)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(2)?.let {
            AbilityScore(abilityScore = it)
        }
    }

    Grid(Modifier.padding(top = 24.dp)) {
        abilityScores.getOrNull(3)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(4)?.let {
            AbilityScore(abilityScore = it)
        }
        abilityScores.getOrNull(5)?.let {
            AbilityScore(abilityScore = it)
        }
    }
}

@Preview
@Composable
fun AbilityScoreGridPreview() = Window {
    AbilityScoreGrid(
        abilityScores = (0..5).map {
            AbilityScore(
                type = AbilityScoreType.CHARISMA,
                value = 20,
                modifier = 5
            )
        }
    )
}