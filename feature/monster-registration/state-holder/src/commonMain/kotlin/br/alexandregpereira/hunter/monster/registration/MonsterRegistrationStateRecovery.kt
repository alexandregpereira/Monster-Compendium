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
import br.alexandregpereira.hunter.ui.getInt
import br.alexandregpereira.hunter.ui.getString

internal fun MonsterRegistrationState.saveState(stateRecovery: StateRecovery): MonsterRegistrationState {
    stateRecovery["monsterRegistration:isOpen"] = isOpen
    stateRecovery.dispatchChanges()
    return this
}

internal fun MonsterRegistrationState.updateState(bundle: Map<String, Any?>): MonsterRegistrationState {
    return copy(isOpen = bundle["monsterRegistration:isOpen"] as? Boolean ?: false)
}

internal fun StateRecovery.saveMetadata(metadata: Metadata) {
    val monster = metadata.monster ?: return
    saveMonsterScalars(monster)
    saveAbilityScores(monster)
    saveSenses(monster)
    saveSpeedValues(monster)
    saveSavingThrows(monster)
    saveSkills(monster)
    saveDamageList("monsterRegistration:metadata:damageVulnerability", monster.damageVulnerabilities)
    saveDamageList("monsterRegistration:metadata:damageResistance", monster.damageResistances)
    saveDamageList("monsterRegistration:metadata:damageImmunity", monster.damageImmunities)
    saveConditionImmunities(monster)
    saveActions("monsterRegistration:metadata:specialAbility", monster.specialAbilities)
    saveActions("monsterRegistration:metadata:action", monster.actions)
    saveActions("monsterRegistration:metadata:bonusAction", monster.bonusActions)
    saveActions("monsterRegistration:metadata:legendaryAction", monster.legendaryActions)
    saveActions("monsterRegistration:metadata:reaction", monster.reactions)
    saveSpellcastings(monster)
    saveMonsterLoreEntries(metadata.monsterLoreEntries)
    dispatchChanges()
}

internal fun StateRecovery.getMetadata(): Metadata {
    val index = this["monsterRegistration:metadata:index"] as? String ?: return Metadata()
    val monsterType = (this["monsterRegistration:metadata:type"] as? String)
        ?.let { runCatching { MonsterType.valueOf(it) }.getOrNull() }
        ?: MonsterType.HUMANOID
    val challengeRatingValue = this["monsterRegistration:metadata:challengeRating"] as? Float ?: 0f
    val status = (this["monsterRegistration:metadata:status"] as? String)
        ?.let { runCatching { MonsterStatus.valueOf(it) }.getOrNull() }
        ?: MonsterStatus.Created
    val imageData = getImageData()
    val monster = Monster(
        index = index,
        name = this["monsterRegistration:metadata:name"] as? String ?: "",
        type = monsterType,
        challengeRatingData = ChallengeRating.create(challengeRatingValue),
        imageData = imageData,
        originalImageData = imageData,
        customMonsterImage = null,
        subtype = this["monsterRegistration:metadata:subtype"] as? String,
        group = this["monsterRegistration:metadata:group"] as? String,
        subtitle = this["monsterRegistration:metadata:subtitle"] as? String ?: "",
        size = this["monsterRegistration:metadata:size"] as? String ?: "",
        alignment = this["monsterRegistration:metadata:alignment"] as? String ?: "",
        stats = Stats(
            armorClass = this["monsterRegistration:metadata:armorClass"] as? Int ?: 0,
            hitPoints = this["monsterRegistration:metadata:hitPoints"] as? Int ?: 0,
            hitDice = this["monsterRegistration:metadata:hitDice"] as? String ?: "",
            initiative = this["monsterRegistration:metadata:initiative"] as? Int,
        ),
        senses = getSenses(),
        languages = this["monsterRegistration:metadata:languages"] as? String ?: "",
        sourceName = this["monsterRegistration:metadata:sourceName"] as? String ?: "",
        speed = Speed(
            hover = this["monsterRegistration:metadata:speedHover"] as? Boolean ?: false,
            values = getSpeedValues(),
        ),
        abilityScores = getAbilityScores(),
        savingThrows = getSavingThrows(),
        skills = getSkills(),
        damageVulnerabilities = getDamageList("monsterRegistration:metadata:damageVulnerability"),
        damageResistances = getDamageList("monsterRegistration:metadata:damageResistance"),
        damageImmunities = getDamageList("monsterRegistration:metadata:damageImmunity"),
        conditionImmunities = getConditionImmunities(),
        specialAbilities = getActions("monsterRegistration:metadata:specialAbility"),
        actions = getActions("monsterRegistration:metadata:action"),
        bonusActions = getActions("monsterRegistration:metadata:bonusAction"),
        legendaryActions = getActions("monsterRegistration:metadata:legendaryAction"),
        reactions = getActions("monsterRegistration:metadata:reaction"),
        spellcastings = getSpellcastings(),
        lore = this["monsterRegistration:metadata:lore"] as? String,
        status = status,
    )
    return Metadata(monster = monster, monsterLoreEntries = getMonsterLoreEntries())
}

