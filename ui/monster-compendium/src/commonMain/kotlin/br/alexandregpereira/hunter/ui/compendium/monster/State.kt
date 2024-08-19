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

import br.alexandregpereira.hunter.ui.compose.AppImageContentScale
import br.alexandregpereira.hunter.ui.resources.Res
import br.alexandregpereira.hunter.ui.resources.ic_aberration
import br.alexandregpereira.hunter.ui.resources.ic_beast
import br.alexandregpereira.hunter.ui.resources.ic_celestial
import br.alexandregpereira.hunter.ui.resources.ic_construct
import br.alexandregpereira.hunter.ui.resources.ic_dragon
import br.alexandregpereira.hunter.ui.resources.ic_elemental
import br.alexandregpereira.hunter.ui.resources.ic_fey
import br.alexandregpereira.hunter.ui.resources.ic_fiend
import br.alexandregpereira.hunter.ui.resources.ic_giant
import br.alexandregpereira.hunter.ui.resources.ic_humanoid
import br.alexandregpereira.hunter.ui.resources.ic_monstrosity
import br.alexandregpereira.hunter.ui.resources.ic_ooze
import br.alexandregpereira.hunter.ui.resources.ic_plant
import br.alexandregpereira.hunter.ui.resources.ic_undead
import org.jetbrains.compose.resources.DrawableResource

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
    val contentScale: AppImageContentScale,
    val isHorizontal: Boolean = false,
    val contentDescription: String = "",
)

data class ColorState(
    val light: String,
    val dark: String
) {

    fun getColor(isDarkTheme: Boolean): String = if (isDarkTheme) dark else light
}

enum class MonsterTypeState(val icon: DrawableResource) {
    ABERRATION(Res.drawable.ic_aberration),
    BEAST(Res.drawable.ic_beast),
    CELESTIAL(Res.drawable.ic_celestial),
    CONSTRUCT(Res.drawable.ic_construct),
    DRAGON(Res.drawable.ic_dragon),
    ELEMENTAL(Res.drawable.ic_elemental),
    FEY(Res.drawable.ic_fey),
    FIEND(Res.drawable.ic_fiend),
    GIANT(Res.drawable.ic_giant),
    HUMANOID(Res.drawable.ic_humanoid),
    MONSTROSITY(Res.drawable.ic_monstrosity),
    OOZE(Res.drawable.ic_ooze),
    PLANT(Res.drawable.ic_plant),
    UNDEAD(Res.drawable.ic_undead)
}
