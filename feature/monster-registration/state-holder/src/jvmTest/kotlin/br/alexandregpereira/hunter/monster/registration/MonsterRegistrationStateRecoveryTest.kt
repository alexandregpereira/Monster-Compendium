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

package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.ChallengeRating
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageContentScale
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlin.test.Test
import kotlin.test.assertEquals

class MonsterRegistrationStateRecoveryTest {

    @Test
    fun `getMetadata returns empty Metadata when nothing is saved`() {
        val stateRecovery = StateRecovery()

        val result = stateRecovery.getMetadata()

        assertEquals(Metadata(), result)
    }

    @Test
    fun `saveMetadata with null monster does not persist any data`() {
        val stateRecovery = StateRecovery()

        stateRecovery.saveMetadata(Metadata(monster = null))

        assertEquals(Metadata(), stateRecovery.getMetadata())
    }

    @Test
    fun `saveMetadata and getMetadata roundtrip preserves scalar fields`() {
        val stateRecovery = StateRecovery()
        val imageData = MonsterImageData(
            url = "https://example.com/goblin.png",
            backgroundColor = Color(light = "#FFFFFF", dark = "#000000"),
            isHorizontal = true,
            contentScale = MonsterImageContentScale.Crop,
            isImageDataFromCustomDatabase = false,
        )
        val monster = buildMinimalMonster().copy(
            index = "goblin",
            name = "Goblin",
            type = MonsterType.HUMANOID,
            challengeRatingData = ChallengeRating.create(0.25f),
            imageData = imageData,
            originalImageData = imageData,
            subtype = "goblinoid",
            group = "Goblins",
            subtitle = "Small humanoid",
            size = "Small",
            alignment = "neutral evil",
            stats = Stats(armorClass = 15, hitPoints = 7, hitDice = "2d6"),
            languages = "Common, Goblin",
            sourceName = "Basic Rules",
            speed = Speed(hover = false, values = emptyList()),
            abilityScores = AbilityScoreType.entries.map { AbilityScore(type = it, value = 0, modifier = 0) },
            lore = "Goblins are small, black-hearted humanoids.",
            status = MonsterStatus.Edited,
        )
        val loreEntries = listOf(
            MonsterLoreEntry(index = "goblin-lore-1", title = "Origins", description = "Goblins dwell in dark places."),
        )
        val metadata = Metadata(monster = monster, monsterLoreEntries = loreEntries)

        stateRecovery.saveMetadata(metadata)
        val result = stateRecovery.getMetadata()

        assertEquals(monster, result.monster)
        assertEquals(loreEntries, result.monsterLoreEntries)
    }

    @Test
    fun `saveMetadata and getMetadata roundtrip preserves all list fields`() {
        val stateRecovery = StateRecovery()
        val imageData = MonsterImageData(
            url = "https://example.com/dragon.png",
            backgroundColor = Color(light = "#FF0000", dark = "#800000"),
            isHorizontal = false,
            contentScale = MonsterImageContentScale.Fit,
            isImageDataFromCustomDatabase = false,
        )
        val savingThrow = SavingThrow(
            index = "str-save",
            modifier = 5,
            type = AbilityScoreType.STRENGTH,
        )
        val condition = Condition(
            index = "blinded",
            type = ConditionType.BLINDED,
            name = "Blinded",
        )
        val damage = Damage(
            index = "fire",
            type = DamageType.FIRE,
            name = "Fire",
        )
        val abilityDescription = AbilityDescription(
            index = "legendary-resistance",
            name = "Legendary Resistance",
            description = "If the dragon fails a saving throw, it can choose to succeed instead.",
            savingThrows = listOf(savingThrow),
            conditions = listOf(condition),
        )
        val damageDice = DamageDice(
            index = "claw-dice",
            dice = "2d6+8",
            damage = damage,
        )
        val action = Action(
            id = "claw",
            attackBonus = 10,
            abilityDescription = abilityDescription,
            damageDices = listOf(damageDice),
        )
        val spellPreview = SpellPreview(
            index = "fireball",
            name = "Fireball",
            level = 3,
            school = SchoolOfMagic.EVOCATION,
        )
        val spellUsage = SpellUsage(
            index = "3rd-level",
            group = "3rd level (3 slots)",
            spells = listOf(spellPreview),
        )
        val spellcasting = Spellcasting(
            index = "dragon-spellcasting",
            description = "The dragon is an 18th-level spellcaster.",
            type = SpellcastingType.SPELLCASTER,
            usages = listOf(spellUsage),
        )
        val monster = buildMinimalMonster(index = "ancient-red-dragon").copy(
            imageData = imageData,
            originalImageData = imageData,
            senses = listOf("blindsight 60 ft.", "darkvision 120 ft."),
            speed = Speed(
                hover = true,
                values = listOf(
                    SpeedValue(type = SpeedType.WALK, valueFormatted = "40 ft.", index = "walk"),
                    SpeedValue(type = SpeedType.FLY, valueFormatted = "80 ft.", index = "fly"),
                ),
            ),
            abilityScores = AbilityScoreType.entries.map { type ->
                AbilityScore(type = type, value = 30, modifier = 0)
            },
            savingThrows = listOf(savingThrow),
            skills = listOf(Skill(index = "perception", modifier = 16, name = "Perception")),
            damageVulnerabilities = listOf(damage.copy(index = "vuln-cold", type = DamageType.COLD, name = "Cold")),
            damageResistances = listOf(damage.copy(index = "res-lightning", type = DamageType.LIGHTNING, name = "Lightning")),
            damageImmunities = listOf(damage),
            conditionImmunities = listOf(condition),
            specialAbilities = listOf(abilityDescription),
            reactions = listOf(abilityDescription.copy(index = "reaction-one", name = "Tail Attack")),
            actions = listOf(action),
            legendaryActions = listOf(action.copy(id = "legendary-claw")),
            spellcastings = listOf(spellcasting),
        )
        val metadata = Metadata(monster = monster)

        stateRecovery.saveMetadata(metadata)
        val result = stateRecovery.getMetadata()

        assertEquals(monster, result.monster)
    }

