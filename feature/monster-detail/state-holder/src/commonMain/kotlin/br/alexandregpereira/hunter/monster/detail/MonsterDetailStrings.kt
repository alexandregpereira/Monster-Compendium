package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterDetailStrings {
    val armorClass: String
    val speedTitle: String
    val speedHover: String
    val vulnerabilities: String
    val resistances: String
    val immunities: String
    val conditionImmunities: String
    val savingThrows: String
    val skills: String
    val senses: String
    val languages: String
    val specialAbilities: String
    val actions: String
    val legendaryActions: String
    val attack: String
    val options: String
    val goToTop: String
    val reactions: String
    val spells: String
    val spellcasting: String
    val innateSpellcasting: String
    val abilityScores: String
    val optionsAddToFolder: String
    val optionsChangeToFeet: String
    val optionsChangeToMeters: String
    val savingThrowStrength: String
    val savingThrowDexterity: String
    val savingThrowConstitution: String
    val savingThrowIntelligence: String
    val savingThrowWisdom: String
    val savingThrowCharisma: String
    val clone: String
    val cloneMonsterName: String
    val edit: String
    val delete: String
    val deleteQuestion: String
    val deleteConfirmation: String
    val save: String
}

data class MonsterDetailEnStrings(
    override val armorClass: String = "Armor Class",
    override val speedTitle: String = "Speed",
    override val speedHover: String = "Hover",
    override val vulnerabilities: String = "Vulnerabilities",
    override val resistances: String = "Resistances",
    override val immunities: String = "Immunities",
    override val conditionImmunities: String = "Condition Immunities",
    override val savingThrows: String = "Saving Throws",
    override val skills: String = "Skills",
    override val senses: String = "Senses",
    override val languages: String = "Languages",
    override val specialAbilities: String = "Special Abilities",
    override val actions: String = "Actions",
    override val legendaryActions: String = "Legendary Actions",
    override val attack: String = "Attack",
    override val options: String = "Options",
    override val goToTop: String = "Go to top",
    override val reactions: String = "Reactions",
    override val spells: String = "Spells",
    override val spellcasting: String = "Spellcasting",
    override val innateSpellcasting: String = "Innate Spellcasting",
    override val abilityScores: String = "Ability Scores",
    override val optionsAddToFolder: String = "Add to Folder",
    override val optionsChangeToFeet: String = "Change to Feet",
    override val optionsChangeToMeters: String = "Change to Meters",
    override val savingThrowStrength: String = "Strength",
    override val savingThrowDexterity: String = "Dexterity",
    override val savingThrowConstitution: String = "Constitution",
    override val savingThrowIntelligence: String = "Intelligence",
    override val savingThrowWisdom: String = "Wisdom",
    override val savingThrowCharisma: String = "Charisma",
    override val clone: String = "Clone",
    override val cloneMonsterName: String = "Name",
    override val edit: String = "Edit",
    override val delete: String = "Delete",
    override val deleteQuestion: String = "Are you sure you want to delete this monster?",
    override val deleteConfirmation: String = "I'm sure",
    override val save: String = "Save",
) : MonsterDetailStrings

internal data class MonsterDetailPtStrings(
    override val armorClass: String = "Classe de Armadura",
    override val speedTitle: String = "Deslocamento",
    override val speedHover: String = "Pairar",
    override val vulnerabilities: String = "Vulnerabilidades",
    override val resistances: String = "Resistências",
    override val immunities: String = "Imunidades",
    override val conditionImmunities: String = "Imunidades à Condição",
    override val savingThrows: String = "Salvaguardas",
    override val skills: String = "Perícias",
    override val senses: String = "Sentidos",
    override val languages: String = "Idiomas",
    override val specialAbilities: String = "Habilidades Especiais",
    override val actions: String = "Ações",
    override val legendaryActions: String = "Ações Lendárias",
    override val attack: String = "Ataque",
    override val options: String = "Opções",
    override val goToTop: String = "Ir para o Topo",
    override val reactions: String = "Reações",
    override val spells: String = "Magias",
    override val spellcasting: String = "Conjuração",
    override val innateSpellcasting: String = "Conjuração Inata",
    override val abilityScores: String = "Atributos",
    override val optionsAddToFolder: String = "Adicionar à Pasta",
    override val optionsChangeToFeet: String = "Mudar para Pés",
    override val optionsChangeToMeters: String = "Mudar para Metros",
    override val savingThrowStrength: String = "Força",
    override val savingThrowDexterity: String = "Destreza",
    override val savingThrowConstitution: String = "Constituição",
    override val savingThrowIntelligence: String = "Inteligência",
    override val savingThrowWisdom: String = "Sabedoria",
    override val savingThrowCharisma: String = "Carisma",
    override val clone: String = "Clonar",
    override val cloneMonsterName: String = "Nome",
    override val edit: String = "Editar",
    override val delete: String = "Remover",
    override val deleteQuestion: String = "Tem certeza que deseja excluir esse monstro?",
    override val deleteConfirmation: String = "Tenho certeza",
    override val save: String = "Salvar",
) : MonsterDetailStrings

internal fun AppLocalization.getStrings(): MonsterDetailStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterDetailEnStrings()
        Language.PORTUGUESE -> MonsterDetailPtStrings()
    }
}
