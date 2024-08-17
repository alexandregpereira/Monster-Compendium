package br.alexandregpereira.hunter.monster.lore.registration

import br.alexandregpereira.hunter.dynamicFormulary.KeyIterator
import br.alexandregpereira.hunter.uuid.generateUUID

data class MonsterLoreRegistrationState(
    val entries: List<MonsterLoreEntryState> = emptyList(),
    val isSaveButtonEnabled: Boolean = false,
    val strings: MonsterLoreRegistrationStrings = MonsterLoreRegistrationStrings(),
    internal val keysList: List<String> = emptyList(),
) {
    val keys: Iterator<String> = KeyIterator(keysList)
}

data class MonsterLoreEntryState(
    val key: String = generateUUID(),
    val title: String? = null,
    val description: String = "",
)

