//
//  MonsterDetailViewModel.swift
//  MonsterCompendium
//
//  Created by Alexandre G Pereira on 02/03/23.
//

import Foundation
import shared

@MainActor class MonsterDetailViewModel : ObservableObject {
    
    @Published var isLoading: Bool = true
    @Published var state: MonsterDetailUiState = MonsterDetailUiState()
    private let monsterDetailFeature: MonsterDetailFeature
    private var stateWatcher : Closeable? = nil
    
    init() {
        monsterDetailFeature = MonsterDetailFeature()
        stateWatcher = monsterDetailFeature.state.collect { (state: Monster_detailMonsterDetailState) -> Void in
            self.state.isShowing = state.showDetail
            self.isLoading = state.isLoading
            if !state.monsters.isEmpty {
                self.state.monster = state.monsters[Int(state.initialMonsterListPositionIndex)].asMonsterDetailItemUiState()
            }
        }
    }
    
    func onClose() {
        monsterDetailFeature.onClose()
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
