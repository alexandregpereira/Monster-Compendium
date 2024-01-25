package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ClickableField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellcastingsForm(
    spellcastings: List<Spellcasting>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<Spellcasting>) -> Unit = {}
) {
    val key = "spellcastings"
    FormLazy(key, { stringResource(R.string.monster_registration_spells) }) {
        val newSpellcastings = spellcastings.toMutableList()
        val options = SpellcastingType.entries

        FormItems(
            key = key,
            items = newSpellcastings,
            addText = { stringResource(R.string.monster_registration_add_spellcasting_type) },
            removeText = { stringResource(R.string.monster_registration_remove_spellcasting_type) },
            createNew = { Spellcasting.create() },
            onChanged = onChanged
        ) { index, spellcasting ->
            formItem(key = "$key-type-$index") {
                val optionStrings = SpellcastingType.entries.map { it.toState().getStringName() }
                PickerField(
                    value = spellcasting.type.toState().getStringName(),
                    label = stringResource(R.string.monster_registration_spellcasting_type_label),
                    options = optionStrings,
                    onValueChange = { optionIndex ->
                        onChanged(newSpellcastings.changeAt(index) { copy(type = options[optionIndex]) })
                    }
                )
            }
            formItem(key = "$key-description-$index") {
                AppTextField(
                    text = spellcasting.description,
                    label = stringResource(R.string.monster_registration_description),
                    multiline = true,
                    onValueChange = { newValue ->
                        onChanged(newSpellcastings.changeAt(index) { copy(description = newValue) })
                    }
                )
            }
            MonsterSpellsUsageForm(
                key = "$key-spellsUsage-$index",
                spellsUsage = spellcasting.usages,
                onSpellClick = onSpellClick,
                onChanged = { newSpellsUsage ->
                    onChanged(newSpellcastings.changeAt(index) { copy(usages = newSpellsUsage) })
                }
            )
        }
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellsUsageForm(
    key: String,
    spellsUsage: List<SpellUsage>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellUsage>) -> Unit = {}
) {
    val newSpellsUsage = spellsUsage.toMutableList()
    FormItems(
        key = key,
        items = newSpellsUsage,
        addText = { stringResource(R.string.monster_registration_add_spell_group) },
        removeText = { stringResource(R.string.monster_registration_remove_spell_group) },
        createNew = { SpellUsage.create() },
        onChanged = onChanged
    ) { index, spellUsage ->
        formItem(key = "$key-group-$index") {
            AppTextField(
                text = spellUsage.group,
                label = stringResource(R.string.monster_registration_spell_group),
                onValueChange = { newValue ->
                    onChanged(newSpellsUsage.changeAt(index) { copy(group = newValue) })
                }
            )
        }
        MonsterSpellsForm(
            key = "$key-spells-$index",
            spells = spellUsage.spells,
            onSpellClick = onSpellClick,
            onChanged = { newSpells ->
                onChanged(newSpellsUsage.changeAt(index) { copy(spells = newSpells) })
            }
        )
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellsForm(
    key: String,
    spells: List<SpellPreview>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellPreview>) -> Unit = {}
) = FormItems(
    key = key,
    items = spells.toMutableList(),
    addText = { stringResource(R.string.monster_registration_add_spell) },
    removeText = { stringResource(R.string.monster_registration_remove_spell) },
    createNew = { SpellPreview.create() },
    onChanged = onChanged
) { index, spell ->
    formItem(key = "$key-spell-name-$index") {
        ClickableField(
            text = spell.name,
            label = stringResource(R.string.monster_registration_spell_label),
            onClick = { onSpellClick(spell.index) },
        )
    }
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
