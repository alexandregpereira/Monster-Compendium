package br.alexandregpereira.hunter.monster.registration.mapper

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Condition
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageDice
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SavingThrow
import br.alexandregpereira.hunter.domain.model.Skill
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType
import br.alexandregpereira.hunter.monster.registration.AbilityDescriptionState
import br.alexandregpereira.hunter.monster.registration.AbilityScoreState
import br.alexandregpereira.hunter.monster.registration.ActionState
import br.alexandregpereira.hunter.monster.registration.ConditionState
import br.alexandregpereira.hunter.monster.registration.DamageDiceState
import br.alexandregpereira.hunter.monster.registration.DamageState
import br.alexandregpereira.hunter.monster.registration.Metadata
import br.alexandregpereira.hunter.monster.registration.MonsterInfoState
import br.alexandregpereira.hunter.monster.registration.MonsterRegistrationStrings
import br.alexandregpereira.hunter.monster.registration.MonsterState
import br.alexandregpereira.hunter.monster.registration.SavingThrowState
import br.alexandregpereira.hunter.monster.registration.SectionTitle
import br.alexandregpereira.hunter.monster.registration.SkillState
import br.alexandregpereira.hunter.monster.registration.SpeedValueState
import br.alexandregpereira.hunter.monster.registration.SpellPreviewState
import br.alexandregpereira.hunter.monster.registration.SpellcastingState
import br.alexandregpereira.hunter.monster.registration.SpellsByGroupState
import br.alexandregpereira.hunter.monster.registration.StatsState
import br.alexandregpereira.hunter.monster.registration.TypeState

internal fun Metadata.asState(strings: MonsterRegistrationStrings): MonsterState {
    return MonsterState(
        index = monster.index,
        info = MonsterInfoState(
            name = monster.name,
            subtitle = monster.subtitle,
            group = monster.group.orEmpty(),
            imageUrl = monster.imageData.url,
            backgroundColorLight = monster.imageData.backgroundColor.light,
            backgroundColorDark = monster.imageData.backgroundColor.dark,
            typeIndex = MonsterType.entries.indexOf(monster.type),
            typeOptions = MonsterType.entries.map { it.name(strings) },
        ),
        stats = StatsState(
            armorClass = monster.stats.armorClass,
            hitPoints = monster.stats.hitPoints,
            hitDice = monster.stats.hitDice,
        ),
        speedValues = monster.speed.values.map { it.asState(strings) },
        abilityScores = monster.abilityScores.map { it.asState(strings) },
        savingThrows = monster.savingThrows.map {
            it.asState(strings, filteredSavingThrowTypes)
        },
        skills = monster.skills.map { it.asState() },
        damageVulnerabilities = monster.damageVulnerabilities.map {
            it.asState(strings, filteredDamageVulnerabilityTypes)
        },
        damageResistances = monster.damageResistances.map {
            it.asState(strings, filteredDamageResistanceTypes)
        },
        damageImmunities = monster.damageImmunities.map {
            it.asState(strings, filteredDamageImmunityTypes)
        },
        conditionImmunities = monster.conditionImmunities.map {
            it.asState(strings, filteredConditionTypes)
        },
        senses = monster.senses,
        languages = monster.languages,
        specialAbilities = monster.specialAbilities.map { it.asState() },
        actions = monster.actions.map { it.asState(strings) },
        legendaryActions = monster.legendaryActions.map { it.asState(strings) },
        reactions = monster.reactions.map { it.asState() },
        spellcastings = monster.spellcastings.map { it.asState(strings) },
    ).run { copy(keysList = createKeys()) }
}

private fun SpeedValue.asState(strings: MonsterRegistrationStrings): SpeedValueState {
    return SpeedValueState(
        key = index,
        typeIndex = SpeedType.entries.indexOf(type),
        value = valueFormatted,
        options = SpeedType.entries.map { it.name(strings) },
    )
}

private fun AbilityScore.asState(strings: MonsterRegistrationStrings): AbilityScoreState {
    return AbilityScoreState(
        key = type.name,
        value = value,
        name = type.name(strings),
    )
}

private fun SavingThrow.asState(
    strings: MonsterRegistrationStrings,
    filteredSavingThrowTypes: List<AbilityScoreType>,
): SavingThrowState {
    return SavingThrowState(
        key = index,
        modifier = modifier,
        selectedIndex = type.ordinal,
        typeOptions = AbilityScoreType.entries.map {
            it.asState(strings, filteredSavingThrowTypes)
        },
    )
}

