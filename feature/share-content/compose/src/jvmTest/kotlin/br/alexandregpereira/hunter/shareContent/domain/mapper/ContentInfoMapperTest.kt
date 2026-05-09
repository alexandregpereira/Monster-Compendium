package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.shareContent.domain.CompendiumFileContentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class ContentInfoMapperTest {

    private val mapper = ContentInfoMapperImpl(
        json = Json,
        dispatcher = Dispatchers.Unconfined,
    )

    @Test
    fun `round-trip with both optional fields null`() = runTest {
        val original = CompendiumFileContentInfo(
            contentTitle = null,
            contentDescription = null,
            fileSizeFormatted = "1.2 MB",
            fileExtension = "compendium",
        )
        val json = mapper.encodeToJson(original)
        val decoded = mapper.decodeFromJson(json)
        assertEquals(original, decoded)
    }

    @Test
    fun `round-trip with both optional fields non-null`() = runTest {
        val original = CompendiumFileContentInfo(
            contentTitle = "My Export",
            contentDescription = "A description",
            fileSizeFormatted = "512 KB",
            fileExtension = "compendium",
        )
        val json = mapper.encodeToJson(original)
        val decoded = mapper.decodeFromJson(json)
        assertEquals(original, decoded)
    }

    @Test
    fun `blank contentTitle and contentDescription are not written to JSON and decode as null`() = runTest {
        val original = CompendiumFileContentInfo(
            contentTitle = "   ",
            contentDescription = "",
            fileSizeFormatted = "100 Bytes",
            fileExtension = "compendium",
        )
        val json = mapper.encodeToJson(original)
        val decoded = mapper.decodeFromJson(json)
        assertNull(decoded.contentTitle)
        assertNull(decoded.contentDescription)
        assertEquals("100 Bytes", decoded.fileSizeFormatted)
        assertEquals("compendium", decoded.fileExtension)
    }

    @Test
    fun `decodeFromJson throws when fileSizeFormatted is missing`() = runTest {
        val json = """{"fileExtension":"compendium"}"""
        assertFailsWith<IllegalStateException> {
            mapper.decodeFromJson(json)
        }
    }

    @Test
    fun `decodeFromJson throws when fileExtension is missing`() = runTest {
        val json = """{"fileSizeFormatted":"1 MB"}"""
        assertFailsWith<IllegalStateException> {
            mapper.decodeFromJson(json)
        }
    }

    @Test
    fun `round-trip with only contentTitle set`() = runTest {
        val original = CompendiumFileContentInfo(
            contentTitle = "Only Title",
            contentDescription = null,
            fileSizeFormatted = "2 MB",
            fileExtension = "compendium",
        )
        val json = mapper.encodeToJson(original)
        val decoded = mapper.decodeFromJson(json)
        assertEquals(original, decoded)
    }
}
