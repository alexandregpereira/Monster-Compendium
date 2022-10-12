package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.spell.detail.R
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.compose.Window

@Composable
fun SpellInfoGrid(
    spell: SpellState,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    val topPadding = 8.dp

    SpellTextInfo(
        title = stringResource(R.string.spell_detail_casting_time),
        description = spell.castingTime
    )

    SpellTextInfo(
        title = stringResource(R.string.spell_detail_range),
        description = spell.range,
        modifier = Modifier.padding(top = topPadding)
    )

    SpellTextInfo(
        title = stringResource(R.string.spell_detail_components),
        description = spell.components,
        modifier = Modifier.padding(top = topPadding)
    )

    val concentrationLabel = if (spell.concentration) {
        "${stringResource(R.string.spell_detail_concentration)}, "
    } else ""
    SpellTextInfo(
        title = stringResource(R.string.spell_detail_duration),
        description = "$concentrationLabel${spell.duration.lowercase()}",
        modifier = Modifier.padding(top = topPadding)
    )

    spell.savingThrowType?.let {
        SpellTextInfo(
            title = stringResource(R.string.spell_detail_save_type),
            description = it.name,
            modifier = Modifier.padding(top = topPadding)
        )
    }
}

@Composable
private fun SpellTextInfo(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) = Text(
    buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
            )
        ) {
            append("$title ")
        }

        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal
            )
        ) {
            append(description)
        }

    },
    fontSize = 14.sp,
    modifier = modifier
)

@Preview
@Composable
private fun SpellGridPreview() = Window {
    SpellInfoGrid(
        spell = SpellState(
            index = "",
            name = "",
            level = 0,
            castingTime = "1 Action",
            components = "Anything",
            duration = "Any",
            range = "30 Feet",
            ritual = false,
            concentration = true,
            savingThrowType = SavingThrowTypeState.WISDOM,
            damageType = null,
            school = SchoolOfMagicState.CONJURATION,
            description = "",
            higherLevel = null
        )
    )
}