private fun AbilityScoreType.asState(
    strings: MonsterRegistrationStrings,
    filteredSavingThrowTypes: List<AbilityScoreType>,
): TypeState {
    return TypeState(
        index = ordinal,
        name = name(strings),
        enabled = filteredSavingThrowTypes.contains(this),
    )
}

private fun Skill.asState(): SkillState {
    return SkillState(
        key = index,
        modifier = modifier,
        name = name,
    )
}

private fun Damage.asState(
    strings: MonsterRegistrationStrings,
    filteredDamageTypes: List<DamageType>,
): DamageState {
    return DamageState(
        key = index,
        selectedIndex = type.ordinal,
        otherName = name.takeIf { type == DamageType.OTHER },
        typeOptions = DamageType.entries.map {
            it.asState(strings, filteredDamageTypes)
        },
    )
}

private fun DamageType.asState(
    strings: MonsterRegistrationStrings,
    filteredDamageTypes: List<DamageType>,
): TypeState {
    return TypeState(
        index = ordinal,
        name = name(strings),
        enabled = filteredDamageTypes.contains(this),
    )
}

private fun Condition.asState(
    strings: MonsterRegistrationStrings,
    filteredConditionTypes: List<ConditionType>,
): ConditionState {
    return ConditionState(
        key = index,
        selectedIndex = type.ordinal,
        typeOptions = ConditionType.entries.map {
            it.asState(strings, filteredConditionTypes)
        },
    )
}

private fun ConditionType.asState(
    strings: MonsterRegistrationStrings,
    filteredConditionTypes: List<ConditionType>,
): TypeState {
    return TypeState(
        index = ordinal,
        name = name(strings),
        enabled = filteredConditionTypes.contains(this),
    )
}

private fun AbilityDescription.asState(): AbilityDescriptionState {
    return AbilityDescriptionState(
        key = index,
        name = name,
        description = description,
    )
}

private fun Action.asState(strings: MonsterRegistrationStrings): ActionState {
    return ActionState(
        key = id,
        damageDices = damageDices.map { it.asState(strings) },
        attackBonus = attackBonus,
        abilityDescription = abilityDescription.asState(),
    )
}

private fun DamageDice.asState(strings: MonsterRegistrationStrings): DamageDiceState {
    return DamageDiceState(
        key = index,
        dice = dice,
        damage = damage.asState(strings, DamageType.entries),
    )
}

private fun Spellcasting.asState(strings: MonsterRegistrationStrings): SpellcastingState {
    return SpellcastingState(
        key = index,
        selectedIndex = SpellcastingType.entries.indexOf(type),
        description = description,
        spellsByGroup = usages.map { it.asState() },
        options = SpellcastingType.entries.map { it.name(strings) },
    )
}

private fun SpellUsage.asState(): SpellsByGroupState {
    return SpellsByGroupState(
        key = index,
        group = group,
        spells = spells.map { it.asState() },
    )
}

private fun SpellPreview.asState(): SpellPreviewState {
    return SpellPreviewState(
        index = index,
        name = name,
    )
}

private fun MonsterType.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        MonsterType.ABERRATION -> strings.aberration
        MonsterType.BEAST -> strings.beast
        MonsterType.CELESTIAL -> strings.celestial
        MonsterType.CONSTRUCT -> strings.construct
        MonsterType.DRAGON -> strings.dragon
        MonsterType.ELEMENTAL -> strings.elemental
        MonsterType.FEY -> strings.fey
        MonsterType.FIEND -> strings.fiend
        MonsterType.GIANT -> strings.giant
        MonsterType.HUMANOID -> strings.humanoid
        MonsterType.MONSTROSITY -> strings.monstrosity
        MonsterType.OOZE -> strings.ooze
        MonsterType.PLANT -> strings.plant
        MonsterType.UNDEAD -> strings.undead
    }
}

private fun SpeedType.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        SpeedType.WALK -> strings.speedTypeWalk
        SpeedType.BURROW -> strings.speedTypeBurrow
        SpeedType.CLIMB -> strings.speedTypeClimb
        SpeedType.FLY -> strings.speedTypeFly
        SpeedType.SWIM -> strings.speedTypeSwim
    }
}

