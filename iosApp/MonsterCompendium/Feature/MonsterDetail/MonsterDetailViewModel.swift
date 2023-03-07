//
//  MonsterDetailViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import Foundation
import shared

@MainActor class MonsterDetailViewModel : ObservableObject {
    
    @Published var isLoading: Bool = false
    @Published var state: MonsterDetailUiState = MonsterDetailUiState()
    private var monsterId: String
    private let monsterDetailFeature: MonsterDetailFeature
    private var stateWatcher : Closeable? = nil
    
    init(monsterId: String) {
        self.monsterId = monsterId
        monsterDetailFeature = MonsterDetailFeature()
        loadData()
    }
    
    func loadData() {
        stateWatcher = monsterDetailFeature.getMonster(monsterIndex: monsterId).collect { (monster: MonsterMonster) -> Void in
            
            self.state.monster = monster.asMonsterDetailItemUiState()
        }
    }
    
    deinit {
        stateWatcher?.close()
    }
}

extension MonsterMonster {
    
    func asMonsterDetailItemUiState() -> MonsterDetailItemUiState {
        let monster = self
        return MonsterDetailItemUiState(
            id: monster.index,
            imageUrl: monster.imageData.url,
            backgroundColorLight: monster.imageData.backgroundColor.light,
            name: monster.name,
            subtitle: monster.subtitle
        )
    }
}
