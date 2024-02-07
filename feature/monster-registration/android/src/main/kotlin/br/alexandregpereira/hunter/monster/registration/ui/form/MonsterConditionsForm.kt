package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterConditionsForm(
    title: @Composable () -> String,
    conditions: List<Condition>,
    onChanged: (List<Condition>) -> Unit = {}
) {
    val newConditions = conditions.toMutableList()
    val currentConditionTypes = conditions.map { it.type.toTypeState() }.toSet()
    val conditionTypes = ConditionType.entries.map { it.toTypeState() }.filterNot {
        currentConditionTypes.contains(it)
    }
    val key = "conditionImmunities"
    FormLazy(
        key = key,
        title = title,
    ) {
        FormItems(
            key = key,
            items = newConditions,
            createNew = { Condition.create() },
            onChanged = onChanged
        ) { i, condition ->
            formItem(key = "$key-name-${condition.index}") {
                val conditionTypeOptions = conditionTypes.map { stringResource(it.stringRes) }
                PickerField(
                    value = stringResource(condition.type.toTypeState().stringRes),
                    label = stringResource(R.string.monster_registration_condition_type),
                    options = conditionTypeOptions,
                    onValueChange = { optionIndex ->
                        onChanged(
                            newConditions.changeAt(i) {
                                copy(
                                    type = ConditionType.valueOf(conditionTypes[optionIndex].name),
                                    name = conditionTypeOptions[optionIndex]
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}

private fun ConditionType.toTypeState(): ConditionTypeState {
    return when (this) {
        ConditionType.BLINDED -> ConditionTypeState.BLINDED
        ConditionType.CHARMED -> ConditionTypeState.CHARMED
        ConditionType.DEAFENED -> ConditionTypeState.DEAFENED
        ConditionType.EXHAUSTION -> ConditionTypeState.EXHAUSTION
        ConditionType.FRIGHTENED -> ConditionTypeState.FRIGHTENED
        ConditionType.GRAPPLED -> ConditionTypeState.GRAPPLED
        ConditionType.PARALYZED -> ConditionTypeState.PARALYZED
        ConditionType.PETRIFIED -> ConditionTypeState.PETRIFIED
        ConditionType.POISONED -> ConditionTypeState.POISONED
        ConditionType.PRONE -> ConditionTypeState.PRONE
        ConditionType.RESTRAINED -> ConditionTypeState.RESTRAINED
        ConditionType.STUNNED -> ConditionTypeState.STUNNED
        ConditionType.UNCONSCIOUS -> ConditionTypeState.UNCONSCIOUS
    }
}

private enum class ConditionTypeState(@StringRes val stringRes: Int) {
    BLINDED(R.string.monster_registration_condition_type_blinded),
    CHARMED(R.string.monster_registration_condition_type_charmed),
    DEAFENED(R.string.monster_registration_condition_type_deafened),
    EXHAUSTION(R.string.monster_registration_condition_type_exhaustion),
    FRIGHTENED(R.string.monster_registration_condition_type_frightened),
    GRAPPLED(R.string.monster_registration_condition_type_grappled),
    PARALYZED(R.string.monster_registration_condition_type_paralyzed),
    PETRIFIED(R.string.monster_registration_condition_type_petrified),
    POISONED(R.string.monster_registration_condition_type_poisoned),
    PRONE(R.string.monster_registration_condition_type_prone),
    RESTRAINED(R.string.monster_registration_condition_type_restrained),
    STUNNED(R.string.monster_registration_condition_type_stunned),
    UNCONSCIOUS(R.string.monster_registration_condition_type_unconscious),
}
