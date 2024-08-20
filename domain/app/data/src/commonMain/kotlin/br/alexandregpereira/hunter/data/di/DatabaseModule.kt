/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.Database
import br.alexandregpereira.hunter.data.database.dao.AlternativeSourceDaoImpl
import br.alexandregpereira.hunter.data.database.dao.MonsterDaoImpl
import br.alexandregpereira.hunter.data.database.dao.MonsterFolderDaoImpl
import br.alexandregpereira.hunter.data.database.dao.MonsterLoreDaoImpl
import br.alexandregpereira.hunter.data.database.dao.SpellDaoImpl
import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.lore.local.dao.MonsterLoreDao
import br.alexandregpereira.hunter.data.source.local.dao.AlternativeSourceDao
import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val databaseModule = module {
    factory<SqlDriver> {
        createSqlDriver()
    }
    single {
        Database(get())
    }
    factory<AlternativeSourceDao> {
        AlternativeSourceDaoImpl(
            dispatcher = getDispatcherIO(),
            queries = get<Database>().alternativeSourceQueries
        )
    }
    factory<MonsterDao> {
        val database = get<Database>()
        MonsterDaoImpl(
            monsterQueries = database.monsterQueries,
            abilityScoreQueries = database.abilityScoreQueries,
            actionQueries = database.actionQueries,
            conditionQueries = database.conditionQueries,
            damageImmunityQueries = database.damageImmunityQueries,
            damageResistanceQueries = database.damageResistanceQueries,
            damageVulnerabilityQueries = database.damageVulnerabilityQueries,
            damageDiceQueries = database.damageDiceQueries,
            savingThrowQueries = database.savingThrowQueries,
            skillQueries = database.skillQueries,
            specialAbilityQueries = database.specialAbilityQueries,
            speedQueries = database.speedQueries,
            speedValueQueries = database.speedValueQueries,
            reactionQueries = database.reactionQueries,
            spellcastingQueries = database.spellcastingQueries,
            spellUsageQueries = database.spellUsageQueries,
            spellcastingSpellUsageCrossRefQueries = database.spellcastingSpellUsageCrossRefQueries,
            spellUsageCrossRefQueries = database.spellUsageSpellCrossRefQueries,
            spellPreviewQueries = database.spellPreviewQueries,
            legendaryActionQueries = database.legendaryActionQueries,
            dispatcher = getDispatcherIO()
        )
    }
    factory<MonsterFolderDao> {
        MonsterFolderDaoImpl(get<Database>().monsterFolderQueries, getDispatcherIO())
    }
    factory<MonsterLoreDao> {
        val database = get<Database>()
        MonsterLoreDaoImpl(
            queries = database.monsterLoreQueries,
            monsterLoreEntryQueries = database.monsterLoreEntryQueries,
            dispatcher = getDispatcherIO()
        )
    }
    factory<SpellDao> { SpellDaoImpl(get<Database>().spellQueries, getDispatcherIO()) }
}

expect fun getDispatcherIO(): CoroutineDispatcher
