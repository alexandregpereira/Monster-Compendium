package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterAbilityDescriptionForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterAbilityScoresForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterForm1
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterProficiencyForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSpeedValuesForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterStatsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterStringValueForm
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize

@OptIn(ExperimentalFoundationApi::class)
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
        modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding() + 32.dp + AppButtonSize.MEDIUM.height.dp),
        contentPadding = contentPadding,
    ) {
        item(key = "monster") {
            MonsterForm1(
                monster = monster,
                modifier = Modifier.padding(16.dp),
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "stats") {
            MonsterStatsForm(
                monster = monster,
                modifier = Modifier.padding(16.dp),
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "speed") {
            MonsterSpeedValuesForm(
                monster = monster,
                modifier = Modifier.padding(16.dp),
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "abilityScores") {
            MonsterAbilityScoresForm(
                monster = monster,
                modifier = Modifier.padding(16.dp),
                onMonsterChanged = intent::onMonsterChanged,
            )
        }
        item(key = "savingThrows") {
            MonsterProficiencyForm(
                title = "Saving Throws",
                proficiencies = monster.savingThrows,
                modifier = Modifier.padding(16.dp),
                onChanged = { intent.onMonsterChanged(monster.copy(savingThrows = it)) },
            )
        }
        item(key = "skills") {
            MonsterProficiencyForm(
                title = "Skills",
                proficiencies = monster.skills,
                modifier = Modifier.padding(16.dp),
                onChanged = { intent.onMonsterChanged(monster.copy(skills = it)) },
            )
        }
        item(key = "senses") {
            MonsterStringValueForm(
                title = "Senses",
                value = monster.senses.joinToString(", "),
                modifier = Modifier.padding(16.dp),
                onChanged = { intent.onMonsterChanged(monster.copy(senses = it.split(", "))) },
            )
        }
        item(key = "languages") {
            MonsterStringValueForm(
                title = "Languages",
                value = monster.languages,
                modifier = Modifier.padding(16.dp),
                onChanged = { intent.onMonsterChanged(monster.copy(languages = it)) },
            )
        }
        item(key = "specialAbilities") {
            MonsterAbilityDescriptionForm(
                title = "Special Abilities",
                abilityDescriptions = monster.specialAbilities,
                modifier = Modifier.padding(16.dp),
                onChanged = { intent.onMonsterChanged(monster.copy(specialAbilities = it)) },
            )
        }
        item(key = "reactions") {
            MonsterAbilityDescriptionForm(
                title = "Reactions",
                abilityDescriptions = monster.reactions,
                modifier = Modifier.padding(16.dp),
                onChanged = { intent.onMonsterChanged(monster.copy(reactions = it)) },
            )
        }
    }

    AppButton(
        text = "Save",
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(bottom = contentPadding.calculateBottomPadding() + 16.dp)
            .align(Alignment.BottomCenter),
        onClick = intent::onSaved
    )
}
