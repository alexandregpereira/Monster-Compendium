package br.alexandregpereira.hunter.monster.registration.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.dynamicFormulary.changeAt
import br.alexandregpereira.hunter.monster.registration.EmptyMonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationIntent
import br.alexandregpereira.hunter.monster.registration.MonsterState
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterAbilityDescriptionForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterAbilityScoresForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterActionsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterConditionsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterDamagesForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterHeaderForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterImageForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterProficiencyForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSavingThrowsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSpeedValuesForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterSpellcastingsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterStatsForm
import br.alexandregpereira.hunter.monster.registration.ui.form.MonsterStringValueForm
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.AppButtonSize
import br.alexandregpereira.hunter.ui.compose.ClearFocusWhenScrolling
import br.alexandregpereira.hunter.ui.compose.PopupContainer
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentItemTypeState
import br.alexandregpereira.hunter.ui.compose.tablecontent.TableContentPopup

@Composable
internal fun MonsterRegistrationForm(
    monster: MonsterState,
    lazyListState: LazyListState,
    isSaveButtonEnabled: Boolean,
    tableContent: Map<String, String>,
    isTableContentOpen: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) = PopupContainer(
    modifier = modifier,
    isOpened = isTableContentOpen,
    onPopupClosed = intent::onTableContentClose,
    content = {
        if (monster.index.isEmpty()) {
            return@PopupContainer
        }
        MonsterRegistrationForm(
            monster = monster,
            lazyListState = lazyListState,
            contentPadding = contentPadding,
            intent = intent,
        )

        AppButton(
            text = "Save",
            enabled = isSaveButtonEnabled,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = contentPadding.calculateBottomPadding() + 16.dp)
                .align(Alignment.BottomCenter),
            onClick = intent::onSaved
        )
    },
    popupContent = {
        val tableContentState = remember(tableContent) {
            tableContent.toTableContentItemStates()
        }

        TableContentPopup(
            icon = Icons.Filled.Menu,
            tableContent = tableContentState,
            tableContentSelectedIndex = -1,
            opened = isTableContentOpen,
            tableContentOpened = true,
            backHandlerEnabled = isTableContentOpen,
            onOpenButtonClicked = intent::onTableContentOpen,
            onCloseButtonClicked = intent::onTableContentClose,
            onTableContentClicked = { i ->
                intent.onTableContentClick(tableContentState[i].id)
            },
            onTableContentClosed = intent::onTableContentClose,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    top = contentPadding.calculateTopPadding() + 16.dp,
                    bottom = contentPadding.calculateBottomPadding()
                            + AppButtonSize.MEDIUM.height.dp
                            + 32.dp
                )
        )
    }
)

@Composable
private fun MonsterRegistrationForm(
    monster: MonsterState,
    lazyListState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(),
    intent: MonsterRegistrationIntent = EmptyMonsterRegistrationIntent(),
) {
    ClearFocusWhenScrolling(lazyListState)
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding() + 32.dp + AppButtonSize.MEDIUM.height.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
        ),
    ) {
        MonsterHeaderForm(
            keys = monster.keys,
            infoState = monster.info,
            onMonsterChanged = { intent.onMonsterChanged(monster.copy(info = it)) },
        )
        MonsterImageForm(
            keys = monster.keys,
            infoState = monster.info,
            onMonsterChanged = { intent.onMonsterChanged(monster.copy(info = it)) },
        )
        MonsterStatsForm(
            keys = monster.keys,
            stats = monster.stats,
            onChanged = { intent.onMonsterChanged(monster.copy(stats = it)) }
        )
        MonsterSpeedValuesForm(
            keys = monster.keys,
            speedValueStates = monster.speedValues,
            onMonsterChanged = { intent.onMonsterChanged(monster.copy(speedValues = it)) },
        )
        MonsterAbilityScoresForm(
            keys = monster.keys,
            abilityScores = monster.abilityScores,
            onChanged = { intent.onMonsterChanged(monster.copy(abilityScores = it)) }
        )
        MonsterSavingThrowsForm(
            keys = monster.keys,
            savingThrows = monster.savingThrows,
            onChanged = { intent.onMonsterChanged(monster.copy(savingThrows = it)) },
        )
        MonsterProficiencyForm(
            keys = monster.keys,
            title = { strings.skills },
            proficiencies = monster.skills,
            onChanged = { intent.onMonsterChanged(monster.copy(skills = it)) },
        )
        MonsterDamagesForm(
            keys = monster.keys,
            title = { strings.damageVulnerabilities },
            damages = monster.damageVulnerabilities,
            onChanged = { intent.onMonsterChanged(monster.copy(damageVulnerabilities = it)) },
        )
        MonsterDamagesForm(
            keys = monster.keys,
            title = { strings.damageResistances },
            damages = monster.damageResistances,
            onChanged = { intent.onMonsterChanged(monster.copy(damageResistances = it)) },
        )
        MonsterDamagesForm(
            keys = monster.keys,
            title = { strings.damageImmunities },
            damages = monster.damageImmunities,
            onChanged = { intent.onMonsterChanged(monster.copy(damageImmunities = it)) },
        )
        MonsterConditionsForm(
            keys = monster.keys,
            title = { strings.conditionImmunities },
            conditions = monster.conditionImmunities,
            onChanged = { intent.onMonsterChanged(monster.copy(conditionImmunities = it)) },
        )
        MonsterStringValueForm(
            keys = monster.keys,
            title = { strings.senses },
            value = monster.senses.joinToString(", "),
            onChanged = { intent.onMonsterChanged(monster.copy(senses = it.split(", "))) },
        )
        MonsterStringValueForm(
            keys = monster.keys,
            title = { strings.languages },
            value = monster.languages,
            onChanged = { intent.onMonsterChanged(monster.copy(languages = it)) },
        )
        MonsterAbilityDescriptionForm(
            keys = monster.keys,
            title = { strings.specialAbilities },
            abilityDescriptions = monster.specialAbilities,
            addText = { strings.addSpecialAbility },
            removeText = { strings.removeSpecialAbility },
            onChanged = { intent.onMonsterChanged(monster.copy(specialAbilities = it)) },
        )
        MonsterActionsForm(
            keys = monster.keys,
            title = { strings.actions },
            actions = monster.actions,
            onChanged = { intent.onMonsterChanged(monster.copy(actions = it)) },
        )
        MonsterAbilityDescriptionForm(
            keys = monster.keys,
            title = { strings.reactions },
            abilityDescriptions = monster.reactions,
            addText = { strings.addReaction },
            removeText = { strings.removeReaction },
            onChanged = { intent.onMonsterChanged(monster.copy(reactions = it)) },
        )
        MonsterActionsForm(
            keys = monster.keys,
            title = { strings.legendaryActions },
            actions = monster.legendaryActions,
            onChanged = { intent.onMonsterChanged(monster.copy(legendaryActions = it)) },
        )
        MonsterSpellcastingsForm(
            keys = monster.keys,
            spellcastings = monster.spellcastings,
            onChanged = { intent.onMonsterChanged(monster.copy(spellcastings = it)) },
            onSpellClick = intent::onSpellClick,
        )
    }
}

private fun Map<String, String>.toTableContentItemStates(): List<TableContentItemState> {
    return map { (key, value) ->
        TableContentItemState(
            id = key,
            text = value,
            type = TableContentItemTypeState.BODY,
        )
    }
}

internal fun <T> MutableList<T>.changeAt(
    index: Int,
    copy: T.() -> T
): List<T> = changeAt(index, copy)
