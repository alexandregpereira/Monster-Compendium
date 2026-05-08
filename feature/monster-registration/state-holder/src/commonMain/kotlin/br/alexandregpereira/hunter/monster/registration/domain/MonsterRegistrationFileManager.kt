/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.registration.domain

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import kotlin.time.Clock

internal interface MonsterRegistrationFileManager {

    suspend fun saveImage(
        bytes: ByteArray,
        imageName: String,
    ): String

    suspend fun deleteLastSavedImageIfExists()

    fun clear()
}

internal class MonsterRegistrationFileManagerImpl(
    private val fileManager: FileManager,
    private val clock: Clock = Clock.System,
) : MonsterRegistrationFileManager {

    private val fileType = FileType.IMAGE
    private var lastFileSaved: String? = null

    override suspend fun saveImage(
        bytes: ByteArray,
        imageName: String,
    ): String {
        val epochMilliSeconds = clock.now().toEpochMilliseconds()
        val fileNameWithTimestamp = "$imageName-$epochMilliSeconds.png"
        val path = fileManager.saveFileToAppStorage(
            bytes = bytes,
            fileName = fileNameWithTimestamp,
            fileType = fileType,
        )
        lastFileSaved?.let {
            try {
                fileManager.deleteFileFromAppStorage(fileName = it, fileType = fileType)
            } catch (cause: Throwable) {
                lastFileSaved = fileNameWithTimestamp
                throw cause
            }
        }
        lastFileSaved = fileNameWithTimestamp
        return path
    }

    override suspend fun deleteLastSavedImageIfExists() {
        lastFileSaved?.let {
            fileManager.deleteFileFromAppStorage(fileName = it, fileType = fileType)
        }
        lastFileSaved = null
    }

    override fun clear() {
        lastFileSaved = null
    }
}
