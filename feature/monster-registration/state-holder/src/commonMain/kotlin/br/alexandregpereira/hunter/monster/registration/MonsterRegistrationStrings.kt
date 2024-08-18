package br.alexandregpereira.hunter.monster.registration

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.Language

interface MonsterRegistrationStrings {
    val savingThrows: String
    val strength: String
    val dexterity: String
    val constitution: String
    val intelligence: String
    val wisdom: String
    val charisma: String
    val actions: String
    val aberration: String
    val beast: String
    val celestial: String
    val construct: String
    val dragon: String
    val elemental: String
    val fey: String
    val fiend: String
    val giant: String
    val humanoid: String
    val monstrosity: String
    val ooze: String
    val plant: String
    val undead: String
    val damageTypeAcid: String
    val damageTypeBludgeoning: String
    val damageTypeCold: String
    val damageTypeFire: String
    val damageTypeLightning: String
    val damageTypeNecrotic: String
    val damageTypePiercing: String
    val damageTypePoison: String
    val damageTypePsychic: String
    val damageTypeRadiant: String
    val damageTypeSlashing: String
    val damageTypeThunder: String
    val damageTypeOther: String
    val conditionTypeBlinded: String
    val conditionTypeCharmed: String
    val conditionTypeDeafened: String
    val conditionTypeExhaustion: String
    val conditionTypeFrightened: String
    val conditionTypeGrappled: String
    val conditionTypeParalyzed: String
    val conditionTypePetrified: String
    val conditionTypePoisoned: String
    val conditionTypeProne: String
    val conditionTypeRestrained: String
    val conditionTypeStunned: String
    val conditionTypeUnconscious: String
    val name: String
    val subtitle: String
    val description: String
    val abilityScores: String
    val attackBonus: String
    val damageType: String
    val damageDice: String
    val conditionType: String
    val header: String
    val group: String
    val imageUrl: String
    val type: String
    val speed: String
    val speedType: String
    val spells: String
    val workInProgress: String
    val stats: String
    val armorClass: String
    val hitPoints: String
    val hitDice: String
    val skills: String
    val damageVulnerabilities: String
    val damageResistances: String
    val damageImmunities: String
    val conditionImmunities: String
    val senses: String
    val languages: String
    val specialAbilities: String
    val reactions: String
    val legendaryActions: String
    val imageBackgroundColorLight: String
    val imageBackgroundColorDark: String
    val speedTypeWalk: String
    val speedTypeFly: String
    val speedTypeSwim: String
    val speedTypeClimb: String
    val speedTypeBurrow: String
    val spellcastingCasterType: String
    val spellcastingInnateType: String
    val spellcastingTypeLabel: String
    val spellGroup: String
    val spellLabel: String
    val addSpellGroup: String
    val removeSpellGroup: String
    val addSpell: String
    val removeSpell: String
    val addSpellcastingType: String
    val removeSpellcastingType: String
    val addDamageDice: String
    val removeDamageDice: String
    val addAction: String
    val removeAction: String
    val addSpecialAbility: String
    val removeSpecialAbility: String
    val addReaction: String
    val removeReaction: String
    val add: String
    val remove: String
    val imageFormTitle: String
    val imageHorizontalSwitchLabel: String
    val darkThemeSwitchLabel: String
    val imageProportion: (String) -> String
    val challengeRating: String
}