private fun AbilityScoreType.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        AbilityScoreType.STRENGTH -> strings.strength
        AbilityScoreType.DEXTERITY -> strings.dexterity
        AbilityScoreType.CONSTITUTION -> strings.constitution
        AbilityScoreType.INTELLIGENCE -> strings.intelligence
        AbilityScoreType.WISDOM -> strings.wisdom
        AbilityScoreType.CHARISMA -> strings.charisma
    }
}

private fun ConditionType.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        ConditionType.BLINDED -> strings.conditionTypeBlinded
        ConditionType.CHARMED -> strings.conditionTypeCharmed
        ConditionType.DEAFENED -> strings.conditionTypeDeafened
        ConditionType.EXHAUSTION -> strings.conditionTypeExhaustion
        ConditionType.FRIGHTENED -> strings.conditionTypeFrightened
        ConditionType.GRAPPLED -> strings.conditionTypeGrappled
        ConditionType.PARALYZED -> strings.conditionTypeParalyzed
        ConditionType.PETRIFIED -> strings.conditionTypePetrified
        ConditionType.POISONED -> strings.conditionTypePoisoned
        ConditionType.PRONE -> strings.conditionTypeProne
        ConditionType.RESTRAINED -> strings.conditionTypeRestrained
        ConditionType.STUNNED -> strings.conditionTypeStunned
        ConditionType.UNCONSCIOUS -> strings.conditionTypeUnconscious
    }
}

internal fun DamageType.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        DamageType.ACID -> strings.damageTypeAcid
        DamageType.BLUDGEONING -> strings.damageTypeBludgeoning
        DamageType.COLD -> strings.damageTypeCold
        DamageType.FIRE -> strings.damageTypeFire
        DamageType.LIGHTNING -> strings.damageTypeLightning
        DamageType.NECROTIC -> strings.damageTypeNecrotic
        DamageType.PIERCING -> strings.damageTypePiercing
        DamageType.POISON -> strings.damageTypePoison
        DamageType.PSYCHIC -> strings.damageTypePsychic
        DamageType.RADIANT -> strings.damageTypeRadiant
        DamageType.SLASHING -> strings.damageTypeSlashing
        DamageType.THUNDER -> strings.damageTypeThunder
        DamageType.OTHER -> strings.damageTypeOther
    }
}

private fun SpellcastingType.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        SpellcastingType.SPELLCASTER -> strings.spellcastingCasterType
        SpellcastingType.INNATE -> strings.spellcastingInnateType
    }
}

internal fun SectionTitle.name(strings: MonsterRegistrationStrings): String {
    return when (this) {
        SectionTitle.Header -> strings.edit
        SectionTitle.Stats -> strings.stats
        SectionTitle.Speed -> strings.speed
        SectionTitle.AbilityScores -> strings.abilityScores
        SectionTitle.SavingThrows -> strings.savingThrows
        SectionTitle.Skills -> strings.skills
        SectionTitle.DamageVulnerabilities -> strings.damageVulnerabilities
        SectionTitle.DamageResistances -> strings.damageResistances
        SectionTitle.DamageImmunities -> strings.damageImmunities
        SectionTitle.ConditionImmunities -> strings.conditionImmunities
        SectionTitle.Senses -> strings.senses
        SectionTitle.Languages -> strings.languages
        SectionTitle.SpecialAbilities -> strings.specialAbilities
        SectionTitle.Actions -> strings.actions
        SectionTitle.Reactions -> strings.reactions
        SectionTitle.LegendaryActions -> strings.legendaryActions
        SectionTitle.Spellcastings -> strings.spells
    }
}

