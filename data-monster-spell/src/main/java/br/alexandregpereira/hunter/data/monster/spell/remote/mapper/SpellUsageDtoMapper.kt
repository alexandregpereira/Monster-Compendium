package br.alexandregpereira.hunter.data.monster.spell.remote.mapper

import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellUsageDto
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage

fun List<SpellUsageDto>.toDomain(): List<SpellUsage> {
    return map { spellUsage ->
        SpellUsage(
            group = spellUsage.group,
            spells = spellUsage.spells.map { monsterSpell ->
                SpellPreview(
                    index = monsterSpell.index,
                    name = monsterSpell.name,
                    level = monsterSpell.level,
                    school = SchoolOfMagic.valueOf(monsterSpell.school.name),
                )
            }
        )
    }
}
