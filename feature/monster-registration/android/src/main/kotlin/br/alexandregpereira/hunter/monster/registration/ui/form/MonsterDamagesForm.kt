package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.monster.registration.R
import br.alexandregpereira.hunter.monster.registration.ui.changeAt
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.PickerField

@Suppress("FunctionName")
internal fun LazyListScope.MonsterDamagesForm(
    key: String,
    title: @Composable () -> String,
    damages: List<Damage>,
    onChanged: (List<Damage>) -> Unit = {}
) {
    val newDamages = damages.toMutableList()
    val currentDamageTypes = damages.map { it.type.toTypeState() }.toSet()
    val damageTypes = DamageType.entries.map { it.toTypeState() }.filterNot {
        currentDamageTypes.contains(it)
    }
    FormLazy(
        key = key,
        title = title,
    ) {
        FormItems(
            key = key,
            items = newDamages,
            createNew = { Damage.create() },
            onChanged = onChanged
        ) { i, damage ->
            formItem(key = "$key-name-${damage.index}") {
                val damageTypeOptions = damageTypes.map { stringResource(it.stringRes) }
                if (i != 0 && damage.type == DamageType.OTHER) Spacer(modifier = Modifier.height(8.dp))

                PickerField(
                    value = stringResource(damage.type.toTypeState().stringRes),
                    label = stringResource(R.string.monster_registration_damage_type),
                    options = damageTypeOptions,
                    onValueChange = { optionIndex ->
                        onChanged(
                            newDamages.changeAt(i) {
                                copy(
                                    type = DamageType.valueOf(damageTypes[optionIndex].name),
                                    name = damageTypeOptions[optionIndex].takeIf {
                                        damageTypes[optionIndex] != DamageTypeState.OTHER
                                    }.orEmpty()
                                )
                            }
                        )
                    }
                )
            }

            formItem(key = "$key-name-other-${damage.index}") {
                if (damage.type == DamageType.OTHER) {
                    AppTextField(
                        text = damage.name,
                        label = stringResource(R.string.monster_registration_damage_type_other),
                        onValueChange = { newValue ->
                            onChanged(newDamages.changeAt(i) { copy(name = newValue) })
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

internal fun DamageType.toTypeState(): DamageTypeState {
    return when (this) {
        DamageType.ACID -> DamageTypeState.ACID
        DamageType.BLUDGEONING -> DamageTypeState.BLUDGEONING
        DamageType.COLD -> DamageTypeState.COLD
        DamageType.FIRE -> DamageTypeState.FIRE
        DamageType.LIGHTNING -> DamageTypeState.LIGHTNING
        DamageType.NECROTIC -> DamageTypeState.NECROTIC
        DamageType.PIERCING -> DamageTypeState.PIERCING
        DamageType.POISON -> DamageTypeState.POISON
        DamageType.PSYCHIC -> DamageTypeState.PSYCHIC
        DamageType.RADIANT -> DamageTypeState.RADIANT
        DamageType.SLASHING -> DamageTypeState.SLASHING
        DamageType.THUNDER -> DamageTypeState.THUNDER
        DamageType.OTHER -> DamageTypeState.OTHER
    }
}

internal enum class DamageTypeState(val stringRes: Int) {
    ACID(R.string.monster_registration_damage_type_acid),
    BLUDGEONING(R.string.monster_registration_damage_type_bludgeoning),
    COLD(R.string.monster_registration_damage_type_cold),
    FIRE(R.string.monster_registration_damage_type_fire),
    LIGHTNING(R.string.monster_registration_damage_type_lightning),
    NECROTIC(R.string.monster_registration_damage_type_necrotic),
    PIERCING(R.string.monster_registration_damage_type_piercing),
    POISON(R.string.monster_registration_damage_type_poison),
    PSYCHIC(R.string.monster_registration_damage_type_psychic),
    RADIANT(R.string.monster_registration_damage_type_radiant),
    SLASHING(R.string.monster_registration_damage_type_slashing),
    THUNDER(R.string.monster_registration_damage_type_thunder),
    OTHER(R.string.monster_registration_damage_type_other),
}

@Composable
internal fun DamageTypeState.getStringName(): String {
    return stringResource(stringRes)
}
