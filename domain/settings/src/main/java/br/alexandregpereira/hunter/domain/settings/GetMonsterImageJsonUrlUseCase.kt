/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (c) 2022. Alexandre Gomes Pereira.
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

package br.alexandregpereira.hunter.domain.settings

import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase.Companion.IMAGE_BASE_URL_KEY
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetMonsterImageJsonUrlUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<String> {
        return settingsRepository.getSettingsValue(key = IMAGE_BASE_URL_KEY)
    }
}