internal data class MonsterRegistrationEnStrings(
    override val savingThrows: String = "Saving Throws",
    override val strength: String = "Strength",
    override val dexterity: String = "Dexterity",
    override val constitution: String = "Constitution",
    override val intelligence: String = "Intelligence",
    override val wisdom: String = "Wisdom",
    override val charisma: String = "Charisma",
    override val actions: String = "Actions",
    override val aberration: String = "Aberration",
    override val beast: String = "Beast",
    override val celestial: String = "Celestial",
    override val construct: String = "Construct",
    override val dragon: String = "Dragon",
    override val elemental: String = "Elemental",
    override val fey: String = "Fey",
    override val fiend: String = "Fiend",
    override val giant: String = "Giant",
    override val humanoid: String = "Humanoid",
    override val monstrosity: String = "Monstrosity",
    override val ooze: String = "Ooze",
    override val plant: String = "Plant",
    override val undead: String = "Undead",
    override val damageTypeAcid: String = "Acid",
    override val damageTypeBludgeoning: String = "Bludgeoning",
    override val damageTypeCold: String = "Cold",
    override val damageTypeFire: String = "Fire",
    override val damageTypeLightning: String = "Lightning",
    override val damageTypeNecrotic: String = "Necrotic",
    override val damageTypePiercing: String = "Piercing",
    override val damageTypePoison: String = "Poison",
    override val damageTypePsychic: String = "Psychic",
    override val damageTypeRadiant: String = "Radiant",
    override val damageTypeSlashing: String = "Slashing",
    override val damageTypeThunder: String = "Thunder",
    override val damageTypeOther: String = "Other",
    override val conditionTypeBlinded: String = "Blinded",
    override val conditionTypeCharmed: String = "Charmed",
    override val conditionTypeDeafened: String = "Deafened",
    override val conditionTypeExhaustion: String = "Exhaustion",
    override val conditionTypeFrightened: String = "Frightened",
    override val conditionTypeGrappled: String = "Grappled",
    override val conditionTypeParalyzed: String = "Paralyzed",
    override val conditionTypePetrified: String = "Petrified",
    override val conditionTypePoisoned: String = "Poisoned",
    override val conditionTypeProne: String = "Prone",
    override val conditionTypeRestrained: String = "Restrained",
    override val conditionTypeStunned: String = "Stunned",
    override val conditionTypeUnconscious: String = "Unconscious",
    override val name: String = "Name",
    override val subtitle: String = "Subtitle",
    override val description: String = "Description",
    override val abilityScores: String = "Ability Scores",
    override val attackBonus: String = "Attack Bonus",
    override val damageType: String = "Damage Type",
    override val damageDice: String = "Damage Dice",
    override val conditionType: String = "Condition Type",
    override val header: String = "Header",
    override val group: String = "Group",
    override val imageUrl: String = "Image Url",
    override val type: String = "Type",
    override val speed: String = "Speed",
    override val speedType: String = "Speed type",
    override val spells: String = "Spells",
    override val workInProgress: String = "Work in Progress",
    override val stats: String = "Stats",
    override val armorClass: String = "Armor Class",
    override val hitPoints: String = "Hit Points",
    override val hitDice: String = "Hit Dice",
    override val skills: String = "Skills",
    override val damageVulnerabilities: String = "Damage Vulnerabilities",
    override val damageResistances: String = "Damage Resistances",
    override val damageImmunities: String = "Damage Immunities",
    override val conditionImmunities: String = "Condition Immunities",
    override val senses: String = "Senses",
    override val languages: String = "Languages",
    override val specialAbilities: String = "Special Abilities",
    override val reactions: String = "Reactions",
    override val legendaryActions: String = "Legendary Actions",
    override val imageBackgroundColorLight: String = "Background Color Light",
    override val imageBackgroundColorDark: String = "Background Color Dark",
    override val speedTypeWalk: String = "Speed",
    override val speedTypeFly: String = "Fly",
    override val speedTypeSwim: String = "Swim",
    override val speedTypeClimb: String = "Climb",
    override val speedTypeBurrow: String = "Burrow",
    override val spellcastingCasterType: String = "Spellcaster",
    override val spellcastingInnateType: String = "Innate Spellcaster",
    override val spellcastingTypeLabel: String = "Spellcasting Type",
    override val spellGroup: String = "Spell Group",
    override val spellLabel: String = "Spell",
    override val addSpellGroup: String = "Add group",
    override val removeSpellGroup: String = "Remove group",
    override val addSpell: String = "Add spell",
    override val removeSpell: String = "Remove spell",
    override val addSpellcastingType: String = "Add spellcasting",
    override val removeSpellcastingType: String = "Remove spellcasting",
    override val addDamageDice: String = "Add damage",
    override val removeDamageDice: String = "Remove damage",
    override val addAction: String = "Add action",
    override val removeAction: String = "Remove action",
    override val addSpecialAbility: String = "Add ability",
    override val removeSpecialAbility: String = "Remove ability",
    override val addReaction: String = "Add reaction",
    override val removeReaction: String = "Remove reaction",
    override val add: String = "Add",
    override val remove: String = "Remove",
    override val imageFormTitle: String = "Image",
    override val imageHorizontalSwitchLabel: String = "Landscape Image",
    override val darkThemeSwitchLabel: String = "Preview Dark Theme",
    override val imageProportion: (String) -> String = { "Proportion - $it" },
    override val challengeRating: String = "Challenge Rating",
) : MonsterRegistrationStrings

