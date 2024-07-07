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

package br.alexandregpereira.hunter.detail.ui

import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SpeedType
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
import br.alexandregpereira.hunter.ui.R as UiR

internal fun SpeedType.toIconRes(): Int {
    return when (this) {
        SpeedType.BURROW -> R.drawable.ic_ghost
        SpeedType.CLIMB -> R.drawable.ic_climbing
        SpeedType.FLY -> R.drawable.ic_superhero
        SpeedType.WALK -> R.drawable.ic_runer_silhouette_running_fast
        SpeedType.SWIM -> R.drawable.ic_swimmer
    }
}

internal fun DamageType.toIconRes(): Int? {
    return when (this) {
        DamageType.ACID -> R.drawable.ic_acid
        DamageType.BLUDGEONING -> R.drawable.ic_bludgeoning
        DamageType.COLD -> R.drawable.ic_cold
        DamageType.FIRE -> UiR.drawable.ic_elemental
        DamageType.LIGHTNING -> R.drawable.ic_lightning
        DamageType.NECROTIC -> UiR.drawable.ic_undead
        DamageType.PIERCING -> R.drawable.ic_piercing
        DamageType.POISON -> R.drawable.ic_poison
        DamageType.PSYCHIC -> R.drawable.ic_psychic
        DamageType.RADIANT -> R.drawable.ic_radiant
        DamageType.SLASHING -> R.drawable.ic_slashing
        DamageType.THUNDER -> R.drawable.ic_thunder
        DamageType.OTHER -> null
    }
}

internal fun ConditionType.toIconRes(): Int {
    return when (this) {
        ConditionType.BLINDED -> R.drawable.ic_blinded
        ConditionType.CHARMED -> R.drawable.ic_charmed
        ConditionType.DEAFENED -> R.drawable.ic_deafened
        ConditionType.EXHAUSTION -> R.drawable.ic_exhausted
        ConditionType.FRIGHTENED -> R.drawable.ic_frightened
        ConditionType.GRAPPLED -> R.drawable.ic_grappled
        ConditionType.PARALYZED -> R.drawable.ic_paralyzed
        ConditionType.PETRIFIED -> R.drawable.ic_petrified
        ConditionType.POISONED -> R.drawable.ic_poison
        ConditionType.PRONE -> R.drawable.ic_prone
        ConditionType.RESTRAINED -> R.drawable.ic_restrained
        ConditionType.STUNNED -> R.drawable.ic_stuned
        ConditionType.UNCONSCIOUS -> R.drawable.ic_unconscious
    }
}

internal fun MonsterType.toIcon(): DrawableResource {
    return when (this) {
        MonsterType.ABERRATION -> Res.drawable.ic_aberration
        MonsterType.BEAST -> Res.drawable.ic_beast
        MonsterType.CELESTIAL -> Res.drawable.ic_celestial
        MonsterType.CONSTRUCT -> Res.drawable.ic_construct
        MonsterType.DRAGON -> Res.drawable.ic_dragon
        MonsterType.ELEMENTAL -> Res.drawable.ic_elemental
        MonsterType.FEY -> Res.drawable.ic_fey
        MonsterType.FIEND -> Res.drawable.ic_fiend
        MonsterType.GIANT -> Res.drawable.ic_giant
        MonsterType.HUMANOID -> Res.drawable.ic_humanoid
        MonsterType.MONSTROSITY -> Res.drawable.ic_monstrosity
        MonsterType.OOZE -> Res.drawable.ic_ooze
        MonsterType.PLANT -> Res.drawable.ic_plant
        MonsterType.UNDEAD -> Res.drawable.ic_undead
    }
}
