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

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.file.FileEntry
import br.alexandregpereira.flow.test.assertNextValue
import br.alexandregpereira.flow.test.testFlow
import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContent
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileManager
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class ShareContentImportStateHolderTest {

    private val testDispatcher = StandardTestDispatcher()
    private val eventDispatcher = ShareContentEventDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Import OnStart with file bytes opens screen in loading state then shows extracted content`() = runTest {
        val zipBytes = byteArrayOf(1, 2, 3)
        val expectedContent = CompendiumFileContent(
            name = "test.compendium",
            shareContent = ShareContent(monsters = null, monstersLore = null, spells = null, monsterImages = null),
            monsterImages = emptyList(),
            sizeFormatted = "3 Bytes",
        )

        val stateHolder = createStateHolder(
            compendiumFileManager = object : CompendiumFileManager {
                override suspend fun getCompendiumFileContent(zipFile: FileEntry): CompendiumFileContent {
                    assertEquals("test.compendium", zipFile.name)
                    assertEquals(zipBytes.toList(), zipFile.content.toList())
                    return expectedContent
                }

                override suspend fun getCompendiumFileContent(fileName: String, shareContent: ShareContent): CompendiumFileContent =
                    TODO("Not needed")

                override suspend fun createCompendiumFile(compendiumFileContent: CompendiumFileContent): String =
                    TODO("Not needed")

                override suspend fun deleteCompendiumFiles() {}
            }
        )

        // Let the state holder's init subscribe to the event flow before dispatching
        advanceUntilIdle()

        val states = testFlow(stateHolder.state) {
            eventDispatcher.dispatchEvent(
                ShareContentEvent.Import.OnStart(
                    compendiumFileName = "test.compendium",
                    compendiumFileBytes = zipBytes,
                )
            )
        }

        // Initial state
        states.assertNextValue(ShareContentImportState(strings = ShareContentImportStrings()))
        // Opened with loading
        states.assertNextValue(
            ShareContentImportState(
                isOpen = true,
                isLoading = true,
                strings = ShareContentImportStrings(),
            )
        )
        // Content extracted
        val finalState = states.removeAt(0)
        assertTrue(finalState.isOpen)
        assertNull(finalState.importError)
        assertEquals(false, finalState.isLoading)
        assertNotNull(finalState.importExtractedState)
        assertEquals("test.compendium", finalState.importExtractedState.fileName)
    }

    @Test
    fun `Import OnStart without file bytes opens screen without loading`() = runTest {
        val stateHolder = createStateHolder()

        // Let the state holder's init subscribe to the event flow before dispatching
        advanceUntilIdle()

        val states = testFlow(stateHolder.state) {
            eventDispatcher.dispatchEvent(ShareContentEvent.Import.OnStart())
        }

        states.assertNextValue(ShareContentImportState(strings = ShareContentImportStrings()))
        val openedState = states.removeAt(0)
        assertTrue(openedState.isOpen)
        assertEquals(false, openedState.isLoading)
        assertNull(openedState.importExtractedState)
    }

    private fun createStateHolder(
        compendiumFileManager: CompendiumFileManager = NoOpCompendiumFileManager(),
    ) = ShareContentImportStateHolder(
        dispatcher = testDispatcher,
        appLocalization = FakeAppLocalization(),
        eventDispatcher = eventDispatcher,
        importContent = { emptyList() },
        analytics = NoOpAnalytics(),
        compendiumFileManager = compendiumFileManager,
    )

    private class FakeAppLocalization : AppReactiveLocalization {
        override val languageFlow: Flow<Language> = MutableStateFlow(Language.ENGLISH)
        override fun getLanguage(): Language = Language.ENGLISH
    }

    private class NoOpCompendiumFileManager : CompendiumFileManager {
        override suspend fun getCompendiumFileContent(zipFile: FileEntry): CompendiumFileContent =
            CompendiumFileContent(
                name = zipFile.name,
                shareContent = ShareContent(monsters = null, monstersLore = null, spells = null, monsterImages = null),
                monsterImages = emptyList(),
                sizeFormatted = "0 Bytes",
            )

        override suspend fun getCompendiumFileContent(fileName: String, shareContent: ShareContent): CompendiumFileContent =
            TODO("Not needed")

        override suspend fun createCompendiumFile(compendiumFileContent: CompendiumFileContent): String =
            TODO("Not needed")

        override suspend fun deleteCompendiumFiles() {}
    }

    private class NoOpAnalytics : Analytics {
        override fun track(eventName: String, params: Map<String, Any?>) {}
        override fun logException(throwable: Throwable) {}
    }
}
