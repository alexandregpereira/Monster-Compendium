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

package br.alexandregpereira.hunter.shareContent.state

import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.file.ZipFileManager
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContent
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContentInfo
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileManagerImpl
import br.alexandregpereira.hunter.shareContent.domain.mapper.ContentInfoMapper
import br.alexandregpereira.hunter.shareContent.domain.mapper.ShareContentMapper
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class CompendiumFileManagerImplTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var originalUserHome: String

    @Before
    fun setUp() {
        originalUserHome = System.getProperty("user.home")
        System.setProperty("user.home", tempFolder.root.absolutePath)
    }

    @After
    fun tearDown() {
        System.setProperty("user.home", originalUserHome)
    }

    @Test
    fun `createCompendiumFile deletes temp JSON files when createZipFile throws`() = runTest {
        val savedPaths = mutableListOf<String>()

        val fileManager = object : FileManager {
            override suspend fun saveFileToAppStorage(
                bytes: ByteArray,
                fileName: String,
                fileType: FileType,
            ): String {
                val dir = File(tempFolder.root, fileType.folder).apply { mkdirs() }
                val file = File(dir, fileName).apply { writeBytes(bytes) }
                val path = "file://${file.absolutePath}"
                savedPaths.add(file.absolutePath)
                return path
            }

            override suspend fun deleteFileFromAppStorage(fileName: String, fileType: FileType) {
                File(tempFolder.root, "${fileType.folder}/$fileName").delete()
            }

            override suspend fun deleteAllFilesFromAppStorage(fileType: FileType) {
                File(tempFolder.root, fileType.folder).deleteRecursively()
            }
        }

        val throwingZipFileManager = object : ZipFileManager {
            override suspend fun createZipFile(
                zipEntryFiles: List<FileEntry>,
                zipFileName: String,
            ): String = throw RuntimeException("Simulated zip failure")

            override suspend fun extractZipFile(bytes: ByteArray): List<FileEntry> =
                error("Not needed")
        }

        val manager = CompendiumFileManagerImpl(
            zipFileManager = throwingZipFileManager,
            fileManager = fileManager,
            shareContentMapper = FakeShareContentMapper(),
            contentInfoMapper = FakeContentInfoMapper(),
        )

        val content = CompendiumFileContent(
            name = "test.compendium",
            shareContent = ShareContent(null, null, null, null),
            monsterImages = emptyList(),
            contentInfo = CompendiumFileContentInfo(
                contentTitle = null,
                contentDescription = null,
                fileSizeFormatted = "0 Bytes",
            ),
        )

        assertFailsWith<RuntimeException> {
            manager.createCompendiumFile("test.compendium", content)
        }

        // Both temp JSON files must be deleted after the failure
        savedPaths.forEach { path ->
            assertFalse(File(path).exists(), "Expected $path to be deleted after zip failure")
        }
    }

    private class FakeShareContentMapper : ShareContentMapper {
        override suspend fun decodeFromJson(contentJson: String): ShareContent =
            ShareContent(null, null, null, null)

        override suspend fun encodeToJson(value: ShareContent): String = "{}"
    }

    private class FakeContentInfoMapper : ContentInfoMapper {
        override suspend fun decodeFromJson(contentJson: String): CompendiumFileContentInfo =
            CompendiumFileContentInfo(null, null, "0 Bytes")

        override suspend fun encodeToJson(value: CompendiumFileContentInfo): String = "{}"
    }
}
