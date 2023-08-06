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

package br.alexandregpereira.hunter.event.monster.detail

sealed class MonsterDetailEvent {

    sealed class OnVisibilityChanges : MonsterDetailEvent() {
        data class Show(
            val index: String,
            val indexes: List<String> = emptyList(),
            val enableMonsterPageChangesEventDispatch: Boolean = false
        ) : OnVisibilityChanges()

        object Hide : OnVisibilityChanges()
    }

    data class OnMonsterPageChanges(
        val monsterIndex: String
    ) : MonsterDetailEvent()
}
