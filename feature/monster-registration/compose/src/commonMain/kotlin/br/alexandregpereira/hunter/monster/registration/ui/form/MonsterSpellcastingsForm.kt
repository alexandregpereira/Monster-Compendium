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
import br.alexandregpereira.hunter.ui.compose.form.FormItems
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSpellcastingsForm(
    keys: Iterator<String>,
    spellcastings: List<SpellcastingState>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellcastingState>) -> Unit = {}
) {
    FormLazy(titleKey = keys.next(), title = { strings.spells }) {
        val newSpellcastings = spellcastings.toMutableList()

        FormItems(
            keys = keys,
            items = newSpellcastings,
            addText = { strings.addSpellcastingType },
            removeText = { strings.removeSpellcastingType },
            createNew = { SpellcastingState() },
            onChanged = onChanged
        ) { index, spellcasting ->
            formItem(key = keys.next()) {
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
            formItem(key = keys.next()) {
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
                keys = keys,
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
    keys: Iterator<String>,
    spellsByGroup: List<SpellsByGroupState>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellsByGroupState>) -> Unit = {}
) {
    val newSpellsByGroupState = spellsByGroup.toMutableList()
    FormItems(
        keys = keys,
        items = newSpellsByGroupState,
        addText = { strings.addSpellGroup },
        removeText = { strings.removeSpellGroup },
        createNew = { SpellsByGroupState() },
        onChanged = onChanged
    ) { index, spellByGroup ->
        formItem(key = keys.next()) {
            AppTextField(
                text = spellByGroup.group,
                label = strings.spellGroup,
                onValueChange = { newValue ->
                    onChanged(newSpellsByGroupState.changeAt(index) { copy(group = newValue) })
                }
            )
        }
        MonsterSpellsForm(
            keys = keys,
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
    keys: Iterator<String>,
    spells: List<SpellPreviewState>,
    onSpellClick: (String) -> Unit = {},
    onChanged: (List<SpellPreviewState>) -> Unit = {}
) = FormItems(
    keys = keys,
    items = spells.toMutableList(),
    addText = { strings.addSpell },
    removeText = { strings.removeSpell },
    createNew = { SpellPreviewState() },
    onChanged = onChanged
) { _, spell ->
    formItem(key = keys.next()) {
        ClickableField(
            text = spell.name,
            label = strings.spellLabel,
            onClick = { onSpellClick(spell.index) },
        )
    }
}
