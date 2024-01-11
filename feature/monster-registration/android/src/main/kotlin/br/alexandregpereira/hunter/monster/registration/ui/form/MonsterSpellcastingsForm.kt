package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.ui.compose.Form

@Composable
internal fun MonsterSpellcastingsForm(
    spellcastings: List<Spellcasting>,
    modifier: Modifier = Modifier,
    onChanged: (List<Action>) -> Unit = {}
) = Form(modifier, "Spells") {
    Text(
        text = "Work in Progress",
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
    )
}
