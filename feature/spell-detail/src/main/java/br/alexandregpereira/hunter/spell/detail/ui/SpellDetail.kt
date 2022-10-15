package br.alexandregpereira.hunter.spell.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState
import br.alexandregpereira.hunter.ui.theme.HunterTheme

@Composable
fun SpellDetail(
    spell: SpellState
) = Column {
    SpellHeader(spell = spell)

    SpellInfoGrid(
        spell = spell,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp, top = 16.dp)
    )

    SpellDescription(
        description = spell.description,
        higherLevel = spell.higherLevel,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .padding(bottom = 16.dp)
    )
}

@Preview
@Composable
private fun SpellDetailPreview() = HunterTheme {
    SpellDetail(
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
            school = SchoolOfMagicState.TRANSMUTATION,
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
}
