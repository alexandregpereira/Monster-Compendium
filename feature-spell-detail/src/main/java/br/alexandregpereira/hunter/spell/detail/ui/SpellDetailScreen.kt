package br.alexandregpereira.hunter.spell.detail.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.alexandregpereira.hunter.spell.detail.SpellDetailViewState
import br.alexandregpereira.hunter.ui.compose.Closeable
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
internal fun SpellDetailScreen(
    state: SpellDetailViewState,
    contentPadding: PaddingValues = PaddingValues(),
    onClose: () -> Unit = {}
) = HunterTheme {
    BackHandler(enabled = state.showDetail, onBack = onClose)
    Closeable(opened = state.showDetail, onClosed = onClose) {
        AnimatedVisibility(
            visible = state.showDetail,
            enter = slideInVertically { fullHeight -> fullHeight * 2 },
            exit = slideOutVertically { fullHeight -> fullHeight * 2 },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            state.spell?.let { SpellDetail(it, contentPadding = contentPadding) }
        }
    }
}

@Preview
@Composable
private fun SpellDetailScreenPreview() = HunterTheme {
    SpellDetailScreen(
        SpellDetailViewState(
            spell = SpellState(
                index = "index",
                name = "Detect Good and Evil",
                level = 1,
                castingTime = "castingTime",
                components = "components",
                duration = "duration",
                range = "range",
                ritual = true,
                concentration = true,
                savingThrowType = SavingThrowTypeState.CONSTITUTION,
                school = SchoolOfMagicState.ABJURATION,
                description = "description description description description description deiption" +
                        "descriptiondesc riptionde scriptiondescriptiondescription descriptionstion" +
                        "descriptiondesc riptionde scriptiondescriptiondescription descriptiription" +
                        "descriptiondesc riptionde scriptiondescriptiondescription descriptioiption" +
                        "dasd asda sd asd asd as as d as",
                higherLevel = "description description description description description descgion" +
                        "descriptiondesc riptionde scriptiondescriptiondescription descriptioiption" +
                        "descriptiondesc riptionde scriptiondescriptiondescription descriptcription"
            )
        )
    )
}
