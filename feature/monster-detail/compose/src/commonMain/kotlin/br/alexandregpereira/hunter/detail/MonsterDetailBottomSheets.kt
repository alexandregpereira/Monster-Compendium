package br.alexandregpereira.hunter.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.strings
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.ui.compose.ConfirmationBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormField
import org.koin.compose.koinInject

@Composable
fun MonsterDetailBottomSheets() {
    val stateHolder: MonsterDetailStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()

    val maxWidth: Dp = Dp.Unspecified
    val widthFraction = .3f

    MonsterDetailOptionPicker(
        options = state.options,
        showOptions = state.showOptions,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        onOptionSelected = stateHolder::onOptionClicked,
        onClosed = stateHolder::onShowOptionsClosed,
    )

    FormBottomSheet(
        title = strings.clone,
        formFields = listOf(
            FormField.Text(
                key = "monsterName",
                label = strings.cloneMonsterName,
                value = state.monsterCloneName,
            )
        ),
        buttonText = strings.save,
        opened = state.showCloneForm,
        buttonEnabled = state.monsterCloneName.isNotBlank(),
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        onFormChanged = remember(stateHolder) {
            { stateHolder.onCloneFormChanged(it.stringValue) }
        },
        onClosed = stateHolder::onCloneFormClosed,
        onSaved = stateHolder::onCloneFormSaved,
    )

    ConfirmationBottomSheet(
        show = state.showDeleteConfirmation,
        description = strings.deleteQuestion,
        buttonText = strings.deleteConfirmation,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        onConfirmed = stateHolder::onDeleteConfirmed,
        onClosed = stateHolder::onDeleteClosed
    )

    ConfirmationBottomSheet(
        show = state.showResetConfirmation,
        description = strings.resetQuestion,
        buttonText = strings.resetConfirmation,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        onConfirmed = stateHolder::onResetConfirmed,
        onClosed = stateHolder::onResetClosed
    )
}