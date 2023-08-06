/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val databaseModule = module {
    single {
        createDatabase(get())
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

private fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(driver)
}