internal fun StateRecovery.saveParams(params: MonsterRegistrationParams) {
    this["monsterRegistration:params:monsterIndex"] = params.monsterIndex
    dispatchChanges()
}

internal fun StateRecovery.getParams(): MonsterRegistrationParams {
    if (!containsKey("monsterRegistration:params:monsterIndex")) return MonsterRegistrationParams()
    return MonsterRegistrationParams(
        monsterIndex = this["monsterRegistration:params:monsterIndex"] as? String
    )
}

private fun Monster.getAbilityScore(type: AbilityScoreType): Int? {
    return abilityScores.firstOrNull { it.type == type }?.value
}

private fun StateRecovery.saveMonsterScalars(monster: Monster) {
    this["monsterRegistration:metadata:index"] = monster.index
    this["monsterRegistration:metadata:name"] = monster.name
    this["monsterRegistration:metadata:type"] = monster.type.name
    this["monsterRegistration:metadata:challengeRating"] = monster.challengeRatingData.value
    this["monsterRegistration:metadata:imageUrl"] = monster.imageData.url
    this["monsterRegistration:metadata:imageBackgroundColorLight"] = monster.imageData.backgroundColor.light
    this["monsterRegistration:metadata:imageBackgroundColorDark"] = monster.imageData.backgroundColor.dark
    this["monsterRegistration:metadata:imageIsHorizontal"] = monster.imageData.isHorizontal
    this["monsterRegistration:metadata:imageContentScale"] = monster.imageData.contentScale?.name
    this["monsterRegistration:metadata:subtype"] = monster.subtype
    this["monsterRegistration:metadata:group"] = monster.group
    this["monsterRegistration:metadata:subtitle"] = monster.subtitle
    this["monsterRegistration:metadata:size"] = monster.size
    this["monsterRegistration:metadata:alignment"] = monster.alignment
    this["monsterRegistration:metadata:armorClass"] = monster.stats.armorClass
    this["monsterRegistration:metadata:hitPoints"] = monster.stats.hitPoints
    this["monsterRegistration:metadata:hitDice"] = monster.stats.hitDice
    this["monsterRegistration:metadata:initiative"] = monster.stats.initiative
    this["monsterRegistration:metadata:languages"] = monster.languages
    this["monsterRegistration:metadata:sourceName"] = monster.sourceName
    this["monsterRegistration:metadata:speedHover"] = monster.speed.hover
    this["monsterRegistration:metadata:lore"] = monster.lore
    this["monsterRegistration:metadata:status"] = monster.status.name
}

private fun StateRecovery.saveAbilityScores(monster: Monster) {
    AbilityScoreType.entries.forEach { type ->
        this["monsterRegistration:metadata:abilityScore:${type.name.lowercase()}"] = monster.getAbilityScore(type)
    }
}

