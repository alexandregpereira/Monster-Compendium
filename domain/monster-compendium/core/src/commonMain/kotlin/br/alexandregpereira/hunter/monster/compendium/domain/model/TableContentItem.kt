/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.compendium.domain.model

import kotlin.native.ObjCName

@ObjCName(name = "TableContentItem", exact = true)
data class TableContentItem(
    val text: String,
    val type: TableContentItemType,
    val id: String = text
)

@ObjCName(name = "TableContentItemType", exact = true)
enum class TableContentItemType {
    HEADER1, HEADER2, BODY
}
