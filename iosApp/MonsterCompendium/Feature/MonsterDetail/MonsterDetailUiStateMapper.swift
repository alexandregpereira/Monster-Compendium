//
//  MonsterDetailUiStateMapper.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 25/03/23.
//

import Foundation
import shared

internal extension Array where Element == MonsterMonster {
    func asUiState() -> [MonsterDetailItemUiState] {
        return self.map { $0.asUiState() }
    }
}

internal extension MonsterMonster {
    func asUiState() -> MonsterDetailItemUiState {
        return MonsterDetailItemUiState(
            id: index,
            imageUrl: imageData.url,
            backgroundColorLight: imageData.backgroundColor.light,
            name: name,
            subtitle: subtitle,
            stats: StatsUiState(
                armorClass: Int(stats.armorClass),
                hitPoints: Int(stats.hitPoints),
                hitDice: stats.hitDice
            ),
            speed: SpeedUiState(
                hover: speed.hover,
                values: speed.values.map { $0.asUiState() }
            ),
            abilityScores: abilityScores.map { $0.asUiState() },
            savingThrows: savingThrows.map { $0.asSavingThrowUiState() },
            skills: skills.map { $0.asUiState() },
            damageVulnerabilities: damageVulnerabilities.map { $0.asUiState() },
            damageResistances: damageResistances.map { $0.asUiState() },
            damageImmunities: damageImmunities.map { $0.asUiState() },
            conditionImmunities: conditionImmunities.map { $0.asUiState() },
            senses: senses,
            languages: languages,
            specialAbilities: specialAbilities.map { $0.asUiState() },
            actions: actions.map { $0.asUiState() },
            legendaryActions: legendaryActions.map { $0.asUiState() },
            reactions: reactions.map { $0.asUiState() },
            spellcastings: spellcastings.map { $0.asUiState() },
            lore: lore.map {
                let loreSize = 180
                let ellipse = $0.count > loreSize ? "..." : ""
                return String($0.prefix(loreSize)) + ellipse
            } ?? ""
        )
    }
}

private extension MonsterSpeedValue {
    func asUiState() -> SpeedValueUiState {
        return SpeedValueUiState(
            name: type.name,
            valueFormatted: valueFormatted
        )
    }
}

private extension MonsterAbilityScore {
    func asUiState() -> AbilityScoreUiState {
        return AbilityScoreUiState(
            type: type.name,
            value: Int(value),
            modifier: Int(modifier)
        )
    }
}

private extension MonsterProficiency {
    func asUiState() -> ProficiencyUiState {
        return ProficiencyUiState(
            index: index,
            modifier: Int(modifier),
            name: name
        )
    }

    func asSavingThrowUiState() -> SavingThrowUiState {
        
        return SavingThrowUiState(
            index: index,
            modifier: Int(modifier),
            name: name
        )
    }
}

private extension MonsterDamage {
    func asUiState() -> DamageUiState {
        return DamageUiState(
            index: index,
            type: type.name,
            name: name
        )
    }
}

private extension MonsterCondition {
    func asUiState() -> ConditionUiState {
        return ConditionUiState(
            index: index,
            name: name
        )
    }
}

private extension MonsterAbilityDescription {
    func asUiState() -> AbilityDescriptionUiState {
        return AbilityDescriptionUiState(
            name: name,
            description: description
        )
    }
}

private extension MonsterAction {
    func asUiState() -> ActionUiState {
        return ActionUiState(
            damageDices: damageDices.map { $0.asUiState() },
            attackBonus: attackBonus.map { Int(truncating: $0) },
            abilityDescription: abilityDescription.asUiState()
        )
    }
}

private extension MonsterDamageDice {
    func asUiState() -> DamageDiceUiState {
        return DamageDiceUiState(
            dice: dice,
            damage: damage.asUiState()
        )
    }
}

private extension MonsterSpellcasting {
    func asUiState() -> SpellcastingUiState {
        return SpellcastingUiState(
            name: type.name,
            description: description,
            spellsByGroup: Dictionary(uniqueKeysWithValues: usages.map { spellUsage in
                (spellUsage.group, spellUsage.spells.map { spell in
                    SpellPreviewUiState(
                        index: spell.index,
                        name: spell.name,
                        school: spell.school.name
                    )
                })
            })
        )
    }
}
