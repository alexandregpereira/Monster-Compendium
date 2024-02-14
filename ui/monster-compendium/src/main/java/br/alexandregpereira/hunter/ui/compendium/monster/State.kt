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

package br.alexandregpereira.hunter.ui.compendium.monster

import androidx.annotation.DrawableRes
import br.alexandregpereira.hunter.ui.R as UiR

data class MonsterCardState(
    val index: String,
    val name: String,
    val imageState: MonsterImageState,
)

data class MonsterImageState(
    val url: String,
    val type: MonsterTypeState,
    val backgroundColor: ColorState,
    val challengeRating: String,
    val isHorizontal: Boolean = false,
    val contentDescription: String = ""
)

data class ColorState(
    val light: String,
    val dark: String
) {

    fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}

enum class MonsterTypeState(@DrawableRes val iconRes: Int) {
    ABERRATION(UiR.drawable.ic_aberration),
    BEAST(UiR.drawable.ic_beast),
    CELESTIAL(UiR.drawable.ic_celestial),
    CONSTRUCT(UiR.drawable.ic_construct),
    DRAGON(UiR.drawable.ic_dragon),
    ELEMENTAL(UiR.drawable.ic_elemental),
    FEY(UiR.drawable.ic_fey),
    FIEND(UiR.drawable.ic_fiend),
    GIANT(UiR.drawable.ic_giant),
    HUMANOID(UiR.drawable.ic_humanoid),
    MONSTROSITY(UiR.drawable.ic_monstrosity),
    OOZE(UiR.drawable.ic_ooze),
    PLANT(UiR.drawable.ic_plant),
    UNDEAD(UiR.drawable.ic_undead)
}