    @Test
    fun `saveMetadata and getMetadata preserves monsterLoreEntries including null title`() {
        val stateRecovery = StateRecovery()
        val monster = buildMinimalMonster()
        val loreEntries = listOf(
            MonsterLoreEntry(index = "entry-1", title = null, description = "Entry without title."),
            MonsterLoreEntry(index = "entry-2", title = "Origins", description = "Entry with title."),
        )
        val metadata = Metadata(monster = monster, monsterLoreEntries = loreEntries)

        stateRecovery.saveMetadata(metadata)
        val result = stateRecovery.getMetadata()

        assertEquals(loreEntries, result.monsterLoreEntries)
    }

    @Test
    fun `saveMetadata and getMetadata with empty lists produces monster with empty lists`() {
        val stateRecovery = StateRecovery()
        val monster = buildMinimalMonster()
        val metadata = Metadata(monster = monster, monsterLoreEntries = emptyList())

        stateRecovery.saveMetadata(metadata)
        val result = stateRecovery.getMetadata()

        assertEquals(emptyList(), result.monster?.senses)
        assertEquals(emptyList(), result.monster?.speed?.values)
        assertEquals(emptyList(), result.monster?.savingThrows)
        assertEquals(emptyList(), result.monster?.skills)
        assertEquals(emptyList(), result.monster?.damageVulnerabilities)
        assertEquals(emptyList(), result.monster?.damageResistances)
        assertEquals(emptyList(), result.monster?.damageImmunities)
        assertEquals(emptyList(), result.monster?.conditionImmunities)
        assertEquals(emptyList(), result.monster?.specialAbilities)
        assertEquals(emptyList(), result.monster?.reactions)
        assertEquals(emptyList(), result.monster?.actions)
        assertEquals(emptyList(), result.monster?.legendaryActions)
        assertEquals(emptyList(), result.monster?.spellcastings)
        assertEquals(emptyList(), result.monsterLoreEntries)
        // abilityScores is always reconstructed with all 6 types (modifier is not persisted)
        assertEquals(
            AbilityScoreType.entries.map { AbilityScore(type = it, value = 0, modifier = 0) },
            result.monster?.abilityScores,
        )
    }

    private fun buildMinimalMonster(index: String = "goblin"): Monster {
        val imageData = MonsterImageData(
            url = "",
            backgroundColor = Color(light = "", dark = ""),
            isHorizontal = false,
            contentScale = null,
            isImageDataFromCustomDatabase = false,
        )
        return Monster(
            index = index,
            name = "",
            type = MonsterType.HUMANOID,
            challengeRatingData = ChallengeRating.create(0f),
            imageData = imageData,
            originalImageData = imageData,
            customMonsterImage = null,
            subtype = null,
            group = null,
            subtitle = "",
            size = "",
            alignment = "",
            stats = Stats(armorClass = 0, hitPoints = 0, hitDice = ""),
            senses = emptyList(),
            languages = "",
            sourceName = "",
            speed = Speed(hover = false, values = emptyList()),
            abilityScores = emptyList(),
            savingThrows = emptyList(),
            skills = emptyList(),
            damageVulnerabilities = emptyList(),
            damageResistances = emptyList(),
            damageImmunities = emptyList(),
            conditionImmunities = emptyList(),
            specialAbilities = emptyList(),
            actions = emptyList(),
            legendaryActions = emptyList(),
            reactions = emptyList(),
            spellcastings = emptyList(),
            lore = null,
            status = MonsterStatus.Created,
        )
    }
}
