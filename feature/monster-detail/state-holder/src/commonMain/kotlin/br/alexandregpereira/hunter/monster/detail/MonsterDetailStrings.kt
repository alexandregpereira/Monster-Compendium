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

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language
import kotlin.native.ObjCName

@ObjCName(name = "MonsterDetailStrings", exact = true)
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
    val resetToOriginal: String
    val resetQuestion: String
    val resetConfirmation: String
    val export: String
    val resetImage: String
    val resetImageQuestion: String
    val resetImageConfirmation: String
    val source: String
    val noInternetConnection: String
    val tryAgain: String
    val iGotIt: String
    val dc: String

}

internal data class MonsterDetailEnStrings(
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
    override val resetToOriginal: String = "Restore to original creature",
    override val resetQuestion: String = "Are you sure you want to restore this creature to its original state?",
    override val resetConfirmation: String = "I'm sure",
    override val export: String = "Share",
    override val resetImage: String = "Restore to default image",
    override val resetImageQuestion: String = "Are you sure you want to restore to default image?",
    override val resetImageConfirmation: String = "I'm sure",
    override val source: String = "Source",
    override val noInternetConnection: String = "No Internet Connection",
    override val tryAgain: String = "Try Again",
    override val iGotIt: String = "I Got It!",
    override val dc: String = "DC"
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
    override val resetToOriginal: String = "Restaurar para a criatura original",
    override val resetQuestion: String = "Tem certeza que deseja restaurar essa criatura para o estado original?",
    override val resetConfirmation: String = "Tenho certeza",
    override val export: String = "Compartilhar",
    override val resetImage: String = "Restaurar para a imagem padrão",
    override val resetImageQuestion: String = "Tem certeza que deseja restaurar para a imagem padrão?",
    override val resetImageConfirmation: String = "Tenho certeza",
    override val source: String = "Fonte",
    override val noInternetConnection: String = "Sem conexão com a Internet",
    override val tryAgain: String = "Tentar novamente",
    override val iGotIt: String = "Entendi!",
    override val dc: String = "CD"
) : MonsterDetailStrings

fun MonsterDetailStrings(): MonsterDetailStrings = MonsterDetailEnStrings()

internal data class MonsterDetailEsStrings(
    override val armorClass: String = "Clase de Armadura",
    override val speedTitle: String = "Velocidad",
    override val speedHover: String = "Levitar",
    override val vulnerabilities: String = "Vulnerabilidades",
    override val resistances: String = "Resistencias",
    override val immunities: String = "Inmunidades",
    override val conditionImmunities: String = "Inmunidades a Condiciones",
    override val savingThrows: String = "Tiradas de Salvación",
    override val skills: String = "Habilidades",
    override val senses: String = "Sentidos",
    override val languages: String = "Idiomas",
    override val specialAbilities: String = "Habilidades Especiales",
    override val actions: String = "Acciones",
    override val legendaryActions: String = "Acciones Legendarias",
    override val attack: String = "Ataque",
    override val options: String = "Opciones",
    override val goToTop: String = "Ir arriba",
    override val reactions: String = "Reacciones",
    override val spells: String = "Conjuros",
    override val spellcasting: String = "Lanzamiento de Conjuros",
    override val innateSpellcasting: String = "Lanzamiento de Conjuros Innato",
    override val abilityScores: String = "Puntuaciones de Habilidad",
    override val optionsAddToFolder: String = "Añadir a Carpeta",
    override val savingThrowStrength: String = "Fuerza",
    override val savingThrowDexterity: String = "Destreza",
    override val savingThrowConstitution: String = "Constitución",
    override val savingThrowIntelligence: String = "Inteligencia",
    override val savingThrowWisdom: String = "Sabiduría",
    override val savingThrowCharisma: String = "Carisma",
    override val clone: String = "Clonar",
    override val cloneMonsterName: String = "Nombre",
    override val edit: String = "Editar",
    override val delete: String = "Eliminar",
    override val deleteQuestion: String = "¿Estás seguro de que quieres eliminar esta criatura?",
    override val deleteConfirmation: String = "Estoy seguro",
    override val save: String = "Guardar",
    override val resetToOriginal: String = "Restaurar a criatura original",
    override val resetQuestion: String = "¿Estás seguro de que quieres restaurar esta criatura a su estado original?",
    override val resetConfirmation: String = "Estoy seguro",
    override val export: String = "Compartir",
    override val resetImage: String = "Restaurar a imagen por defecto",
    override val resetImageQuestion: String = "¿Estás seguro de que quieres restaurar a la imagen por defecto?",
    override val resetImageConfirmation: String = "Estoy seguro",
    override val source: String = "Fuente",
    override val noInternetConnection: String = "Sin Conexión a Internet",
    override val tryAgain: String = "Reintentar",
    override val iGotIt: String = "Entendido!",
    override val dc: String = "CD"
) : MonsterDetailStrings

internal fun AppLocalization.getStrings(): MonsterDetailStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterDetailEnStrings()
        Language.PORTUGUESE -> MonsterDetailPtStrings()
        Language.SPANISH -> MonsterDetailEsStrings()
    }
}