private fun MonsterState.createKeys(): List<String> {
    val monster = this
    return buildList {
        add(SectionTitle.Header.name)
        add("monsterHeader-name")
        add("monsterHeader-subtitle")
        add("monsterHeader-group")
        add("monsterHeader-imageUrl")
        add("monsterHeader-imageBackgroundColor")
        add("monsterHeader-type")
        add(SectionTitle.Stats.name)
        add("stats-armorClass")
        add("stats-hitPoints")
        add("stats-hitDice")
        monster.speedValues.createDynamicFormKeys(
            key = SectionTitle.Speed,
            getItemKey = { it.key },
            addKeys = {
                add("name")
                add("value")
            }
        ).also { addAll(it) }
        add(SectionTitle.AbilityScores.name)
        monster.abilityScores.forEachIndexed { index, _ ->
            add("abilityScores-$index")
        }
        monster.savingThrows.createDynamicFormKeys(
            key = SectionTitle.SavingThrows,
            getItemKey = { it.key },
            addKeys = {
                add("type")
                add("modifier")
            }
        ).also { addAll(it) }
        monster.skills.createDynamicFormKeys(
            key = SectionTitle.Skills,
            getItemKey = { it.key },
            addKeys = {
                add("name")
                add("value")
            }
        ).also { addAll(it) }
        monster.damageVulnerabilities.createDynamicFormKeys(
            key = SectionTitle.DamageVulnerabilities,
            getItemKey = { it.key },
            addKeys = {
                add("type")
                add("otherValue")
            }
        ).also { addAll(it) }
        monster.damageResistances.createDynamicFormKeys(
            key = SectionTitle.DamageResistances,
            getItemKey = { it.key },
            addKeys = {
                add("type")
                add("otherValue")
            }
        ).also { addAll(it) }
        monster.damageImmunities.createDynamicFormKeys(
            key = SectionTitle.DamageImmunities,
            getItemKey = { it.key },
            addKeys = {
                add("type")
                add("otherValue")
            }
        ).also { addAll(it) }
        monster.conditionImmunities.createDynamicFormKeys(
            key = SectionTitle.ConditionImmunities,
            getItemKey = { it.key },
            addKeys = { add("type") }
        ).also { addAll(it) }
        add(SectionTitle.Senses.name)
        add("senses-value")
        add(SectionTitle.Languages.name)
        add("languages-value")
        monster.specialAbilities.createDynamicFormKeys(
            key = SectionTitle.SpecialAbilities,
            getItemKey = { it.key },
            addKeys = {
                add("name")
                add("description")
            }
        ).also { addAll(it) }
        monster.actions.createDynamicFormKeys(
            key = SectionTitle.Actions,
            getItemKey = { it.key },
            addKeys = { action ->
                add("name")
                add("description")
                add("attackBonus")
                action.damageDices.createDynamicFormKeys(
                    key = SectionTitle.Actions,
                    getItemKey = { it.key },
                    hasTitle = false,
                    addKeys = {
                        add("type")
                        add("value")
                    }
                ).also { addAll(it) }
            }
        ).also { addAll(it) }
        monster.reactions.createDynamicFormKeys(
            key = SectionTitle.Reactions,
            getItemKey = { it.key },
            addKeys = {
                add("name")
                add("description")
            }
        ).also { addAll(it) }
        monster.legendaryActions.createDynamicFormKeys(
            key = SectionTitle.LegendaryActions,
            getItemKey = { it.key },
            addKeys = { action ->
                add("name")
                add("description")
                add("attackBonus")
                action.damageDices.createDynamicFormKeys(
                    key = SectionTitle.LegendaryActions,
                    getItemKey = { it.key },
                    hasTitle = false,
                    addKeys = {
                        add("type")
                        add("value")
                    }
                ).also { addAll(it) }
            }
        ).also { addAll(it) }
        monster.spellcastings.createDynamicFormKeys(
            key = SectionTitle.Spellcastings,
            getItemKey = { it.key },
            addKeys = { spellcasting ->
                add("name")
                add("description")
                spellcasting.spellsByGroup.createDynamicFormKeys(
                    key = "spellcastings-usages",
                    getItemKey = { it.key },
                    hasTitle = false,
                    addKeys = { usage ->
                        add("value")
                        usage.spells.createDynamicFormKeys(
                            key = "spellcastings-usages-spells",
                            getItemKey = { it.index },
                            hasTitle = false,
                            addKeys = {
                                add("value")
                            }
                        ).also { addAll(it) }
                    }
                ).also { addAll(it) }
            }
        ).also { addAll(it) }
    }
}

private fun <T> List<T>.createDynamicFormKeys(
    key: Any,
    getItemKey: (T) -> String,
    hasTitle: Boolean = true,
    addKeys: MutableList<String>.(T) -> Unit,
): List<String> {
    val list = this
    return buildList {
        if (hasTitle) add("$key")
        add("$key-add-remove-buttons")
        list.forEach { item ->
            mutableListOf<String>().also {
                it.addKeys(item)
            }.forEach {
                add("$key-$it-${getItemKey(item)}")
            }
            add("$key-add-remove-buttons-${getItemKey(item)}")
        }
    }
}
