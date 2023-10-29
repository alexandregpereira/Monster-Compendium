package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.ui.compose.Form
import br.alexandregpereira.hunter.ui.compose.FormField

@Composable
internal fun MonsterForm1(
    monster: Monster,
    modifier: Modifier = Modifier,
    onMonsterChanged: (Monster) -> Unit = {}
) {
    Form(
        modifier = modifier,
        title = "Test",
        formFields = listOf(
            FormField(
                key = "monsterName",
                label = "Name",
                value = monster.name,
            ),
            FormField(
                key = "group",
                label = "Group",
                value = monster.group.orEmpty(),
            ),
            FormField(
                key = "imageUrl",
                label = "Image Url",
                value = monster.imageData.url,
            ),
        ),
        onFormChanged = { field ->
            when (field.key) {
                "monsterName" -> onMonsterChanged(monster.copy(name = field.value))
                "group" -> onMonsterChanged(monster.copy(group = field.value.takeUnless { it.isBlank() }))
                "imageUrl" -> onMonsterChanged(monster.copy(imageData = monster.imageData.copy(url = field.value)))
            }
        },
    )
}