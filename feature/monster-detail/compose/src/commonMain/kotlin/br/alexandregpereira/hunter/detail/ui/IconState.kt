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

package br.alexandregpereira.hunter.detail.ui

import br.alexandregpereira.hunter.detail.ui.resources.Res
import br.alexandregpereira.hunter.detail.ui.resources.ic_acid
import br.alexandregpereira.hunter.detail.ui.resources.ic_blinded
import br.alexandregpereira.hunter.detail.ui.resources.ic_bludgeoning
import br.alexandregpereira.hunter.detail.ui.resources.ic_charmed
import br.alexandregpereira.hunter.detail.ui.resources.ic_climbing
import br.alexandregpereira.hunter.detail.ui.resources.ic_cold
import br.alexandregpereira.hunter.detail.ui.resources.ic_deafened
import br.alexandregpereira.hunter.detail.ui.resources.ic_exhausted
import br.alexandregpereira.hunter.detail.ui.resources.ic_frightened
import br.alexandregpereira.hunter.detail.ui.resources.ic_ghost
import br.alexandregpereira.hunter.detail.ui.resources.ic_grappled
import br.alexandregpereira.hunter.detail.ui.resources.ic_lightning
import br.alexandregpereira.hunter.detail.ui.resources.ic_paralyzed
import br.alexandregpereira.hunter.detail.ui.resources.ic_petrified
import br.alexandregpereira.hunter.detail.ui.resources.ic_piercing
import br.alexandregpereira.hunter.detail.ui.resources.ic_poison
import br.alexandregpereira.hunter.detail.ui.resources.ic_prone
import br.alexandregpereira.hunter.detail.ui.resources.ic_psychic
import br.alexandregpereira.hunter.detail.ui.resources.ic_radiant
import br.alexandregpereira.hunter.detail.ui.resources.ic_restrained
import br.alexandregpereira.hunter.detail.ui.resources.ic_runer_silhouette_running_fast
import br.alexandregpereira.hunter.detail.ui.resources.ic_slashing
import br.alexandregpereira.hunter.detail.ui.resources.ic_stuned
import br.alexandregpereira.hunter.detail.ui.resources.ic_superhero
import br.alexandregpereira.hunter.detail.ui.resources.ic_swimmer
import br.alexandregpereira.hunter.detail.ui.resources.ic_thunder
import br.alexandregpereira.hunter.detail.ui.resources.ic_unconscious
import br.alexandregpereira.hunter.domain.model.ConditionType
import br.alexandregpereira.hunter.domain.model.DamageType
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.SpeedType
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
import br.alexandregpereira.hunter.ui.resources.Res as UiRes

internal fun SpeedType.toIcon(): DrawableResource {
    return when (this) {
        SpeedType.BURROW -> Res.drawable.ic_ghost
        SpeedType.CLIMB -> Res.drawable.ic_climbing
        SpeedType.FLY -> Res.drawable.ic_superhero
        SpeedType.WALK -> Res.drawable.ic_runer_silhouette_running_fast
        SpeedType.SWIM -> Res.drawable.ic_swimmer
    }
}

internal fun DamageType.toIcon(): DrawableResource? {
    return when (this) {
        DamageType.ACID -> Res.drawable.ic_acid
        DamageType.BLUDGEONING -> Res.drawable.ic_bludgeoning
        DamageType.COLD -> Res.drawable.ic_cold
        DamageType.FIRE -> UiRes.drawable.ic_elemental
        DamageType.LIGHTNING -> Res.drawable.ic_lightning
        DamageType.NECROTIC -> UiRes.drawable.ic_undead
        DamageType.PIERCING -> Res.drawable.ic_piercing
        DamageType.POISON -> Res.drawable.ic_poison
        DamageType.PSYCHIC -> Res.drawable.ic_psychic
        DamageType.RADIANT -> Res.drawable.ic_radiant
        DamageType.SLASHING -> Res.drawable.ic_slashing
        DamageType.THUNDER -> Res.drawable.ic_thunder
        DamageType.OTHER -> null
    }
}

internal fun ConditionType.toIcon(): DrawableResource {
    return when (this) {
        ConditionType.BLINDED -> Res.drawable.ic_blinded
        ConditionType.CHARMED -> Res.drawable.ic_charmed
        ConditionType.DEAFENED -> Res.drawable.ic_deafened
        ConditionType.EXHAUSTION -> Res.drawable.ic_exhausted
        ConditionType.FRIGHTENED -> Res.drawable.ic_frightened
        ConditionType.GRAPPLED -> Res.drawable.ic_grappled
        ConditionType.PARALYZED -> Res.drawable.ic_paralyzed
        ConditionType.PETRIFIED -> Res.drawable.ic_petrified
        ConditionType.POISONED -> Res.drawable.ic_poison
        ConditionType.PRONE -> Res.drawable.ic_prone
        ConditionType.RESTRAINED -> Res.drawable.ic_restrained
        ConditionType.STUNNED -> Res.drawable.ic_stuned
        ConditionType.UNCONSCIOUS -> Res.drawable.ic_unconscious
    }
}

internal fun MonsterType.toIcon(): DrawableResource {
    return when (this) {
        MonsterType.ABERRATION -> UiRes.drawable.ic_aberration
        MonsterType.BEAST -> UiRes.drawable.ic_beast
        MonsterType.CELESTIAL -> UiRes.drawable.ic_celestial
        MonsterType.CONSTRUCT -> UiRes.drawable.ic_construct
        MonsterType.DRAGON -> UiRes.drawable.ic_dragon
        MonsterType.ELEMENTAL -> UiRes.drawable.ic_elemental
        MonsterType.FEY -> UiRes.drawable.ic_fey
        MonsterType.FIEND -> UiRes.drawable.ic_fiend
        MonsterType.GIANT -> UiRes.drawable.ic_giant
        MonsterType.HUMANOID -> UiRes.drawable.ic_humanoid
        MonsterType.MONSTROSITY -> UiRes.drawable.ic_monstrosity
        MonsterType.OOZE -> UiRes.drawable.ic_ooze
        MonsterType.PLANT -> UiRes.drawable.ic_plant
        MonsterType.UNDEAD -> UiRes.drawable.ic_undead
    }
}
