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
    private var stateWatcher : Closeable? = nil
    
    init() {
        monsterCompendiumFeature = MonsterCompendiumFeature()
        stateWatcher = monsterCompendiumFeature.state.collect { (state: MonsterCompendiumStateIos) -> Void in
            self.isLoading = state.isLoading
            self.state = state.asMonsterCompendiumUiState()
        }
    }
    
    func onItemCLick(index: String) {
        
    }
    
    deinit {
        stateWatcher?.close()
    }
}

extension MonsterCompendiumStateIos {
    
    func asMonsterCompendiumItemUiState() -> [MonsterCompendiumItemUiState] {
        self.items.map { item in
            if (item.title != nil) {
                return MonsterCompendiumItemUiState.title(MonsterCompendiumItemUiState.Title(value: item.title!.value, id: item.title!.id, isHeader: item.title!.isHeader))
            } else {
                return MonsterCompendiumItemUiState.item(MonsterCompendiumItemUiState.Item(value: item.monster!.asUiState()))
            }
        }
    }
    
    func asMonsterCompendiumUiState() -> MonsterCompendiumUiState {
        return MonsterCompendiumUiState(
            items: asMonsterCompendiumItemUiState()
        )
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
