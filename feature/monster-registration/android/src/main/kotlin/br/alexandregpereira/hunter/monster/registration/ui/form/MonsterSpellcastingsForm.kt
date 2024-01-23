package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ClickableField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.PickerField

@Composable
internal fun MonsterSpellcastingsForm(
    spellcastings: List<Spellcasting>,
    modifier: Modifier = Modifier,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<Spellcasting>) -> Unit = {}
) = Form(modifier, stringResource(R.string.monster_registration_spells)) {
    val newSpellcastings = spellcastings.toMutableList()
    val options = SpellcastingType.entries
    val optionStrings = SpellcastingType.entries.map { it.toState().getStringName() }

    spellcastings.forEachIndexed { index, spellcasting ->
        PickerField(
            value = spellcasting.type.toState().getStringName(),
            label = stringResource(R.string.monster_registration_spellcasting_type_label),
            options = optionStrings,
            onValueChange = { optionIndex ->
                onChanged(newSpellcastings.changeAt(index) { copy(type = options[optionIndex]) })
            }
        )
        AppTextField(
            text = spellcasting.description,
            label = stringResource(R.string.monster_registration_description),
            multiline = true,
            onValueChange = { newValue ->
                onChanged(newSpellcastings.changeAt(index) { copy(description = newValue) })
            }
        )

        MonsterSpellsUsageForm(
            spellsUsage = spellcasting.usages,
            onSpellClick = onSpellClick,
            onChanged = { newSpellsUsage ->
                onChanged(newSpellcastings.changeAt(index) { copy(usages = newSpellsUsage) })
            }
        )

        AddButton(text = stringResource(R.string.monster_registration_add_spellcasting_type))
    }
}

@Composable
internal fun MonsterSpellsUsageForm(
    spellsUsage: List<SpellUsage>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellUsage>) -> Unit = {}
) {
    val newSpellsUsage = spellsUsage.toMutableList()

    AddButton(text = stringResource(R.string.monster_registration_add_spell_group))

    spellsUsage.forEachIndexed { index, spellUsage ->
        AppTextField(
            text = spellUsage.group,
            label = stringResource(R.string.monster_registration_spell_group),
            onValueChange = { newValue ->
                onChanged(newSpellsUsage.changeAt(index) { copy(group = newValue) })
            }
        )

        MonsterSpellsForm(spells = spellUsage.spells, onSpellClick = onSpellClick)

        AddButton(text = stringResource(R.string.monster_registration_add_spell_group))
    }
}

@Composable
internal fun MonsterSpellsForm(
    spells: List<SpellPreview>,
    onSpellClick: (String) -> Unit = {}
) {
    spells.forEach { spell ->
        ClickableField(
            text = spell.name,
            label = stringResource(R.string.monster_registration_spell_label),
            onClick = { onSpellClick(spell.index) },
        )
    }

    AddButton(text = stringResource(R.string.monster_registration_add_spell))
}

private fun SpellcastingType.toState() = when (this) {
    SpellcastingType.SPELLCASTER -> SpellcastingTypeState.SPELLCASTER
    SpellcastingType.INNATE -> SpellcastingTypeState.INNATE
}

private enum class SpellcastingTypeState(val stringRes: Int) {
    SPELLCASTER(R.string.monster_registration_spellcasting_caster_type),
    INNATE(R.string.monster_registration_spellcasting_innate_type),
}

@Composable
private fun SpellcastingTypeState.getStringName() = stringResource(stringRes)

