/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.detail

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.detail.ui.MonsterState

data class MonsterDetailViewState(
    val initialMonsterListPositionIndex: Int = 0,
    val monsters: List<MonsterState> = emptyList(),
    val showOptions: Boolean = false,
    val options: List<MonsterDetailOptionState> = emptyList(),
    val showDetail: Boolean = false,
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
    val formTitle: FormTitleViewState = FormTitleViewState.CLONE,
    val formFields: Map<FormFieldKeyViewState, String> = emptyMap(),
    val formButtonEnabled: Boolean = false,
)

enum class FormTitleViewState(val titleRes: Int) {
    CLONE(R.string.monster_detail_clone),
}

enum class FormFieldKeyViewState(val labelRes: Int) {
    CLONE_MONSTER_NAME(R.string.monster_detail_clone_monster_name),
}

fun SavedStateHandle.getState(): MonsterDetailViewState {
    val formFieldKeys = (this["formFieldKeys"] ?: emptyArray<String>()).map { FormFieldKeyViewState.valueOf(it) }
    val formFieldValues = this["formFieldValues"] ?: emptyArray<String>()
    return MonsterDetailViewState(
        showDetail = this["showDetail"] ?: false,
        showForm = this["showForm"] ?: false,
        formTitle = this.get<String>("formTitle")?.let { FormTitleViewState.valueOf(it) } ?: FormTitleViewState.CLONE,
        formFields = formFieldKeys.zip(formFieldValues).toMap(),
    )
}

fun MonsterDetailViewState.saveState(savedStateHandle: SavedStateHandle): MonsterDetailViewState {
    savedStateHandle["showDetail"] = showDetail
    savedStateHandle["showForm"] = showForm
    savedStateHandle["formTitle"] = formTitle.name
    savedStateHandle["formFieldKeys"] = formFields.keys.map { it.name }.toTypedArray()
    savedStateHandle["formFieldValues"] = formFields.values.toTypedArray()
    return this
}
