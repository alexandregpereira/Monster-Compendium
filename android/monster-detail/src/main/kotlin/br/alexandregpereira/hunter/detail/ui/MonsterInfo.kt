/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.ui.transition.AlphaTransition

fun LazyListScope.monsterInfo(
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

    item(key = "reactions") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.reactions },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ReactionBlock(reactions = it)
        }
    }

    item(key = "space") {
        Spacer(
            modifier = Modifier
                .height(contentPadding.calculateBottomPadding())
                .fillMaxWidth()
                .animateItemPlacement()
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
        MonsterRequireSectionAlphaTransition(
            monsters,
            pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            SpeedBlock(speed = monster.speed)
        }
    }
    item(key = "abilityScores") {
        MonsterRequireSectionAlphaTransition(
            monsters,
            pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            AbilityScoreBlock(abilityScores = monster.abilityScores)
        }
    }
}

private fun LazyListScope.monsterInfoPart2(
    monsters: List<MonsterState>,
    pagerState: PagerState,
    getItemsKeys: () -> List<Any> = { emptyList() },
) {
    item(key = "savingThrows") {
        MonsterOptionalSectionAlphaTransition(
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
                        name = stringResource(saving.type.stringRes)
                    )
                },
                title = stringResource(R.string.monster_detail_saving_throws)
            )
        }
    }
    item(key = "skills") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.skills },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            ProficiencyBlock(
                proficiencies = it,
                title = stringResource(R.string.monster_detail_skills)
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
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.damageVulnerabilities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageVulnerabilitiesBlock(damages = it)
        }
    }
    item(key = "damageResistances") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.damageResistances },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageResistancesBlock(damages = it)
        }
    }
    item(key = "damageImmunities") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.damageImmunities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            DamageImmunitiesBlock(damages = it)
        }
    }
    item(key = "conditionImmunities") {
        MonsterOptionalSectionAlphaTransition(
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
            valueToValidate = { it.senses },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            SensesBlock(senses = it)
        }
    }
    item(key = "languages") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.languages },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            TextBlock(
                title = stringResource(R.string.monster_detail_languages),
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
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.specialAbilities },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) {
            SpecialAbilityBlock(specialAbilities = it)
        }
    }

    item(key = "actions") {
        MonsterRequireSectionAlphaTransition(
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) { monster ->
            ActionBlock(actions = monster.actions)
        }
    }

    item(key = "legendaryActions") {
        MonsterOptionalSectionAlphaTransition(
            valueToValidate = { it.legendaryActions },
            dataList = monsters,
            pagerState = pagerState,
            getItemsKeys = getItemsKeys,
        ) { legendaryActions ->
            ActionBlock(
                title = stringResource(R.string.monster_detail_legendary_actions),
                actions = legendaryActions
            )
        }
    }
}

@Composable
fun MonsterSectionAlphaTransition(
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
fun LazyItemScope.MonsterRequireSectionAlphaTransition(
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(MonsterState) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItemPlacement(), getItemsKeys
) { monster ->
    BlockSection(showDivider = showDivider) {
        content(monster)
    }
}

@Composable
fun <T> LazyItemScope.MonsterOptionalSectionAlphaTransition(
    valueToValidate: (MonsterState) -> T,
    dataList: List<MonsterState>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    getItemsKeys: () -> List<Any> = { emptyList() },
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(T) -> Unit
) = MonsterSectionAlphaTransition(
    dataList, pagerState, modifier.animateItemPlacement(), getItemsKeys
) { monster ->
    OptionalBlockSection(valueToValidate(monster), showDivider = showDivider) { value ->
        content(value)
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
private fun <T> ColumnScope.OptionalBlockSection(
    value: T,
    showDivider: Boolean = true,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    if ((value is String && value.trim().isEmpty()) || (value is List<*> && value.isEmpty())) return
    BlockSection(showDivider = showDivider) {
        content(value)
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
