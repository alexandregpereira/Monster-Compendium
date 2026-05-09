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
import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContentInfo
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class ShareContentExportStateHolderTest {

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
    fun `exportButtonEnabled is false in initial state`() = runTest {
        val stateHolder = createStateHolder()
        assertFalse(stateHolder.state.value.exportButtonEnabled)
    }

    @Test
    fun `exportButtonEnabled is true after content is loaded`() = runTest {
        val stateHolder = createStateHolder(
            compendiumFileManager = fakeCompendiumFileManager(),
        )

        advanceUntilIdle()

        testFlow(stateHolder.state) {
            eventDispatcher.dispatchEvent(
                ShareContentEvent.Export.OnStart(monsterIndexes = listOf("goblin"))
            )
        }.apply {
            assertNextValue(ShareContentExportState(strings = ShareContentExportStrings()))
            assertNextValue(
                ShareContentExportState(
                    isOpen = true,
                    isLoading = true,
                    strings = ShareContentExportStrings(),
                )
            )
            val loaded = removeAt(0)
            assertTrue(loaded.isOpen)
            assertFalse(loaded.isLoading)
            assertNotNull(loaded.exportExtractedState)
            assertTrue(loaded.exportButtonEnabled)
        }
    }

    @Test
    fun `onEditContentTitle with valid title updates fileName in state`() = runTest {
        val stateHolder = createStateHolder(
            compendiumFileManager = fakeCompendiumFileManager(),
        )

        advanceUntilIdle()
        eventDispatcher.dispatchEvent(
            ShareContentEvent.Export.OnStart(monsterIndexes = listOf("goblin"))
        )
        advanceUntilIdle()

        stateHolder.onEditContentTitle("My Monsters")
        val state = stateHolder.state.value

        assertEquals("my-monsters.compendium", state.exportExtractedState?.fileName)
        assertTrue(state.exportButtonEnabled)
    }

    @Test
    fun `onEditContentTitle with blank title falls back to existing fileName`() = runTest {
        val stateHolder = createStateHolder(
            compendiumFileManager = fakeCompendiumFileManager(),
        )

        advanceUntilIdle()
        eventDispatcher.dispatchEvent(
            ShareContentEvent.Export.OnStart(monsterIndexes = listOf("goblin"))
        )
        advanceUntilIdle()

        val fileNameBeforeEdit = stateHolder.state.value.exportExtractedState?.fileName
        assertNotNull(fileNameBeforeEdit)

        stateHolder.onEditContentTitle("   ")
        val state = stateHolder.state.value

        // Blank title falls back to the existing fileName (not empty string)
        val fileNameAfterEdit = state.exportExtractedState?.fileName
        assertNotNull(fileNameAfterEdit)
        assertTrue(fileNameAfterEdit.isNotBlank())
        assertTrue(state.exportButtonEnabled)
    }

    @Test
    fun `onExportToFile when compendiumFileContent is null calls logException`() = runTest {
        val analytics = RecordingAnalytics()
        val stateHolder = createStateHolder(analytics = analytics)

        advanceUntilIdle()

        stateHolder.onExportToFile()
        advanceUntilIdle()

        assertTrue(analytics.loggedExceptions.isNotEmpty())
        assertTrue(analytics.loggedExceptions.first() is IllegalStateException)
    }

    @Test
    fun `onEditContentTitle with path-traversal characters sanitizes fileName`() = runTest {
        val stateHolder = createStateHolder(
            compendiumFileManager = fakeCompendiumFileManager(),
        )

        advanceUntilIdle()
        eventDispatcher.dispatchEvent(
            ShareContentEvent.Export.OnStart(monsterIndexes = listOf("goblin"))
        )
        advanceUntilIdle()

        stateHolder.onEditContentTitle("../../evil")
        val state = stateHolder.state.value

        val fileName = state.exportExtractedState?.fileName
        assertNotNull(fileName)
        assertFalse(fileName.contains("/"))
        assertFalse(fileName.contains("\\"))
        assertFalse(fileName.contains(".."))
    }

    private fun createStateHolder(
        compendiumFileManager: CompendiumFileManager = NoOpCompendiumFileManager(),
        analytics: Analytics = NoOpAnalytics(),
    ) = ShareContentExportStateHolder(
        dispatcher = testDispatcher,
        analytics = analytics,
        appLocalization = FakeAppLocalization(),
        compendiumFileManager = compendiumFileManager,
        getMonstersShareContent = { ShareContent(null, null, null, null) },
        eventDispatcher = eventDispatcher,
    )

    private fun fakeCompendiumFileManager() = object : CompendiumFileManager {
        override suspend fun getCompendiumFileContent(zipFile: FileEntry): CompendiumFileContent =
            TODO("Not needed")

        override suspend fun getCompendiumFileContent(
            fileName: String,
            shareContent: ShareContent,
        ): CompendiumFileContent = CompendiumFileContent(
            name = "$fileName.compendium",
            shareContent = shareContent,
            monsterImages = emptyList(),
            contentInfo = CompendiumFileContentInfo(
                contentTitle = "",
                contentDescription = "",
                fileSizeFormatted = "0 Bytes",
            ),
        )

        override suspend fun createCompendiumFile(
            fileName: String,
            compendiumFileContent: CompendiumFileContent,
        ): String = "file:///tmp/$fileName"

        override suspend fun deleteCompendiumFiles() {}
    }

    private class NoOpCompendiumFileManager : CompendiumFileManager {
        override suspend fun getCompendiumFileContent(zipFile: FileEntry): CompendiumFileContent =
            TODO("Not needed")

        override suspend fun getCompendiumFileContent(
            fileName: String,
            shareContent: ShareContent,
        ): CompendiumFileContent = TODO("Not needed")

        override suspend fun createCompendiumFile(
            fileName: String,
            compendiumFileContent: CompendiumFileContent,
        ): String = TODO("Not needed")

        override suspend fun deleteCompendiumFiles() {}
    }

    private class RecordingAnalytics : Analytics {
        val loggedExceptions = mutableListOf<Throwable>()
        override fun track(eventName: String, params: Map<String, Any?>) {}
        override fun logException(throwable: Throwable) {
            loggedExceptions.add(throwable)
        }
    }

    private class NoOpAnalytics : Analytics {
        override fun track(eventName: String, params: Map<String, Any?>) {}
        override fun logException(throwable: Throwable) {}
    }

    private class FakeAppLocalization : AppReactiveLocalization {
        override val languageFlow: Flow<Language> = MutableStateFlow(Language.ENGLISH)
        override fun getLanguage(): Language = Language.ENGLISH
    }
}
