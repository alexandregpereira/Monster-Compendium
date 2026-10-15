package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SpellContentJsonMapperImplTest {

    private val mapper = SpellContentJsonMapperImpl(Json { ignoreUnknownKeys = true })

    @Test
    fun `encode and decode keeps the spell status`() = runTest {
        SpellStatus.entries.forEach { status ->
            val spell = createSpell(status = status)

            val decoded = mapper.decodeFromJson(mapper.encodeToJson(listOf(spell)))

            assertEquals(listOf(spell), decoded)
        }
    }

    @Test
    fun `decode without status maps to Original`() = runTest {
        val decoded = mapper.decodeFromJson(spellJson(status = null))

        assertEquals(SpellStatus.Original, decoded.single().status)
    }

    @Test
    fun `decode with unknown status maps to Imported`() = runTest {
        val decoded = mapper.decodeFromJson(spellJson(status = "\"Unknown\""))

        assertEquals(SpellStatus.Imported, decoded.single().status)
    }

    private fun spellJson(status: String?): String {
        val statusField = status?.let { ""","status":$it""" }.orEmpty()
        return """[{"index":"spell-1","name":"Spell","level":3,"casting_time":"","components":"",""" +
            """"duration":"","range":"","ritual":false,"concentration":false,""" +
            """"saving_throw_type":null,"damage_type":null,"school":"ABJURATION",""" +
            """"description":"","higher_level":null$statusField}]"""
    }

    private fun createSpell(status: SpellStatus) = Spell(
        index = "spell-1",
        name = "Spell",
        level = 3,
        castingTime = "",
        components = "",
        duration = "",
        range = "",
        ritual = false,
        concentration = false,
        savingThrowType = null,
        damageType = null,
        school = SchoolOfMagic.ABJURATION,
        description = "",
        higherLevel = null,
        status = status,
    )
}