private fun StateRecovery.saveSenses(monster: Monster) {
    this["monsterRegistration:metadata:sense:size"] = monster.senses.size
    monster.senses.forEachIndexed { i, sense ->
        this["monsterRegistration:metadata:sense:$i"] = sense
    }
}

private fun StateRecovery.saveSpeedValues(monster: Monster) {
    this["monsterRegistration:metadata:speedValue:size"] = monster.speed.values.size
    monster.speed.values.forEachIndexed { i, sv ->
        this["monsterRegistration:metadata:speedValue:type:$i"] = sv.type.name
        this["monsterRegistration:metadata:speedValue:valueFormatted:$i"] = sv.valueFormatted
        this["monsterRegistration:metadata:speedValue:index:$i"] = sv.index
    }
}

private fun StateRecovery.saveSavingThrows(monster: Monster) {
    this["monsterRegistration:metadata:savingThrow:size"] = monster.savingThrows.size
    monster.savingThrows.forEachIndexed { i, st ->
        this["monsterRegistration:metadata:savingThrow:index:$i"] = st.index
        this["monsterRegistration:metadata:savingThrow:modifier:$i"] = st.modifier
        this["monsterRegistration:metadata:savingThrow:type:$i"] = st.type.name
    }
}

private fun StateRecovery.saveSkills(monster: Monster) {
    this["monsterRegistration:metadata:skill:size"] = monster.skills.size
    monster.skills.forEachIndexed { i, skill ->
        this["monsterRegistration:metadata:skill:index:$i"] = skill.index
        this["monsterRegistration:metadata:skill:modifier:$i"] = skill.modifier
        this["monsterRegistration:metadata:skill:name:$i"] = skill.name
    }
}

private fun StateRecovery.saveConditionImmunities(monster: Monster) {
    this["monsterRegistration:metadata:conditionImmunity:size"] = monster.conditionImmunities.size
    monster.conditionImmunities.forEachIndexed { i, c ->
        this["monsterRegistration:metadata:conditionImmunity:index:$i"] = c.index
        this["monsterRegistration:metadata:conditionImmunity:type:$i"] = c.type.name
        this["monsterRegistration:metadata:conditionImmunity:name:$i"] = c.name
    }
}

private fun StateRecovery.saveSpellcastings(monster: Monster) {
    this["monsterRegistration:metadata:spellcasting:size"] = monster.spellcastings.size
    monster.spellcastings.forEachIndexed { i, sc ->
        this["monsterRegistration:metadata:spellcasting:index:$i"] = sc.index
        this["monsterRegistration:metadata:spellcasting:description:$i"] = sc.description
        this["monsterRegistration:metadata:spellcasting:type:$i"] = sc.type.name
        this["monsterRegistration:metadata:spellcasting:usage:size:$i"] = sc.usages.size
        sc.usages.forEachIndexed { j, usage ->
            this["monsterRegistration:metadata:spellcasting:usage:index:$i:$j"] = usage.index
            this["monsterRegistration:metadata:spellcasting:usage:group:$i:$j"] = usage.group
            this["monsterRegistration:metadata:spellcasting:usage:spell:size:$i:$j"] = usage.spells.size
            usage.spells.forEachIndexed { k, spell ->
                this["monsterRegistration:metadata:spellcasting:usage:spell:index:$i:$j:$k"] = spell.index
                this["monsterRegistration:metadata:spellcasting:usage:spell:name:$i:$j:$k"] = spell.name
                this["monsterRegistration:metadata:spellcasting:usage:spell:level:$i:$j:$k"] = spell.level
                this["monsterRegistration:metadata:spellcasting:usage:spell:school:$i:$j:$k"] = spell.school.name
            }
        }
    }
}

private fun StateRecovery.saveDamageList(prefix: String, list: List<Damage>) {
    this["$prefix:size"] = list.size
    list.forEachIndexed { i, d ->
        this["$prefix:index:$i"] = d.index
        this["$prefix:type:$i"] = d.type.name
        this["$prefix:name:$i"] = d.name
    }
}

