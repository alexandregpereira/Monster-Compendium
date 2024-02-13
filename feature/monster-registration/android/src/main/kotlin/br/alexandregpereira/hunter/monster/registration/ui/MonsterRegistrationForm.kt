package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterState
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterAbilityDescriptionForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterAbilityScoresForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterActionsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterConditionsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterDamagesForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterHeaderForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterProficiencyForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSavingThrowsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSpeedValuesForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSpellcastingsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterStatsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterStringValueForm
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize

@Composable
internal fun MonsterRegistrationForm(
    monster: MonsterState,
    isSaveButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) = Box(
    modifier = modifier.fillMaxSize()
) {
    LazyColumn(
        modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding() + 32.dp + AppButtonSize.MEDIUM.height.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
        ),
    ) {
        MonsterHeaderForm(
            infoState = monster.info,
            onMonsterChanged = { intent.onMonsterChanged(monster.copy(info = it)) },
        )
        MonsterStatsForm(
            stats = monster.stats,
            onChanged = { intent.onMonsterChanged(monster.copy(stats = it)) }
        )
        MonsterSpeedValuesForm(
            speedValueStates = monster.speedValues,
            onMonsterChanged = { intent.onMonsterChanged(monster.copy(speedValues = it)) },
        )
        MonsterAbilityScoresForm(
            abilityScores = monster.abilityScores,
            onChanged = { intent.onMonsterChanged(monster.copy(abilityScores = it)) }
        )
        MonsterSavingThrowsForm(
            savingThrows = monster.savingThrows,
            onChanged = { intent.onMonsterChanged(monster.copy(savingThrows = it)) },
        )
        MonsterProficiencyForm(
            title = { strings.skills },
            proficiencies = monster.skills,
            onChanged = { intent.onMonsterChanged(monster.copy(skills = it)) },
        )
        MonsterDamagesForm(
            key = "damageVulnerabilities",
            title = { strings.damageVulnerabilities },
            damages = monster.damageVulnerabilities,
            onChanged = { intent.onMonsterChanged(monster.copy(damageVulnerabilities = it)) },
        )
        MonsterDamagesForm(
            key = "damageResistances",
            title = { strings.damageResistances },
            damages = monster.damageResistances,
            onChanged = { intent.onMonsterChanged(monster.copy(damageResistances = it)) },
        )
        MonsterDamagesForm(
            key = "damageImmunities",
            title = { strings.damageImmunities },
            damages = monster.damageImmunities,
            onChanged = { intent.onMonsterChanged(monster.copy(damageImmunities = it)) },
        )
        MonsterConditionsForm(
            title = { strings.conditionImmunities },
            conditions = monster.conditionImmunities,
            onChanged = { intent.onMonsterChanged(monster.copy(conditionImmunities = it)) },
        )
        MonsterStringValueForm(
            key = "senses",
            title = { strings.senses },
            value = monster.senses.joinToString(", "),
            onChanged = { intent.onMonsterChanged(monster.copy(senses = it.split(", "))) },
        )
        MonsterStringValueForm(
            key = "languages",
            title = { strings.languages },
            value = monster.languages,
            onChanged = { intent.onMonsterChanged(monster.copy(languages = it)) },
        )
        MonsterAbilityDescriptionForm(
            key = "specialAbilities",
            title = { strings.specialAbilities },
            abilityDescriptions = monster.specialAbilities,
            addText = { strings.addSpecialAbility },
            removeText = { strings.removeSpecialAbility },
            onChanged = { intent.onMonsterChanged(monster.copy(specialAbilities = it)) },
        )
        MonsterActionsForm(
            key = "actions",
            title = { strings.actions },
            actions = monster.actions,
            onChanged = { intent.onMonsterChanged(monster.copy(actions = it)) },
        )
        MonsterAbilityDescriptionForm(
            key = "reactions",
            title = { strings.reactions },
            abilityDescriptions = monster.reactions,
            addText = { strings.addReaction },
            removeText = { strings.removeReaction },
            onChanged = { intent.onMonsterChanged(monster.copy(reactions = it)) },
        )
        MonsterActionsForm(
            key = "legendaryActions",
            title = { strings.legendaryActions },
            actions = monster.legendaryActions,
            onChanged = { intent.onMonsterChanged(monster.copy(legendaryActions = it)) },
        )
        MonsterSpellcastingsForm(
            spellcastings = monster.spellcastings,
            onChanged = { intent.onMonsterChanged(monster.copy(spellcastings = it)) },
            onSpellClick = intent::onSpellClick,
        )
    }

    AppButton(
        text = "Save",
        enabled = isSaveButtonEnabled,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = contentPadding.calculateBottomPadding() + 16.dp)
            .align(Alignment.BottomCenter),
        onClick = intent::onSaved
    )
}

internal fun <T> MutableList<T>.changeAt(
    index: Int,
    copy: T.() -> T
): List<T> {
    return also {
        it[index] = it[index].copy()
    }
}

internal fun <T> MutableList<T>.alsoAdd(index: Int, value: T): List<T> {
    return also { it.add(index, value) }
}

internal fun <T> MutableList<T>.alsoRemoveAt(index: Int): List<T> {
    return also { it.removeAt(index) }
}
