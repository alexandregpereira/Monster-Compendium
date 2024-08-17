package br.alexandregpereira.hunter.monster.lore.registration.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import br.alexandregpereira.hunter.dynamicFormulary.changeAt
import br.alexandregpereira.hunter.monster.lore.registration.MonsterLoreEntryState
import br.alexandregpereira.hunter.monster.lore.registration.MonsterLoreRegistrationState
import br.alexandregpereira.hunter.ui.compose.AppTextField
import br.alexandregpereira.hunter.ui.compose.form.FormItems
import br.alexandregpereira.hunter.ui.compose.form.FormLazy
import br.alexandregpereira.hunter.ui.compose.form.formItem

@Composable
internal fun MonsterRegistrationScreen(
    state: MonsterLoreRegistrationState,
    onChanged: (List<MonsterLoreEntryState>) -> Unit
) {
    val entries = state.entries
    val keys = state.keys.takeUnless { it.hasNext().not() } ?: return
    LazyColumn {
        FormLazy(
            titleKey = keys.next(),
            title = { "Monster Lore Registration" },
        ) {
            val mutableEntries = entries.toMutableList()
            FormItems(
                items = mutableEntries,
                addText = { "Add Entry" },
                removeText = { "Remove Entry" },
                keys = keys,
                createNew = { MonsterLoreEntryState() },
                onChanged = onChanged
            ) { index, entry ->
                formItem(key = keys.next()) {
                    AppTextField(
                        text = entry.title.orEmpty(),
                        label = "Title",
                        onValueChange = {
                            onChanged(
                                mutableEntries.changeAt(index) {
                                    entry.copy(
                                        title = it
                                    )
                                }
                            )
                        }
                    )
                }

                formItem(key = keys.next()) {
                    AppTextField(
                        text = entry.description,
                        label = "Description",
                        multiline = true,
                        onValueChange = {
                            onChanged(
                                mutableEntries.changeAt(index) {
                                    entry.copy(
                                        description = it
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}
