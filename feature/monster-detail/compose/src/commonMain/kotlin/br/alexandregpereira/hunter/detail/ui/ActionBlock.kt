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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.monster.detail.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.detail.ActionState
import br.alexandregpereira.hunter.monster.detail.ConditionState
import br.alexandregpereira.hunter.monster.detail.DamageDiceState
import br.alexandregpereira.hunter.monster.detail.DamageState
import br.alexandregpereira.hunter.monster.detail.ProficiencyState
import br.alexandregpereira.hunter.monster.detail.SpellPreviewState
import br.alexandregpereira.hunter.ui.compose.SpellIconInfo
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ActionBlock(
    actions: List<ActionState>,
    modifier: Modifier = Modifier,
    title: String = strings.actions,
    onConditionClicked: (String) -> Unit = {},
    onSpellClicked: (String) -> Unit = {},
) = AbilityDescriptionBlock(
    title = title,
    abilityDescriptions = actions.map { it.abilityDescription },
    modifier = modifier
) { index ->

    val attackBonus = actions[index].attackBonus
    val damageDices = actions[index].damageDices
    if (attackBonus != null
        || damageDices.isNotEmpty()
        || actions[index].abilityDescription.savingThrows.isNotEmpty()
        || actions[index].abilityDescription.conditions.isNotEmpty()
    ) {
        ActionDamageGrid(
            attackBonus = actions[index].attackBonus,
            damageDices = actions[index].damageDices,
            savingThrows = actions[index].abilityDescription.savingThrows,
            conditions = actions[index].abilityDescription.conditions,
            modifier = Modifier.padding(top = 16.dp),
            onConditionClicked = onConditionClicked,
        )
    }

    actions[index].spellsByGroup.forEach { (group, spells) ->
        Spacer(modifier = Modifier.height(16.dp))
        ActionSpells(group = group, spells = spells, onSpellClicked = onSpellClicked)
    }
}

@Composable
internal fun ActionDamageGrid(
    attackBonus: Int?,
    damageDices: List<DamageDiceState>,
    savingThrows: List<ProficiencyState>,
    conditions: List<ConditionState>,
    modifier: Modifier = Modifier,
    onConditionClicked: (String) -> Unit = {},
) = Grid(modifier = modifier) {

    val iconSize = 48.dp
    attackBonus.takeUnless { it == 0 }?.let {
        Bonus(
            value = it,
            name = strings.attack,
            iconSize = iconSize,
            valueTextSize = 16.sp,
            modifier = Modifier
                .width(GridItemWidth)
                .padding(horizontal = 8.dp),
        )
    }

    damageDices.forEach { damageDice ->
        val iconRes = damageDice.damage.type.toIcon()
        if (iconRes != null) {
            IconInfo(
                title = damageDice.dice,
                painter = painterResource(iconRes),
                iconColor = damageDice.damage.type.getIconColor(),
                iconAlpha = 1f,
                iconSize = iconSize,
                modifier = Modifier
                    .width(GridItemWidth)
                    .padding(horizontal = 8.dp),
            )
        }
    }

    savingThrows.forEach { savingThrow ->
        DifficultyClass(
            value = savingThrow.modifier,
            name = savingThrow.name,
            iconSize = iconSize,
            modifier = Modifier
                .width(GridItemWidth)
                .padding(horizontal = 8.dp),
        )
    }

    conditions.forEach { condition ->
        Condition(
            condition = condition,
            iconSize = iconSize,
            modifier = Modifier
                .width(GridItemWidth)
                .padding(horizontal = 8.dp),
            onConditionClicked = onConditionClicked,
        )
    }
}

@Composable
private fun ActionSpells(
    group: String,
    spells: List<SpellPreviewState>,
    onSpellClicked: (String) -> Unit = {}
) {
    Text(
        text = remember(group) { group.toAnnotatedString() },
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Grid {
        spells.forEach { spell ->
            SpellIconInfo(
                name = spell.name,
                school = spell.school.asState(),
                modifier = Modifier.width(GridItemWidth),
                onClick = { onSpellClicked(spell.index) }
            )
        }
    }
}

private fun String.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
    val pattern = Regex("""\*\*(.+?)\*\*|\*(.+?)\*""")
    var lastIndex = 0
    pattern.findAll(this@toAnnotatedString).forEach { match ->
        append(this@toAnnotatedString.substring(lastIndex, match.range.first))
        when {
            match.groupValues[1].isNotEmpty() ->
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(match.groupValues[1])
                }
            match.groupValues[2].isNotEmpty() ->
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(match.groupValues[2])
                }
        }
        lastIndex = match.range.last + 1
    }
    append(this@toAnnotatedString.substring(lastIndex))
}

@Preview(showBackground = true)
@Composable
private fun ActionBlockPreview() = HunterTheme {
    val actions = listOf(
        ActionState(
            damageDices = listOf(
                DamageDiceState(
                    dice = "1d6",
                    damage = DamageState(
                        index = "acid",
                        type = DamageType.ACID,
                        name = "Acid",
                    )
                ),
                DamageDiceState(
                    dice = "100 (20d10+50)",
                    damage = DamageState(
                        index = "fire",
                        type = DamageType.FIRE,
                        name = "Fogo",
                    )
                ),
            ),
            attackBonus = 10,
            abilityDescription = AbilityDescriptionState(
                name = "Melee Attack",
                description = "Melee attack description",
                savingThrows = listOf(
                    ProficiencyState(
                        index = "strength",
                        modifier = 21,
                        name = "Strength"
                    ),
                    ProficiencyState(
                        index = "strength1",
                        modifier = 21,
                        name = "Constitution"
                    ),
                    ProficiencyState(
                        index = "strength2",
                        modifier = 21,
                        name = "Constitution"
                    ),
                    ProficiencyState(
                        index = "strength3",
                        modifier = 21,
                        name = "Constitution"
                    ),
                ),
                conditions = listOf(
                    ConditionState(
                        index = "blinded4",
                        type = ConditionType.RESTRAINED,
                        name = "Blinded"
                    )
                )
            ),
            spellsByGroup = mapOf(
                "**1st level:** *test*" to listOf(
                    SpellPreviewState(
                        index = "acid-splash",
                        name = "Acid Splash",
                        school = SchoolOfMagic.ILLUSION,
                    ),
                    SpellPreviewState(
                        index = "acid-splash",
                        name = "Acid Splash",
                        school = SchoolOfMagic.ABJURATION,
                    ),
                    SpellPreviewState(
                        index = "acid-splash",
                        name = "Acid Splash",
                        school = SchoolOfMagic.ENCHANTMENT,
                    ),
                    SpellPreviewState(
                        index = "acid-splash",
                        name = "Acid Splash",
                        school = SchoolOfMagic.CONJURATION,
                    ),
                    SpellPreviewState(
                        index = "acid-splash",
                        name = "Acid Splash",
                        school = SchoolOfMagic.DIVINATION,
                    ),
                    SpellPreviewState(
                        index = "acid-splash",
                        name = "Acid Splash",
                        school = SchoolOfMagic.ILLUSION,
                    ),
                ),
            )
        )
    )
    ActionBlock(
        title = "Actions",
        actions = actions,
    )
}
