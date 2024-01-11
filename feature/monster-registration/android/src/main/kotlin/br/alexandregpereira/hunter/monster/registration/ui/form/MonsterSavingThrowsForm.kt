package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppKeyboardType
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.PickerField

@Composable
internal fun MonsterSavingThrowsForm(
    savingThrows: List<SavingThrow>,
    modifier: Modifier = Modifier,
    onChanged: (List<SavingThrow>) -> Unit = {}
) {
    val types = savingThrows.map { it.type.name }
    val options = AbilityScoreType.entries.filterNot { types.contains(it.name) }
    val optionsString = AbilityScoreTypeState.entries.filterNot { types.contains(it.name) }
        .map { it.getStringResource() }
    val mutableSavingThrows = savingThrows.toMutableList()

    Form(
        modifier = modifier,
        title = stringResource(R.string.monster_registration_saving_throws),
    ) {
        savingThrows.forEachIndexed { i, savingThrow ->
            val typeName = savingThrow.type.toState().getStringResource()

            PickerField(
                value = typeName,
                label = "Name",
                options = optionsString,
                onValueChange = { optionIndex ->
                    onChanged(
                        mutableSavingThrows.changeAt(i) {
                            copy(type = options[optionIndex])
                        }
                    )
                }
            )

            AppTextField(
                text = savingThrow.modifier.toString(),
                label = typeName,
                keyboardType = AppKeyboardType.NUMBER,
                onValueChange = { newValue ->
                    onChanged(
                        mutableSavingThrows.changeAt(i) {
                            copy(modifier = newValue.toIntOrNull() ?: 0)
                        }
                    )
                }
            )
        }

        if (savingThrows.size < AbilityScoreType.entries.size) {
            AddButton()
        }
    }
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
