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

package br.alexandregpereira.hunter.monster.content.preview

import androidx.lifecycle.ViewModel
import br.alexandregpereira.hunter.state.ActionHandler
import br.alexandregpereira.hunter.state.StateHolder

internal class MonsterContentPreviewViewModel(
    private val stateHolder: MonsterContentPreviewStateHolder,
) : ViewModel(),
    StateHolder<MonsterContentPreviewState> by stateHolder,
    ActionHandler<MonsterContentPreviewAction> by stateHolder {

    fun onClose() {
        stateHolder.onClose()
    }

    fun onTableContentOpenButtonClick() {
        stateHolder.onTableContentOpenButtonClick()
    }

    fun onTableContentClose() {
        stateHolder.onTableContentClose()
    }

    fun onTableContentClick(index: Int) {
        stateHolder.onTableContentClick(index)
    }

    fun onFirstVisibleItemChange(compendiumItemIndex: Int) {
        stateHolder.onFirstVisibleItemChange(compendiumItemIndex)
    }

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }
}
