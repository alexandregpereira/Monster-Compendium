package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterSavingThrowsForm(
    savingThrows: List<SavingThrow>,
    onChanged: (List<SavingThrow>) -> Unit = {}
) {
    val types = savingThrows.map { it.type.name }
    val options = AbilityScoreType.entries.filterNot { types.contains(it.name) }
    val mutableSavingThrows = savingThrows.toMutableList()
    val key = "saving-throws"

    FormLazy(
        key = key,
        title = { stringResource(R.string.monster_registration_saving_throws) },
    ) {

        FormItems(
            key = key,
            items = mutableSavingThrows,
            createNew = { SavingThrow.create() },
            onChanged = onChanged
        ) { i, savingThrow ->
            formItem(key = "$key-name-$i") {
                val typeName = savingThrow.type.toState().getStringResource()
                val optionsString = AbilityScoreTypeState.entries.filterNot { types.contains(it.name) }
                    .map { it.getStringResource() }
                PickerField(
                    value = typeName,
                    label = stringResource(R.string.monster_registration_name),
                    options = optionsString,
                    onValueChange = { optionIndex ->
                        onChanged(
                            mutableSavingThrows.changeAt(i) {
                                copy(type = options[optionIndex])
                            }
                        )
                    }
                )
            }

            formItem(key = "$key-modifier-$i") {
                val typeName = savingThrow.type.toState().getStringResource()
                AppTextField(
                    value = savingThrow.modifier,
                    label = typeName,
                    onValueChange = { newValue ->
                        onChanged(
                            mutableSavingThrows.changeAt(i) {
                                copy(modifier = newValue)
                            }
                        )
                    }
                )
            }
        }
    }
}

internal fun AbilityScoreType.toState(): AbilityScoreTypeState {
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
internal fun AbilityScoreTypeState.getStringResource(): String = stringResource(stringRes)

internal enum class AbilityScoreTypeState(val stringRes: Int) {
    STRENGTH(R.string.monster_registration_strength),
    DEXTERITY(R.string.monster_registration_dexterity),
    CONSTITUTION(R.string.monster_registration_constitution),
    INTELLIGENCE(R.string.monster_registration_intelligence),
    WISDOM(R.string.monster_registration_wisdom),
    CHARISMA(R.string.monster_registration_charisma)
}
