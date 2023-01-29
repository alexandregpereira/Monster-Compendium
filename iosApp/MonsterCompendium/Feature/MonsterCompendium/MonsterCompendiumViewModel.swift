//
//  MonsterCompendiumViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation
import shared

@MainActor class MonsterCompendiumViewModel : ObservableObject {

    @Published var isLoading: Bool = false
    @Published var monsters: [MonsterUiState] = []
    
    init() {
        loadData()
    }
    
    func loadData() {
        isLoading = true
        Task {
            do {
                let domainMosters = try await IosAppModuleHelper().getMonsters()
                
                self.monsters = domainMosters.map({ monster in
                    var type: MonsterTypeUiState
                    switch monster.type {
                    case MonsterMonsterType.aberration:
                        type = MonsterTypeUiState.ABERRATION
                    case MonsterMonsterType.beast:
                        type = MonsterTypeUiState.BEAST
                    case MonsterMonsterType.celestial:
                        type = MonsterTypeUiState.CELESTIAL
                    case MonsterMonsterType.construct:
                        type = MonsterTypeUiState.CONSTRUCT
                    case MonsterMonsterType.dragon:
                        type = MonsterTypeUiState.DRAGON
                    case MonsterMonsterType.elemental:
                        type = MonsterTypeUiState.ELEMENTAL
                    case MonsterMonsterType.fey:
                        type = MonsterTypeUiState.ELEMENTAL
                    case MonsterMonsterType.fiend:
                        type = MonsterTypeUiState.ELEMENTAL
                    case MonsterMonsterType.giant:
                        type = MonsterTypeUiState.ELEMENTAL
                    case MonsterMonsterType.humanoid:
                        type = MonsterTypeUiState.ELEMENTAL
                    case MonsterMonsterType.monstrosity:
                        type = MonsterTypeUiState.ELEMENTAL
                    case MonsterMonsterType.ooze:
                        type = MonsterTypeUiState.ELEMENTAL
                    default:
                        type = MonsterTypeUiState.ELEMENTAL
                    }
                    return MonsterUiState(index: monster.index, name: monster.name, type: type, challengeRating: monster.challengeRating, imageUrl: monster.imageData.url, backgroundColorLight: monster.imageData.backgroundColor.light, backgroundColorDark: monster.imageData.backgroundColor.dark)
                })
            } catch {
                print(error)
            }
            isLoading = false
        }
    }
}
