package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.spell.detail.R
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import br.alexandregpereira.hunter.ui.compose.SpellIconInfo
import br.alexandregpereira.hunter.ui.compose.SpellIconSize
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun SpellHeader(
    spell: SpellState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column {
            Spacer(modifier = Modifier.height(48.dp))

            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                elevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                val ritualLabel = if (spell.ritual) {
                    "(${stringResource(R.string.spell_detail_ritual)})"
                } else ""
                val subtitle = "${spell.level} level, ${spell.school.name} $ritualLabel"
                ScreenHeader(
                    title = spell.name,
                    subTitle = subtitle,
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(top = 24.dp)
                )
            }
        }

        SpellIconInfo(
            school = spell.school,
            size = SpellIconSize.LARGE,
            modifier = Modifier
                .align(TopCenter)
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = CircleShape
                )
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun SpellHeaderPreview() = HunterTheme {
    SpellHeader(
        spell = SpellState(
            index = "",
            name = "Detect Good and Evil",
            level = 0,
            castingTime = "",
            components = "",
            duration = "",
            range = "",
            ritual = true,
            concentration = false,
            savingThrowType = null,
            damageType = null,
            school = SchoolOfMagicState.ABJURATION,
            description = "",
            higherLevel = null
        )
    )
}
