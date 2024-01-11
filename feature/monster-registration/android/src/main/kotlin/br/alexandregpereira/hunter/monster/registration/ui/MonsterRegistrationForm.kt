package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.R
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
    monster: Monster,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) = Box(
    modifier = modifier.fillMaxSize()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.Top),
        modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding() + 32.dp + AppButtonSize.MEDIUM.height.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
        ),
    ) {
        item(key = "monster") {
            MonsterHeaderForm(
                monster = monster,
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "stats") {
            MonsterStatsForm(
                monster = monster,
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "speed") {
            MonsterSpeedValuesForm(
                monster = monster,
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "abilityScores") {
            MonsterAbilityScoresForm(
                abilityScores = monster.abilityScores,
                onChanged = { intent.onMonsterChanged(monster.copy(abilityScores = it)) }
            )
        }
        item(key = "savingThrows") {
            MonsterSavingThrowsForm(
                savingThrows = monster.savingThrows,
                onChanged = { intent.onMonsterChanged(monster.copy(savingThrows = it)) },
            )
        }
        item(key = "skills") {
            MonsterProficiencyForm(
                title = "Skills",
                proficiencies = monster.skills,
                onChanged = { intent.onMonsterChanged(monster.copy(skills = it)) },
            )
        }
        item(key = "damageVulnerabilities") {
            MonsterDamagesForm(
                title = "Damage Vulnerabilities",
                damages = monster.damageVulnerabilities,
                onChanged = { intent.onMonsterChanged(monster.copy(damageVulnerabilities = it)) },
            )
        }
        item(key = "damageResistances") {
            MonsterDamagesForm(
                title = "Damage Resistances",
                damages = monster.damageResistances,
                onChanged = { intent.onMonsterChanged(monster.copy(damageResistances = it)) },
            )
        }
        item(key = "damageImmunities") {
            MonsterDamagesForm(
                title = "Damage Immunities",
                damages = monster.damageImmunities,
                onChanged = { intent.onMonsterChanged(monster.copy(damageImmunities = it)) },
            )
        }
        item(key = "conditionImmunities") {
            MonsterConditionsForm(
                title = "Condition Immunities",
                conditions = monster.conditionImmunities,
                onChanged = { intent.onMonsterChanged(monster.copy(conditionImmunities = it)) },
            )
        }
        item(key = "senses") {
            MonsterStringValueForm(
                title = "Senses",
                value = monster.senses.joinToString(", "),
                onChanged = { intent.onMonsterChanged(monster.copy(senses = it.split(", "))) },
            )
        }
        item(key = "languages") {
            MonsterStringValueForm(
                title = "Languages",
                value = monster.languages,
                onChanged = { intent.onMonsterChanged(monster.copy(languages = it)) },
            )
        }
        item(key = "specialAbilities") {
            MonsterAbilityDescriptionForm(
                title = "Special Abilities",
                abilityDescriptions = monster.specialAbilities,
                onChanged = { intent.onMonsterChanged(monster.copy(specialAbilities = it)) },
            )
        }
        item(key = "actions") {
            MonsterActionsForm(
                title = stringResource(R.string.monster_registration_actions),
                actions = monster.actions,
                onChanged = { intent.onMonsterChanged(monster.copy(actions = it)) },
            )
        }
        item(key = "reactions") {
            MonsterAbilityDescriptionForm(
                title = "Reactions",
                abilityDescriptions = monster.reactions,
                onChanged = { intent.onMonsterChanged(monster.copy(reactions = it)) },
            )
        }
        item(key = "legendaryActions") {
            MonsterActionsForm(
                title = "Legendary Actions",
                actions = monster.legendaryActions,
                onChanged = { intent.onMonsterChanged(monster.copy(legendaryActions = it)) },
            )
        }
        item(key = "spells") {
            MonsterSpellcastingsForm(
                spellcastings = monster.spellcastings,
            )
        }
    }

    AppButton(
        text = "Save",
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
