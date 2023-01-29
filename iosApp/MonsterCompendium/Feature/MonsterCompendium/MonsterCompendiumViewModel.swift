//
//  MonsterCompendiumViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 29/01/23.
//

import Foundation
import shared

@MainActor class MonsterCompendiumViewModel : ObservableObject {
    
    private let monsterCompendiumFeature: MonsterCompendiumFeature

    @Published var isLoading: Bool = false
    @Published var state: MonsterCompendiumUiState = MonsterCompendiumUiState()
    
    init() {
        monsterCompendiumFeature = MonsterCompendiumFeature()
        loadData()
    }
    
    func loadData() {
        isLoading = true
        Task {
            do {
                self.state = MonsterCompendiumUiState(
                    items: try await monsterCompendiumFeature.getMonsterCompendium().asUiState()
                )
            } catch {
                print(error)
            }
            isLoading = false
        }
    }
}

extension MonsterCompendiumIos {
    
    func asUiState() -> [MonsterCompendiumItemUiState] {
        self.items.map { item in
            if (item.title != nil) {
                return MonsterCompendiumItemUiState.title(MonsterCompendiumItemUiState.Title(value: item.title!.value, id: item.title!.id, isHeader: item.title!.isHeader))
            } else {
                return MonsterCompendiumItemUiState.item(MonsterCompendiumItemUiState.Item(value: item.monster!.asUiState()))
            }
        }
    }
}

extension MonsterMonster {

    func asUiState() -> MonsterUiState {
        let monster = self
        return MonsterUiState(
            index: monster.index,
            name: monster.name,
            challengeRating: monster.challengeRating,
            imageUrl: monster.imageData.url,
            backgroundColorLight: monster.imageData.backgroundColor.light,
            backgroundColorDark: monster.imageData.backgroundColor.dark,
            isImageHorizontal: monster.imageData.isHorizontal
        )
    }
}
