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

package br.alexandregpereira.hunter.event.folder.detail

import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResult.OnVisibilityChanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface FolderDetailResultListener {

    val result: Flow<FolderDetailResult>
}

fun FolderDetailResultListener.collectOnVisibilityChanges(
    action: suspend (OnVisibilityChanges) -> Unit
): Flow<OnVisibilityChanges> {
    return result.map { it as? OnVisibilityChanges }.filterNotNull().onEach(action)
}
