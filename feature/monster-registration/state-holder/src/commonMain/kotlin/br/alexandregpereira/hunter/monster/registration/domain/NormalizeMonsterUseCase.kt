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

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal fun interface NormalizeMonsterUseCase {
    operator fun invoke(monster: Monster): Flow<Monster>
}

internal fun NormalizeMonsterUseCase(): NormalizeMonsterUseCase = NormalizeMonsterUseCase { monster ->
    flowOf(monster)
        .map { it.changeAbilityScoresModifier() }
}

private fun Monster.changeAbilityScoresModifier(): Monster {
    val monster = this
    return monster.copy(
        abilityScores = monster.abilityScores.map { abilityScore ->
            val newValue = abilityScore.value.coerceAtLeast(2).coerceAtMost(99)
            abilityScore.copy(
                value = newValue,
                modifier = when {
                    newValue < 2 -> -5
                    newValue < 4 -> -4
                    newValue < 6 -> -3
                    newValue < 8 -> -2
                    newValue < 10 -> -1
                    newValue < 12 -> 0
                    newValue < 14 -> 1
                    newValue < 16 -> 2
                    newValue < 18 -> 3
                    newValue < 20 -> 4
                    newValue < 22 -> 5
                    newValue < 24 -> 6
                    newValue < 26 -> 7
                    newValue < 28 -> 8
                    newValue < 30 -> 9
                    else -> 10
                }
            )
        },
        imageData = monster.imageData.copy(
            backgroundColor = monster.imageData.backgroundColor.normalizeColor(),
        ),
    )
}

private fun Color.normalizeColor(): Color {
    val newColor = this.light.takeIf { it.isNotBlank() }
        ?.replace("#", "")?.let { "#$it" }?.uppercase().orEmpty()
    return this.copy(
        light = newColor,
        dark = newColor,
    )
}
