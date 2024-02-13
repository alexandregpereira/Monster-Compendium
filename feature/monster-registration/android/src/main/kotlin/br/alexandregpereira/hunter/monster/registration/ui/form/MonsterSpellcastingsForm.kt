package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import br.alexandregpereira.hunter.monster.registration.SpellPreviewState
import br.alexandregpereira.hunter.monster.registration.SpellcastingState
import br.alexandregpereira.hunter.monster.registration.SpellsByGroupState
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.monster.registration.ui.strings
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.ClickableField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellcastingsForm(
    spellcastings: List<SpellcastingState>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellcastingState>) -> Unit = {}
) {
    val key = "spellcastings"
    FormLazy(key, { strings.spells }) {
        val newSpellcastings = spellcastings.toMutableList()

        FormItems(
            key = key,
            items = newSpellcastings,
            addText = { strings.addSpellcastingType },
            removeText = { strings.removeSpellcastingType },
            createNew = { SpellcastingState() },
            onChanged = onChanged
        ) { index, spellcasting ->
            formItem(key = "$key-type-${spellcasting.key}") {
                PickerField(
                    value = spellcasting.name,
                    label = strings.spellcastingTypeLabel,
                    options = spellcasting.options,
                    onValueChange = { optionIndex ->
                        onChanged(newSpellcastings.changeAt(index) {
                            copy(selectedIndex = optionIndex)
                        })
                    }
                )
            }
            formItem(key = "$key-description-${spellcasting.key}") {
                AppTextField(
                    text = spellcasting.description,
                    label = strings.description,
                    multiline = true,
                    onValueChange = { newValue ->
                        onChanged(newSpellcastings.changeAt(index) { copy(description = newValue) })
                    }
                )
            }
            MonsterSpellsUsageForm(
                key = "$key-spellsUsage-${spellcasting.key}",
                spellsByGroup = spellcasting.spellsByGroup,
                onSpellClick = onSpellClick,
                onChanged = { newSpellsByGroup ->
                    onChanged(newSpellcastings.changeAt(index) {
                        copy(spellsByGroup = newSpellsByGroup)
                    })
                }
            )
        }
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellsUsageForm(
    key: String,
    spellsByGroup: List<SpellsByGroupState>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellsByGroupState>) -> Unit = {}
) {
    val newSpellsByGroupState = spellsByGroup.toMutableList()
    FormItems(
        key = key,
        items = newSpellsByGroupState,
        addText = { strings.addSpellGroup },
        removeText = { strings.removeSpellGroup },
        createNew = { SpellsByGroupState() },
        onChanged = onChanged
    ) { index, spellByGroup ->
        formItem(key = "$key-group-${spellByGroup.key}") {
            AppTextField(
                text = spellByGroup.group,
                label = strings.spellGroup,
                onValueChange = { newValue ->
                    onChanged(newSpellsByGroupState.changeAt(index) { copy(group = newValue) })
                }
            )
        }
        MonsterSpellsForm(
            key = "$key-spells-${spellByGroup.key}",
            spells = spellByGroup.spells,
            onSpellClick = onSpellClick,
            onChanged = { newSpells ->
                onChanged(newSpellsByGroupState.changeAt(index) { copy(spells = newSpells) })
            }
        )
    }
}

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellsForm(
    key: String,
    spells: List<SpellPreviewState>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellPreviewState>) -> Unit = {}
) = FormItems(
    key = key,
    items = spells.toMutableList(),
    addText = { strings.addSpell },
    removeText = { strings.removeSpell },
    createNew = { SpellPreviewState() },
    onChanged = onChanged
) { index, spell ->
    formItem(key = "$key-spell-name-${spell.index}") {
        ClickableField(
            text = spell.name,
            label = strings.spellLabel,
            onClick = { onSpellClick(spell.index) },
        )
    }
}