private fun StateRecovery.saveActions(prefix: String, list: List<Action>) {
    this["$prefix:size"] = list.size
    list.forEachIndexed { i, action ->
        this["$prefix:id:$i"] = action.id
        this["$prefix:attackBonus:$i"] = action.attackBonus
        this["$prefix:name:$i"] = action.abilityDescription.name
        this["$prefix:description:$i"] = action.abilityDescription.description
        this["$prefix:abilityIndex:$i"] = action.abilityDescription.index
        this["$prefix:savingThrow:size:$i"] = action.abilityDescription.savingThrows.size
        action.abilityDescription.savingThrows.forEachIndexed { j, st ->
            this["$prefix:savingThrow:index:$i:$j"] = st.index
            this["$prefix:savingThrow:modifier:$i:$j"] = st.modifier
            this["$prefix:savingThrow:type:$i:$j"] = st.type.name
        }
        this["$prefix:condition:size:$i"] = action.abilityDescription.conditions.size
        action.abilityDescription.conditions.forEachIndexed { j, c ->
            this["$prefix:condition:index:$i:$j"] = c.index
            this["$prefix:condition:type:$i:$j"] = c.type.name
            this["$prefix:condition:name:$i:$j"] = c.name
        }
        this["$prefix:damageDice:size:$i"] = action.damageDices.size
        action.damageDices.forEachIndexed { j, dd ->
            this["$prefix:damageDice:index:$i:$j"] = dd.index
            this["$prefix:damageDice:dice:$i:$j"] = dd.dice
            this["$prefix:damageDice:damage:index:$i:$j"] = dd.damage.index
            this["$prefix:damageDice:damage:type:$i:$j"] = dd.damage.type.name
            this["$prefix:damageDice:damage:name:$i:$j"] = dd.damage.name
        }
    }
}

private fun StateRecovery.getImageData(): MonsterImageData {
    val contentScale = (this["monsterRegistration:metadata:imageContentScale"] as? String)
        ?.let { runCatching { MonsterImageContentScale.valueOf(it) }.getOrNull() }
    return MonsterImageData(
        url = this["monsterRegistration:metadata:imageUrl"] as? String ?: "",
        backgroundColor = Color(
            light = this["monsterRegistration:metadata:imageBackgroundColorLight"] as? String ?: "",
            dark = this["monsterRegistration:metadata:imageBackgroundColorDark"] as? String ?: "",
        ),
        isHorizontal = this["monsterRegistration:metadata:imageIsHorizontal"] as? Boolean ?: false,
        contentScale = contentScale,
        isImageDataFromCustomDatabase = false,
    )
}

private fun StateRecovery.getAbilityScores(): List<AbilityScore> {
    return AbilityScoreType.entries.map { type ->
        AbilityScore(
            type = type,
            value = getInt("monsterRegistration:metadata:abilityScore:${type.name.lowercase()}") ?: 0,
            modifier = 0,
        )
    }
}

private fun StateRecovery.getSenses(): List<String> {
    val size = getInt("monsterRegistration:metadata:sense:size") ?: return emptyList()
    return (0 until size).mapNotNull { i ->
        getString("monsterRegistration:metadata:sense:$i")
    }
}

private fun StateRecovery.getSpeedValues(): List<SpeedValue> {
    val size = getInt("monsterRegistration:metadata:speedValue:size") ?: return emptyList()
    return (0 until size).map { i ->
        SpeedValue(
            type = (getString("monsterRegistration:metadata:speedValue:type:$i"))
                ?.let { runCatching { SpeedType.valueOf(it) }.getOrNull() }
                ?: SpeedType.WALK,
            valueFormatted = getString("monsterRegistration:metadata:speedValue:valueFormatted:$i") ?: "",
            index = getString("monsterRegistration:metadata:speedValue:index:$i") ?: "",
        )
    }
}

