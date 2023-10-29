package br.alexandregpereira.hunter.monster.registration.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationState
import br.alexandregpereira.hunter.ui.compose.SwipeVerticalToDismiss
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
internal fun MonsterRegistrationScreen(
    state: MonsterRegistrationState,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) {
    BackHandler(enabled = state.isOpen, onBack = intent::onClose)

    SwipeVerticalToDismiss(visible = state.isOpen, onClose = intent::onClose) {
        Window(
            Modifier.fillMaxSize()
        ) {
            MonsterRegistrationForm(
                monster = state.monster,
                modifier = Modifier,
                contentPadding = contentPadding,
                intent = intent,
            )
        }
    }
}

@Preview
@Composable
private fun MonsterRegistrationScreenPreview() {
    MonsterRegistrationScreen(
        state = MonsterRegistrationState(
            isOpen = true,
            monster = Monster(
                index = "1",
                name = "Monster Name",
                group = "Group",
                stats = Stats(
                    armorClass = 10,
                    hitPoints = 10,
                    hitDice = "1d10",
                ),
                speed = Speed(
                    hover = false,
                    values = listOf(
                        SpeedValue(
                            type = SpeedType.WALK,
                            valueFormatted = "10 ft.",
                        ),
                        SpeedValue(
                            type = SpeedType.SWIM,
                            valueFormatted = "10 ft.",
                        ),
                    ),
                ),
                abilityScores = AbilityScoreType.entries.map {
                    AbilityScore(
                        type = it,
                        value = 10,
                        modifier = 0,
                    )
                },
            )
        ),
    )
}
