package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterSavingThrowsForm(
    savingThrows: List<SavingThrow>,
    modifier: Modifier = Modifier,
    onChanged: (List<SavingThrow>) -> Unit = {}
) {
    val types = savingThrows.map { it.type.name }
    val options = AbilityScoreTypeState.entries.filterNot { types.contains(it.name) }
        .map { it.getStringResource() }
    val allOptions = AbilityScoreTypeState.entries.map { it.getStringResource() }

    Form(
        modifier = modifier,
        title = stringResource(R.string.monster_registration_saving_throws),
        formFields = savingThrows.map { savingThrow ->
            val typeName = savingThrow.type.toState().getStringResource()
            listOf(
                FormField.Picker(
                    key = savingThrow.index + "-type",
                    label = "Name",
                    value = typeName,
                    options = options,
                ),
                FormField.Number(
                    key = savingThrow.index + "-modifier",
                    label = typeName,
                    value = savingThrow.modifier,
                )
            )
        }.reduceOrNull { acc, texts -> acc + texts } ?: emptyList(),
        onFormChanged = { field ->
            val newSavingThrows = savingThrows.toMutableList()
            val index = newSavingThrows.indexOfFirst { field.key.startsWith(it.index) }
            if (index != -1) {
                when (field.key) {
                    "${newSavingThrows[index].index}-type" -> {
                        newSavingThrows[index] = newSavingThrows[index].copy(
                            type = AbilityScoreType.entries[allOptions.indexOf(field.stringValue)]
                        )
                    }

                    "${newSavingThrows[index].index}-modifier" -> {
                        newSavingThrows[index] = newSavingThrows[index].copy(
                            modifier = field.intValue
                        )
                    }
                }
                onChanged(newSavingThrows)
            }
        },
    )
}

private fun AbilityScoreType.toState(): AbilityScoreTypeState {
    return when (this) {
        AbilityScoreType.STRENGTH -> AbilityScoreTypeState.STRENGTH
        AbilityScoreType.DEXTERITY -> AbilityScoreTypeState.DEXTERITY
        AbilityScoreType.CONSTITUTION -> AbilityScoreTypeState.CONSTITUTION
        AbilityScoreType.INTELLIGENCE -> AbilityScoreTypeState.INTELLIGENCE
        AbilityScoreType.WISDOM -> AbilityScoreTypeState.WISDOM
        AbilityScoreType.CHARISMA -> AbilityScoreTypeState.CHARISMA
    }
}

@Composable
private fun AbilityScoreTypeState.getStringResource(): String = stringResource(stringRes)

private enum class AbilityScoreTypeState(val stringRes: Int) {
    STRENGTH(R.string.monster_registration_strength),
    DEXTERITY(R.string.monster_registration_dexterity),
    CONSTITUTION(R.string.monster_registration_constitution),
    INTELLIGENCE(R.string.monster_registration_intelligence),
    WISDOM(R.string.monster_registration_wisdom),
    CHARISMA(R.string.monster_registration_charisma)
}