private fun StateRecovery.getSavingThrows(): List<SavingThrow> {
    val size = getInt("monsterRegistration:metadata:savingThrow:size") ?: return emptyList()
    return (0 until size).map { i ->
        SavingThrow(
            index = getString("monsterRegistration:metadata:savingThrow:index:$i") ?: "",
            modifier = getInt("monsterRegistration:metadata:savingThrow:modifier:$i") ?: 0,
            type = (getString("monsterRegistration:metadata:savingThrow:type:$i"))
                ?.let { runCatching { AbilityScoreType.valueOf(it) }.getOrNull() }
                ?: AbilityScoreType.STRENGTH,
        )
    }
}

private fun StateRecovery.getSkills(): List<Skill> {
    val size = getInt("monsterRegistration:metadata:skill:size") ?: return emptyList()
    return (0 until size).mapNotNull { i ->
        val index = getString("monsterRegistration:metadata:skill:index:$i")
            ?: return@mapNotNull null
        Skill(
            index = index,
            modifier = getInt("monsterRegistration:metadata:skill:modifier:$i") ?: 0,
            name = getString("monsterRegistration:metadata:skill:name:$i") ?: "",
        )
    }
}

private fun StateRecovery.getDamageList(prefix: String): List<Damage> {
    val size = getInt("$prefix:size") ?: return emptyList()
    return (0 until size).map { i ->
        Damage(
            index = getString("$prefix:index:$i") ?: "",
            type = (getString("$prefix:type:$i"))
                ?.let { runCatching { DamageType.valueOf(it) }.getOrNull() }
                ?: DamageType.BLUDGEONING,
            name = getString("$prefix:name:$i") ?: "",
        )
    }
}

private fun StateRecovery.getConditionImmunities(): List<Condition> {
    val size = getInt("monsterRegistration:metadata:conditionImmunity:size") ?: return emptyList()
    return (0 until size).map { i ->
        Condition(
            index = getString("monsterRegistration:metadata:conditionImmunity:index:$i") ?: "",
            type = (getString("monsterRegistration:metadata:conditionImmunity:type:$i"))
                ?.let { runCatching { ConditionType.valueOf(it) }.getOrNull() }
                ?: ConditionType.BLINDED,
            name = getString("monsterRegistration:metadata:conditionImmunity:name:$i") ?: "",
        )
    }
}

private fun StateRecovery.getActions(prefix: String): List<Action> {
    val size = getInt("$prefix:size") ?: return emptyList()
    return (0 until size).map { i ->
        Action(
            id = getString("$prefix:id:$i") ?: "",
            attackBonus = getInt("$prefix:attackBonus:$i"),
            abilityDescription = AbilityDescription(
                name = getString("$prefix:name:$i") ?: "",
                description = getString("$prefix:description:$i") ?: "",
                index = getString("$prefix:abilityIndex:$i") ?: "",
                savingThrows = run {
                    val stSize = getInt("$prefix:savingThrow:size:$i") ?: 0
                    (0 until stSize).map { j ->
                        SavingThrow(
                            index = getString("$prefix:savingThrow:index:$i:$j") ?: "",
                            modifier = getInt("$prefix:savingThrow:modifier:$i:$j") ?: 0,
                            type = (getString("$prefix:savingThrow:type:$i:$j"))
                                ?.let { runCatching { AbilityScoreType.valueOf(it) }.getOrNull() }
                                ?: AbilityScoreType.STRENGTH,
                        )
                    }
                },
                conditions = run {
                    val condSize = getInt("$prefix:condition:size:$i") ?: 0
                    (0 until condSize).map { j ->
                        Condition(
                            index = getString("$prefix:condition:index:$i:$j") ?: "",
                            type = (getString("$prefix:condition:type:$i:$j"))
                                ?.let { runCatching { ConditionType.valueOf(it) }.getOrNull() }
                                ?: ConditionType.BLINDED,
                            name = getString("$prefix:condition:name:$i:$j") ?: "",
                        )
                    }
                },
            ),
            damageDices = run {
                val ddSize = getInt("$prefix:damageDice:size:$i") ?: 0
                (0 until ddSize).map { j ->
                    DamageDice(
                        index = getString("$prefix:damageDice:index:$i:$j") ?: "",
                        dice = getString("$prefix:damageDice:dice:$i:$j") ?: "",
                        damage = Damage(
                            index = getString("$prefix:damageDice:damage:index:$i:$j") ?: "",
                            type = (getString("$prefix:damageDice:damage:type:$i:$j"))
                                ?.let { runCatching { DamageType.valueOf(it) }.getOrNull() }
                                ?: DamageType.BLUDGEONING,
                            name = getString("$prefix:damageDice:damage:name:$i:$j") ?: "",
                        ),
                    )
                }
            },
        )
    }
}

