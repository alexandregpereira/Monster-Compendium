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

package br.alexandregpereira.hunter.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.ui.MonsterDetailOptionPicker
import br.alexandregpereira.hunter.detail.ui.strings
import br.alexandregpereira.hunter.detail.ui.toIcon
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.monster.detail.ConditionLoadingState
import br.alexandregpereira.hunter.monster.detail.ConditionState
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.ui.compose.AppButton
import br.alexandregpereira.hunter.ui.compose.BottomSheet
import br.alexandregpereira.hunter.ui.compose.ConfirmationBottomSheet
import br.alexandregpereira.hunter.ui.compose.EmptyScreenMessageContent
import br.alexandregpereira.hunter.ui.compose.FormBottomSheet
import br.alexandregpereira.hunter.ui.compose.FormField
import br.alexandregpereira.hunter.ui.compose.LoadingScreen
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import br.alexandregpereira.hunter.ui.compose.PreviewWindow
import br.alexandregpereira.hunter.ui.compose.ScreenHeader
import com.mikepenz.markdown.m2.Markdown
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun MonsterDetailBottomSheets(
    contentPadding: PaddingValues = PaddingValues(),
) {
    val stateHolder: MonsterDetailStateHolder = koinInject()
    val state by stateHolder.state.collectAsState()

    val maxWidth: Dp = Dp.Unspecified
    val widthFraction = .3f

    MonsterDetailOptionPicker(
        options = state.options,
        showOptions = state.showOptions,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        contentPadding = contentPadding,
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
        contentPadding = contentPadding,
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
        contentPadding = contentPadding,
        onConfirmed = stateHolder::onDeleteConfirmed,
        onClosed = stateHolder::onDeleteClosed
    )

    ConfirmationBottomSheet(
        show = state.showResetConfirmation,
        description = strings.resetQuestion,
        buttonText = strings.resetConfirmation,
        maxWidth = maxWidth,
        widthFraction = widthFraction,
        contentPadding = contentPadding,
        onConfirmed = stateHolder::onResetConfirmed,
        onClosed = stateHolder::onResetClosed
    )

    ConditionBottomSheet(
        isOpen = state.isConditionDetailOpen,
        state = state.selectedCondition,
        widthFraction = widthFraction,
        onClose = stateHolder::onCloseConditionDetail,
        onTryAgain = stateHolder::onConditionTryAgainClicked,
    )
}

@Composable
private fun ConditionBottomSheet(
    isOpen: Boolean = false,
    state: ConditionLoadingState,
    widthFraction: Float = 1f,
    onClose: () -> Unit = {},
    onTryAgain: (conditionIndex: String) -> Unit = {},
) = BottomSheet(
    opened = isOpen,
    maxWidth = Dp.Unspecified,
    widthFraction = widthFraction,
    onClose = onClose,
) {
    val loadingScreenState = remember(state) {
        when(state) {
            ConditionLoadingState.Loading -> LoadingScreenState.LoadingScreen
            is ConditionLoadingState.Loaded -> LoadingScreenState.Success(state.selectedCondition)
            is ConditionLoadingState.Error -> LoadingScreenState.Error(state.index)
        }
    }
    LoadingScreen<ConditionState, String>(
        state = loadingScreenState,
        fillMaxSize = false,
        errorContent = { conditionIndex ->
            EmptyScreenMessageContent(
                title = strings.noInternetConnection,
                buttonText = strings.tryAgain,
                onButtonClick = { onTryAgain(conditionIndex) },
                modifier = Modifier.padding(16.dp)
            )
        },
    ) { condition ->
        Box {
            val density = LocalDensity.current
            val size = 180.dp
            val translation = with(density) { (size / 4).toPx() }
            Icon(
                painter = painterResource(condition.type.toIcon()),
                contentDescription = "",
                modifier = Modifier
                    .size(size)
                    .alpha(0.1f)
                    .align(Alignment.TopEnd)
                    .graphicsLayer(
                        translationX = translation,
                        translationY = -translation,
                    )
            )
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ScreenHeader(
                    title = condition.name,
                )
                Markdown(
                    content = condition.description,
                )
                AppButton(
                    text = strings.iGotIt,
                    onClick = onClose,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun  ConditionBottomSheetPreview() = PreviewWindow {
    val condition = ConditionState(
        index = "",
        type = ConditionType.POISONED,
        name = "Blinded",
        description = "Some special abilities and environmental hazards, such as starvation and the long-term effects of freezing or scorching temperatures, can lead to a special condition called exhaustion. Exhaustion is measured in six levels. An effect can give a creature one or more levels of exhaustion, as specified in the effect's description.\n\n1. Disadvantage on ability checks\n2. Speed halved\n3. Disadvantage on attack rolls and saving throws\n4. Hit point maximum halved\n5. Speed reduced to 0\n6. Death\n\nIf an already exhausted creature suffers another effect that causes exhaustion, its current level of exhaustion increases by the amount specified in the effect's description.\n\nA creature suffers the effect of its current level of exhaustion as well as all lower levels. For example, a creature suffering level 2 exhaustion has its speed halved and has disadvantage on ability checks.\n\nAn effect that removes exhaustion reduces its level as specified in the effect's description, with all exhaustion effects ending if a creature's exhaustion level is reduced below 1.\n\nFinishing a long rest reduces a creature's exhaustion level by 1, provided that the creature has also ingested some food and drink.",
    )
    ConditionBottomSheet(
        isOpen = true,
        state = ConditionLoadingState.Loaded(selectedCondition = condition),
    )
}
