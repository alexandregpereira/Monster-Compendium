//
//  MonsterUiUiState.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import Foundation

struct MonsterDetailItemUiState {
    var id: String = ""
    var imageUrl: String = ""
    var backgroundColorLight: String = ""
    var name: String = ""
    var subtitle: String = ""
    var stats: StatsUiState = StatsUiState()
    var speed: SpeedUiState = SpeedUiState()
    var abilityScores: [AbilityScoreUiState] = []
    var savingThrows: [SavingThrowUiState] = []
    var skills: [ProficiencyUiState] = []
    var damageVulnerabilities: [DamageUiState] = []
    var damageResistances: [DamageUiState] = []
    var damageImmunities: [DamageUiState] = []
    var conditionImmunities: [ConditionUiState] = []
    var senses: [String] = []
    var languages: String = ""
    var specialAbilities: [AbilityDescriptionUiState] = []
    var actions: [ActionUiState] = []
    var legendaryActions: [ActionUiState] = []
    var reactions: [AbilityDescriptionUiState] = []
    var spellcastings: [SpellcastingUiState] = []
    var lore: String = ""
}

struct StatsUiState {
    var armorClass: Int = 0
    var hitPoints: Int = 0
    var hitDice: String = ""
}

struct SpeedUiState {
    var hover: Bool = false
    var values: [SpeedValueUiState] = []
}

struct SpeedValueUiState {
    var name: String
    var valueFormatted: String
}

struct AbilityScoreUiState {
    var type: String
    var value: Int
    var modifier: Int
}

struct ProficiencyUiState {
    var index: String
    var modifier: Int
    var name: String
}

struct SavingThrowUiState {
    var index: String
    var modifier: Int
    var name: String
}

struct DamageUiState {
    var index: String
    var name: String
}

struct ConditionUiState {
    var index: String
    var name: String
}

struct AbilityDescriptionUiState {
    var name: String
    var description: String
}

struct ActionUiState {
    var damageDices: [DamageDiceUiState]
    var attackBonus: Int?
    var abilityDescription: AbilityDescriptionUiState
}

struct DamageDiceUiState {
    var dice: String
    var damage: DamageUiState
}

struct SpellcastingUiState {
    var name: String
    var description: String
    var spellsByGroup: [String: [SpellPreviewUiState]]
}

struct SpellPreviewUiState {
    var index: String
    var name: String
    var school: String
}
