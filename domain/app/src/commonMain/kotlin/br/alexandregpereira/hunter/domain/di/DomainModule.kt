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

package br.alexandregpereira.hunter.domain.di

import br.alexandregpereira.hunter.domain.folder.di.monsterFolderDomainModule
import br.alexandregpereira.hunter.domain.monster.lore.di.monsterLoreDomainModule
import br.alexandregpereira.hunter.domain.settings.di.settingsDomainModule
import br.alexandregpereira.hunter.domain.source.di.alternativeSourceDomainModule
import br.alexandregpereira.hunter.domain.spell.di.spellDomainModule
import br.alexandregpereira.hunter.domain.sync.di.syncDomainModule
import br.alexandregpereira.hunter.monster.compendium.domain.di.monsterCompendiumDomainModule

val domainModules = listOf(
    alternativeSourceDomainModule,
    monsterDomainModule,
    monsterCompendiumDomainModule,
    monsterFolderDomainModule,
    monsterLoreDomainModule,
    settingsDomainModule,
    spellDomainModule,
    syncDomainModule
)