internal data class MonsterRegistrationPtStrings(
    override val savingThrows: String = "Salvaguardas",
    override val strength: String = "Força",
    override val dexterity: String = "Destreza",
    override val constitution: String = "Constituição",
    override val intelligence: String = "Inteligência",
    override val wisdom: String = "Sabedoria",
    override val charisma: String = "Carisma",
    override val actions: String = "Ações",
    override val aberration: String = "Aberração",
    override val beast: String = "Fera",
    override val celestial: String = "Celestial",
    override val construct: String = "Construto",
    override val dragon: String = "Dragão",
    override val elemental: String = "Elemental",
    override val fey: String = "Fada",
    override val fiend: String = "Diabo/Demônio",
    override val giant: String = "Gigante",
    override val humanoid: String = "Humanoide",
    override val monstrosity: String = "Monstrosidade",
    override val ooze: String = "Gosma",
    override val plant: String = "Planta",
    override val undead: String = "Morto-vivo",
    override val damageTypeAcid: String = "Ácido",
    override val damageTypeBludgeoning: String = "Contundente",
    override val damageTypeCold: String = "Frio",
    override val damageTypeFire: String = "Fogo",
    override val damageTypeLightning: String = "Elétrico",
    override val damageTypeNecrotic: String = "Necrótico",
    override val damageTypePiercing: String = "Perfurante",
    override val damageTypePoison: String = "Veneno",
    override val damageTypePsychic: String = "Psíquico",
    override val damageTypeRadiant: String = "Radiante",
    override val damageTypeSlashing: String = "Cortante",
    override val damageTypeThunder: String = "Trovão",
    override val damageTypeOther: String = "Outro",
    override val conditionTypeBlinded: String = "Cego",
    override val conditionTypeCharmed: String = "Encantado",
    override val conditionTypeDeafened: String = "Surdo",
    override val conditionTypeExhaustion: String = "Exausto",
    override val conditionTypeFrightened: String = "Amendrotado",
    override val conditionTypeGrappled: String = "Agarrado",
    override val conditionTypeParalyzed: String = "Paralisado",
    override val conditionTypePetrified: String = "Petrificado",
    override val conditionTypePoisoned: String = "Envenenado",
    override val conditionTypeProne: String = "Caído",
    override val conditionTypeRestrained: String = "Contido",
    override val conditionTypeStunned: String = "Atordoado",
    override val conditionTypeUnconscious: String = "Inconsciente",
    override val name: String = "Nome",
    override val subtitle: String = "Subtítutlo",
    override val description: String = "Descrição",
    override val abilityScores: String = "Atributos",
    override val attackBonus: String = "Ataque Bônus",
    override val damageType: String = "Tipo de Dano",
    override val damageDice: String = "Dados de Dano",
    override val conditionType: String = "Tipo de Condição",
    override val header: String = "Cabeçalho",
    override val group: String = "Grupo",
    override val imageUrl: String = "Url da Imagem",
    override val type: String = "Tipo",
    override val speed: String = "Deslocamento",
    override val speedType: String = "Tipo de Deslocamento",
    override val spells: String = "Magias",
    override val workInProgress: String = "Em construção",
    override val stats: String = "Stats",
    override val armorClass: String = "Classe de Armadura",
    override val hitPoints: String = "Pontos de Vida",
    override val hitDice: String = "Dados de Vida",
    override val skills: String = "Perícias",
    override val damageVulnerabilities: String = "Vulnerabilidades a Danos",
    override val damageResistances: String = "Resistências a Danos",
    override val damageImmunities: String = "Imunidades a Danos",
    override val conditionImmunities: String = "Imunidades à Condição",
    override val senses: String = "Sentidos",
    override val languages: String = "Idiomas",
    override val specialAbilities: String = "Habilidades Especiais",
    override val reactions: String = "Reações",
    override val legendaryActions: String = "Ações Lendárias",
    override val imageBackgroundColorLight: String = "Cor de Fundo Light",
    override val imageBackgroundColorDark: String = "Cor de Fundo Dark",
    override val speedTypeWalk: String = "Deslocamento",
    override val speedTypeFly: String = "Voo",
    override val speedTypeSwim: String = "Natação",
    override val speedTypeClimb: String = "Escalagem",
    override val speedTypeBurrow: String = "Escavação",
    override val spellcastingCasterType: String = "Conjuração",
    override val spellcastingInnateType: String = "Conjuração Inata",
    override val spellcastingTypeLabel: String = "Tipo de Conjuração",
    override val spellGroup: String = "Grupo de Magia",
    override val spellLabel: String = "Magia",
    override val addSpellGroup: String = "Adicionar grupo",
    override val removeSpellGroup: String = "Remover grupo",
    override val addSpell: String = "Adicionar magia",
    override val removeSpell: String = "Remover magia",
    override val addSpellcastingType: String = "Adicionar conjuração",
    override val removeSpellcastingType: String = "Remover conjuração",
    override val addDamageDice: String = "Adicionar dano",
    override val removeDamageDice: String = "Remover dano",
    override val addAction: String = "Adicionar ação",
    override val removeAction: String = "Remover ação",
    override val addSpecialAbility: String = "Adicionar habilidade",
    override val removeSpecialAbility: String = "Remover habilidade",
    override val addReaction: String = "Adicionar reação",
    override val removeReaction: String = "Remover reação",
    override val add: String = "Adicionar",
    override val remove: String = "Remover",
    override val imageFormTitle: String = "Imagem",
    override val imageHorizontalSwitchLabel: String = "Imagem Landscape",
    override val darkThemeSwitchLabel: String = "Pré visualizar em Tema Escuro",
    override val imageProportion: (String) -> String = { "Proporção - $it" },
    override val challengeRating: String = "Nível de Desafio",
) : MonsterRegistrationStrings

fun MonsterRegistrationStrings(): MonsterRegistrationStrings = MonsterRegistrationEnStrings()

internal fun AppLocalization.getStrings(): MonsterRegistrationStrings {
    return when (getLanguage()) {
        Language.ENGLISH -> MonsterRegistrationEnStrings()
        Language.PORTUGUESE -> MonsterRegistrationPtStrings()
    }
}