private fun StateRecovery.saveMonsterLoreEntries(entries: List<MonsterLoreEntry>) {
    this["monsterRegistration:metadata:loreEntry:size"] = entries.size
    entries.forEachIndexed { i, entry ->
        this["monsterRegistration:metadata:loreEntry:index:$i"] = entry.index
        this["monsterRegistration:metadata:loreEntry:title:$i"] = entry.title
        this["monsterRegistration:metadata:loreEntry:description:$i"] = entry.description
    }
}

private fun StateRecovery.getMonsterLoreEntries(): List<MonsterLoreEntry> {
    val size = getInt("monsterRegistration:metadata:loreEntry:size") ?: return emptyList()
    return (0 until size).map { i ->
        MonsterLoreEntry(
            index = getString("monsterRegistration:metadata:loreEntry:index:$i") ?: "",
            title = getString("monsterRegistration:metadata:loreEntry:title:$i"),
            description = getString("monsterRegistration:metadata:loreEntry:description:$i") ?: "",
        )
    }
}

private fun StateRecovery.getSpellcastings(): List<Spellcasting> {
    val size = getInt("monsterRegistration:metadata:spellcasting:size") ?: return emptyList()
    return (0 until size).map { i ->
        Spellcasting(
            index = getString("monsterRegistration:metadata:spellcasting:index:$i") ?: "",
            description = getString("monsterRegistration:metadata:spellcasting:description:$i") ?: "",
            type = (getString("monsterRegistration:metadata:spellcasting:type:$i"))
                ?.let { runCatching { SpellcastingType.valueOf(it) }.getOrNull() }
                ?: SpellcastingType.SPELLCASTER,
            usages = run {
                val usageSize = getInt("monsterRegistration:metadata:spellcasting:usage:size:$i") ?: 0
                (0 until usageSize).map { j ->
                    SpellUsage(
                        index = getString("monsterRegistration:metadata:spellcasting:usage:index:$i:$j") ?: "",
                        group = getString("monsterRegistration:metadata:spellcasting:usage:group:$i:$j") ?: "",
                        spells = run {
                            val spellSize = getInt("monsterRegistration:metadata:spellcasting:usage:spell:size:$i:$j") ?: 0
                            (0 until spellSize).map { k ->
                                SpellPreview(
                                    index = getString("monsterRegistration:metadata:spellcasting:usage:spell:index:$i:$j:$k") ?: "",
                                    name = getString("monsterRegistration:metadata:spellcasting:usage:spell:name:$i:$j:$k") ?: "",
                                    level = getInt("monsterRegistration:metadata:spellcasting:usage:spell:level:$i:$j:$k") ?: 0,
                                    school = (getString("monsterRegistration:metadata:spellcasting:usage:spell:school:$i:$j:$k"))
                                        ?.let { runCatching { SchoolOfMagic.valueOf(it) }.getOrNull() }
                                        ?: SchoolOfMagic.ABJURATION,
                                )
                            }
                        },
                    )
                }
            },
        )
    }
}
