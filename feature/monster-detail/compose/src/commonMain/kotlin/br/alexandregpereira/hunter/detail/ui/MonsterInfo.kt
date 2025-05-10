/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.detail.MonsterState
import br.alexandregpereira.hunter.monster.detail.ProficiencyState
import br.alexandregpereira.hunter.ui.transition.AlphaTransition

internal fun LazyListScope.monsterInfo(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    getItemsKeys: () -> List<Any> = { emptyList() },
    onSpellClicked: (String) -> Unit = {},
    onLoreClick: (String) -> Unit = {}
) {
    monsterInfoPart1(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
        onLoreClick = onLoreClick
    )

    monsterInfoPart2(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart3(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart4(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    monsterInfoPart5(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
    )

    spellBlock(
        monsters = monsters,
        pagerState = pagerState,
        getItemsKeys = getItemsKeys,
        onSpellClicked = onSpellClicked
    )

    item(key = "space") {
        Spacer(
            modifier = Modifier
                .height(contentPadding.calculateBottomPadding())
                .fillMaxWidth()
                .animateItem()
        )
    }
}

private fun LazyListScope.monsterInfoPart1(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
    onLoreClick: (String) -> Unit = {}
) {
    item(key = "lore") {
        MonsterRequireSectionAlphaTransition(
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
            showDivider = false
        ) { monster ->
            monster.lore.takeIf { it.isNotBlank() }?.let {
                LoreBlock(text = it) { onLoreClick(monster.index) }
            }
        }
    }
    item(key = "stats") {
        MonsterRequireSectionAlphaTransition(
            monsters,
            pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            StatsBlock(stats = monster.stats)
        }
    }
    item(key = "speed") {
        MonsterOptionalSectionAlphaTransition(
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
            valueToValidate = { it.speed.values.isNotEmpty() },
        ) { monster ->
            SpeedBlock(speed = monster.speed)
        }
    }
    item(key = "abilityScores") {
        ListOptionalSectionAlphaTransition(
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
            valueToValidate = { it.abilityScores }
        ) { abilityScores ->
            AbilityScoreBlock(abilityScores = abilityScores)
        }
    }
}

private fun LazyListScope.monsterInfoPart2(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "savingThrows") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.savingThrows },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ProficiencyBlock(
                proficiencies = it.map { saving ->
                    ProficiencyState(
                        index = saving.index,
                        modifier = saving.modifier,
                        name = saving.name,
                    )
                },
                title = strings.savingThrows
            )
        }
    }
    item(key = "skills") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.skills },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ProficiencyBlock(
                proficiencies = it,
                title = strings.skills
            )
        }
    }
}

private fun LazyListScope.monsterInfoPart3(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "damageVulnerabilities") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.damageVulnerabilities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageVulnerabilitiesBlock(damages = it)
        }
    }
    item(key = "damageResistances") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.damageResistances },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageResistancesBlock(damages = it)
        }
    }
    item(key = "damageImmunities") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.damageImmunities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageImmunitiesBlock(damages = it)
        }
    }
    item(key = "conditionImmunities") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.conditionImmunities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ConditionBlock(conditions = it)
        }
    }
}

private fun LazyListScope.monsterInfoPart4(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "senses") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { monster -> monster.senses.all { it.isNotBlank() } },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            SensesBlock(senses = it.senses)
        }
    }
    item(key = "languages") {
        StringOptionalSectionAlphaTransition(
            valueToValidate = { it.languages },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            TextBlock(
                title = strings.languages,
                text = it
            )
        }
    }
}

private fun LazyListScope.monsterInfoPart5(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "specialAbilities") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.specialAbilities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            SpecialAbilityBlock(specialAbilities = it)
        }
    }

    item(key = "actions") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.actions },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) { actions ->
            ActionBlock(actions = actions)
        }
    }

    item(key = "reactions") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.reactions },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ReactionBlock(reactions = it)
        }
    }

    item(key = "legendaryActions") {
        ListOptionalSectionAlphaTransition(
            valueToValidate = { it.legendaryActions },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) { legendaryActions ->
            ActionBlock(
                title = strings.legendaryActions,
                actions = legendaryActions
            )
        }
    }
}

@Composable
internal fun MonsterSectionAlphaTransition(
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    content: @Composable ColumnScope.(MonsterState) -> Unit
) {
    val enableGesture by remember {
        derivedStateOf {
            getItemsKeys().find { it is String && it.contains(SPELLCASTING_ITEM_KEY) } == null
        }
    }

    AlphaTransition(
        dataList = dataList,
        pagerState = pagerState,
        enableGesture = { enableGesture },
        modifier = modifier
    ) { monster ->
        MonsterInfoSectionColumn {
            content(monster)
        }
    }
}

@Composable
internal fun LazyItemScope.MonsterRequireSectionAlphaTransition(
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(MonsterState) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItem(), getItemsKeys
) { monster ->
    BlockSection(showDivider = showDivider) {
        content(monster)
    }
}

@Composable
internal fun LazyItemScope.MonsterOptionalSectionAlphaTransition(
    valueToValidate: (MonsterState) -> Boolean,
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(MonsterState) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItem(), getItemsKeys
) { monster ->
    OptionalBlockSection(isValid = { valueToValidate(monster) }, showDivider = showDivider) {
        content(monster)
    }
}

@Composable
internal fun <T> LazyItemScope.ListOptionalSectionAlphaTransition(
    valueToValidate: (MonsterState) -> List<T>,
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(List<T>) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItem(), getItemsKeys
) { monster ->
    val list = valueToValidate(monster)
    OptionalBlockSection(isValid = { list.isNotEmpty() }, showDivider = showDivider) {
        content(list)
    }
}

@Composable
internal fun LazyItemScope.StringOptionalSectionAlphaTransition(
    valueToValidate: (MonsterState) -> String,
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(String) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItem(), getItemsKeys
) { monster ->
    val string = valueToValidate(monster)
    OptionalBlockSection(isValid = { string.isNotBlank() }, showDivider = showDivider) {
        content(string)
    }
}

@Composable
private fun MonsterInfoSectionColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier.fillMaxWidth(),
    content = content
)

@Composable
private fun ColumnScope.OptionalBlockSection(
    isValid: () -> Boolean,
    showDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (isValid().not()) return
    BlockSection(showDivider = showDivider) {
        content()
    }
}

@Composable
private fun ColumnScope.BlockSection(
    showDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (showDivider) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
        )
    }

    content()
}
