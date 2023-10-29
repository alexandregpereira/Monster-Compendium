package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.selectedIndex

@Composable
internal fun MonsterHeaderForm(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = "Test",
        formFields = listOf(
            FormField.Text(
                key = "monsterName",
                label = "Name",
                value = monster.name,
            ),
            FormField.Text(
                key = "group",
                label = "Group",
                value = monster.group.orEmpty(),
            ),
            FormField.Text(
                key = "imageUrl",
                label = "Image Url",
                value = monster.imageData.url,
            ),
            FormField.Picker(
                key = "type",
                label = "Type",
                value = monster.type.name,
                options = MonsterType.entries.map { it.name },
            ),
        ),
        onFormChanged = { field ->
            when (field.key) {
                "monsterName" -> onMonsterChanged(monster.copy(name = field.stringValue))
                "group" -> onMonsterChanged(monster.copy(group = field.stringValue.takeUnless { it.isBlank() }))
                "imageUrl" -> onMonsterChanged(monster.copy(imageData = monster.imageData.copy(url = field.stringValue)))
                "type" -> onMonsterChanged(monster.copy(type = MonsterType.entries[field.selectedIndex]))
            }
        },
    )
}