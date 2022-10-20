/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.folder.preview.domain

import br.alexandregpereira.hunter.domain.folder.MonsterFolderRepository
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase.Companion.TEMPORARY_FOLDER_NAME
import br.alexandregpereira.hunter.folder.preview.domain.model.MonsterFolderPreview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

internal class RemoveMonsterFromFolderPreviewUseCase @Inject constructor(
    private val monsterFolderRepository: MonsterFolderRepository,
    private val getMonstersFromFolderPreview: GetMonstersFromFolderPreviewUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(index: String): Flow<List<MonsterFolderPreview>> {
        return monsterFolderRepository.removeMonster(TEMPORARY_FOLDER_NAME, index)
            .flatMapLatest {
                getMonstersFromFolderPreview()
            }
    }
}
