package br.alexandregpereira.hunter.monster.registration.ui.form

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.registration.R
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
                value = stringResource(monster.type.toMonsterTypeState().stringRes),
                options = MonsterType.entries.map {
                    stringResource(it.toMonsterTypeState().stringRes)
                },
            ),
        ),
        onFormChanged = { field ->
            when (field.key) {
                "monsterName" -> onMonsterChanged(monster.copy(name = field.stringValue))

                "group" -> onMonsterChanged(
                    monster.copy(group = field.stringValue.takeUnless { it.isBlank() })
                )

                "imageUrl" -> onMonsterChanged(
                    monster.copy(imageData = monster.imageData.copy(url = field.stringValue))
                )

                "type" -> onMonsterChanged(
                    monster.copy(
                        type = MonsterType.entries[field.selectedIndex]
                    )
                )
            }
        },
    )
}

private fun MonsterType.toMonsterTypeState(): MonsterTypeState {
    return when (this) {
        MonsterType.ABERRATION -> MonsterTypeState.ABERRATION
        MonsterType.BEAST -> MonsterTypeState.BEAST
        MonsterType.CELESTIAL -> MonsterTypeState.CELESTIAL
        MonsterType.CONSTRUCT -> MonsterTypeState.CONSTRUCT
        MonsterType.DRAGON -> MonsterTypeState.DRAGON
        MonsterType.ELEMENTAL -> MonsterTypeState.ELEMENTAL
        MonsterType.FEY -> MonsterTypeState.FEY
        MonsterType.FIEND -> MonsterTypeState.FIEND
        MonsterType.GIANT -> MonsterTypeState.GIANT
        MonsterType.HUMANOID -> MonsterTypeState.HUMANOID
        MonsterType.MONSTROSITY -> MonsterTypeState.MONSTROSITY
        MonsterType.OOZE -> MonsterTypeState.OOZE
        MonsterType.PLANT -> MonsterTypeState.PLANT
        MonsterType.UNDEAD -> MonsterTypeState.UNDEAD
    }
}

enum class MonsterTypeState(@StringRes val stringRes: Int) {
    ABERRATION(R.string.monster_registration_aberration),
    BEAST(R.string.monster_registration_beast),
    CELESTIAL(R.string.monster_registration_celestial),
    CONSTRUCT(R.string.monster_registration_construct),
    DRAGON(R.string.monster_registration_dragon),
    ELEMENTAL(R.string.monster_registration_elemental),
    FEY(R.string.monster_registration_fey),
    FIEND(R.string.monster_registration_fiend),
    GIANT(R.string.monster_registration_giant),
    HUMANOID(R.string.monster_registration_humanoid),
    MONSTROSITY(R.string.monster_registration_monstrosity),
    OOZE(R.string.monster_registration_ooze),
    PLANT(R.string.monster_registration_plant),
    UNDEAD(R.string.monster_registration_undead)
}
